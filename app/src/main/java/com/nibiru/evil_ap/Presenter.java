package com.nibiru.evil_ap;

import android.content.Context;

import com.nibiru.evil_ap.log.Client;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Nibiru on 2016-12-06.
 */

public class Presenter implements IMVP.PresenterOps, IMVP.RequiredPresenterOps {
    /**************************************CLASS FIELDS********************************************/
    protected final String TAG = getClass().getSimpleName();
    // Layer View reference
    private WeakReference<IMVP.RequiredViewOps> mView;
    // Layer Model reference
    private IMVP.ModelOps mModel;
    // Configuration change state
    private boolean mIsChangingConfig;
    /**************************************CLASS METHODS*******************************************/
    public Presenter(IMVP.RequiredViewOps mView) {
        this.mView = new WeakReference<>(mView);
        this.mModel = new Model(this);
    }

    public void onTrafficRedirect(String traffic, boolean on){
        mModel.onTrafficRedirect(traffic, on);
    }

    public ArrayList<Client> getCurrentClients(){
        ArrayList<String> output = mModel.getCurrentClients();
        ArrayList<Client> clients = new ArrayList<>(output.size());
        for (String line : output) {
            String[] split = line.split(" +");
            //IP idx = 0 , MAC idx = 4, flags idx = 5
            if (split.length == 6 && (split[5].equals("REACHABLE") || split[5].equals("STALE")) ) {
                clients.add(new Client(split[0], split[4]));
            }
        }
        return clients;
    }

    public boolean isApOn(Context ctx){
        return mModel.isApOn(ctx);
    }

    public boolean apBtnPressed(String SSID, String pass, Context ctx){
        return mModel.apToggle(SSID, pass, ctx);
    }

    public void checkIfDeviceRooted(){
        if (!mModel.isDeviceRooted()){
            mView.get().showToast("Application will only function properly on rooted phones with " +
                    "root permissions.");
        }
    }

    /**
     * Sent from Activity after a configuration changes
     * @param view  View reference
     */
    @Override
    public void onConfigurationChanged(IMVP.RequiredViewOps view) {

    }
    /**
     * Receives {@link MainActivity#onDestroy()} event
     * @param isChangingConfig  Config change state
     */
    @Override
    public void onDestroy(boolean isChangingConfig) {

    }
    /**
     * receive errors
     */
    @Override
    public void onError(String errorMsg) {

    }
}