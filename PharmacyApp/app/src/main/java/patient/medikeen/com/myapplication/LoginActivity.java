
package patient.medikeen.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import patient.medikeen.com.myapplication.utils.Constants;
import patient.medikeen.com.myapplication.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    Button submit;
    EditText username, password;

    InputStream inputStream;
    StringBuilder stringBuilder;
    String jsonResponseString;

    ProgressDialog progressDialog;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);

        sessionManager = new SessionManager(LoginActivity.this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = username.getText().toString();
                String passwordStr = password.getText().toString();

                new LoginAsync().execute();
            }
        });
    }

    class LoginAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            JSONStringer userProfileJsonStringer = new JSONStringer();

            try {
                URL url = new URL(Constants.LOGIN_URL);

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-type", "application/json");

                OutputStream os = conn.getOutputStream();

                userProfileJsonStringer.object().key("email")
                        .value("do_not_reply@medikeen.com")
                        .key("password")
                        .value("nighojuk").endObject();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(userProfileJsonStringer.toString());

                writer.flush();
                writer.close();
                os.close();

                inputStream = conn.getInputStream();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream), 1000);
                stringBuilder = new StringBuilder();
                stringBuilder.append(reader.readLine() + "\n");

                String line = "0";
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                inputStream.close();
                jsonResponseString = stringBuilder.toString();

            } catch (Exception e) {
                Log.e("STRING BUILDER ERROR: ", "STRING BUILDER ERROR: " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject jsonObject = new JSONObject(jsonResponseString);

                String success = jsonObject.getString("success");
                String error = jsonObject.getString("error");

                JSONObject pharmacyUserJsonObject = jsonObject.getJSONObject("parmacyUser");

                long pharmacyUserId = pharmacyUserJsonObject.getLong("pharmacy_user_id");
                String pharmacyUserFirstName = pharmacyUserJsonObject.getString("first_name");
                String pharmacyUserLastName = pharmacyUserJsonObject.getString("last_name");
                String pharmacyUserEmailAddress = pharmacyUserJsonObject.getString("email_address");
                String pharmacyUserIsActive = pharmacyUserJsonObject.getString("is_active");
                String pharmacyUserSessionId = pharmacyUserJsonObject.getString("authentication_session_id");

                JSONObject pharmacyProfileJsonObject = jsonObject.getJSONObject("pharmacyProfile");

                long pharmacyProfileId = pharmacyProfileJsonObject.getLong("pharmacy_profile_id");
                String pharmacyProfileName = pharmacyProfileJsonObject.getString("pharmacy_name");
                String pharmacyProfileEmailAddress = pharmacyProfileJsonObject.getString("pharmacy_email_address");
                String pharmacyProfileIsActive = pharmacyProfileJsonObject.getString("pharmacy_address");
                String pharmacyProfileAddress = pharmacyProfileJsonObject.getString("pharmacy_phone_number");
                String pharmacyProfilePhone = pharmacyProfileJsonObject.getString("is_active");

                sessionManager.createLoginSession(true, pharmacyUserId, pharmacyUserFirstName, pharmacyUserLastName, pharmacyUserEmailAddress, pharmacyUserIsActive, pharmacyUserSessionId, pharmacyProfileId, pharmacyProfileName, pharmacyProfileEmailAddress, pharmacyProfileIsActive, pharmacyProfileAddress, pharmacyProfilePhone);

                if (success.equalsIgnoreCase("true")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }

}
