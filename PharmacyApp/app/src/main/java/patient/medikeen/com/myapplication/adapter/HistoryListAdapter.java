package patient.medikeen.com.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import patient.medikeen.com.myapplication.R;
import patient.medikeen.com.myapplication.bean.HistoryBean;
import patient.medikeen.com.myapplication.bean.HomeBean;

/**
 * Created by Varun on 2/21/2016.
 */
public class HistoryListAdapter extends ArrayAdapter<HistoryBean> {

    Context context;
    LayoutInflater inflater;
    ArrayList<HistoryBean> list;

    public HistoryListAdapter(Context context, ArrayList<HistoryBean> list) {
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

            convertView = inflater.inflate(R.layout.history_list_item, parent, false);

            holder.orderNumber = (TextView) convertView.findViewById(R.id.history_order_number);
            holder.name = (TextView) convertView.findViewById(R.id.history_name);
            holder.address = (TextView) convertView.findViewById(R.id.history_address);
            holder.contact = (TextView) convertView.findViewById(R.id.history_contact);
            holder.image = (ImageView) convertView.findViewById(R.id.history_image);

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
}
