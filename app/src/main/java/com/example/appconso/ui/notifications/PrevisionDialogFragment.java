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

    private EditText previsionEauEditText;
    private EditText previsionElecEditText;
    private EditText previsionGazEditText;
    private Button saveButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_prevision_dialog, container, false);

        previsionEauEditText = root.findViewById(R.id.editConsEau); // Assurez-vous que les ID sont corrects
        previsionElecEditText = root.findViewById(R.id.editConsElec);
        previsionGazEditText = root.findViewById(R.id.editConsGaz);
        saveButton = root.findViewById(R.id.buttonSavePrevision);

        // Set the current values in the fields if they exist
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(NotificationsFragment.PREFS_NAME, Context.MODE_PRIVATE);
        previsionEauEditText.setText(String.valueOf(sharedPreferences.getFloat(NotificationsFragment.getKeyPrevisionEau(), 0)));
        previsionElecEditText.setText(String.valueOf(sharedPreferences.getFloat(NotificationsFragment.getKeyPrevisionElec(), 0)));
        previsionGazEditText.setText(String.valueOf(sharedPreferences.getFloat(NotificationsFragment.getKeyPrevisionGaz(), 0)));

        saveButton.setOnClickListener(v -> savePrevisions());

        return root;
    }

    private void savePrevisions() {
        // Get the values entered by the user
        String eauStr = previsionEauEditText.getText().toString();
        String elecStr = previsionElecEditText.getText().toString();
        String gazStr = previsionGazEditText.getText().toString();

        // Check if the values are valid
        if (eauStr.isEmpty() || elecStr.isEmpty() || gazStr.isEmpty()) {
            Toast.makeText(getActivity(), "Veuillez entrer toutes les prévisions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the values to float
        float previsionEau = Float.parseFloat(eauStr);
        float previsionElec = Float.parseFloat(elecStr);
        float previsionGaz = Float.parseFloat(gazStr);

        // Save the values in SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(NotificationsFragment.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(NotificationsFragment.getKeyPrevisionEau(), previsionEau);
        editor.putFloat(NotificationsFragment.getKeyPrevisionElec(), previsionElec);
        editor.putFloat(NotificationsFragment.getKeyPrevisionGaz(), previsionGaz);
        editor.apply();

        // Notify the user
        Toast.makeText(getActivity(), "Prévisions enregistrées", Toast.LENGTH_SHORT).show();

        // Close the dialog
        dismiss();
    }
}
