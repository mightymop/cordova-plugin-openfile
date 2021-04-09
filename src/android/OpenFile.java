package de.mopsdom.openfile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

public class OpenFile extends CordovaPlugin {

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.d("cordova-plugin-openfire", "Initializing openfile");
    }

    private void openNewActivity(Context context,String file, CallbackContext callbackContext) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        File ffile = new File(file);

        Uri uri = null;
		String applicationId = (String) org.apache.cordova.BuildHelper.getBuildConfigValue(cordova.getActivity(), "APPLICATION_ID");
        try {
            uri = OpenfileProvider.getUriForFile(context, applicationId+".openfile.provider", ffile);
        }
        catch (Exception e)
        {
            try {
                uri = OpenfileProvider.getUriForFile(context, applicationId+".openfile.provider", new File(context.getFilesDir(),file));
            }
            catch (Exception e1)
            {

            }
        }

        android.content.ContentResolver  cR = context.getContentResolver();
        String mime = cR.getType(uri);

        intent.setDataAndType(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try
        {
            this.cordova.getActivity().startActivity(intent);
            callbackContext.success();
        }
        catch (Exception e)
        {
            callbackContext.error(e.getMessage());
        }
    }

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("open"))
        {
            this.openNewActivity(cordova.getActivity(),data.getString(0),callbackContext);
            return true;
        } else {
            callbackContext.error("Method not found");
            return false;
        }
    }
}
