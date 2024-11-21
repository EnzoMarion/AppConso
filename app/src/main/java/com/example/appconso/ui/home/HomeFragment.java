package com.example.appconso.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.appconso.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        // Binding pour accéder directement aux éléments du layout
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Mise à jour des sections avec des données
        homeViewModel.getEnergyConsumptionData().observe(getViewLifecycleOwner(), data -> {
            binding.energyConsumptionData.setText(data);
        });

        homeViewModel.getFranceEffortsData().observe(getViewLifecycleOwner(), data -> {
            binding.franceEffortsData.setText(data);
        });

        homeViewModel.getClimateChangeData().observe(getViewLifecycleOwner(), data -> {
            binding.climateChangeData.setText(data);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
