package com.example.foodsaver;



import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FirestoreTarifManager {
    private final FirebaseFirestore db;
    private final CollectionReference tariflerRef;

    public FirestoreTarifManager() {
        db = FirebaseFirestore.getInstance();
        tariflerRef = db.collection("tarifler");
    }


    public void tarifEkle(Tarif tarif, Runnable onSuccess, Consumer<Exception> onFailure) {
        tariflerRef.add(tarif)
                .addOnSuccessListener(docRef -> onSuccess.run())
                .addOnFailureListener(e -> onFailure.accept(e));
    }


    public void tumTarifleriGetir(Consumer<List<Tarif>> onComplete) {
        tariflerRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Tarif> tarifList = new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                tarifList.add(mapleTarif(doc));
            }
            onComplete.accept(tarifList);
        });
    }


    public void favoriTarifleriGetir(Consumer<List<Tarif>> onComplete) {
        tariflerRef.whereEqualTo("favori", true).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Tarif> favoriler = new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                favoriler.add(mapleTarif(doc));
            }
            onComplete.accept(favoriler);
        });
    }


    public void favoriGuncelle(String yemekAdi, boolean favori) {
        tariflerRef.whereEqualTo("yemekAdi", yemekAdi)
                .get()
                .addOnSuccessListener(query -> {
                    for (DocumentSnapshot doc : query) {
                        tariflerRef.document(doc.getId()).update("favori", favori);
                    }
                });
    }


    public void yemekDetayiGetir(String yemekAdi, Consumer<DocumentSnapshot> onComplete) {
        tariflerRef.whereEqualTo("yemekAdi", yemekAdi)
                .limit(1)
                .get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        onComplete.accept(query.getDocuments().get(0));
                    }
                });
    }


    public void tarifAra(String kelime, Consumer<List<Tarif>> onComplete) {
        tariflerRef.get().addOnSuccessListener(querySnapshot -> {
            List<Tarif> filtrelenmis = new ArrayList<>();
            for (QueryDocumentSnapshot doc : querySnapshot) {
                String yemekAdi = doc.getString("yemekAdi");
                String malzemeler = doc.getString("malzemeler");
                if ((yemekAdi != null && yemekAdi.toLowerCase().contains(kelime.toLowerCase()))
                        || (malzemeler != null && malzemeler.toLowerCase().contains(kelime.toLowerCase()))) {
                    filtrelenmis.add(mapleTarif(doc));
                }
            }
            onComplete.accept(filtrelenmis);
        });
    }


    private Tarif mapleTarif(QueryDocumentSnapshot doc) {
        String yemekAdi = doc.getString("yemekAdi");
        String malzemeler = doc.getString("malzemeler");
        String yapilis = doc.getString("yapilis");
        String gorselUrl = doc.getString("gorselUrl");
        String besinDegeri = doc.getString("besinDegeri");
        String ipuclari = doc.getString("ipuclari");
        boolean favori = doc.getBoolean("favori") != null && doc.getBoolean("favori");
        return new Tarif(yemekAdi, malzemeler, yapilis, gorselUrl, besinDegeri, ipuclari, favori);
    }
}
