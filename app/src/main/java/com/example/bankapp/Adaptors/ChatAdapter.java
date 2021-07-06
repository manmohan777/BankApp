package com.example.bankapp.Adaptors;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Database.Room.Chat;
import com.example.bankapp.R;

import java.text.SimpleDateFormat;

public class ChatAdapter extends PagedListAdapter<Chat, ChatAdapter.ViewHolder> {

    private Listener listener;
    private View view;

    public ChatAdapter(Listener listener) {
        super(Chat.DIFF_UTIL);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = getItem(position);
        if (chat == null) {
            return;
        }
        Log.e("CHAT", chat.toString());
        View.OnClickListener l = view -> listener.onChatSelected(chat);
        
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        holder.contentTxt.setText(chat.getContent());
        holder.timeTxt.setText(sdfs.format(chat.getCreatedDate()));
        holder.iconTxt.setText(chat.getIconText());

        holder.contentTxt.setOnClickListener(l);
        holder.timeTxt.setOnClickListener(l);

        if(chat.getMessageCount()>0){
            holder.badgeTxt.setVisibility(View.VISIBLE);
            holder.badgeTxt.setText(String.valueOf(chat.getMessageCount()));
        }else {
            holder.badgeTxt.setVisibility(View.INVISIBLE);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView contentTxt;
        TextView timeTxt;
        TextView badgeTxt;
        TextView iconTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTxt = itemView.findViewById(R.id.contentTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            badgeTxt = itemView.findViewById(R.id.badgeTxt);
            iconTxt = itemView.findViewById(R.id.iconTxt);
        }
    }

    public interface Listener{

        void onChatSelected(Chat chat);
    }
}
