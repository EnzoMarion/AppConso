package com.example.appconso.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.appconso.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private EditText EditConsEau, EditConsElec, EditConsGaz, EditPers;

    private static final String PREFS_NAME = "AppConsoPrefs";
    private static final String KEY_CONS_EAU = "key_cons_eau";
    private static final String KEY_CONS_ELEC = "key_cons_elec";
    private static final String KEY_CONS_GAZ = "key_cons_gaz";
    private static final String KEY_PERSONNES = "key_personnes";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditConsEau = binding.EditConsEau;
        EditConsElec = binding.EditConsElec;
        EditConsGaz = binding.EditConsGaz;
        EditPers = binding.EditPers;  // Ajout du champ pour le nombre de personnes

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
                    editor.putInt(KEY_PERSONNES, personnesValue);  // Sauvegarde du nombre de personnes
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
