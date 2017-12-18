package id.sch.smkn13bdg.adhi.hospitalreport.indexrs;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.sch.smkn13bdg.adhi.hospitalreport.R;
import id.sch.smkn13bdg.adhi.hospitalreport.volley.MySingleton;
import id.sch.smkn13bdg.adhi.hospitalreport.volley.Server;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarberJohnsonFragment extends Fragment {

    private ProgressDialog pd;

    //file yang dipakai pada pengambilan data server
    String urldata = "indexrs12bln.php";

    ArrayList<Entry> garisbor = new ArrayList<>();


    ArrayList<String> label = new ArrayList<>();
    ArrayList<Entry> nilai01 = new ArrayList<>();
    ArrayList<Entry> nilai02 = new ArrayList<>();
    ArrayList<Entry> nilai03 = new ArrayList<>();
    ArrayList<Entry> nilai04 = new ArrayList<>();

    String url = Server.url_server +urldata;
    LineChart lineChart;
    LineData data;

    public BarberJohnsonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barber_johnson, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        lineChart = (LineChart)view.findViewById(R.id.lineBarberJhonson);

        load_data_from_server();

        // Inflate the layout for this fragment
        return view;
    }

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

                                String periode = jsonobject.getString("periode").trim();
                                String bor = jsonobject.getString("bor").trim();
                                String los = jsonobject.getString("los").trim();
                                String toi = jsonobject.getString("toi").trim();
                                String bto = jsonobject.getString("bto").trim();

                                //nilai01.add(new Entry(Float.parseFloat(bor), i));
                                label.add(periode);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        nilai01.add(new Entry(0, 0));
                        nilai01.add(new Entry((int)7.1, (int) 2));

                        ArrayList<LineDataSet> linedatasets = new ArrayList<>();

                        LineDataSet dataset1 = new LineDataSet(nilai01, "BOR");
                        dataset1.setDrawCircles(false);
                        dataset1.setColor(Color.BLUE);

                        linedatasets.add(dataset1);

                        final String labels [] = label.toArray(new String[label.size()]);
                        data = new LineData(labels,linedatasets);
                        lineChart.setData(data);
                        lineChart.setDescription("COBA");
                        lineChart.animateY(2000);
                        lineChart.setVisibleXRange(65f);
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
