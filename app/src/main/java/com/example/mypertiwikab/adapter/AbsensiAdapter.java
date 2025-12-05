package com.example.mypertiwikab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypertiwikab.R;
import com.example.mypertiwikab.model.AbsensiHistory;

import java.util.List;

public class AbsensiAdapter extends RecyclerView.Adapter<AbsensiAdapter.AbsensiViewHolder> {

    private final Context context;
    private final List<AbsensiHistory> historyList;

    public AbsensiAdapter(Context context, List<AbsensiHistory> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public AbsensiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_riwayat_absensi, parent, false);
        return new AbsensiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsensiViewHolder holder, int position) {
        AbsensiHistory history = historyList.get(position);

        holder.tvTanggal.setText(history.getTanggal());

        // Cek jika jam datang/pulang masih null/kosong
        String jamDatang = history.getJamDatang();
        String jamPulang = history.getJamPulang();

        holder.tvJamDatang.setText(jamDatang != null && !jamDatang.isEmpty() ? jamDatang + " WIB" : "-");
        holder.tvJamPulang.setText(jamPulang != null && !jamPulang.isEmpty() ? jamPulang + " WIB" : "-");
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class AbsensiViewHolder extends RecyclerView.ViewHolder {
        TextView tvTanggal, tvJamDatang, tvJamPulang;

        public AbsensiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJamDatang = itemView.findViewById(R.id.tvJamDatang);
            tvJamPulang = itemView.findViewById(R.id.tvJamPulang);
        }
    }
}