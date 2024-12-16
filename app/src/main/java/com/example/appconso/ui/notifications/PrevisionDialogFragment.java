package com.example.appconso.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.appconso.R;

public class PrevisionDialogFragment extends DialogFragment {

    // Déclaration des variables pour les champs de saisie et le bouton
    private EditText previsionEauEditText;
    private EditText previsionElecEditText;
    private EditText previsionGazEditText;
    private Button saveButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_prevision_dialog, container, false);

        // Initialisation des champs de saisie pour les prévisions
        previsionEauEditText = root.findViewById(R.id.editConsEau);
        previsionElecEditText = root.findViewById(R.id.editConsElec);
        previsionGazEditText = root.findViewById(R.id.editConsGaz);
        saveButton = root.findViewById(R.id.buttonSavePrevision);

        // Récupérer les valeurs enregistrées dans SharedPreferences si elles existent et les afficher dans les champs
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(NotificationsFragment.PREFS_NAME, Context.MODE_PRIVATE);
        previsionEauEditText.setText(String.valueOf(sharedPreferences.getFloat(NotificationsFragment.getKeyPrevisionEau(), 0)));
        previsionElecEditText.setText(String.valueOf(sharedPreferences.getFloat(NotificationsFragment.getKeyPrevisionElec(), 0)));
        previsionGazEditText.setText(String.valueOf(sharedPreferences.getFloat(NotificationsFragment.getKeyPrevisionGaz(), 0)));

        // Lorsque le bouton est cliqué, on enregistre les prévisions
        saveButton.setOnClickListener(v -> savePrevisions());

        return root;
    }

    // Méthode pour enregistrer les prévisions
    private void savePrevisions() {
        // Obtenir les valeurs saisies par l'utilisateur dans les champs
        String eauStr = previsionEauEditText.getText().toString();
        String elecStr = previsionElecEditText.getText().toString();
        String gazStr = previsionGazEditText.getText().toString();

        // Vérifier si tous les champs sont remplis
        if (eauStr.isEmpty() || elecStr.isEmpty() || gazStr.isEmpty()) {
            Toast.makeText(getActivity(), "Veuillez entrer toutes les prévisions", Toast.LENGTH_SHORT).show();
            return; // Retourner si des champs sont vides
        }

        // Convertir les valeurs saisies en float
        float previsionEau = Float.parseFloat(eauStr);
        float previsionElec = Float.parseFloat(elecStr);
        float previsionGaz = Float.parseFloat(gazStr);

        // Sauvegarder les prévisions dans SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(NotificationsFragment.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(NotificationsFragment.getKeyPrevisionEau(), previsionEau);
        editor.putFloat(NotificationsFragment.getKeyPrevisionElec(), previsionElec);
        editor.putFloat(NotificationsFragment.getKeyPrevisionGaz(), previsionGaz);
        editor.apply();  // Appliquer les modifications

        // Afficher un message pour confirmer l'enregistrement
        Toast.makeText(getActivity(), "Prévisions enregistrées", Toast.LENGTH_SHORT).show();

        // Fermer le dialog
        dismiss();
    }
}
