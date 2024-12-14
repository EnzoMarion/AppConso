package com.example.appconso.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appconso.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private EditText EditConsEau, EditConsElec, EditConsGaz, EditPers;
    private CheckBox checkboxWater, checkboxSolar, checkboxWood;
    private EditText editTextWater, editTextSolar, editTextWood;

    private static final String PREFS_NAME = "AppConsoPrefs";
    private static final String KEY_CONS_EAU = "key_cons_eau";
    private static final String KEY_CONS_ELEC = "key_cons_elec";
    private static final String KEY_CONS_GAZ = "key_cons_gaz";
    private static final String KEY_PERSONNES = "key_personnes";
    private static final String KEY_WATER = "key_water";
    private static final String KEY_SOLAR = "key_solar";
    private static final String KEY_BOIS_KWH = "key_bois_kwh";
    private static final String KEY_BOIS_ECONOMIE = "key_bois_economie";

    private static final double ECONOMIE_KWH_PAR_HEURE_BOIS = 5;
    private static final double PRIX_KWH_GAZ = 0.09; // Prix moyen du kWh de gaz en €

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialisation des champs
        EditConsEau = binding.EditConsEau;
        EditConsElec = binding.EditConsElec;
        EditConsGaz = binding.EditConsGaz;
        EditPers = binding.EditPers;

        checkboxWater = binding.checkboxWater;
        checkboxSolar = binding.checkboxSolar;
        checkboxWood = binding.checkboxWood;

        editTextWater = binding.editTextWater;
        editTextSolar = binding.editTextSolar;
        editTextWood = binding.editTextWood;

        // Rendre les champs supplémentaires invisibles
        editTextWater.setVisibility(View.GONE);
        editTextSolar.setVisibility(View.GONE);
        editTextWood.setVisibility(View.GONE);

        // Gestion des CheckBox
        checkboxWater.setOnCheckedChangeListener((buttonView, isChecked) ->
                editTextWater.setVisibility(isChecked ? View.VISIBLE : View.GONE));
        checkboxSolar.setOnCheckedChangeListener((buttonView, isChecked) ->
                editTextSolar.setVisibility(isChecked ? View.VISIBLE : View.GONE));
        checkboxWood.setOnCheckedChangeListener((buttonView, isChecked) ->
                editTextWood.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        // Sauvegarde des données
        binding.button.setOnClickListener(v -> {
            String consEauInput = EditConsEau.getText().toString();
            String consElecInput = EditConsElec.getText().toString();
            String consGazInput = EditConsGaz.getText().toString();
            String persInput = EditPers.getText().toString();

            if (!consEauInput.isEmpty() && !consElecInput.isEmpty() && !consGazInput.isEmpty() && !persInput.isEmpty()) {
                try {
                    double consEauValue = Double.parseDouble(consEauInput);
                    double consElecValue = Double.parseDouble(consElecInput);
                    double consGazValue = Double.parseDouble(consGazInput);
                    int personnesValue = Integer.parseInt(persInput);

                    // Récupérer le temps d'utilisation du chauffage au bois
                    double tempsBois = 0;
                    if (checkboxWood.isChecked() && !editTextWood.getText().toString().isEmpty()) {
                        tempsBois = Double.parseDouble(editTextWood.getText().toString());
                    }

                    // Calcul de l'économie en kWh de gaz et de l'économie financière
                    double economieKWh = tempsBois * ECONOMIE_KWH_PAR_HEURE_BOIS;
                    double economieFinanciere = economieKWh * PRIX_KWH_GAZ;

                    // Sauvegarde des données dans SharedPreferences
                    SharedPreferences sharedPreferences = getActivity()
                            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat(KEY_CONS_EAU, (float) consEauValue);
                    editor.putFloat(KEY_CONS_ELEC, (float) consElecValue);
                    editor.putFloat(KEY_CONS_GAZ, (float) consGazValue);
                    editor.putInt(KEY_PERSONNES, personnesValue);
                    editor.putFloat(KEY_BOIS_KWH, (float) economieKWh);
                    editor.putFloat(KEY_BOIS_ECONOMIE, (float) economieFinanciere);

                    // Sauvegarde pour l'eau
                    if (checkboxWater.isChecked() && !editTextWater.getText().toString().isEmpty()) {
                        editor.putFloat(KEY_WATER, Float.parseFloat(editTextWater.getText().toString()));
                    } else {
                        editor.remove(KEY_WATER);
                    }

                    // Sauvegarde pour les panneaux solaires
                    if (checkboxSolar.isChecked() && !editTextSolar.getText().toString().isEmpty()) {
                        int nombrePanneaux = Integer.parseInt(editTextSolar.getText().toString());
                        editor.putInt(KEY_SOLAR, nombrePanneaux);
                    } else {
                        editor.remove(KEY_SOLAR);
                    }

                    editor.apply();
                    Toast.makeText(getActivity(), "Données enregistrées avec succès !", Toast.LENGTH_SHORT).show();

                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Veuillez entrer des nombres valides.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
