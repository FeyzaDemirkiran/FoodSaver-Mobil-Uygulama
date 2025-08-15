package com.example.foodsaver;



import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.GorunumTutucu> {

    private Context context;
    private List<Product> urunler;


    private final int[] renkListesi = {
            R.color.kart_renk1, R.color.kart_renk2, R.color.kart_renk3, R.color.kart_renk4, R.color.kart_renk5,
            R.color.kart_renk6, R.color.kart_renk7, R.color.kart_renk8, R.color.kart_renk9, R.color.kart_renk10,
            R.color.kart_renk11, R.color.kart_renk12, R.color.kart_renk13, R.color.kart_renk14, R.color.kart_renk15,
            R.color.kart_renk16, R.color.kart_renk17, R.color.kart_renk18, R.color.kart_renk19, R.color.kart_renk20,
            R.color.kart_renk21, R.color.kart_renk22, R.color.kart_renk23, R.color.kart_renk24, R.color.kart_renk25,
            R.color.kart_renk26, R.color.kart_renk27, R.color.kart_renk28, R.color.kart_renk29, R.color.kart_renk30
    };

    public ProductAdapter(Context context, List<Product> urunler) {
        this.context = context;
        this.urunler = urunler;
    }

    @Override
    public GorunumTutucu onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.urun_satir, parent, false);
        return new GorunumTutucu(view);
    }

    @Override
    public void onBindViewHolder(GorunumTutucu holder, int position) {
        Product urun = urunler.get(position);

        holder.kategoriAdi.setText(urun.getAd());

        Glide.with(context)
                .load(urun.getResimUrl())
                .into(holder.kategoriResmi);

        int renkIndex = position % renkListesi.length;
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, renkListesi[renkIndex]));

        holder.itemView.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(urun.getAd())
                    .setMessage("Muhafaza: " + urun.getMuhafaza())
                    .setPositiveButton("Tamam", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return urunler.size();
    }

    public static class GorunumTutucu extends RecyclerView.ViewHolder {
        ImageView kategoriResmi;
        TextView kategoriAdi;
        CardView cardView;

        public GorunumTutucu(View itemView) {
            super(itemView);
            kategoriResmi = itemView.findViewById(R.id.urunResmi);
            kategoriAdi = itemView.findViewById(R.id.urunAdi);
            cardView = itemView.findViewById(R.id.urunCardView);
        }
    }
}