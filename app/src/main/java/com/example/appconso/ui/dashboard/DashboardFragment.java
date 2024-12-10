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
import androidx.lifecycle.ViewModelProvider;

import com.example.appconso.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private EditText EditConsEau, EditConsElec, EditConsGaz, EditPers;
    private CheckBox checkboxWater, checkboxSolar, checkboxWood; // Déclarations des CheckBox
    private EditText editTextWater, editTextSolar, editTextWood; // Déclarations des EditText

    private static final String PREFS_NAME = "AppConsoPrefs";
    private static final String KEY_CONS_EAU = "key_cons_eau";
    private static final String KEY_CONS_ELEC = "key_cons_elec";
    private static final String KEY_CONS_GAZ = "key_cons_gaz";
    private static final String KEY_PERSONNES = "key_personnes";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialisation des EditText et CheckBox
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

        // Initialisez les EditText à invisibles par défaut
        editTextWater.setVisibility(View.GONE);
        editTextSolar.setVisibility(View.GONE);
        editTextWood.setVisibility(View.GONE);

        // Ecouteur pour la CheckBox de la récupération d'eau de pluie
        checkboxWater.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editTextWater.setVisibility(View.VISIBLE);
            } else {
                editTextWater.setVisibility(View.GONE);
            }
        });

        // Ecouteur pour la CheckBox des panneaux solaires
        checkboxSolar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editTextSolar.setVisibility(View.VISIBLE);
            } else {
                editTextSolar.setVisibility(View.GONE);
            }
        });

        // Ecouteur pour la CheckBox du chauffage au bois
        checkboxWood.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editTextWood.setVisibility(View.VISIBLE);
            } else {
                editTextWood.setVisibility(View.GONE);
            }
        });

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

                    // Sauvegarde des données dans SharedPreferences
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat(KEY_CONS_EAU, (float) consEauValue);
                    editor.putFloat(KEY_CONS_ELEC, (float) consElecValue);
                    editor.putFloat(KEY_CONS_GAZ, (float) consGazValue);
                    editor.putInt(KEY_PERSONNES, personnesValue);

                    // Ajoutez ici la sauvegarde des valeurs des autres champs si nécessaire
                    if (checkboxWater.isChecked()) {
                        String waterInput = editTextWater.getText().toString();
                        if (!waterInput.isEmpty()) {
                            editor.putFloat("key_water", Float.parseFloat(waterInput));
                        }
                    }
                    if (checkboxSolar.isChecked()) {
                        String solarInput = editTextSolar.getText().toString();
                        if (!solarInput.isEmpty()) {
                            editor.putFloat("key_solar", Float.parseFloat(solarInput));
                        }
                    }
                    if (checkboxWood.isChecked()) {
                        String woodInput = editTextWood.getText().toString();
                        if (!woodInput.isEmpty()) {
                            editor.putFloat("key_wood", Float.parseFloat(woodInput));
                        }
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
