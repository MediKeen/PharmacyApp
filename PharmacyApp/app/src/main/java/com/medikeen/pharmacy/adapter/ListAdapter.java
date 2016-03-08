package com.medikeen.pharmacy.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.medikeen.pharmacy.PrescriptionActivity;
import com.medikeen.pharmacy.R;
import com.medikeen.pharmacy.bean.PrescriptionBean;
import com.medikeen.pharmacy.utils.Constants;
import com.medikeen.pharmacy.utils.SessionManager;
import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;

/**
 * Created by Varun on 2/27/2016.
 */
public class ListAdapter extends ArrayAdapter<PrescriptionBean> {

    Context context;
    LayoutInflater inflater;
    ArrayList<PrescriptionBean> list;

    String orderStatus;

    InputStream inputStream;
    StringBuilder stringBuilder;
    String jsonResponseString;

    ProgressDialog progressDialog;

    SessionManager sessionManager;

    String orderNumber, sessionId;

    ViewHolder holder;

    View rootView;

    public ListAdapter(Context context, ArrayList<PrescriptionBean> list) {
        super(context, R.layout.home_list_item, list);

        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;

        sessionManager = new SessionManager(context);
        sessionId = sessionManager.getUserDetails().getPharmacyUserSessionId();

    }

    class ViewHolder {
        TextView orderNumber, name, address, contact;
        ImageView image;
        Button updateStatus;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.home_list_item, parent, false);

            rootView = convertView;

            holder.orderNumber = (TextView) convertView.findViewById(R.id.order_number);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.contact = (TextView) convertView.findViewById(R.id.contact);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.updateStatus = (Button) convertView.findViewById(R.id.update_status);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //SetImage

        Picasso.with(context).load("http://www.medikeen.com/android_api/prescription/thumbnail.php?image="
                + list.get(position).getResource_id()).into(holder.image);

        holder.orderNumber.setText("Order Number: " + list.get(position).getResource_id());
        holder.name.setText(list.get(position).getRecepient_name());
        holder.address.setText(list.get(position).getRecepient_address());
        holder.contact.setText("Contact: " + list.get(position).getRecepient_number());

        holder.updateStatus.setText(list.get(position).getOrder_status());

        holder.updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                orderNumber = list.get(position).getResource_id();

                String orderStatusButtonText = list.get(position).getOrder_status();

                if (orderStatusButtonText.equalsIgnoreCase("New") || orderStatusButtonText.equalsIgnoreCase("PendingVerification")) {
                    Intent intent = new Intent(context, PrescriptionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ORDER NUMBER", orderNumber);
                    intent.putExtra("ORDER STATUS", orderStatusButtonText);
                    context.startActivity(intent);
                } else if (orderStatusButtonText.equalsIgnoreCase("ProcessingPrescription")) {
                    orderStatus = "OutForDelivery";
                    customDialogStatus(position);
                } else if (orderStatusButtonText.equalsIgnoreCase("OutForDelivery")) {
                    orderStatus = "Delivery";
                    customDialogStatus(position);
                } else if (orderStatusButtonText.equalsIgnoreCase("Delivery")) {
                    customDialog(position);
                }
            }
        });
        return convertView;
    }

    public void customDialogStatus(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_status);

        Button dialog_ok = (Button) dialog.findViewById(R.id.status_dialog_ok);
        Button dialog_cancel = (Button) dialog.findViewById(R.id.status_dialog_cancel);

        dialog_ok.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             new UpdateStatusAsync(position).execute();
                                             dialog.dismiss();
                                         }
                                     }
        );

        dialog_cancel.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 dialog.dismiss();
                                             }
                                         }
        );

        dialog.show();

    }


    public void customDialog(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        final RadioButton failed = (RadioButton) dialog.findViewById(R.id.failed);
        final RadioButton success = (RadioButton) dialog.findViewById(R.id.success);
        Button dialog_ok = (Button) dialog.findViewById(R.id.dialog_ok);

        dialog_ok.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (failed.isChecked()) {
                                                 orderStatus = "DeliveryFailed";
                                                 list.remove(position);
                                                 new UpdateStatusAsync(position).execute();
                                             } else if (success.isChecked()) {
                                                 orderStatus = "Delivered";
                                                 list.remove(position);
                                                 new UpdateStatusAsync(position).execute();
                                             }
                                             dialog.dismiss();
                                         }
                                     }
        );
        dialog.show();

    }

    class UpdateStatusAsync extends AsyncTask<Void, Void, Void> {

        private int _position;
        public UpdateStatusAsync(int position) {
            _position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            JSONStringer userProfileJsonStringer = new JSONStringer();

            try {
                URL url = new URL(Constants.UPDATE_STATUS_URL);

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
                        .key("status")
                        .value(orderStatus).endObject();

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

            Log.e("UDPATE STAUTS RESP: ", "UDPATE STAUTS RESP: " + jsonResponseString);

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
                list.get(_position).setOrder_status(orderStatus);
                notifyDataSetChanged();
            } else {
                Log.e("UDPATE STATUS RESP: ", "UDPATE STATUS RESP: " + errorMessage);
            }
        }
    }
}
