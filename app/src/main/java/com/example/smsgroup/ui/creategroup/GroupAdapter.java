package com.example.smsgroup.ui.creategroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsgroup.GroupModel;
import com.example.smsgroup.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder>{

    ArrayList<GroupModel> groupList;

    public GroupAdapter(ArrayList<GroupModel> groupList){
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupViewHolder holder = new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_group_vertical, parent, false));
        return holder;
        
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupModel groupModel = groupList.get(position);
        holder.setData(groupModel);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{

        ImageView grpitemImgGroupProfile;
        TextView grpitemTxtGroupName, grpitemTxtGroupDescription;

        public GroupViewHolder(View itemView) {
            super(itemView);

            grpitemImgGroupProfile = itemView.findViewById(R.id.grpitemImgGroupProfile);
            grpitemTxtGroupName = itemView.findViewById(R.id.grpitemTxtGroupName);
            grpitemTxtGroupDescription = itemView.findViewById(R.id.grpitemTxtGroupDescription);
        }

        private void setData(GroupModel group){
            grpitemTxtGroupName.setText(group.getName());
            grpitemTxtGroupDescription.setText(group.getDescription());

            if (group.getImage() != null) {
                Picasso.get().load(group.getImage()).into(grpitemImgGroupProfile);
            }
        }
    }
}
