package com.example.appconso.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appconso.R;

public class NotificationsFragment extends Fragment {

    // Définition des clés utilisées pour accéder aux SharedPreferences
    static final String PREFS_NAME = "AppConsoPrefs";
    static final String KEY_CONS_EAU = "key_cons_eau";
    static final String KEY_CONS_ELEC = "key_cons_elec";
    static final String KEY_CONS_GAZ = "key_cons_gaz";
    private static final String KEY_PERSONNES = "key_personnes";
    private static final String KEY_WATER = "key_water";
    private static final String KEY_SOLAR = "key_solar";
    private static final String KEY_BOIS_KWH = "key_bois_kwh";
    private static final String KEY_BOIS_ECONOMIE = "key_bois_economie";

    private static final String KEY_PREVISION_EAU = "key_prevision_eau";
    private static final String KEY_PREVISION_ELEC = "key_prevision_elec";
    private static final String KEY_PREVISION_GAZ = "key_prevision_gaz";

    // Définition des moyennes de consommation et des prix
    private static final double MOYENNE_EAU = 4.55; // m³ par mois
    private static final double MOYENNE_ELEC = 300; // kWh par mois
    private static final double MOYENNE_GAZ = 186; // kWh de gaz par mois
    private static final double PRIX_EAU = 3.5; // €/m³ (exemple)
    private static final double PRIX_ELEC_KWH = 0.15; // Prix moyen de l'électricité en €/kWh

    // Déclaration des éléments de l'interface utilisateur
    private TextView consEauTextView, consElecTextView, consGazTextView;
    private TextView comparaisonEau, comparaisonElec, comparaisonGaz, consPluie, pluie;
    private TextView consSolaire, Solaire;
    private TextView consBois, Bois;
    private Button buttonPrevision;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Infla la vue du fragment
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Initialisation des vues
        consEauTextView = root.findViewById(R.id.consEau);
        consElecTextView = root.findViewById(R.id.consElec);
        consGazTextView = root.findViewById(R.id.consGaz);
        comparaisonEau = root.findViewById(R.id.comparaisonEau);
        comparaisonElec = root.findViewById(R.id.comparaisonElec);
        comparaisonGaz = root.findViewById(R.id.comparaisonGaz);
        consPluie = root.findViewById(R.id.consPluie);
        pluie = root.findViewById(R.id.pluie);
        consSolaire = root.findViewById(R.id.consSolaire);
        Solaire = root.findViewById(R.id.Solaire);
        consBois = root.findViewById(R.id.consBois);
        Bois = root.findViewById(R.id.Bois);
        buttonPrevision = root.findViewById(R.id.buttonPrevision);

        // Récupération des données depuis SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float consEau = sharedPreferences.getFloat(KEY_CONS_EAU, -1);
        float consElec = sharedPreferences.getFloat(KEY_CONS_ELEC, -1);
        float consGaz = sharedPreferences.getFloat(KEY_CONS_GAZ, -1);
        float eauDePluie = sharedPreferences.getFloat(KEY_WATER, -1);
        int personnes = sharedPreferences.getInt(KEY_PERSONNES, 1);
        int nombrePanneaux = sharedPreferences.getInt(KEY_SOLAR, 0);

        // Récupération des données sur le chauffage au bois
        float boisKwh = sharedPreferences.getFloat(KEY_BOIS_KWH, 0);
        float economieBois = sharedPreferences.getFloat(KEY_BOIS_ECONOMIE, 0);

        // Récupération des prévisions
        float previsionEau = sharedPreferences.getFloat(KEY_PREVISION_EAU, -1);
        float previsionElec = sharedPreferences.getFloat(KEY_PREVISION_ELEC, -1);
        float previsionGaz = sharedPreferences.getFloat(KEY_PREVISION_GAZ, -1);

        // Affichage de l'information sur l'eau de pluie
        if (eauDePluie != -1) {
            double economie = eauDePluie * PRIX_EAU / 1000;
            pluie.setText(String.format("Vous avez récupéré %.2f litres d'eau de pluie, économisant %.2f €.", eauDePluie, economie));
        } else {
            pluie.setText("Aucune donnée sur l'eau de pluie.");
        }

        // Affichage des informations sur le chauffage au bois
        if (boisKwh > 0) {
            consBois.setText("Chauffage au bois :");
            Bois.setText(String.format("Temps d'utilisation : %.2f heures\nÉconomie de gaz : %.2f kWh\nÉconomie financière : %.2f €", boisKwh / 5, boisKwh, economieBois));
        } else {
            consBois.setText("Aucune donnée sur le chauffage au bois.");
            Bois.setText("Aucune donnée disponible.");
        }

