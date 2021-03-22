package com.example.compasapp.presenter;

import com.example.compasapp.interfaces.ContractMVP;
import com.example.compasapp.model.Options;

public class FragmentPresenterMVP extends BasePresenter<ContractMVP.View> implements ContractMVP.Presenter {

    private Options mOptions;

    public FragmentPresenterMVP(Options mOptions){
        this.mOptions = mOptions;
    }

    @Override
    public void getLatitudeDestination() {
        double latitude = mOptions.getLatitude();
        view.showLatitudeDestination(latitude);
    }

    @Override
    public void getLongitudeDestination() {
        double longitude = mOptions.getLongitude();
        view.showlongitudeDestination(longitude);
    }
}
