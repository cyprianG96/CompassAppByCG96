package com.example.compasapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.compasapp.R;
import com.example.compasapp.interfaces.ContractMVP;
import com.example.compasapp.model.Options;
import com.example.compasapp.presenter.FragmentPresenterMVP;
import com.example.compasapp.repos.LocalTrackRepo;

public class FragmentOptions extends Fragment implements ContractMVP.View{

    private final static String TAG = "FragmentOptionsDebugLog";

    // Views
    private RelativeLayout mRelativeLayout;
    private ProgressBar mProgressBar;
    private Button mBtnCoordinates;
    private EditText mEditLongitude;
    private EditText mEditLatitude;

    private Options mOptions;
    private FragmentPresenterMVP mPresenter;
    private LocalTrackRepo mLocalTrackRepo;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_options_layout, container, false);
        mBtnCoordinates = view.findViewById(R.id.setCoordinates);
        mEditLatitude = view.findViewById(R.id.latitude);
        mEditLongitude = view.findViewById(R.id.longitude);

        mProgressBar = getActivity().findViewById(R.id.progressBar);
        mRelativeLayout = getActivity().findViewById(R.id.progressBarContainer);

        mLocalTrackRepo = new LocalTrackRepo(0.0, 0.0);

        mBtnCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditLongitude.getText().toString().isEmpty() || mEditLatitude.getText().toString().isEmpty() || Double.parseDouble(mEditLongitude.getText().toString()) > 180 || Double.parseDouble(mEditLongitude.getText().toString()) < 0 || Double.parseDouble(mEditLatitude.getText().toString()) > 90 || Double.parseDouble(mEditLatitude.getText().toString()) < 0){
                    Toast.makeText(getContext(), "Please set right longitude(0-180°) and latitude(0-90°)!", Toast.LENGTH_LONG).show();
                } else {
                    mLocalTrackRepo = new LocalTrackRepo(Double.parseDouble(mEditLatitude.getText().toString()), Double.parseDouble(mEditLongitude.getText().toString()));
                    mLocalTrackRepo.getLocation(getContext(), getActivity());
                    mOptions = new Options();
                    mPresenter = new FragmentPresenterMVP(mOptions);
                    mPresenter.attach(FragmentOptions.this);

                    mRelativeLayout.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    getActivity().onBackPressed();
                }
            }
        });

        return view;
    }

    @Override
    public void showLatitudeDestination(double latitude) {}

    @Override
    public void showlongitudeDestination(double longitude) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mLocalTrackRepo != null) {
            mLocalTrackRepo.deleteLocationTracker(getContext());
        }
    }
}