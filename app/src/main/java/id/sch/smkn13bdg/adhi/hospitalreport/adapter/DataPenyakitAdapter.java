package id.sch.smkn13bdg.adhi.hospitalreport.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import id.sch.smkn13bdg.adhi.hospitalreport.R;
import id.sch.smkn13bdg.adhi.hospitalreport.getset.DataGrafikController;
import id.sch.smkn13bdg.adhi.hospitalreport.pendapatan.PendapatanPoliFragment;

/**
 * Created by adhi on 05/12/17.
 */

public class DataPenyakitAdapter extends RecyclerView.Adapter<DataPenyakitAdapter.MyViewHolder> {

    private List<DataGrafikController> Listdata;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView label, nilai;
        public RelativeLayout klik;

        public MyViewHolder(View view) {
            super(view);
            label = (TextView) view.findViewById(R.id.labelgrafik);
            nilai = (TextView) view.findViewById(R.id.nilaigrafik);
            klik = (RelativeLayout) view.findViewById(R.id.klikpenyakit);
        }
    }

    public DataPenyakitAdapter(List<DataGrafikController> Listdata) {
        this.Listdata = Listdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_datagrafik3, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DataGrafikController data = Listdata.get(position);
        holder.label.setText(data.getLabel());
        holder.nilai.setText(data.getNilai());
        holder.label.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }
        });

        holder.nilai.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }
        });

        holder.klik.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


            }
        });

    }

    @Override
    public int getItemCount() {
        return Listdata.size();
    }

}
