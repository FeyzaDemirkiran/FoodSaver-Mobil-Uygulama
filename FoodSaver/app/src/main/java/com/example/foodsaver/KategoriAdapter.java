package com.example.foodsaver;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.GorunumTutucu> implements Filterable {

    private Context context;
    private List<Kategori> kategoriListesi;
    private List<Kategori> orijinalListe;
    private final int[] renkListesi = {
            R.color.kart_renk1, R.color.kart_renk2, R.color.kart_renk3, R.color.kart_renk4, R.color.kart_renk5,
            R.color.kart_renk6, R.color.kart_renk7, R.color.kart_renk8, R.color.kart_renk9, R.color.kart_renk10,
            R.color.kart_renk11, R.color.kart_renk12, R.color.kart_renk13, R.color.kart_renk14, R.color.kart_renk15,
            R.color.kart_renk16, R.color.kart_renk17, R.color.kart_renk18, R.color.kart_renk19, R.color.kart_renk20,
            R.color.kart_renk21, R.color.kart_renk22, R.color.kart_renk23, R.color.kart_renk24, R.color.kart_renk25,
            R.color.kart_renk26, R.color.kart_renk27, R.color.kart_renk28, R.color.kart_renk29, R.color.kart_renk30
    };

    public KategoriAdapter(Context context, List<Kategori> kategoriListesi) {
        this.context = context;
        this.kategoriListesi = new ArrayList<>(kategoriListesi);
        this.orijinalListe = new ArrayList<>(kategoriListesi);
    }

    @Override
    public GorunumTutucu onCreateViewHolder(ViewGroup parent, int viewType) {
        View gorunum = LayoutInflater.from(context).inflate(R.layout.kategori_satir, parent, false);
        return new GorunumTutucu(gorunum);
    }

    @Override
    public void onBindViewHolder(GorunumTutucu tutucu, int pozisyon) {
        Kategori kategori = kategoriListesi.get(pozisyon);
        tutucu.kategoriAdi.setText(kategori.getAd());


        int renkIndex = pozisyon % renkListesi.length;
        int renk = ContextCompat.getColor(context, renkListesi[renkIndex]);
        tutucu.layout.setBackgroundColor(renk);


        Glide.with(context)
                .load(kategori.getResimUrl())
                .error(R.drawable.ic_error)
                .into(tutucu.kategoriResmi);


        tutucu.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductListActivity.class);
            intent.putExtra("kategori", kategori.getAd());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return kategoriListesi.size();
    }

    public static class GorunumTutucu extends RecyclerView.ViewHolder {
        ImageView kategoriResmi;
        TextView kategoriAdi;
        LinearLayout layout;

        public GorunumTutucu(View itemView) {
            super(itemView);
            kategoriResmi = itemView.findViewById(R.id.kategoriResmi);
            kategoriAdi = itemView.findViewById(R.id.kategoriAdi);
            layout = itemView.findViewById(R.id.kategoriArkaPlan);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Kategori> filtrelenmis = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filtrelenmis.addAll(orijinalListe);
                } else {
                    String filtre = constraint.toString().toLowerCase().trim();
                    for (Kategori k : orijinalListe) {
                        if (k.getAd().toLowerCase().contains(filtre)) {
                            filtrelenmis.add(k);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filtrelenmis;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                kategoriListesi.clear();
                kategoriListesi.addAll((List<Kategori>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}