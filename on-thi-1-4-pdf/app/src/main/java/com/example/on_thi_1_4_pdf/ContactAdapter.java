package com.example.on_thi_1_4_pdf;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private final Context context;
    private List<User> displayList;  // currently shown (after search/sort)
    private List<User> fullList;     // original full list

    public ContactAdapter(Context context, List<User> users) {
        this.context = context;
        this.fullList = new ArrayList<>(users);
        this.displayList = new ArrayList<>(users);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = displayList.get(position);

        holder.tvName.setText(user.getName());
        holder.tvPhone.setText(user.getPhone());

        // Sync checkbox state without triggering listener
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(user.isChecked());

        // Auto update User checked state when checkbox toggled
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                user.setChecked(isChecked));

        // Call intent
        holder.btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + user.getPhone()));
            context.startActivity(intent);
        });

        // SMS intent
        holder.btnSms.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO,
                    Uri.parse("smsto:" + user.getPhone()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    /** Filter displayList by name query */
    public void filter(String query) {
        displayList.clear();
        if (query == null || query.trim().isEmpty()) {
            displayList.addAll(fullList);
        } else {
            String lower = query.trim().toLowerCase();
            for (User user : fullList) {
                if (user.getName().toLowerCase().contains(lower)) {
                    displayList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

    /** Replace entire dataset (e.g. after add/delete/sort) */
    public void updateList(List<User> users) {
        fullList = new ArrayList<>(users);
        displayList = new ArrayList<>(users);
        notifyDataSetChanged();
    }

    /** Return all checked users from the full list */
    public List<User> getCheckedUsers() {
        List<User> checked = new ArrayList<>();
        for (User u : fullList) {
            if (u.isChecked()) checked.add(u);
        }
        return checked;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone;
        ImageButton btnCall, btnSms;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnSms = itemView.findViewById(R.id.btnSms);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}

