package com.example.on_thi_1_3_pdf;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class VeAdapter extends RecyclerView.Adapter<VeAdapter.VeViewHolder> {

    public interface OnItemLongClickListener {
        void onSua(Ve ve);
        void onXoa(Ve ve);
    }

    private List<Ve> veList;
    private OnItemLongClickListener listener;
    private Ve selectedVe;

    public VeAdapter(List<Ve> veList, OnItemLongClickListener listener) {
        this.veList = veList;
        this.listener = listener;
    }

    public void setData(List<Ve> list) {
        this.veList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ve, parent, false);
        return new VeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VeViewHolder holder, int position) {
        Ve ve = veList.get(position);
        holder.tvRoute.setText(ve.getGaDi() + "  ->  " + ve.getGaDen());
        holder.tvLoaiVe.setText(ve.getLoaiVeText());
        holder.tvGia.setText(String.format(Locale.getDefault(), "%,.3f", ve.tinhGia()));

        holder.itemView.setOnLongClickListener(v -> {
            selectedVe = ve;
            return false;
        });

        holder.itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
            menu.add(Menu.NONE, 0, 0, "Sửa").setOnMenuItemClickListener(item -> {
                if (listener != null && selectedVe != null)
                    listener.onSua(selectedVe);
                return true;
            });
            menu.add(Menu.NONE, 1, 1, "Xóa").setOnMenuItemClickListener(item -> {
                if (listener != null && selectedVe != null)
                    listener.onXoa(selectedVe);
                return true;
            });
        });
    }

    @Override
    public int getItemCount() {
        return veList == null ? 0 : veList.size();
    }

    static class VeViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoute, tvLoaiVe, tvGia;

        VeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoute = itemView.findViewById(R.id.tvRoute);
            tvLoaiVe = itemView.findViewById(R.id.tvLoaiVe);
            tvGia = itemView.findViewById(R.id.tvGia);
        }
    }
}


