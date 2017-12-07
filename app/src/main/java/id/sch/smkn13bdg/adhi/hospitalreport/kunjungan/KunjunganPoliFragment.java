package id.sch.smkn13bdg.adhi.hospitalreport.kunjungan;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.HashMap;
import java.util.Map;

import id.sch.smkn13bdg.adhi.hospitalreport.volley.MySingleton;
import id.sch.smkn13bdg.adhi.hospitalreport.R;
import id.sch.smkn13bdg.adhi.hospitalreport.volley.Server;

/**
 * A simple {@link Fragment} subclass.
 */
public class KunjunganPoliFragment extends Fragment {

    private ProgressDialog pd;

    //file yang dipakai pada pengambilan data server
    String urldata = "kunjunganpoli.php";

    //untuk grafik
    ArrayList yAxis;
    ArrayList yValues;
    ArrayList xAxis1;
    BarEntry values ;
    BarChart chart;
    String url = Server.url_server +urldata;
    BarData data;

    //untuk jumlah kunjungan
    public ArrayList<Integer> listnilaikunjungan = new ArrayList<Integer>();
    TextView jumlahkunjungan;
    public int sumkunjungan = 0;


    public KunjunganPoliFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kunjungan_poli, container, false);

        //data poli dari fragment kunjungan
        String datatoserver = this.getArguments().getString("key");
        FancyToast.makeText(getActivity().getApplicationContext(),datatoserver,FancyToast.LENGTH_SHORT, FancyToast.INFO,true).show();

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        //jumlahkunjungan
        jumlahkunjungan = (TextView) view.findViewById(R.id.totalkunjungantxt);

        chart = (BarChart) view.findViewById(R.id.barchartkunjpoli);
        load_data_from_server(datatoserver);

        // Inflate the layout for this fragment
        return view;
    }

    public void load_data_from_server(final String data03) {
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

                                //untuk grafik
                                xAxis1.add(label);
                                values = new BarEntry(Float.valueOf(nilai),i);
                                yValues.add(values);

                                //menggambil nilai untuk jumlah kunjungan
                                listnilaikunjungan.add(Integer.valueOf(nilai));

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

                        //untuk grafik
                        BarDataSet barDataSet1 = new BarDataSet(yValues, "LAPORAN DATA : "+data03);
                        barDataSet1.setColor(Color.rgb(0, 82, 159));

                        yAxis = new ArrayList<>();
                        yAxis.add(barDataSet1);
                        final String labels [] = (String[]) xAxis1.toArray(new String[xAxis1.size()]);
                        data = new BarData(labels,yAxis);
                        chart.setData(data);
                        chart.setDescription("");
                        chart.animateXY(2000, 2000);
                        chart.invalidate();

                        chart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
                            @Override
                            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                                Log.i("Entry", String.valueOf(e));
                                String data01 = labels[e.getXIndex()];
                                String data02 = String.valueOf(e.getVal());

                                FancyToast.makeText(getActivity().getApplicationContext(),data01 + " : " +data02,FancyToast.LENGTH_SHORT, FancyToast.WARNING,true).show();
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
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("poli", data03);
                return params;
            }
        };

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
