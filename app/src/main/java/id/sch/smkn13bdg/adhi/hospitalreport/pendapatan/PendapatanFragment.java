package id.sch.smkn13bdg.adhi.hospitalreport.pendapatan;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.sch.smkn13bdg.adhi.hospitalreport.adapter.DataPendapatanAdapter;
import id.sch.smkn13bdg.adhi.hospitalreport.getset.DataGrafikController;
import id.sch.smkn13bdg.adhi.hospitalreport.volley.MySingleton;
import id.sch.smkn13bdg.adhi.hospitalreport.R;
import id.sch.smkn13bdg.adhi.hospitalreport.volley.Server;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendapatanFragment extends Fragment{

    private ProgressDialog pd;

    //file yang dipakai pada pengambilan data server
    String urldata = "pendapatan.php";

    //untuk list recycle pedapatan
    public List<DataGrafikController> listdatagrafik = new ArrayList<DataGrafikController>();
    public RecyclerView recyclerView;
    public DataPendapatanAdapter mAdapter;

    //untuk grafik
    ArrayList yAxis;
    ArrayList yValues;
    ArrayList xAxis1;
    BarEntry values ;
    BarChart chart;
    String url = Server.url_server +urldata;
    BarData data;

    //untuk jumlah pedapatan
    public ArrayList<Integer> listnilaipedapatan = new ArrayList<Integer>();
    TextView jumlahpedapatan;
    public int sumpendapatan = 0;

    public PendapatanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pendapatan, container, false);

        //list recycle pendapatan poli
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclependapatan);
        mAdapter = new DataPendapatanAdapter(listdatagrafik);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        //jumlah pendaptan
        jumlahpedapatan = (TextView) view.findViewById(R.id.totalpendapatantxt);

        chart = (BarChart) view.findViewById(R.id.barchartpedapatan1);
        load_data_from_server();

        // Inflate the layout for this fragment
        return view;
    }

    public void load_data_from_server() {
        pd.show();
        xAxis1 = new ArrayList<>();
        yAxis = null;
        yValues = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);


                                String nilai = jsonobject.getString("nilai").trim();
                                String label = jsonobject.getString("label").trim();

                                //untuk ricycle list
                                DataGrafikController d1 = new DataGrafikController();
                                d1.setLabel(label.toString());
                                d1.setNilai(nilai.toString());
                                listdatagrafik.add(d1);

                                //untuk grafik
                                xAxis1.add(label);
                                values = new BarEntry(Float.valueOf(nilai),i);
                                yValues.add(values);

                                //menggambil nilai untuk jumlah pedapatan
                                listnilaipedapatan.add(Integer.valueOf(nilai));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //untuk list
                        mAdapter.notifyDataSetChanged();

                        //menghitung jumlah pendapatan
                        for (int x =0; x <listnilaipedapatan.size(); x++){
                            sumpendapatan = sumpendapatan + listnilaipedapatan.get(x);
                        }

                        jumlahpedapatan.setText(String.valueOf(sumpendapatan));
                        Log.d("string", String.valueOf(sumpendapatan));

                        //untuk grafik
                        final BarDataSet barDataSet1 = new BarDataSet(yValues, "LAPORAN DATA PENDAPATAN");
                        barDataSet1.setColor(Color.rgb(255, 69, 0));

                        yAxis = new ArrayList<>();
                        yAxis.add(barDataSet1);
                        final String labels [] = (String[]) xAxis1.toArray(new String[xAxis1.size()]);
                        data = new BarData(labels,yAxis);
                        chart.setData(data);
                        chart.setDescription("");
                        chart.animateXY(2000, 2000);
                        chart.invalidate();
                        YAxis yAxisRight = chart.getAxisRight();
                        YAxis yAxisLeft = chart.getAxisLeft();
                        yAxisRight.setEnabled(false);
                        yAxisLeft.setEnabled(false);

                        chart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
                            @Override
                            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                                Log.i("Entry", String.valueOf(e));
                                String data01 = labels[e.getXIndex()];
                                //Toast.makeText(getActivity().getApplicationContext(), data01, Toast.LENGTH_SHORT).show();

                                Bundle bundle = new Bundle();
                                bundle.putString("key", data01);

                                PendapatanPoliFragment nextfragment = new PendapatanPoliFragment();
                                nextfragment.setArguments(bundle);

                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.frame_layout, nextfragment)
                                        .commit();
                            }

                            @Override
                            public void onNothingSelected() {

                            }
                        });

                        pd.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            FancyToast.makeText(getActivity().getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            pd.hide();
                        }
                    }
                }

        );

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
