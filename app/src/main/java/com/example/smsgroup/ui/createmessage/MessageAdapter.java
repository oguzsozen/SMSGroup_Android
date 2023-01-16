package com.example.smsgroup.ui.createmessage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsgroup.GroupModel;
import com.example.smsgroup.MessageModel;
import com.example.smsgroup.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    ArrayList<MessageModel> messageList;

    public MessageAdapter(ArrayList<MessageModel> messageList){
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageAdapter.MessageViewHolder holder = new MessageAdapter.MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_message_vertical, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        MessageModel messageModel = messageList.get(position);
        holder.setData(messageModel);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView msgitemTxtMessageName, msgitemTxtMessage;

        public MessageViewHolder(View itemView) {
            super(itemView);

            msgitemTxtMessageName = itemView.findViewById(R.id.msgitemTxtMessageName);
            msgitemTxtMessage = itemView.findViewById(R.id.msgitemTxtMessage);
        }

        private void setData(MessageModel message){
            msgitemTxtMessageName.setText(message.getName());
            msgitemTxtMessage.setText(message.getMessage());
        }
    }
}
