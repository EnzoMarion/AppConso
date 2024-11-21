package com.example.appconso.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.appconso.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private EditText EditConsEau;
    private TextView consEau;

    private static final String PREFS_NAME = "AppConsoPrefs";
    private static final String KEY_CONSOMMATION = "key_consommation";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditConsEau = binding.EditConsEau;
        consEau = binding.ConsEau;


        // Récupérer la consommation d'eau enregistrée (si disponible) au démarrage
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        double consommation = sharedPreferences.getFloat(KEY_CONSOMMATION, -1); // -1 si pas de donnée
        if (consommation != -1) {
            consEau.setText("Consommation d'eau : " + consommation + " litres");
        }

        // Bouton pour enregistrer la consommation
        binding.button.setOnClickListener(v -> {
            String input = EditConsEau.getText().toString();
            if (!input.isEmpty()) {
                try {
                    double consommationValue = Double.parseDouble(input);
                    consEau.setText("Consommation d'eau : " + consommationValue + " litres");

                    // Sauvegarder la consommation d'eau dans SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat(KEY_CONSOMMATION, (float) consommationValue);
                    editor.apply();  // Sauvegarde de manière asynchrone

                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Veuillez entrer un nombre valide.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Veuillez remplir le champ.", Toast.LENGTH_SHORT).show();
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
