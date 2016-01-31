package moetto.moso4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by moetto on 1/29/16.
 */
public class CallMonitor extends BroadcastReceiver {
    private final static String TAG = "CallMonitor";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received broadcast");

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(intent.getStringExtra(TelephonyManager.EXTRA_STATE))) {
            Log.d(TAG, "Incoming call from " + intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
            Cursor contacts = context.getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                    ContactsContract.Contacts.DISPLAY_NAME + " = ?",
                    new String[]{intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)},
                    null
            );
            if (contacts != null) {
                while (contacts.moveToNext()) {
                    Log.d(TAG, contacts.getString(0));
                }
            }
            else {
                Log.d(TAG, "Unknown number");
                return;
            }
            //TODO get name from facebook

            //TODO post notification
        }
    }
}
