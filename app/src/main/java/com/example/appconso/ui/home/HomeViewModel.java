package com.example.appconso.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> energyConsumptionData;
    private final MutableLiveData<String> franceEffortsData;
    private final MutableLiveData<String> climateChangeData;

    public HomeViewModel() {
        energyConsumptionData = new MutableLiveData<>();
        franceEffortsData = new MutableLiveData<>();
        climateChangeData = new MutableLiveData<>();

        // Initialisation des données
        energyConsumptionData.setValue("Énergie consommée : 450 TWh");
        franceEffortsData.setValue("Réduction des émissions de CO2 : 10% par rapport à 2022.");
        climateChangeData.setValue("Température mondiale moyenne : +1.5°C par rapport à l'ère préindustrielle.");
    }

    public LiveData<String> getEnergyConsumptionData() {
        return energyConsumptionData;
    }

    public LiveData<String> getFranceEffortsData() {
        return franceEffortsData;
    }

    public LiveData<String> getClimateChangeData() {
        return climateChangeData;
    }
}
