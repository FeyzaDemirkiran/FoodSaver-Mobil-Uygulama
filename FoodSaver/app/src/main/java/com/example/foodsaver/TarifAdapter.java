package com.example.foodsaver;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.util.List;

public class TarifAdapter extends ArrayAdapter<Tarif> {

    private final Context context;
    private final List<Tarif> tarifList;
    private final FirestoreTarifManager firestoreManager;

    private final int[] renkListesi = {
            R.color.kart_renk1, R.color.kart_renk2, R.color.kart_renk3, R.color.kart_renk4, R.color.kart_renk5,
            R.color.kart_renk6, R.color.kart_renk7, R.color.kart_renk8, R.color.kart_renk9, R.color.kart_renk10,
            R.color.kart_renk11, R.color.kart_renk12, R.color.kart_renk13, R.color.kart_renk14, R.color.kart_renk15,
            R.color.kart_renk16, R.color.kart_renk17, R.color.kart_renk18, R.color.kart_renk19, R.color.kart_renk20,
            R.color.kart_renk21, R.color.kart_renk22, R.color.kart_renk23, R.color.kart_renk24, R.color.kart_renk25,
            R.color.kart_renk26, R.color.kart_renk27, R.color.kart_renk28, R.color.kart_renk29, R.color.kart_renk30
    };

    public TarifAdapter(Context context, List<Tarif> tarifList) {
        super(context, 0, tarifList);
        this.context = context;
        this.tarifList = tarifList;
        this.firestoreManager = new FirestoreTarifManager();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tarif, parent, false);
        }

        Tarif tarif = tarifList.get(position);

        TextView yemekAdiText = convertView.findViewById(R.id.textYemekAdi);
        ImageView yemekImage = convertView.findViewById(R.id.imageYemek);
        ImageView kalpImage = convertView.findViewById(R.id.imageFavori);
        CardView cardView = convertView.findViewById(R.id.tarifCardView);


        int renkIndex = position % renkListesi.length;
        int renk = ContextCompat.getColor(context, renkListesi[renkIndex]);
        cardView.setCardBackgroundColor(renk);


        yemekAdiText.setText(tarif.getYemekAdi());


        Glide.with(context)
                .load(tarif.getGorselUrl())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(yemekImage);


        kalpImage.setImageResource(tarif.isFavori() ? R.drawable.dolukalp : R.drawable.boskalp);


        kalpImage.setOnClickListener(v -> {
            boolean yeniFavori = !tarif.isFavori();
            tarif.setFavori(yeniFavori);
            firestoreManager.favoriGuncelle(tarif.getYemekAdi(), yeniFavori);
            notifyDataSetChanged();
        });

        return convertView;
    }
}