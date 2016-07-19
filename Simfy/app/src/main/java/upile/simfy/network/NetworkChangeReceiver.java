package upile.simfy.network;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Toast;

import upile.simfy.AppData;

/**
 * Created by Upile.Milanzi on 7/18/2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    static boolean closeDialog;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(AppData.getActivity());
        closeDialog = false;
        if (status.equalsIgnoreCase("Not connected to Internet")) {
            displayDialog(status);
        }
    }

    public static void displayDialog(final String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppData.getActivity());
        builder.setTitle("Network").setMessage(status).setCancelable(false)
                .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtil.getConnectivityStatusString(AppData.getActivity()).equalsIgnoreCase("Not connected to Internet")) {
                    closeDialog = true;
                }
                if (closeDialog) {
                    alert.cancel();
                } else {
                    alert.cancel();
                    displayDialog(status);
                }
            }
        });
    }
}