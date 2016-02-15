package moetto.moso4;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by moetto on 1/29/16.
 */
public class CallMonitor extends BroadcastReceiver {
    private final static String TAG = "CallMonitor";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "Received broadcast");
        final String name;
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(intent.getStringExtra(TelephonyManager.EXTRA_STATE))) {
            Log.d(TAG, "Incoming call from " + intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)));
            final Cursor contacts = context.getContentResolver().query(
                    uri,
                    new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                    null,
                    null,
                    null
            );

            if (contacts != null && contacts.moveToFirst()) {
                Log.d(TAG, contacts.getString(0));
                name = contacts.getString(0);
                Log.d(TAG, "Caller is " + name);
            } else {
                Log.d(TAG, "Unknown number");
                return;
            }
            Bundle parameters = new Bundle();
            parameters.putInt("limit", 500);
            final GraphRequestAsyncTask friends = new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/me/friends",
                    parameters,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            Log.d(TAG, "Faboresponse: " + response.getRawResponse());
                            JSONObject friends = response.getJSONObject();

                            try {
                                JSONArray friendList = friends.getJSONArray("data");
                                for (int i = 0; i < friendList.length(); i++) {
                                    JSONObject friend = friendList.getJSONObject(i);
                                    if (friend.getString("name").equals(name)) {
                                        long id = friend.getLong("id");
                                        Log.d(TAG, "Found friend " + friend.getString("name") + " with id " + id);

                                        final GraphRequestAsyncTask posts = new GraphRequest(AccessToken.getCurrentAccessToken(),
                                                "/" + id + "/posts",

                                                null,
                                                HttpMethod.GET,
                                                new GraphRequest.Callback() {
                                                    @Override
                                                    public void onCompleted(GraphResponse response) {
                                                        try {
                                                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context).
                                                                    setSmallIcon(R.drawable.com_facebook_button_icon)
                                                                    .setContentTitle(name)
                                                                    .setContentText(response.getJSONObject().getJSONArray("data").getJSONObject(0).getString("message"));

                                                            int notID = 1;
                                                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                            notificationManager.notify(notID, notificationBuilder.build());
                                                        } catch (JSONException ex) {
                                                            Log.d(TAG, "Invalid JSON");
                                                            return;
                                                        }

                                                        Log.d(TAG, "" + response.getRawResponse());
                                                    }
                                                }
                                        ).executeAsync();
                                    }
                                }
                            } catch (JSONException ex) {
                                Log.d(TAG, "JSON is invalid");
                                return;
                            }
                            //TODO post notification
                        }
                    }).executeAsync();
        }
    }
}
