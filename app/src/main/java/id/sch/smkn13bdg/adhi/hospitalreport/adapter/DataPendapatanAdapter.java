package id.sch.smkn13bdg.adhi.hospitalreport.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.sch.smkn13bdg.adhi.hospitalreport.R;
import id.sch.smkn13bdg.adhi.hospitalreport.getset.DataGrafikController;
import id.sch.smkn13bdg.adhi.hospitalreport.pendapatan.PendapatanPoliFragment;

/**
 * Created by adhi on 05/12/17.
 */

public class DataPendapatanAdapter extends RecyclerView.Adapter<DataPendapatanAdapter.MyViewHolder> {

    private List<DataGrafikController> Listdata;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView label, nilai, klik;

        public MyViewHolder(View view) {
            super(view);
            label = (TextView) view.findViewById(R.id.labelgrafik);
            nilai = (TextView) view.findViewById(R.id.nilaigrafik);
            klik = (TextView) view.findViewById(R.id.klikkinjungan);
        }
    }

    public DataPendapatanAdapter(List<DataGrafikController> Listdata) {
        this.Listdata = Listdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_datagrafik2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DataGrafikController data = Listdata.get(position);
        holder.label.setText(data.getLabel());
        holder.nilai.setText(data.getNilai());
        final String data01 = String.valueOf(data.getLabel());
        holder.label.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Bundle bundle = new Bundle();
                bundle.putString("key", data01);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                PendapatanPoliFragment nextfragment = new PendapatanPoliFragment();
                nextfragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,nextfragment).commit();

            }
        });

        holder.nilai.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Bundle bundle = new Bundle();
                bundle.putString("key", data01);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                PendapatanPoliFragment nextfragment = new PendapatanPoliFragment();
                nextfragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,nextfragment).commit();

            }
        });

        holder.klik.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Bundle bundle = new Bundle();
                bundle.putString("key", data01);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                PendapatanPoliFragment nextfragment = new PendapatanPoliFragment();
                nextfragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,nextfragment).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return Listdata.size();
    }

}
