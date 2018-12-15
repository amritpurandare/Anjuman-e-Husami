package anjuman.e.husami;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationIntentService extends IntentService {
    // abbreviated tag name
    private static final String TAG = "RegIntentService";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String GCM_TOKEN = "gcmToken";

    public static String REGISTRATION_ID;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Make a call to Instance API
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String senderId = getResources().getString(R.string.gcm_defaultSenderId);
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);

            sharedPreferences.edit().putString(GCM_TOKEN, token).apply();

            Log.d(TAG, "GCM Registration Token: " + token);

            // pass along this data
            sendRegistrationToServer(token);

        } catch (IOException e) {
            e.printStackTrace();

            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        // send network request
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceId", token);
            jsonObject.put("itsid", REGISTRATION_ID);

            String result = register(MainActivity.mRegisterURL, jsonObject.toString());

            if (TextUtils.isEmpty(result)) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
            } else {
                // if registration sent was successful, store a boolean that indicates whether the generated token has been sent to server
                JSONObject jsonObject1 = new JSONObject(result);
                if (jsonObject1.has("status")) {
                    String status = jsonObject1.getString("status");
                    if (!TextUtils.isEmpty(status) && status.equalsIgnoreCase("success")) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
                    } else {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
                    }
                } else {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String register(String urlString, String parameter) {

        URL url;
        try {

            url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setDoInput(true);

            connection.setDoOutput(true);

            connection.setFixedLengthStreamingMode(parameter.length());

            connection.setReadTimeout(10000);

            connection.setConnectTimeout(45000);

            connection.connect();

            DataOutputStream wr;

            wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parameter);
            wr.flush();
            wr.close();

            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
