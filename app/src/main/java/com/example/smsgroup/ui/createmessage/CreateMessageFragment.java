package com.example.smsgroup.ui.createmessage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smsgroup.GroupModel;
import com.example.smsgroup.MessageModel;
import com.example.smsgroup.R;
import com.example.smsgroup.ui.creategroup.GroupAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CreateMessageFragment extends Fragment {

    TextView crtmsgTxtMessageName, crtmsgTxtMessage;
    Button crtmsgBtnCreateMessage;
    RecyclerView crtmsgRvwMessageList;

    FirebaseAuth mAuth;
    FirebaseStorage mStorage;
    FirebaseFirestore mFirestore;

    String userId;

    ArrayList<MessageModel> messageList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_message, container, false);

        crtmsgTxtMessageName = view.findViewById(R.id.crtmsgTxtMessageName);
        crtmsgTxtMessage = view.findViewById(R.id.crtmsgTxtMessage);
        crtmsgBtnCreateMessage = view.findViewById(R.id.crtmsgBtnCreateMessage);
        crtmsgRvwMessageList = view.findViewById(R.id.crtmsgRvwMessageList);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        messageList = new ArrayList<MessageModel>();

        crtmsgBtnCreateMessage.setOnClickListener(v -> {

            String name = crtmsgTxtMessageName.getText().toString();
            String message = crtmsgTxtMessage.getText().toString();

            if(name == null){
                Toast.makeText(getContext(),"Grup adı boş bırakılamaz.", Toast.LENGTH_SHORT).show();
            }
            if(message == null){
                Toast.makeText(getContext(),"Grup açıklaması boş bırakılamaz.", Toast.LENGTH_SHORT).show();
            }

            CreateMessage(name, message);

        });

        FetchMessages();

        return view;
    }

    private void CreateMessage(String name, String message){
        mFirestore.collection("/users/"+ userId + "/messages").add(new HashMap<String,String>(){{
            put("name",name);
            put("message",message);

        }}).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(),"Kayıt başarılı.", Toast.LENGTH_SHORT).show();

            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                MessageModel messageModel = new MessageModel(name, message, documentSnapshot.getId());
                messageList.add(messageModel);
                crtmsgRvwMessageList.getAdapter().notifyItemInserted(messageList.size() - 1);
            });

        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(),"Kayıt başarısız.", Toast.LENGTH_SHORT).show();
        });
    }

    private void FetchMessages(){
        mFirestore.collection("/users/"+ userId + "/messages").get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                messageList.add(new MessageModel(snapshot.getString("name"), snapshot.getString("message"),
                        snapshot.getId()));
            }

            crtmsgRvwMessageList.setAdapter(new MessageAdapter(messageList));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            crtmsgRvwMessageList.setLayoutManager(linearLayoutManager);

        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(),"Liste getirilemedi.", Toast.LENGTH_SHORT).show();
        });
    }
}