package com.example.appconso.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import com.example.appconso.R;

public class NotificationsFragment extends Fragment {

    private static final String PREFS_NAME = "AppConsoPrefs";
    private static final String KEY_CONS_EAU = "key_cons_eau";
    private static final String KEY_CONS_ELEC = "key_cons_elec";
    private static final String KEY_CONS_GAZ = "key_cons_gaz";
    private static final String KEY_PERSONNES = "key_personnes";

    // Moyennes nationales par personne (en unités mensuelles)
    private static final double MOYENNE_EAU = 4.55; // m³ par mois
    private static final double MOYENNE_ELEC = 300; // kWh par mois
    private static final double MOYENNE_GAZ = 100; // m³ par mois

    private TextView consEauTextView, consElecTextView, consGazTextView;
    private TextView comparaisonEau, comparaisonElec, comparaisonGaz;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Lier la vue avec le layout
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Initialisation des TextViews
        consEauTextView = root.findViewById(R.id.consEau);
        consElecTextView = root.findViewById(R.id.consElec);
        consGazTextView = root.findViewById(R.id.consGaz);
        comparaisonEau = root.findViewById(R.id.comparaisonEau);
        comparaisonElec = root.findViewById(R.id.comparaisonElec);
        comparaisonGaz = root.findViewById(R.id.comparaisonGaz);

        // Récupérer les données de consommation et le nombre de personnes
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float consEau = sharedPreferences.getFloat(KEY_CONS_EAU, -1);
        float consElec = sharedPreferences.getFloat(KEY_CONS_ELEC, -1);
        float consGaz = sharedPreferences.getFloat(KEY_CONS_GAZ, -1);
        int personnes = sharedPreferences.getInt(KEY_PERSONNES, 1); // Par défaut 1 personne

        // Calcul de la consommation par personne
        double consEauParPersonne = consEau / personnes;
        double consElecParPersonne = consElec / personnes;
        double consGazParPersonne = consGaz / personnes;

        // Mise à jour de l'affichage avec les valeurs récupérées
        if (consEau != -1) {
            consEauTextView.setText("Consommation d'eau: " + consEau + " m³");
            updateComparison(consEauParPersonne, MOYENNE_EAU, comparaisonEau, "eau");
        } else {
            consEauTextView.setText("Aucune donnée pour la consommation d'eau");
            comparaisonEau.setVisibility(View.GONE);
        }

        if (consElec != -1) {
            consElecTextView.setText("Consommation d'électricité: " + consElec + " kWh");
            updateComparison(consElecParPersonne, MOYENNE_ELEC, comparaisonElec, "électricité");
        } else {
            consElecTextView.setText("Aucune donnée pour la consommation d'électricité");
            comparaisonElec.setVisibility(View.GONE);
        }

        if (consGaz != -1) {
            consGazTextView.setText("Consommation de gaz: " + consGaz + " m³");
            updateComparison(consGazParPersonne, MOYENNE_GAZ, comparaisonGaz, "gaz");
        } else {
            consGazTextView.setText("Aucune donnée pour la consommation de gaz");
            comparaisonGaz.setVisibility(View.GONE);
        }

        return root;
    }

    private void updateComparison(double consParPersonne, double moyenne, TextView comparaisonTextView, String typeConsommation) {
        double difference = consParPersonne - moyenne;
        double pourcentage = (difference / moyenne) * 100; // Calcul de la différence en pourcentage
        String message;
        int color;

        // Déterminer si la consommation est plus ou moins que la moyenne
        if (difference == 0) {
            message = String.format("Vous consommez **0%%** (0.00) d'%s que la moyenne française par personne par mois (%.2f).", typeConsommation, moyenne);
            color = getResources().getColor(android.R.color.darker_gray); // Couleur neutre
        } else if (difference < 0) {
            message = String.format("Vous consommez **%.2f%%** (%.2f) **moins** d'%s que la moyenne française par personne par mois (%.2f).",
                    Math.abs(pourcentage), Math.abs(difference), typeConsommation, moyenne);
            color = getResources().getColor(android.R.color.holo_green_dark); // Consommation inférieure à la moyenne
        } else {
            message = String.format("Vous consommez **%.2f%%** (%.2f) **plus** d'%s que la moyenne française par personne par mois (%.2f).",
                    pourcentage, difference, typeConsommation, moyenne);
            color = getResources().getColor(android.R.color.holo_red_dark); // Consommation supérieure à la moyenne
        }

        comparaisonTextView.setText(message);
        comparaisonTextView.setTextColor(color);
    }
}
