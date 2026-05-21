package com.example.on_thi_1_docx;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NhaHangAdapter extends RecyclerView.Adapter<NhaHangAdapter.NhaHangViewHolder> {
    public interface OnItemLongClickListener {
        void onItemLongClick(NhaHang nhaHang);
    }

    private final List<NhaHang> items = new ArrayList<>();
    private final OnItemLongClickListener longClickListener;

    public NhaHangAdapter(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setItems(List<NhaHang> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NhaHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nha_hang, parent, false);
        return new NhaHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NhaHangViewHolder holder, int position) {
        NhaHang nhaHang = items.get(position);
        holder.txtTen.setText(nhaHang.getTen());
        holder.txtDiaChi.setText(nhaHang.getDiaChi());
        holder.txtDanhGia.setText(String.format(Locale.US, "%.1f", nhaHang.getDanhGia()));

        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClick(nhaHang);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class NhaHangViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen;
        TextView txtDiaChi;
        TextView txtDanhGia;

        public NhaHangViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
            txtDanhGia = itemView.findViewById(R.id.txtDanhGia);
        }
    }
}

