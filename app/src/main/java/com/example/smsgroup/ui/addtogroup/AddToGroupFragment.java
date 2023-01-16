package com.example.smsgroup.ui.addtogroup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smsgroup.ContactModel;
import com.example.smsgroup.GroupModel;
import com.example.smsgroup.R;
import com.example.smsgroup.ui.creategroup.GroupAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.checkerframework.checker.signature.qual.SignatureUnknown;

import java.util.ArrayList;
import java.util.HashMap;

public class AddToGroupFragment extends Fragment {

    RecyclerView addgrpRvwGroupList, addgrpRvwContactList;
    TextView addgrpLblSelectedGroup;

    FirebaseAuth mAuth;
    FirebaseStorage mStorage;
    FirebaseFirestore mFirestore;

    String userId;

    ArrayList<GroupModel> groupList;
    ArrayList<ContactModel> contactList;

    GroupModel selectedGroup;
    ContactModel selectedContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_group, container, false);

        addgrpRvwGroupList = view.findViewById(R.id.addgrpRvwGroupList);
        addgrpRvwContactList = view.findViewById(R.id.addgrpRvwContactList);
        addgrpLblSelectedGroup = view.findViewById(R.id.addgrpLblSelectedGroup);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        groupList = new ArrayList<GroupModel>();
        contactList = new ArrayList<ContactModel>();

        ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGrant -> {
            if (isGrant) {
                FetchContacts();
            }
            else {
                Toast.makeText(getContext(),"Uygulamanın düzgün çalışması için rehbere erişim izni gerekli.", Toast.LENGTH_SHORT).show();
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            launcher.launch(Manifest.permission.READ_CONTACTS);
        }
        else {
            FetchContacts();
        }

        FetchGroups();

        return view;
    }

    private void FetchGroups(){
        mFirestore.collection("/users/"+ userId + "/groups").get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                groupList.add(new GroupModel(snapshot.getString("name"), snapshot.getString("description"), snapshot.getString("downloadUrl"),
                        snapshot.getId(), (ArrayList<String>)snapshot.get("numbers")));
            }

            addgrpRvwGroupList.setAdapter(new GroupAdapterHorizontal(groupList, position -> {
                selectedGroup = groupList.get(position);
                addgrpLblSelectedGroup.setText("Seçilen Grup: " + selectedGroup.getName());
            }));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            addgrpRvwGroupList.setLayoutManager(linearLayoutManager);

        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(),"Liste getirilemedi.", Toast.LENGTH_SHORT).show();
        });
    }
    private void FetchContacts(){
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, null,null,null);

        while (cursor.moveToNext()){
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            ContactModel contact = new ContactModel(name, number, image);
            contactList.add(contact);
        }

        addgrpRvwContactList.setAdapter(new ContactAdapter(contactList, position -> {
            selectedContact = contactList.get(position);

            if(selectedGroup != null) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Kişi Ekle")
                        .setMessage(selectedContact.getName() + " kişisini " + selectedGroup.getName() + " grubuna eklemek istediğinizden emin misiniz?")
                        .setPositiveButton("Evet", (dialog, which) -> {
                            mFirestore.collection("/users/"+ userId + "/groups").document(selectedGroup.getUid()).update(new HashMap<String, Object>(){{
                                put("numbers", FieldValue.arrayUnion(selectedContact.getNumber()));
                            }}).addOnSuccessListener(a -> {
                                Toast.makeText(getContext(), selectedContact.getName()+" kişisi eklendi.", Toast.LENGTH_SHORT).show();
                            });
                        }).setNegativeButton("Hayır", null).show();
            }
        }));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        addgrpRvwContactList.setLayoutManager(linearLayoutManager);

    }
}