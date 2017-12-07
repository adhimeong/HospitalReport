package id.sch.smkn13bdg.adhi.hospitalreport;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import id.sch.smkn13bdg.adhi.hospitalreport.fastmoving.FastMovingFragment;
import id.sch.smkn13bdg.adhi.hospitalreport.indexrs.IndexRsFragment;
import id.sch.smkn13bdg.adhi.hospitalreport.kunjungan.KunjunganFragment;
import id.sch.smkn13bdg.adhi.hospitalreport.pendapatan.PendapatanFragment;
import id.sch.smkn13bdg.adhi.hospitalreport.penyakit.PenyakitFragment;
import id.sch.smkn13bdg.adhi.hospitalreport.slowmoving.SlowMovingFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new DashboardUtamaFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.dasbordutama){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new DashboardUtamaFragment()).commit();
        }else if (id == R.id.kunjungan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new KunjunganFragment()).commit();
        } else if (id == R.id.pendapatan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PendapatanFragment()).commit();
        } else if (id == R.id.penyakit) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PenyakitFragment()).commit();
        } else if (id == R.id.fastmoving) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FastMovingFragment()).commit();
        } else if (id == R.id.slowmoving) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new SlowMovingFragment()).commit();
        } else if (id == R.id.indexrumahsakit) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new IndexRsFragment()).commit();
        } else if (id == R.id.pengaturan){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
