package cl.telios.quivolgo.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rubro on 17-01-2017.
 */

public class CheckNetwork {
    private static final String TAG = "Develop";
    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            //LogViewer.d(TAG,"no internet connection");
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                //LogViewer.d(TAG," internet connection available...");
                return true;
            }
            else
            {
                //LogViewer.d(TAG," internet connection");
                return true;
            }

        }
    }
}