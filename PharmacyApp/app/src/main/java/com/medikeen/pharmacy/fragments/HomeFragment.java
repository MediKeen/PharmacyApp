package com.medikeen.pharmacy.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.medikeen.pharmacy.LoginActivity;
import com.medikeen.pharmacy.PrescriptionActivity;
import com.medikeen.pharmacy.R;
import com.medikeen.pharmacy.adapter.ListAdapter;
import com.medikeen.pharmacy.bean.PrescriptionBean;
import com.medikeen.pharmacy.utils.ConnectionDetector;
import com.medikeen.pharmacy.utils.Constants;
import com.medikeen.pharmacy.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Varun on 2/21/2016.
 */
public class HomeFragment extends Fragment {

    ListView homeListView;
    ArrayList<PrescriptionBean> homelist;
    ListAdapter adapter;

    InputStream inputStream;
    StringBuilder stringBuilder;
    String jsonResponseString;

    ProgressDialog progressDialog;

    SessionManager sessionManager;

    String sessionId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        homeListView = (ListView) rootView.findViewById(R.id.home_list_view);

        sessionManager = new SessionManager(HomeFragment.this.getActivity());

        sessionId = sessionManager.getUserDetails().getPharmacyUserSessionId();

        homelist = new ArrayList<>();

        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                    Log.e("LIST ITEM CLICKED: ", "LIST ITEM CLICKED: " + homelist.get(position).getResource_id());

                                                    Intent intent = new Intent(HomeFragment.this.getActivity(), PrescriptionActivity.class);

                                                    intent.putExtra("ORDER NUMBER", homelist.get(position).getResource_id());
                                                    intent.putExtra("ORDER STATUS", "");

                                                    startActivity(intent);

                                                }
                                            }
        );
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ConnectionDetector conn = new ConnectionDetector();
        conn.execute();

        if (conn.isInternetConnected() == true) {
            homelist.clear();
            new GetPrescriptionAsync().execute();
        } else {
            Toast.makeText(HomeFragment.this.getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    class GetPrescriptionAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(HomeFragment.this.getActivity());
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL(Constants.PRESCRIPTION_URL + "?ordertype=1");
//                +"?pageno=2&limit=10&ordertype=1"

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-type",
                        "application/x-www-form-urlencoded");
                conn.setRequestProperty("Authorization", "Basic " + sessionId);


                // FOR HANDLING AUTHENTICATION FAILED
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

                if (jsonResponseString != null) {

                    JSONObject jsonObject = new JSONObject(jsonResponseString);

                    String success = jsonObject.getString("success");
                    String error = jsonObject.getString("error");

                    if (success.equalsIgnoreCase("true")) {
                        String pharmacyProfileId = jsonObject.getString("pharmacy_profile_id");

                        JSONArray prescriptionsJsonArray = jsonObject.getJSONArray("prescriptions");

                        for (int i = 0; i < prescriptionsJsonArray.length(); i++) {
                            JSONObject presriptionObject = prescriptionsJsonArray.getJSONObject(i);

                            String resource_id = presriptionObject.getString("resource_id");
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

                            homelist.add(new PrescriptionBean(resource_id, resource_type, person_id, recepient_name, recepient_address, recepient_number, offer_type, is_image_uploaded, is_valid, is_email_sent, created_date, updated_date, pharmacy_profile_id, order_status, cost, rejection_code, rejection_details));

                            adapter = new ListAdapter(HomeFragment.this.getActivity(), homelist);

                            homeListView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            adapter.notifyDataSetInvalidated();
                        }
                    } else if (error.equalsIgnoreCase("true")) {
                        sessionManager.logoutUser();
                        Intent i = new Intent(HomeFragment.this.getActivity(), LoginActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }
}
