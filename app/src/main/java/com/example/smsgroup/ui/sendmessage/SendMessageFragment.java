package com.example.smsgroup.ui.sendmessage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smsgroup.GroupModel;
import com.example.smsgroup.MessageModel;
import com.example.smsgroup.R;
import com.example.smsgroup.ui.addtogroup.GroupAdapterHorizontal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class SendMessageFragment extends Fragment {

    RecyclerView sndmsgRvwGroupList, sndmsgRvwMessageList;
    TextView sndmsgLblSelectedGroup, sndmsgLblSelectedMessage;
    Button sndmsgBtnSend;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    String userId;

    ArrayList<GroupModel> groupList;
    ArrayList<MessageModel> messageList;

    GroupModel selectedGroup;
    MessageModel selectedMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_message, container, false);

        sndmsgRvwGroupList = view.findViewById(R.id.sndmsgRvwGroupList);
        sndmsgRvwMessageList = view.findViewById(R.id.sndmsgRvwMessageList);
        sndmsgLblSelectedGroup = view.findViewById(R.id.sndmsgLblSelectedGroup);
        sndmsgLblSelectedMessage = view.findViewById(R.id.sndmsgLblSelectedMessage);
        sndmsgBtnSend = view.findViewById(R.id.sndmsgBtnSend);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        groupList = new ArrayList<GroupModel>();
        messageList = new ArrayList<MessageModel>();

        ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGrant -> {
            if (isGrant) {
                sendSMS();
            }
            else {
                Toast.makeText(getContext(),"Uygulamanın düzgün çalışması için SMS gönderme izni gerekli.", Toast.LENGTH_SHORT).show();
            }
        });

        sndmsgBtnSend.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    getContext().checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                launcher.launch(Manifest.permission.SEND_SMS);
            }
            else {
                sendSMS();
            }
        });

        FetchGroups();
        FetchMessages();

        return view;
    }
    private void FetchGroups(){
        mFirestore.collection("/users/"+ userId + "/groups").get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                groupList.add(new GroupModel(snapshot.getString("name"), snapshot.getString("description"), snapshot.getString("downloadUrl"),
                        snapshot.getId(), (ArrayList<String>)snapshot.get("numbers")));
            }

            sndmsgRvwGroupList.setAdapter(new GroupAdapterHorizontal(groupList, position -> {
                selectedGroup = groupList.get(position);
                sndmsgLblSelectedGroup.setText("Seçilen Grup: " + selectedGroup.getName());
            }));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            sndmsgRvwGroupList.setLayoutManager(linearLayoutManager);

        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(),"Liste getirilemedi.", Toast.LENGTH_SHORT).show();
        });
    }

    private void FetchMessages(){
        mFirestore.collection("/users/"+ userId + "/messages").get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                messageList.add(new MessageModel(snapshot.getString("name"), snapshot.getString("message"), snapshot.getId()));
            }

            sndmsgRvwMessageList.setAdapter(new MessageAdapterHorizontal(messageList, position -> {
                selectedMessage = messageList.get(position);
                sndmsgLblSelectedMessage.setText("Seçilen Grup: " + selectedMessage.getName());
            }));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            sndmsgRvwMessageList.setLayoutManager(linearLayoutManager);

        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(),"Liste getirilemedi.", Toast.LENGTH_SHORT).show();
        });
    }

    private void sendSMS(){
        if(selectedGroup == null || selectedMessage == null){
            Toast.makeText(getContext(),"Lütfen bir grup ve bir mesaj seçiniz.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedGroup.getNumbers().size() > 0){
            SmsManager smsManager = SmsManager.getDefault();
            for(String number : selectedGroup.getNumbers()){
                smsManager.sendTextMessage(number, null, selectedMessage.getMessage(), null, null);
            }
            Toast.makeText(getContext(),"Mesajlar gönderildi.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(),"Mesajlar gönderilemedi.", Toast.LENGTH_SHORT).show();
    }
}