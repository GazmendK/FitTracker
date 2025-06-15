package com.example.tracker.ThirdFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tracker.R;

import java.util.AbstractMap;
import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private final ArrayList<ArrayList<String>> plans;
    private final OnItemClickListener onItemClickListener;
    private final OnChangeNameClickListener changeNameClickListener;

    public CardAdapter(ArrayList<ArrayList<String>> plans, OnItemClickListener listener, OnChangeNameClickListener ChangeNameClickListener) {
        this.plans = plans;
        this.onItemClickListener = listener;
        this.changeNameClickListener = ChangeNameClickListener;
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Hier wird das Layout des CardView geladen
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_third_cardview, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ArrayList<String> plan = plans.get(position);
        holder.textView.setText(plan.get(1));
        holder.exerciseCount.setText(plan.get(2) + " Exercises");
        holder.dayCount.setText(plan.get(3) + " Workouts");


        View.OnClickListener onChangeNameClicked = v -> {
            changeNameClickListener.onChangeNameClicked(position);
        };

        holder.test(onChangeNameClicked);

        holder.cardView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    // Methode zum Hinzufügen eines neuen Items
    public void addItem(String pos, String name, String exercises, String sets) {
        ArrayList<String> etwas = new ArrayList<>();
        etwas.add(pos);
        etwas.add(name);
        etwas.add(exercises);
        etwas.add(sets);


        plans.add(etwas);
        notifyItemInserted(plans.size() - 1);  // Benachrichtige den Adapter über die hinzugefügte Zeile
    }

    public void updateItem(String pos, String name, String exercises, String sets) {
        ArrayList<String> etwas = new ArrayList<>();
        etwas.add(pos);
        etwas.add(name);
        etwas.add(exercises);
        etwas.add(sets);

        int position = Integer.parseInt(pos);
        plans.set(position, etwas);
    }



    // ViewHolder-Klasse
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView; // Hier verwendest du CardView und nicht TextView
        TextView textView;
        TextView exerciseCount;
        TextView dayCount;
        View editPlan;


        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.item_card); // Der CardView wird korrekt referenziert
            textView = itemView.findViewById(R.id.idTVCourseName);
            exerciseCount = itemView.findViewById(R.id.uebung_count);
            dayCount = itemView.findViewById(R.id.daysCount);
            editPlan = itemView.findViewById(R.id.editPlan);
        }

        public void test(View.OnClickListener onChangeNameClicked) {
            editPlan.setOnClickListener(onChangeNameClicked);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnChangeNameClickListener {
        void onChangeNameClicked(int position);
    }

}
