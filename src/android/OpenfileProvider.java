package de.mopsdom.openfile;

import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;


public class OpenfileProvider extends FileProvider {
    public final static String PROVIDER_URI = "content://"+BuildConfig.APPLICATION_ID;
    public final static String AUTHORITY = BuildConfig.APPLICATION_ID;
    public final static String CACHENAME = "cache";

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        File f;
        if ((f=new File(getContext().getCacheDir()+uri.getPath().replace("/"+CACHENAME,""))).exists())
        {
            Log.d(getContext().getString(R.string.app_name),"DELETE FILE FROM PROVIDER URI="+uri.toString()+" FILE="+f.getAbsolutePath());
            f.delete();
            return 1;
        }
        else
        if ((f = new File(getContext().getCacheDir()+uri.getPath())).exists())
        {
            Log.d(getContext().getString(R.string.app_name),"DELETE FILE FROM PROVIDER URI="+uri.toString()+" FILE="+f.getAbsolutePath());
            f.delete();
            return 1;
        }

        return 0;
    }

    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        Log.i("cordova-plugin-openfile","uri="+uri.toString()+" mode="+mode);

        String uristr = uri.toString();
        File fname = null;
        if (uristr.contains("content://de.mopsdom.openfile.provider/internal/")) {
            uristr = uristr.replace("content://de.mopsdom.openfile.provider/internal/", "");
            fname = new File(getContext().getFilesDir(),"files/"+uristr);
        }
        else
        if (uristr.contains("content://de.mopsdom.openfile.provider/external/")) {
            uristr = uristr.replace("content://de.mopsdom.openfile.provider/external/", "");
            fname = new File(Environment.getExternalStorageDirectory(),uristr);
        }
        else
        if (uristr.contains("content://de.mopsdom.openfile.provider/external2/")) {
            uristr = uristr.replace("content://de.mopsdom.openfile.provider/external2/", "");
            fname = new File(getContext().getExternalFilesDir(null),uristr);
        }
        else
        if ((uristr=uri.getPath()).contains("/"+CACHENAME)) {
            uristr = uristr.replace("/"+CACHENAME,"");
            fname = new File(getContext().getCacheDir()+uristr);
            Log.d(getContext().getString(R.string.app_name),"OPEN FILE FROM PROVIDER URI="+uri.toString()+" FILE="+f.getAbsolutePath());
        }

        if (fname==null)
        {
            throw new FileNotFoundException(uristr);
        }

        ParcelFileDescriptor pfd;

        int imode = 0;
        if (mode.equalsIgnoreCase("r"))
        {
            imode|=ParcelFileDescriptor.MODE_READ_ONLY;
        }
        else
        if (mode.equalsIgnoreCase("rt")||mode.equalsIgnoreCase("tr"))
        {
            imode|=ParcelFileDescriptor.MODE_READ_ONLY|ParcelFileDescriptor.MODE_TRUNCATE;
        }
        else
        if (mode.toLowerCase().contains("rw")||mode.toLowerCase().contains("wr"))
        {
            imode|=ParcelFileDescriptor.MODE_READ_WRITE;
            if (mode.toLowerCase().contains("t"))
            {
                imode|=ParcelFileDescriptor.MODE_TRUNCATE;
            }
        }
        else
        if (mode.equalsIgnoreCase("w"))
        {
            imode|=ParcelFileDescriptor.MODE_WRITE_ONLY;
        }
        else
        if (mode.equalsIgnoreCase("tw"))
        {
            imode|=ParcelFileDescriptor.MODE_WRITE_ONLY|ParcelFileDescriptor.MODE_TRUNCATE;
        }

        try {
            Log.i("cordova-plugin-openfile","fname="+fname);
            pfd = ParcelFileDescriptor.open(fname, imode);
            return pfd;
        }
        catch (Exception e)
        {
            Log.e("cordova-plugin-openfile",e.getMessage(),e);
            try {
                String strname = fname.getAbsolutePath();
                strname=strname.replace("/data/user/0/","/data/data/");
                strname=strname.replace("/storage/emulated/0/","/sdcard/");
                fname=new File(strname);
                Log.i("cordova-plugin-openfile","try path="+strname);
                pfd = ParcelFileDescriptor.open(fname, imode);
                return pfd;
            }
            catch (Exception e2)
            {
                Log.e("cordova-plugin-openfile",e.getMessage(),e);
                return null;
            }
        }
    }
}
