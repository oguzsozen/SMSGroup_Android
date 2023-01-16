package com.example.smsgroup.ui.creategroup;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smsgroup.GroupModel;
import com.example.smsgroup.R;
import com.example.smsgroup.SinginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreateGroupFragment extends Fragment {

    TextView crtgrpTxtGroupName, crtgrpTxtGroupDescription;
    ImageView crtgrpImgGroupProfile;
    Button crtgrpBtnCreateGroup;
    RecyclerView crtgrpRvwGroupList;

    Uri filepath;

    FirebaseAuth mAuth;
    FirebaseStorage mStorage;
    FirebaseFirestore mFirestore;

    String userId;

    ArrayList<GroupModel> groupList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        crtgrpTxtGroupName = view.findViewById(R.id.crtgrpTxtGroupName);
        crtgrpTxtGroupDescription = view.findViewById(R.id.crtgrpTxtGroupDescription);
        crtgrpImgGroupProfile = view.findViewById(R.id.crtgrpImgGroupProfile);
        crtgrpBtnCreateGroup = view.findViewById(R.id.crtgrpBtnCreateGroup);
        crtgrpRvwGroupList = view.findViewById(R.id.crtgrpRvwGroupList);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        groupList = new ArrayList<GroupModel>();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK){
                        Intent data = result.getData();
                        filepath = data.getData();
                        crtgrpImgGroupProfile.setImageURI(filepath);
                }
                }
        );

        crtgrpImgGroupProfile.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
        });

        crtgrpBtnCreateGroup.setOnClickListener(v -> {

            String name = crtgrpTxtGroupName.getText().toString();
            String description = crtgrpTxtGroupDescription.getText().toString();

            if(name == null){
                Toast.makeText(getContext(),"Grup adı boş bırakılamaz.", Toast.LENGTH_SHORT).show();
            }
            if(description == null){
                Toast.makeText(getContext(),"Grup açıklaması boş bırakılamaz.", Toast.LENGTH_SHORT).show();
            }
            if(filepath != null){
                StorageReference storageReference = mStorage.getReference().child("img/"+userId+"/"+UUID.randomUUID().toString());

                storageReference.putFile(filepath).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            Toast.makeText(getContext(),"Resim başarıyla yüklendi.", Toast.LENGTH_SHORT).show();

                            CreateGroup(name, description, downloadUrl);
                        });
                    }
                });
            }
            else {
                CreateGroup(name, description, null);
            }
        });

        FetchGroups();

        return view;
    }

    private void CreateGroup(String name, String description, String downloadUrl) {

        mFirestore.collection("/users/"+ userId + "/groups").add(new HashMap<String,Object>(){{
            put("name",name);
            put("description",description);
            put("downloadUrl",downloadUrl);
            put("numbers",new ArrayList<String>());
        }}).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(),"Kayıt başarılı.", Toast.LENGTH_SHORT).show();

            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                GroupModel groupModel = new GroupModel(name, description, downloadUrl, documentSnapshot.getId(),
                        (ArrayList<String>)documentSnapshot.get("numbers"));
                groupList.add(groupModel);
                crtgrpRvwGroupList.getAdapter().notifyItemInserted(groupList.size() - 1);
            });

        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(),"Kayıt başarısız.", Toast.LENGTH_SHORT).show();
        });

    }

    private void FetchGroups(){
        mFirestore.collection("/users/"+ userId + "/groups").get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                groupList.add(new GroupModel(snapshot.getString("name"), snapshot.getString("description"), snapshot.getString("downloadUrl"),
                        snapshot.getId(), (ArrayList<String>)snapshot.get("numbers")));
            }

            crtgrpRvwGroupList.setAdapter(new GroupAdapter(groupList));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            crtgrpRvwGroupList.setLayoutManager(linearLayoutManager);

        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(),"Liste getirilemedi.", Toast.LENGTH_SHORT).show();
        });
    }
}