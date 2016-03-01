package patient.medikeen.com.myapplication.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.List;

import patient.medikeen.com.myapplication.PrescriptionActivity;
import patient.medikeen.com.myapplication.R;
import patient.medikeen.com.myapplication.bean.HomeBean;
import patient.medikeen.com.myapplication.utils.Constants;
import patient.medikeen.com.myapplication.utils.SessionManager;

/**
 * Created by Varun on 2/21/2016.
 */
public class HomeListAdapter extends ArrayAdapter<HomeBean> {

    Context context;
    LayoutInflater inflater;
    ArrayList<HomeBean> list;

    InputStream inputStream;
    StringBuilder stringBuilder;
    String jsonResponseString;

    ProgressDialog progressDialog;

    SessionManager sessionManager;

    public HomeListAdapter(Context context, ArrayList<HomeBean> list) {
        super(context, R.layout.home_list_item, list);

        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    class ViewHolder {
        TextView orderNumber, name, address, contact;
        ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.home_list_item, parent, false);

            holder.orderNumber = (TextView) convertView.findViewById(R.id.order_number);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.contact = (TextView) convertView.findViewById(R.id.contact);
            holder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //SetImage

        holder.orderNumber.setText("Order Number: " + list.get(position).getOrderNumber());
        holder.name.setText(list.get(position).getName());
        holder.address.setText(list.get(position).getAddress());
        holder.contact.setText("Contact: " + list.get(position).getPhone());

        return convertView;
    }

    class UpdateStatusAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);
            progressDialog.show();
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

                OutputStream os = conn.getOutputStream();

                userProfileJsonStringer.object().key("orderNo")
                        .value("21")
                        .key("status")
                        .value("Delivered").endObject();

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
            progressDialog.dismiss();
        }
    }
}
