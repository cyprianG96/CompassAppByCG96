package com.example.compasapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.compasapp.R
import com.example.compasapp.interfaces.ContractMVP
import com.example.compasapp.model.Options
import com.example.compasapp.presenter.FragmentPresenterMVP
import com.example.compasapp.repos.LocalTrackRepo

class FragmentOptions : Fragment(), ContractMVP.View {

    private var mRelativeLayout: RelativeLayout? = null
    private var mProgressBar: ProgressBar? =null
    private var mBtnCoordinates: Button? = null
    private var mEditLongitude: EditText? = null
    private var mEditLatitude: EditText? = null

    private var mOptions: Options? = null
    private var mPresenter: FragmentPresenterMVP? = null
    private var mLocalTrackRepo: LocalTrackRepo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_options_layout, container, false)

        mRelativeLayout = activity?.findViewById(R.id.progressBarContainer)
        mProgressBar = activity?.findViewById(R.id.progressBar)

        mBtnCoordinates = view?.findViewById(R.id.setCoordinates)
        mEditLongitude = view?.findViewById(R.id.longitude)
        mEditLatitude = view?.findViewById(R.id.latitude)

        mLocalTrackRepo = LocalTrackRepo(0.0, 0.0);

        mBtnCoordinates?.setOnClickListener(View.OnClickListener {
            if(mEditLongitude?.text.toString().isEmpty() || mEditLatitude?.text.toString().isEmpty() || mEditLongitude?.text.toString().toDouble() > 180 || mEditLongitude?.text.toString().toDouble() < 0 || mEditLatitude?.text.toString().toDouble() > 90 || mEditLatitude?.text.toString().toDouble() < 0){
                Toast.makeText(context, "Please set right longitude(0-180°) and latitude(0-90°)!", Toast.LENGTH_LONG).show()
            } else {
                mLocalTrackRepo = LocalTrackRepo(mEditLatitude?.text.toString().toDouble(), mEditLongitude?.text.toString().toDouble())
                mLocalTrackRepo?.getLocation(context, activity)
                mOptions =  Options()
                mPresenter =  FragmentPresenterMVP(mOptions)
                mPresenter?.attach(this@FragmentOptions)

                mRelativeLayout?.visibility = View.VISIBLE
                mProgressBar?.visibility = View.VISIBLE
                activity?.onBackPressed()
            }
        })

        return view
    }

    override fun showLatitudeDestination(latitude: Double) {
        TODO("Not yet implemented")
    }

    override fun showlongitudeDestination(longitude: Double) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocalTrackRepo?.deleteLocationTracker(context)
    }

}

