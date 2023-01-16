package com.example.smsgroup.ui.sendmessage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsgroup.MessageModel;
import com.example.smsgroup.OnClickItemEventListener;
import com.example.smsgroup.R;

import java.util.ArrayList;

public class MessageAdapterHorizontal extends RecyclerView.Adapter<MessageAdapterHorizontal.MessageViewHolder> {
    ArrayList<MessageModel> messageList;
    OnClickItemEventListener onClickItemEventListener;

    public MessageAdapterHorizontal(ArrayList<MessageModel> messageList, OnClickItemEventListener onClickItemEventListener){
        this.messageList = messageList;
        this.onClickItemEventListener = onClickItemEventListener;
    }

    @NonNull
    @Override
    public MessageAdapterHorizontal.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageAdapterHorizontal.MessageViewHolder holder = new MessageAdapterHorizontal.MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_message_horizontal, parent, false), onClickItemEventListener);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterHorizontal.MessageViewHolder holder, int position) {
        MessageModel message = messageList.get(position);
        holder.setData(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView msgitemTxtMessageNameHorizontal, msgitemTxtMessageHorizontal;

        OnClickItemEventListener onClickItemEventListener;

        public MessageViewHolder(View itemView, OnClickItemEventListener onClickItemEventListener) {
            super(itemView);

            msgitemTxtMessageNameHorizontal = itemView.findViewById(R.id.msgitemTxtMessageNameHorizontal);
            msgitemTxtMessageHorizontal = itemView.findViewById(R.id.msgitemTxtMessageHorizontal);

            this.onClickItemEventListener = onClickItemEventListener;
            itemView.setOnClickListener(this);
        }

        private void setData(MessageModel message){
            msgitemTxtMessageNameHorizontal.setText(message.getName());
            msgitemTxtMessageHorizontal.setText(message.getMessage());
        }

        @Override
        public void onClick(View view) {
            onClickItemEventListener.onClickItemEvent(getAdapterPosition());
        }
    }
}
