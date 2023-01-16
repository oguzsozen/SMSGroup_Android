package com.example.smsgroup.ui.addtogroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsgroup.GroupModel;
import com.example.smsgroup.OnClickItemEventListener;
import com.example.smsgroup.R;
import com.example.smsgroup.ui.creategroup.GroupAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupAdapterHorizontal extends RecyclerView.Adapter<GroupAdapterHorizontal.GroupViewHolder> {
    ArrayList<GroupModel> groupList;
    OnClickItemEventListener onClickItemEventListener;

    public GroupAdapterHorizontal(ArrayList<GroupModel> groupList, OnClickItemEventListener onClickItemEventListener){
        this.groupList = groupList;
        this.onClickItemEventListener = onClickItemEventListener;
    }

    @NonNull
    @Override
    public GroupAdapterHorizontal.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupAdapterHorizontal.GroupViewHolder holder = new GroupAdapterHorizontal.GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_group_horizontal, parent, false), onClickItemEventListener);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapterHorizontal.GroupViewHolder holder, int position) {
        GroupModel groupModel = groupList.get(position);
        holder.setData(groupModel);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView grpitemImgGroupProfileHorizontal;
        TextView grpitemTxtGroupNameHorizontal, grpitemTxtGroupDescriptionHorizontal;

        OnClickItemEventListener onClickItemEventListener;

        public GroupViewHolder(View itemView, OnClickItemEventListener onClickItemEventListener) {
            super(itemView);

            grpitemImgGroupProfileHorizontal = itemView.findViewById(R.id.grpitemImgGroupProfileHorizontal);
            grpitemTxtGroupNameHorizontal = itemView.findViewById(R.id.grpitemTxtGroupNameHorizontal);
            grpitemTxtGroupDescriptionHorizontal = itemView.findViewById(R.id.grpitemTxtGroupDescriptionHorizontal);

            this.onClickItemEventListener = onClickItemEventListener;
            itemView.setOnClickListener(this);
        }

        private void setData(GroupModel group){
            grpitemTxtGroupNameHorizontal.setText(group.getName());
            grpitemTxtGroupDescriptionHorizontal.setText(group.getDescription());

            if (group.getImage() != null) {
                Picasso.get().load(group.getImage()).into(grpitemImgGroupProfileHorizontal);
            }
        }

        @Override
        public void onClick(View view) {
            onClickItemEventListener.onClickItemEvent(getAdapterPosition());
        }
    }
}
