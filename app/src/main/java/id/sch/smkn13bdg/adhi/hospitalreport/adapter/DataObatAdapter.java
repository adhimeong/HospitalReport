package id.sch.smkn13bdg.adhi.hospitalreport.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.sch.smkn13bdg.adhi.hospitalreport.R;
import id.sch.smkn13bdg.adhi.hospitalreport.getset.DataObatController;


/**
 * Created by adhi on 02/12/17.
 */

public class DataObatAdapter extends BaseAdapter {

    private List<DataObatController> data;
    Activity activity;
    TextView idobat, nama, kode, jumlah;

    public DataObatAdapter(Activity activity, List<DataObatController> data) {
        super();
        this.data = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int location) {
        return data.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(activity);
            v = vi.inflate(R.layout.list_dataobat, null);

            idobat = (TextView) v.findViewById(R.id.listidobat);
            nama = (TextView) v.findViewById(R.id.listnamaobat);
            kode = (TextView) v.findViewById(R.id.listkodeobat);
            jumlah = (TextView) v.findViewById(R.id.listjumlah);

            DataObatController d = data.get(position);

            idobat.setText(String.valueOf(d.getId_obat()));
            nama.setText(String.valueOf(d.getNama()));
            kode.setText(String.valueOf(d.getKode()));
            jumlah.setText(String.valueOf(d.getJumlah()));

        }

        return v;
    }

}
