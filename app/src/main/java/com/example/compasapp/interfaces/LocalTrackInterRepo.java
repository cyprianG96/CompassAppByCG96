package com.example.compasapp.interfaces;

import android.app.Activity;
import android.content.Context;

public interface LocalTrackInterRepo {
    void getLocation(Context context, Activity activity);
    void deleteLocationTracker(Context context);
}
