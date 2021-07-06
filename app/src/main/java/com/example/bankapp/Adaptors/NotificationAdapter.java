package com.example.bankapp.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Database.Room.Notification;
import com.example.bankapp.R;

import java.text.SimpleDateFormat;

public class NotificationAdapter  extends PagedListAdapter<Notification, NotificationAdapter.ViewHolder> {

    private Listener listener;
    private View view;

    public NotificationAdapter(Listener listener) {
        super(Notification.DIFF_UTIL);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = getItem(position);
        if (notification == null) {
            return;
        }
        holder.contentTxt.setText(notification.getContent());
        holder.refTxt.setText(notification.getReference());
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        holder.timeTxt.setText(sdfs.format(notification.getCreatedDate()));
        if(notification.isRead()){
            holder.badgeTxt.setVisibility(View.INVISIBLE);
        }else {
            holder.badgeTxt.setVisibility(View.VISIBLE);
        }
        holder.infoIcon.setOnClickListener(view -> listener.onInfoClick(notification));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView contentTxt;
        TextView timeTxt;
        TextView badgeTxt;
        TextView refTxt;
        ImageView infoIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTxt = itemView.findViewById(R.id.contentTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            badgeTxt = itemView.findViewById(R.id.badgeTxt);
            refTxt = itemView.findViewById(R.id.refTxt);
            infoIcon = itemView.findViewById(R.id.infoIcon);
        }
    }

    public interface Listener{

        void onInfoClick(Notification notification);
    }
}
