package id.sch.smkn13bdg.adhi.hospitalreport.indexrs;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import id.sch.smkn13bdg.adhi.hospitalreport.volley.MySingleton;
import id.sch.smkn13bdg.adhi.hospitalreport.R;
import id.sch.smkn13bdg.adhi.hospitalreport.volley.Server;

/**
 * A simple {@link Fragment} subclass.
 */
public class GrafikIndex12Fragment extends Fragment {

    private ProgressDialog pd;

    //file yang dipakai pada pengambilan data server
    String urldata = "indexrs12bln.php";

    ArrayList<String> label = new ArrayList<>();
    ArrayList<Entry> nilai01 = new ArrayList<>();
    ArrayList<Entry> nilai02 = new ArrayList<>();
    ArrayList<Entry> nilai03 = new ArrayList<>();
    ArrayList<Entry> nilai04 = new ArrayList<>();

    String url = Server.url_server +urldata;
    LineChart lineChart;
    LineData data;


    public GrafikIndex12Fragment() {
        // Required empty public constructor
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    IndexRsFragment fragment1 = new IndexRsFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, fragment1);
                    fragmentTransaction.commit();

                    return true;
                case R.id.navigation_dashboard:

                    GrafikIndex6RsFragment fragment2 = new GrafikIndex6RsFragment();
                    FragmentManager fragmentManager2 = getFragmentManager();
                    FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.replace(R.id.frame_layout, fragment2);
                    fragmentTransaction2.commit();

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grafik_index12, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation02);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_notifications);

        lineChart = (LineChart)view.findViewById(R.id.linechart02);

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

                                nilai01.add(new Entry(Float.parseFloat(bor), i));
                                nilai02.add(new Entry(Float.parseFloat(los), i));
                                nilai03.add(new Entry(Float.parseFloat(toi), i));
                                nilai04.add(new Entry(Float.parseFloat(bto), i));
                                label.add(periode);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ArrayList<LineDataSet> linedatasets = new ArrayList<>();

                        LineDataSet dataset1 = new LineDataSet(nilai01, "BOR");
                        dataset1.setDrawCircles(false);
                        dataset1.setColor(Color.BLUE);
                        LineDataSet dataset2 = new LineDataSet(nilai02, "LOS");
                        dataset2.setDrawCircles(false);
                        dataset2.setColor(Color.GREEN);
                        LineDataSet dataset3 = new LineDataSet(nilai03, "TOI");
                        dataset3.setDrawCircles(false);
                        dataset3.setColor(Color.RED);
                        LineDataSet dataset4 = new LineDataSet(nilai04, "BTO");
                        dataset4.setDrawCircles(false);
                        dataset4.setColor(Color.MAGENTA);

                        linedatasets.add(dataset1);
                        linedatasets.add(dataset2);
                        linedatasets.add(dataset3);
                        linedatasets.add(dataset4);

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