        // Affichage des consommations d'eau, d'électricité et de gaz
        if (consEau != -1) {
            consEauTextView.setText("Consommation d'eau: " + consEau + " m³");
            double moyenneEau = MOYENNE_EAU * personnes;
            updateComparison(consEau, moyenneEau, comparaisonEau, "eau", personnes, previsionEau);
        }

        if (consElec != -1) {
            consElecTextView.setText("Consommation d'électricité: " + consElec + " kWh");
            double moyenneElec = MOYENNE_ELEC * personnes;
            updateComparison(consElec, moyenneElec, comparaisonElec, "électricité", personnes, previsionElec);
        }

        if (consGaz != -1) {
            consGazTextView.setText("Consommation de gaz: " + consGaz + " kWh");
            double moyenneGaz = MOYENNE_GAZ * personnes;
            updateComparison(consGaz, moyenneGaz, comparaisonGaz, "gaz", personnes, previsionGaz);
        }

        // Affichage des panneaux solaires
        if (nombrePanneaux > 0) {
            double productionSolaireMensuelle = nombrePanneaux * 25; // kWh
            double economieFinanciere = productionSolaireMensuelle * PRIX_ELEC_KWH;

            consSolaire.setText("Panneaux solaires " + nombrePanneaux + " panneaux");
            Solaire.setText(String.format("Production mensuelle : %.2f kWh\nÉconomie financière : %.2f €", productionSolaireMensuelle, economieFinanciere));
        } else {
            consSolaire.setText("Aucun panneau solaire");
            Solaire.setText("Aucune donnée disponible.");
        }

        // Ajout de l'action du bouton pour afficher le PrevisionDialogFragment
        buttonPrevision.setOnClickListener(v -> {
            // Créer et afficher le DialogFragment pour les prévisions
            PrevisionDialogFragment dialog = new PrevisionDialogFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            dialog.show(transaction, "PrevisionDialogFragment");
        });

        return root;
    }

    // Méthode pour mettre à jour les comparaisons (moyenne vs consommation réelle et prévisions)
    private void updateComparison(double consTotale, double moyenne, TextView comparaisonTextView, String typeConsommation, int personnes, float prevision) {
        double difference = consTotale - moyenne;
        double pourcentage = (difference / moyenne) * 100;
        String message;
        int color;

        // Comparaison avec la moyenne
        if (difference == 0) {
            message = String.format("Vous consommez autant d'%s que la moyenne française pour un foyer de %d personnes par mois (%.2f).", typeConsommation, personnes, moyenne);
            color = getResources().getColor(android.R.color.darker_gray);
        } else if (difference < 0) {
            message = String.format("Vous consommez %.2f%% (%.2f) moins d'%s que la moyenne française pour un foyer de %d personnes par mois (%.2f).",
                    Math.abs(pourcentage), Math.abs(difference), typeConsommation, personnes, moyenne);
            color = getResources().getColor(android.R.color.holo_green_dark);
        } else {
            message = String.format("Vous consommez %.2f%% (%.2f) plus d'%s que la moyenne française pour un foyer de %d personnes par mois (%.2f).",
                    pourcentage, difference, typeConsommation, personnes, moyenne);
            color = getResources().getColor(android.R.color.holo_red_dark);
        }

        // Ajout de la comparaison avec les prévisions
        if (prevision != -1) {
            double differencePrevision = consTotale - prevision;
            double pourcentagePrevision = (differencePrevision / prevision) * 100;

            if (differencePrevision == 0) {
                message += String.format("\nVous avez atteint votre objectif ! Votre consommation est exactement égale à vos prévisions : %.2f m³.", prevision);
            } else if (differencePrevision < 0) {
                message += String.format("\nVous consommez %.2f%% (%.2f) moins que vos prévisions. Bon travail !", Math.abs(pourcentagePrevision), Math.abs(differencePrevision));
            } else {
                message += String.format("\nVous consommez %.2f%% (%.2f) plus que vos prévisions. Il serait peut-être utile d'ajuster votre consommation.", pourcentagePrevision, differencePrevision);
            }
        }

        // Mise à jour du texte et de la couleur
        comparaisonTextView.setText(message);
        comparaisonTextView.setTextColor(color);
    }

    // Méthodes publiques pour obtenir les clés des prévisions
    public static String getKeyPrevisionEau() {
        return KEY_PREVISION_EAU;
    }

    public static String getKeyPrevisionElec() {
        return KEY_PREVISION_ELEC;
    }

    public static String getKeyPrevisionGaz() {
        return KEY_PREVISION_GAZ;
    }
}
