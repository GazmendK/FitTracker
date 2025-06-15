package com.example.tracker.SecondFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.tracker.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DailyOverview  {

    private Context context;
    private View rootView;
    private NutritionDatabase nutritionDatabase;
    private MaxNutritionsData maxNutritionsData;
    View includedView;
    ProductCardAdapter adapter;

    public DailyOverview(Context context, View rootView, NutritionDatabase nutritionDatabase, MaxNutritionsData maxNutritionsData, View includedView, ProductCardAdapter adapter) {
        this.context = context;
        this.rootView = rootView;
        this.nutritionDatabase = nutritionDatabase;
        this.maxNutritionsData = maxNutritionsData;
        this.includedView = includedView;
        this.adapter = adapter;
    }






    public void loadNutritionCardView() {
        CircularProgressIndicator KcalCountBar = rootView.findViewById(R.id.progressCalories);
        LinearProgressIndicator CarbCountBar = rootView.findViewById(R.id.CarbCountBar);
        LinearProgressIndicator ProteinCountBar = rootView.findViewById(R.id.ProteinCountBar);
        LinearProgressIndicator FatCountBar = rootView.findViewById(R.id.FatCountBar);

        TextView currentKcal = rootView.findViewById(R.id.currentKcal);
        TextView currentCarbs = rootView.findViewById(R.id.currentCarb);
        TextView currentProtein = rootView.findViewById(R.id.currentProtein);
        TextView currentFat = rootView.findViewById(R.id.currentFat);

        double totalCalories = 0;
        double totalCarbs = 0;
        double totalProteins = 0;
        double totalFats = 0;

        ArrayList<ArrayList<String>> nutrition = nutritionDatabase.loadNutritionData();
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(currentDate);

        // Summe der Kalorien nur für den aktuellen Tag berechnen
        for (int i = nutrition.size() - 1; i >= 0; i--) {
            ArrayList<String> item = nutrition.get(i);

            if (item.get(0).equals(formattedDate)) {
                try {
                    totalCalories += (Double.parseDouble(item.get(2)) * 100); // Kalorien-Wert (Index anpassen, falls nötig)
                    totalCarbs += (Double.parseDouble(item.get(3)) * 100); // Kalorien-Wert (Index anpassen, falls nötig)
                    totalProteins += (Double.parseDouble(item.get(4)) * 100); // Kalorien-Wert (Index anpassen, falls nötig)
                    totalFats += (Double.parseDouble(item.get(5)) * 100); // Kalorien-Wert (Index anpassen, falls nötig)
                } catch (NumberFormatException e) {
                    Log.e("DailyOverview", "Fehler beim Parsen von Kalorien: " + item.get(2), e);
                }
            } else {
                break;
            }
        }

        currentKcal.setText(String.valueOf((int) Math.round(totalCalories / 100)));
        currentCarbs.setText(String.valueOf((int) Math.round(totalCarbs / 100)) + "g / ");
        currentProtein.setText(String.valueOf((int) Math.round(totalProteins / 100)) + "g / ");
        currentFat.setText(String.valueOf((int) Math.round(totalFats / 100)) + "g / ");

        KcalCountBar.setProgress((int) totalCalories, true);
        CarbCountBar.setProgress((int) totalCarbs, true);
        ProteinCountBar.setProgress(((int) totalProteins), true);
        FatCountBar.setProgress((int) totalFats, true);

        Log.d("DailyOverview", "Kcal-Fortschritt gesetzt: " + totalCalories);
    }

    @SuppressLint("SetTextI18n")
    public void settings() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view1 = LayoutInflater.from(context).inflate(R.layout.fragment_second_adjust_nutritions_sheet, null);
        bottomSheetDialog.setContentView(view1);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view1.getParent());
        behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);  // Sicherstellen, dass es an den Inhalt angepasst wird
        bottomSheetDialog.show();

        TextInputEditText editKcal = view1.findViewById(R.id.editTextWeightKcal);
        TextInputEditText editCarbs = view1.findViewById(R.id.editTextWeightCarbs);
        TextInputEditText editProtein = view1.findViewById(R.id.editTextWeightProtein);
        TextInputEditText editFat = view1.findViewById(R.id.editTextWeightfat);




        Button submit = view1.findViewById(R.id.editNutritionButton);

        submit.setOnClickListener(v -> {

            if(maxNutritionsData.loadNutritionData() == null){
                maxNutritionsData.insertValue(2500,250,188,83);
            }else{
                maxNutritionsData.updateMaxNutrition(
                        Double.parseDouble(Objects.requireNonNull(editKcal.getText()).toString()),
                        Double.parseDouble(Objects.requireNonNull(editCarbs.getText()).toString()),
                        Double.parseDouble(Objects.requireNonNull(editProtein.getText()).toString()),
                        Double.parseDouble(Objects.requireNonNull(editFat.getText()).toString()));
            }



            writeMaxNutritions();

            bottomSheetDialog.dismiss();

        });

        view1.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            // Sichtbaren Bereich des Viewports abrufen
            view1.getWindowVisibleDisplayFrame(rect);
            int screenHeight = view1.getRootView().getHeight();
            int keypadHeight = screenHeight - rect.bottom;

            // Wenn die Tastatur sichtbar ist
            if (keypadHeight > screenHeight * 0.15) {
                // Tastatur ist sichtbar, das Bottom Sheet nach oben verschieben
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);  // BottomSheet öffnen
            } else {
                // Tastatur ist nicht sichtbar
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);  // BottomSheet zusammenfalten
            }
        });


    }

    public void writeMaxNutritions() {
        TextView maxKcal = includedView.findViewById(R.id.maxKcal);
        TextView maxCarb = includedView.findViewById(R.id.maxCarb);
        TextView maxProtein = includedView.findViewById(R.id.maxProtein);
        TextView maxFat = includedView.findViewById(R.id.maxFat);

        if (maxNutritionsData.loadNutritionData().isEmpty()) {
            // Wenn keine Daten vorhanden, Standardwerte einfügen
            maxNutritionsData.insertValue(2500, 250, 188, 83);

            // Werte direkt im UI setzen
            maxKcal.setText("2500");
            maxCarb.setText("250g");
            maxProtein.setText("188g");
            maxFat.setText("83g");
        }else{
            maxKcal.setText(maxNutritionsData.loadNutritionData().get(0).toString());
            maxCarb.setText(maxNutritionsData.loadNutritionData().get(1).toString() + "g");
            maxProtein.setText(maxNutritionsData.loadNutritionData().get(2).toString() + "g");
            maxFat.setText(maxNutritionsData.loadNutritionData().get(3).toString() + "g");
        }

        CircularProgressIndicator KcalCountBar = includedView.findViewById(R.id.progressCalories);
        LinearProgressIndicator CarbCountBar = includedView.findViewById(R.id.CarbCountBar);
        LinearProgressIndicator ProteinCountBar = includedView.findViewById(R.id.ProteinCountBar);
        LinearProgressIndicator FatCountBar = includedView.findViewById(R.id.FatCountBar);

        KcalCountBar.setMax(maxNutritionsData.loadNutritionData().get(0)*100);
        CarbCountBar.setMax(maxNutritionsData.loadNutritionData().get(1)*100);
        ProteinCountBar.setMax(maxNutritionsData.loadNutritionData().get(2)*100);
        FatCountBar.setMax(maxNutritionsData.loadNutritionData().get(3)*100);



    }

    public void bottomSheet(Bundle args) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view1 = LayoutInflater.from(context).inflate(R.layout.fragment_second_nutrition_sheet, null);
        bottomSheetDialog.setContentView(view1);

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view1.getParent());
        behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);  // Sicherstellen, dass es an den Inhalt angepasst wird


        bottomSheetDialog.show();


        TextView productName = view1.findViewById(R.id.ProductName);
        TextView calorieCount = view1.findViewById(R.id.CalorieCountProduct);
        TextView carbCount = view1.findViewById(R.id.CarbCountProduct);
        TextView proteinCount = view1.findViewById(R.id.ProteinCountProduct);
        TextView fatCount = view1.findViewById(R.id.FatCountProduct);

        Button buttonManuel = view1.findViewById(R.id.buttonManuel);
        Button buttonServing = view1.findViewById(R.id.buttonServing);

        TextInputEditText editTextWeight = view1.findViewById(R.id.editTextWeight);
        TextInputLayout textInputLayout = view1.findViewById(R.id.textFieldLayout);


        ArrayList<String> nutrition =new ArrayList<>();
        if (args != null) {
            for (int i=0; i<args.size(); i++){
                nutrition.add(args.getString(""+i));
                Log.d("TableFragment", "Card ID: " + nutrition.get(i) + "   " +i);
            }

            productName.setText(nutrition.get(1));

            Button submit = view1.findViewById(R.id.insertNutrition);

            final AtomicInteger mode = new AtomicInteger(0); // 0: kein Modus, 1: Manuell, 2: Serving
            buttonManuel.setOnClickListener(v -> {
                mode.set(1);
                textInputLayout.setHint("How many grams");
            });
            buttonServing.setOnClickListener(v -> {
                mode.set(2);
                textInputLayout.setHint("How many portions");
            });

            editTextWeight.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    String weightText = editTextWeight.getText().toString().trim();
                    if (!weightText.isEmpty()) {
                        try {
                            double factor;

                            // Berechne den Faktor basierend auf dem Modus
                            if (mode.get() == 1) {
                                factor = 0.01; // Manuell
                            } else if (mode.get() == 2) {
                                // Verwende nutrition.get(5) und berechne den Faktor für Serving
                                factor = Double.parseDouble(nutrition.get(6).replace(",", ".")) / 100;
                            } else {
                                Log.w("SecondFragment", "Kein Modus ausgewählt");
                                return; // Breche die Berechnung ab, wenn kein Modus ausgewählt ist
                            }

                            // Berechne die Werte basierend auf dem Gewicht und dem Faktor
                            double weight = Double.parseDouble(weightText) * factor;

                            // Setze die berechneten Werte in die TextViews
                            calorieCount.setText(String.format("%.2f", Double.parseDouble(nutrition.get(2)) * weight));
                            carbCount.setText(String.format("%.2f", Double.parseDouble(nutrition.get(3)) * weight));
                            proteinCount.setText(String.format("%.2f", Double.parseDouble(nutrition.get(4)) * weight));
                            fatCount.setText(String.format("%.2f", Double.parseDouble(nutrition.get(5)) * weight));
                        } catch (NumberFormatException e) {
                            Log.e("SecondFragment", "Ungültige Eingabe im Gewichtsfeld", e);
                        }
                    } else {
                        Log.w("SecondFragment", "Gewichtsfeld ist leer!");
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onClick(View v) {
                    String weightText = editTextWeight.getText().toString().trim();
                    if (!weightText.isEmpty()) {
                        try {
                            double factor;

                            // Berechne den Faktor basierend auf dem Modus
                            if (mode.get() == 1) {
                                factor = 0.01; // Manuell

                            } else if (mode.get() == 2) {
                                // Verwende nutrition.get(5) und berechne den Faktor für Serving
                                factor = Double.parseDouble(nutrition.get(6).replace(",", ".")) / 100;
                            } else {
                                Log.w("SecondFragment", "Kein Modus ausgewählt");
                                return; // Breche die Berechnung ab, wenn kein Modus ausgewählt ist
                            }

                            // Berechne die Werte basierend auf dem Gewicht und dem Faktor
                            double weight = Double.parseDouble(weightText) * factor;

                            nutrition.set(2, String.format("%.2f", Double.parseDouble(nutrition.get(2).replace(",", ".")) * weight ).replace(",", "."));
                            nutrition.set(3, String.format("%.2f", Double.parseDouble(nutrition.get(3).replace(",", ".")) * weight ).replace(",", "."));
                            nutrition.set(4, String.format("%.2f", Double.parseDouble(nutrition.get(4).replace(",", ".")) * weight ).replace(",", "."));
                            nutrition.set(5, String.format("%.2f", Double.parseDouble(nutrition.get(5).replace(",", ".")) * weight ).replace(",", "."));

                            // In die Datenbank speichern
                            saveNutritionToDatabase(nutrition);
                            bottomSheetDialog.dismiss();

                            adapter.addItem(nutrition);

                        } catch (NumberFormatException e) {
                            Log.e("SecondFragment", "Ungültige Eingabe im Gewichtsfeld", e);
                        }
                    } else {
                        Log.w("SecondFragment", "Gewichtsfeld ist leer!");
                    }
                }
            });
        }

        view1.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            // Sichtbaren Bereich des Viewports abrufen
            view1.getWindowVisibleDisplayFrame(rect);
            int screenHeight = view1.getRootView().getHeight();
            int keypadHeight = screenHeight - rect.bottom;

            // Wenn die Tastatur sichtbar ist
            if (keypadHeight > screenHeight * 0.15) {
                // Tastatur ist sichtbar, das Bottom Sheet nach oben verschieben
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);  // BottomSheet öffnen
            } else {
                // Tastatur ist nicht sichtbar
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);  // BottomSheet zusammenfalten
            }
        });

    }


    private void saveNutritionToDatabase(ArrayList<String> nutrition) {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);


        String date = nutrition.get(0);
        String name = nutrition.get(1);
        double calorie = Double.parseDouble(nutrition.get(2));
        double carb = Double.parseDouble(nutrition.get(3));
        double protein = Double.parseDouble(nutrition.get(4));
        double fat = Double.parseDouble(nutrition.get(5));
        double serving = Double.parseDouble(nutrition.get(6));

        nutritionDatabase.insertValue(date, name, calorie, carb, protein, fat, serving);
        loadNutritionCardView();

    }


}
