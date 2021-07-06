package com.example.bankapp.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Database.Room.Message;
import com.example.bankapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MessageAdapter extends PagedListAdapter<Message, MessageAdapter.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Listener listener;
    private View view;

    public MessageAdapter(Listener listener) {
        super(Message.DIFF_UTIL);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.send_message_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.received_message_item, parent, false);
        }
        return new ViewHolder(view);

    }

    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);

        if(message!=null){
            if (listener.getUserId().equals(message.getSentBy())) {
                // If the current user is the sender of the message
                return VIEW_TYPE_MESSAGE_SENT;
            }
        }
        // If some other user sent the message
        return VIEW_TYPE_MESSAGE_RECEIVED;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = getItem(position);
        Message messageBefore = null;

        if(position>0){
            messageBefore = getItem(position-1);
        }
        if (message == null) {
            return;
        }
        if(messageBefore!=null){
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(message.getCreatedDate());
            cal2.setTime(messageBefore.getCreatedDate());
            boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
            if(sameDay){
                holder.dateTxt.setVisibility(View.GONE);
            }else{
                SimpleDateFormat sdfs = new SimpleDateFormat("yyyy MMMM dd");
                holder.dateTxt.setVisibility(View.VISIBLE);
                holder.dateTxt.setText(sdfs.format(message.getCreatedDate()));
            }
        }else{
            SimpleDateFormat sdfs = new SimpleDateFormat("yyyy MMMM dd");
            holder.dateTxt.setVisibility(View.VISIBLE);
            holder.dateTxt.setText(sdfs.format(message.getCreatedDate()));
        }
        holder.contentTxt.setText(message.getContent());
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        holder.timeTxt.setText(sdfs.format(message.getCreatedDate()));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView contentTxt;
        TextView timeTxt;
        TextView dateTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTxt = itemView.findViewById(R.id.contentTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            dateTxt = itemView.findViewById(R.id.dateTxt);
        }
    }

    public interface Listener{
        String getUserId();
    }
}
