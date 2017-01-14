package com.nibiru.evil_ap.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.nibiru.evil_ap.R;

public class MainFragment extends Fragment implements View.OnClickListener {
    /************************************* CLASS FIELDS *******************************************/
    protected final String TAG = getClass().getSimpleName();
    private OnMainFragmentInteraction mListener;
    private EditText et;
    private EditText et2;

    /************************************** CLASS METHODS *****************************************/
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // only for marshmallow and newer versions, we need user to explicitly grant us
        // WRITE_SETTINGS permissions to be able to change hotspot configuration
        //TODO: what about other versions ? FIX NEEDED
        //http://stackoverflow.com/questions/32083410/cant-get-write-settings-permission/32083622#32083622
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !Settings.System.canWrite(getContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getContext().getPackageName()));
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        Button bt = (Button) v.findViewById(R.id.button);
        bt.setOnClickListener(this);
        et = (EditText) v.findViewById(R.id.editText);
        et2 = (EditText) v.findViewById(R.id.editText2);
        CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        et2.setVisibility(View.VISIBLE);
                    }
                    else{
                        et2.setVisibility(View.INVISIBLE);
                    }
                }
            }
        );

        return v;
    }

    public void setBtnUI(boolean ApOn) {
        try {
            Button btn = (Button) getView().findViewById(R.id.button);
            if (ApOn) {
                btn.setBackgroundResource(R.drawable.onoffon);
                mListener.enableTabLayout();
                Log.e(TAG, "Set btn ui on");
            }
            else if(!ApOn){
                btn.setBackgroundResource(R.drawable.onoff);
                mListener.disableTabLayout();
                Log.e(TAG, "Set btn ui off");
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "findViewById null pointer!");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (mListener.onApPressed(et.getText().toString(), et2.getText().toString())) {
                    Log.e(TAG, "onclick btn ui on");
                    et.setFocusable(false);
                    et2.setFocusable(false);
                    setBtnUI(true);
                } else if(mListener.isApOn()){
                    Log.e(TAG, "onclick btn ui off");
                    mListener.onApPressed("","");
                    et.setFocusable(true);
                    et2.setFocusable(true);
                    setBtnUI(false);
                }
                break;
        }
    }

/******************************** Fragment Stuff **************************************************/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnMainFragmentInteraction {
        //return true is Ap was turn on, false otherwise
        boolean onApPressed(String SSID, String pass);
        boolean isApOn();
        void enableTabLayout();
        void disableTabLayout();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        if (context instanceof OnMainFragmentInteraction) {
            mListener = (OnMainFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteraction interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
