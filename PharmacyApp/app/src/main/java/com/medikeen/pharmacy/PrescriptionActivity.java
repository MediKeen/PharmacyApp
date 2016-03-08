package com.medikeen.pharmacy;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.medikeen.pharmacy.fragments.HistoryFragment;
import com.medikeen.pharmacy.utils.ConnectionDetector;
import com.medikeen.pharmacy.utils.Constants;
import com.medikeen.pharmacy.utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class PrescriptionActivity extends AppCompatActivity {

    ImageViewTouch imageView;

    LinearLayout actionHolder, validHolder, invalidHolder;

    Button valid, invalid, save;

    InputStream inputStream;
    StringBuilder stringBuilder;
    String jsonResponseString;

    ProgressDialog progressDialog;

    SessionManager sessionManager;

    String orderNumber, sessionId, orderStatus = "";

    String resource_id;

    boolean isValid;

    String validAmount, invalidReason, invalidReasonComments;

    EditText amount, invalid_comments;
    RadioButton reason1, reason2, reason3, reason4;
    RadioGroup reason_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(PrescriptionActivity.this);

        sessionId = sessionManager.getUserDetails().getPharmacyUserSessionId();

        orderNumber = getIntent().getStringExtra("ORDER NUMBER");
        orderStatus = getIntent().getStringExtra("ORDER STATUS");

        imageView = (ImageViewTouch) findViewById(R.id.prescription_imageView);

        actionHolder = (LinearLayout) findViewById(R.id.action_holder);
        validHolder = (LinearLayout) findViewById(R.id.valid_holder);
        invalidHolder = (LinearLayout) findViewById(R.id.invalid_holder);

        valid = (Button) findViewById(R.id.valid);
        invalid = (Button) findViewById(R.id.invalid);
        save = (Button) findViewById(R.id.save);

        amount = (EditText) findViewById(R.id.amount);
        reason1 = (RadioButton) findViewById(R.id.reason1);
        reason2 = (RadioButton) findViewById(R.id.reason2);
        reason3 = (RadioButton) findViewById(R.id.reason3);
        reason4 = (RadioButton) findViewById(R.id.reason4);
        reason_group = (RadioGroup) findViewById(R.id.reason_group);
        invalid_comments = (EditText) findViewById(R.id.invalid_comments);

        if (orderStatus.equalsIgnoreCase("New") || !orderStatus.isEmpty()) {
            actionHolder.setVisibility(View.VISIBLE);
        } else {
            actionHolder.setVisibility(View.GONE);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (orderStatus.equalsIgnoreCase("New") || !orderStatus.isEmpty()) {
                                                 if (actionHolder.getVisibility() == View.GONE) {
                                                     actionHolder.setVisibility(View.VISIBLE);
                                                 } else {
                                                     actionHolder.setVisibility(View.GONE);
                                                 }
                                             } else {
                                                 actionHolder.setVisibility(View.GONE);
                                             }
                                         }
                                     }

        );

        valid.setOnClickListener(new View.OnClickListener()

                                 {
                                     @Override
                                     public void onClick(View v) {
                                         validHolder.setVisibility(View.VISIBLE);
                                         invalidHolder.setVisibility(View.GONE);
                                         isValid = true;
                                     }
                                 }

        );

        invalid.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {
                                           validHolder.setVisibility(View.GONE);
                                           invalidHolder.setVisibility(View.VISIBLE);
                                           isValid = false;
                                       }
                                   }

        );

        save.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        if (isValid) {
                                            validAmount = amount.getText().toString();

                                            if (validAmount.isEmpty()) {
                                                amount.setError("Please enter the amount");
                                            } else {
                                                new ValidateOrderAsync().execute();
                                            }
                                        } else {
                                            if (reason1.isChecked()) {
                                                invalidReason = reason1.getText().toString();
                                            } else if (reason2.isChecked()) {
                                                invalidReason = reason2.getText().toString();
                                            } else if (reason3.isChecked()) {
                                                invalidReason = reason3.getText().toString();
                                            } else if (reason4.isChecked()) {
                                                invalidReason = reason4.getText().toString();
                                            }
                                            invalidReasonComments = invalid_comments.getText().toString();

                                            if (invalidReasonComments.isEmpty() && !reason1.isChecked() && !reason2.isChecked() && !reason3.isChecked() && !reason4.isChecked()) {
                                                invalid_comments.setError("Please add comments and select a reason");
                                            } else {
                                                new InValidateOrderAsync().execute();
                                            }
                                        }
                                    }
                                }

        );

        ConnectionDetector conn = new ConnectionDetector();
        conn.execute();

        if (conn.isInternetConnected() == true) {
            new SinglePrescriptionAsync().execute();
        } else {
            Toast.makeText(PrescriptionActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ValidateOrderAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(PrescriptionActivity.this);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            JSONStringer userProfileJsonStringer = new JSONStringer();

            try {
                URL url = new URL(Constants.VALIDATE_ORDER_URL);

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-type", "application/json");
                conn.setRequestProperty("Authorization", "Basic " + sessionId);

                OutputStream os = conn.getOutputStream();

                userProfileJsonStringer.object().key("orderNo")
                        .value(orderNumber)
                        .key("isValid")
                        .value(isValid).key("cost")
                        .value(Float.valueOf(validAmount)).endObject();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(userProfileJsonStringer.toString());

                writer.flush();
                writer.close();
                os.close();

                inputStream = conn.getErrorStream();

                if (inputStream == null) {
                    inputStream = conn.getInputStream();
                }

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
            progressDialog.dismiss();

            Log.e("VALID JSON RESPONSE: ", "VALID JSON RESPONSE: " + jsonResponseString);

            String success = null, error = null, errorMessage = null, errorCode = null;

            try {
                JSONObject root = new JSONObject(jsonResponseString);

                success = root.getString("success");
                error = root.getString("error");

                if (error.equalsIgnoreCase("true")) {
                    errorMessage = root.getString("errorMessage");
                    errorCode = root.getString("errorCode");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if ((success != null) && success.equalsIgnoreCase("true")) {
                onBackPressed();
            } else {
                Log.e("VALID PRESC ERROR: ", "VALID PRESC ERROR: " + errorMessage);
            }
        }
    }

    class InValidateOrderAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(PrescriptionActivity.this);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            JSONStringer userProfileJsonStringer = new JSONStringer();

            try {
                URL url = new URL(Constants.INVALIDATE_ORDER_URL);

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-type", "application/json");
                conn.setRequestProperty("Authorization", "Basic " + sessionId);

                OutputStream os = conn.getOutputStream();

                userProfileJsonStringer.object().key("orderNo")
                        .value(orderNumber)
                        .key("isValid")
                        .value(isValid).key("rejectionCode")
                        .value(invalidReason).key("rejectionDetails")
                        .value(invalidReasonComments).endObject();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(userProfileJsonStringer.toString());

                writer.flush();
                writer.close();
                os.close();

                inputStream = conn.getErrorStream();

                if (inputStream == null) {
                    inputStream = conn.getInputStream();
                }

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

                Log.e("JSON STRINGER: ", "JSON STRINGER: " + jsonResponseString);

            } catch (Exception e) {
                Log.e("STRING BUILDER ERROR: ", "STRING BUILDER ERROR: " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            Log.e("INVALID JSON RESPONSE: ", "INVALID JSON RESPONSE: " + jsonResponseString);

            String success = null, error = null, errorMessage = null, errorCode = null;

            try {
                JSONObject root = new JSONObject(jsonResponseString);

                success = root.getString("success");
                error = root.getString("error");
                if (error.equalsIgnoreCase("true")) {
                    errorMessage = root.getString("errorMessage");
                    errorCode = root.getString("errorCode");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if ((success != null) && success.equalsIgnoreCase("true")) {
                onBackPressed();
            } else {
                Log.e("INVALID PRESC ERROR: ", "INVALID PRESC ERROR: " + errorMessage);
            }
        }
    }

    class SinglePrescriptionAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(PrescriptionActivity.this);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(Constants.SINGLE_PRESCRIPTION_URL + "?orderno=" + orderNumber);

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-type",
                        "application/x-www-form-urlencoded");
                conn.setRequestProperty("Authorization", "Basic " + sessionId);

                inputStream = conn.getErrorStream();

                if (inputStream == null) {
                    inputStream = conn.getInputStream();
                }

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

                String success = null, error = null, errorMessage = null, errorCode = null;

                JSONObject root = new JSONObject(jsonResponseString);

                success = root.getString("success");
                error = root.getString("error");
                if (error.equalsIgnoreCase("true")) {
                    errorMessage = root.getString("errorMessage");
                    errorCode = root.getString("errorCode");
                }

                if ((success != null) && success.equalsIgnoreCase("true")) {
                    JSONArray prescriptionsJsonArray = root.getJSONArray("prescription");
                    for (int i = 0; i < prescriptionsJsonArray.length(); i++) {
                        JSONObject presriptionObject = prescriptionsJsonArray.getJSONObject(i);

                        resource_id = presriptionObject.getString("resource_id");
                        String resource_type = presriptionObject.getString("resource_type");
                        String person_id = presriptionObject.getString("person_id");
                        String recepient_name = presriptionObject.getString("recepient_name");
                        String recepient_address = presriptionObject.getString("recepient_address");
                        String recepient_number = presriptionObject.getString("recepient_number");
                        String offer_type = presriptionObject.getString("offer_type");
                        String is_image_uploaded = presriptionObject.getString("is_image_uploaded");
                        String is_valid = presriptionObject.getString("is_valid");
                        String is_email_sent = presriptionObject.getString("is_email_sent");
                        String created_date = presriptionObject.getString("created_date");
                        String updated_date = presriptionObject.getString("updated_date");
                        String pharmacy_profile_id = presriptionObject.getString("pharmacy_profile_id");
                        String order_status = presriptionObject.getString("order_status");
                        String cost = presriptionObject.getString("cost");
                        String rejection_code = presriptionObject.getString("rejection_code");
                        String rejection_details = presriptionObject.getString("rejection_details");
                    }
                    Picasso.with(PrescriptionActivity.this).load("http://www.medikeen.com/android_api/prescription/uploads/" + resource_id).into(imageView);
                } else {
                    Log.e("PRESC ERROR: ", "PRESC ERROR: " + errorMessage);
                }
            } catch (Exception e) {

                Log.e("PRESC ACTIVITY ERROR: ", "PRESC ACTIVITY ERROR: " + e);
            }

            progressDialog.dismiss();
        }
    }

}
