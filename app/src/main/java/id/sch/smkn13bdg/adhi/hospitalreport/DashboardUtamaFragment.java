package id.sch.smkn13bdg.adhi.hospitalreport;


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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.sch.smkn13bdg.adhi.hospitalreport.adapter.DataKunjunganAdapter;
import id.sch.smkn13bdg.adhi.hospitalreport.getset.DataGrafikController;
import id.sch.smkn13bdg.adhi.hospitalreport.volley.MySingleton;
import id.sch.smkn13bdg.adhi.hospitalreport.volley.Server;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardUtamaFragment extends Fragment {

    //untuk list recycle kunjungan
    public List<DataGrafikController> listdatagrafik = new ArrayList<DataGrafikController>();
    public RecyclerView recyclerView;
    public DataKunjunganAdapter mAdapter;
    //untuk jumlah kunjungan
    public ArrayList<Integer> listnilaikunjungan = new ArrayList<Integer>();
    TextView jumlahkunjungan;
    public int sumkunjungan = 0;

    //url yang dipakai
    private ProgressDialog pd;
    String urldata = "kunjungan.php";
    String urldata02 = "rekappendapatan.php";
    String url = Server.url_server +urldata;
    String url02 = Server.url_server +urldata02;

    //untuk grafik pendapatan
    ArrayList<String> label02 = new ArrayList<>();
    ArrayList<Entry> nilai02 = new ArrayList<>();
    LineChart lineChart;
    LineData data;
    //untuk jumlah pendapatan
    public ArrayList<Integer> listnilaipendapatan = new ArrayList<Integer>();
    TextView jumlahpendapatan;
    public int sumpendapatan;

    public DashboardUtamaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboardutama, container, false);

        //list recycle kunjungan poli
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclekunjungan);
        mAdapter = new DataKunjunganAdapter(listdatagrafik);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        //jumlahkunjungan
        jumlahkunjungan = (TextView) view.findViewById(R.id.totalkunjungantxt);
        //jumlahpendpatan
        jumlahpendapatan = (TextView) view.findViewById(R.id.totalpendapatantxt);

        //ambil data server
        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        //untuk grafik pendapatan
        lineChart = (LineChart)view.findViewById(R.id.linechartrekappendapatan);

        load_data_from_server();
        load_data_from_server02();

        return view;
    }

    //ambil data kunjungan dari server
    public void load_data_from_server() {
        pd.show();

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

                                String label = jsonobject.getString("label").trim();
                                String nilai = jsonobject.getString("nilai").trim();

                                //untuk ricycle list
                                DataGrafikController d1 = new DataGrafikController();
                                d1.setLabel(label.toString());
                                d1.setNilai(nilai.toString());
                                listdatagrafik.add(d1);

                                //menggambil nilai untuk jumlah kunjungan
                                listnilaikunjungan.add(Integer.valueOf(d1.getNilai()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghitung jumlah kunjungan
                        for (int x =0; x <listnilaikunjungan.size(); x++){
                            sumkunjungan = sumkunjungan + listnilaikunjungan.get(x);
                        }

                        jumlahkunjungan.setText(String.valueOf(sumkunjungan));
                        Log.d("string", String.valueOf(sumkunjungan));

                        mAdapter.notifyDataSetChanged();
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
    private void load_data_from_server02() {
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url02,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String periode = jsonobject.getString("tanggal").trim();
                                String nilai03 = jsonobject.getString("nilai").trim();

                                nilai02.add(new Entry(Float.parseFloat(nilai03), i));
                                label02.add(periode);


                                //menggambil nilai untuk jumlah kunjungan
                                listnilaipendapatan.add(Integer.valueOf(nilai03));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghitung jumlah kunjungan
                        for (int x =0; x <listnilaipendapatan.size(); x++){
                            sumpendapatan = sumpendapatan + listnilaipendapatan.get(x);
                        }

                        jumlahpendapatan.setText(String.valueOf(sumpendapatan));
                        Log.d("string", String.valueOf(sumpendapatan));

                        ArrayList<LineDataSet> linedatasets = new ArrayList<>();

                        LineDataSet dataset1 = new LineDataSet(nilai02, "Pendapatan");
                        dataset1.setDrawCircles(false);
                        dataset1.setColor(Color.BLUE);

                        linedatasets.add(dataset1);

                        final String labels [] = label02.toArray(new String[label02.size()]);
                        data = new LineData(labels,linedatasets);
                        lineChart.setData(data);
                        lineChart.setDescription("Pendapatan");
                        lineChart.animateY(2000);
                        YAxis yAxisRight = lineChart.getAxisRight();
                        YAxis yAxisLeft = lineChart.getAxisLeft();
                        yAxisLeft.setEnabled(false);
                        yAxisRight.setEnabled(false);
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
