package com.example.tracker.SecondFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProductCardAdapter extends RecyclerView.Adapter<ProductCardAdapter.CardViewHolder> {

    private ArrayList<ArrayList<String>> products = new ArrayList<>(); // Liste der Produkte

    public ProductCardAdapter(){

    }

    @NonNull
    @Override
    public ProductCardAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_second_product_cardview, parent, false);
        return new ProductCardAdapter.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCardAdapter.CardViewHolder holder, int position) {
        ArrayList<String> product = products.get(position);

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(currentDate);


        if (product.get(0).equals(formattedDate)) {
            holder.productName.setText(product.get(1));
            holder.kcalCount.setText(product.get(2));
            holder.carbsCount.setText(product.get(3));
            holder.proteinCount.setText(product.get(4));
            holder.fatCount.setText(product.get(5));
        }


    }

    @Override
    public int getItemCount() {
        return products.size(); // Größe der Liste
    }

    public void addItem(ArrayList<String> product) {
        products.add(product); // Produkt hinzufügen
        notifyItemInserted(products.size() - 1); // RecyclerView aktualisieren
    }

    public void setItems(ArrayList<ArrayList<String>> productList) {
        this.products = productList; // Liste ersetzen
        notifyDataSetChanged(); // RecyclerView neu laden
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView productName;
        TextView kcalCount;
        TextView carbsCount;
        TextView proteinCount;
        TextView fatCount;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.ProductCardView);

            productName = itemView.findViewById(R.id.ProductName);
            kcalCount = itemView.findViewById(R.id.ProductKcal);
            carbsCount = itemView.findViewById(R.id.ProductCarbs);
            proteinCount = itemView.findViewById(R.id.ProductProtein);
            fatCount = itemView.findViewById(R.id.ProductFat);


        }
    }
}
