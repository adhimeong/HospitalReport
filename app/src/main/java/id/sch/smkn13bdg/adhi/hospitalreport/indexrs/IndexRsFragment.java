package id.sch.smkn13bdg.adhi.hospitalreport.indexrs;


import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santalu.respinner.ReSpinner;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import id.sch.smkn13bdg.adhi.hospitalreport.volley.MySingleton;
import id.sch.smkn13bdg.adhi.hospitalreport.R;
import id.sch.smkn13bdg.adhi.hospitalreport.volley.Server;
import id.sch.smkn13bdg.adhi.hospitalreport.getset.DataPeriodeController;


/**
 * A simple {@link Fragment} subclass.
 */
public class IndexRsFragment extends Fragment {

    private ProgressDialog pd;

    //file yang dipakai pada pengambilan data server
    String urldata = "indexrs.php";
    TextView tviewbor, tviewlos, tviewtoi, tviewbto;
    String url = Server.url_server +urldata;

    List<DataPeriodeController> dataPeriodeControllers = new ArrayList<DataPeriodeController>();
    ArrayList<String> periodelist =new ArrayList<String>();
    ReSpinner pilihan;


    public IndexRsFragment() {
        // Required empty public constructor
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_dashboard:

                    GrafikIndex6RsFragment fragment1 = new GrafikIndex6RsFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, fragment1);
                    fragmentTransaction.commit();

                    return true;
                case R.id.navigation_notifications:

                    BarberJohnsonFragment fragment3 = new BarberJohnsonFragment();
                    FragmentManager fragmentManager3 = getFragmentManager();
                    FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                    fragmentTransaction3.replace(R.id.frame_layout, fragment3);
                    fragmentTransaction3.commit();

                    return true;
            }
            return false;
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index_rs, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        tviewbor = (TextView) view.findViewById(R.id.bortxt);
        tviewlos = (TextView) view.findViewById(R.id.lostxt);
        tviewtoi = (TextView) view.findViewById(R.id.toitxt);
        tviewbto = (TextView) view.findViewById(R.id.btotxt);

        pilihan = (ReSpinner) view.findViewById(R.id.periodespinner);

        pilihan.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, periodelist));
        pilihan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tviewbor.setText(dataPeriodeControllers.get(i).getBor());
                tviewlos.setText(dataPeriodeControllers.get(i).getLos());
                tviewtoi.setText(dataPeriodeControllers.get(i).getToi());
                tviewbto.setText(dataPeriodeControllers.get(i).getBto());

                FancyToast.makeText(getActivity().getApplicationContext(), String.valueOf(adapterView.getSelectedItem()),FancyToast.LENGTH_SHORT, FancyToast.INFO,true).show();
                //Toast.makeText(getActivity().getApplicationContext(), adapterView.getSelectedItem() + " Selected", Toast.LENGTH_LONG).show();
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

                                String periode = jsonobject.getString("periode").trim();
                                String bor = jsonobject.getString("bor").trim();
                                String los = jsonobject.getString("los").trim();
                                String toi = jsonobject.getString("toi").trim();
                                String bto = jsonobject.getString("bto").trim();

                                //data untuk ditampilkan
                                DataPeriodeController d2 = new DataPeriodeController();
                                d2.setPeriode(periode.toString());
                                d2.setBor(bor.toString());
                                d2.setLos(los.toString());
                                d2.setToi(toi.toString());
                                d2.setBto(bto.toString());

                                dataPeriodeControllers.add(d2);

                                //data untuk spinner
                                periodelist.add(periode.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
