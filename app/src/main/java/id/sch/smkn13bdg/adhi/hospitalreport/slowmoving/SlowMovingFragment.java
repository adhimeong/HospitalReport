package id.sch.smkn13bdg.adhi.hospitalreport.slowmoving;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.sch.smkn13bdg.adhi.hospitalreport.volley.MySingleton;
import id.sch.smkn13bdg.adhi.hospitalreport.R;
import id.sch.smkn13bdg.adhi.hospitalreport.volley.Server;
import id.sch.smkn13bdg.adhi.hospitalreport.adapter.DataObatAdapter;
import id.sch.smkn13bdg.adhi.hospitalreport.getset.DataObatController;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlowMovingFragment extends Fragment {

    private ProgressDialog pd;
    String urldata = "fastmoving.php";
    String url = Server.url_server +urldata;

    List<DataObatController> dataController = new ArrayList<DataObatController>();
    DataObatAdapter adapter;
    ListView listView;


    public SlowMovingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slow_moving, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        listView = (ListView)view.findViewById(R.id.listview02);
        adapter = new DataObatAdapter(getActivity(), dataController );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String data04 = dataController.get(position).getId_obat();
                String data05 = dataController.get(position).getNama();
                String data06 = dataController.get(position).getJumlah();
                FancyToast.makeText(getActivity().getApplicationContext(),data05 + " : " +data06,FancyToast.LENGTH_SHORT, FancyToast.WARNING,true).show();
            }
        });


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

                                String id_obat = jsonobject.getString("id_obat").trim();
                                String nama = jsonobject.getString("nama").trim();
                                String kode = jsonobject.getString("kode").trim();
                                String jumlah = jsonobject.getString("jumlah").trim();

                                DataObatController d1 = new DataObatController();
                                d1.setId_obat(id_obat.toString());
                                d1.setNama(nama.toString());
                                d1.setKode(kode.toString());
                                d1.setJumlah(jumlah.toString());

                                dataController.add(d1);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();

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
