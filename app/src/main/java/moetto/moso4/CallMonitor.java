package moetto.moso4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by moetto on 1/29/16.
 */
public class CallMonitor extends BroadcastReceiver {
    private final static String TAG = "CallMonitor";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received broadcast");
        String name;
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(intent.getStringExtra(TelephonyManager.EXTRA_STATE))) {
            Log.d(TAG, "Incoming call from " + intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)));
            Cursor contacts = context.getContentResolver().query(
                    uri,
                    new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                    null,
                    null,
                    null
            );

            if (contacts != null) {
                if (contacts.moveToFirst()) {
                    Log.d(TAG, contacts.getString(0));
                    name = contacts.getString(0);
                }
            } else {
                Log.d(TAG, "Unknown number");
                return;
            }

            //TODO get name from facebook
            GraphRequestAsyncTask friends = new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/me/friends",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            Log.d(TAG, "Faboresponse: " + response.getRawResponse());
                            //TODO post notification
                        }
                    }).executeAsync();
            /*
            JSONArray friendList = friends.getJSONObject().getJSONArray("data");
            for (JSONObject friend : friendList) {
                if (friend.getString("name").equals())
            }
            */


        }
    }
}
