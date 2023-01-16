package com.example.smsgroup.ui.addtogroup;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsgroup.ContactModel;
import com.example.smsgroup.GroupModel;
import com.example.smsgroup.OnClickItemEventListener;
import com.example.smsgroup.R;
import com.example.smsgroup.ui.creategroup.GroupAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>  {

    ArrayList<ContactModel> contactList;
    OnClickItemEventListener onClickItemEventListener;

    public ContactAdapter(ArrayList<ContactModel> contactList, OnClickItemEventListener onClickItemEventListener){
        this.contactList = contactList;
        this.onClickItemEventListener = onClickItemEventListener;
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactAdapter.ContactViewHolder holder = new ContactAdapter.ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_contact_vertical, parent, false), onClickItemEventListener);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {
        ContactModel contactModel = contactList.get(position);
        holder.setData(contactModel);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView cntitemImgProfile;
        TextView cntitemTxtName;

        OnClickItemEventListener onClickItemEventListener;

        public ContactViewHolder(View itemView, OnClickItemEventListener onClickItemEventListener) {
            super(itemView);

            cntitemImgProfile = itemView.findViewById(R.id.cntitemImgProfile);
            cntitemTxtName = itemView.findViewById(R.id.cntitemTxtName);

            this.onClickItemEventListener = onClickItemEventListener;
            itemView.setOnClickListener(this);
        }

        private void setData(ContactModel contact){
            cntitemTxtName.setText(contact.getName());

            if (contact.getImage() != null) {
                cntitemImgProfile.setImageURI(Uri.parse(contact.getImage()));
            }
        }

        @Override
        public void onClick(View view) {
            onClickItemEventListener.onClickItemEvent(getAdapterPosition());
        }
    }
}
