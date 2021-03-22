package com.example.compasapp.interfaces;

public interface ContractMVP {

    interface View {
        void showLatitudeDestination(double latitude);
        void showlongitudeDestination(double longitude);
    }

    interface Presenter {
        void getLatitudeDestination();
        void getLongitudeDestination();
    }
}