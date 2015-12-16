package com.example.koppa.driverlicensev2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import Models.IFragmentsStarter;
import fragments.AdminFragment;
import fragments.CUQuestionFragment;
import fragments.ClientFragment;
import fragments.LoginFragment;
import fragments.QuestionListFragment;
import fragments.TestLicense;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IFragmentsStarter {


    private static int QUESTIONNUM = 26;
    private static int TESTTIME = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addLoginFragment();
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
        //getMenuInflater().inflate(R.menu.main, menu);
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




    public void addLoginFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        fragmentTransaction.add(R.id.fragment_container, loginFragment);
        fragmentTransaction.commit();
    }

    public void addClientFragment(){
        Fragment frag = null;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationView;
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.nav_menu_client);
        frag = new ClientFragment();
        toolbar.setVisibility(View.VISIBLE);

        fragmentTransaction.replace(R.id.fragment_container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void addAdminFragment(){
        Fragment frag = null;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationView;
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.nav_menu_admin);
        frag = new AdminFragment();
        toolbar.setVisibility(View.VISIBLE);

        fragmentTransaction.replace(R.id.fragment_container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void addCUQuestionFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CUQuestionFragment cuQuestionFragment = new CUQuestionFragment();
        fragmentTransaction.replace(R.id.fragment_container, cuQuestionFragment);
        fragmentTransaction.commit();
    }

    public void addQuestionListFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        QuestionListFragment cuQuestionFragment = new QuestionListFragment();
        fragmentTransaction.replace(R.id.fragment_container, cuQuestionFragment);
        fragmentTransaction.commit();
    }

    public void addTestLicenseFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TestLicense testLicense = new TestLicense();
        fragmentTransaction.replace(R.id.fragment_container, testLicense);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_admin_add:
                Toast.makeText(this,"Admin ADD", Toast.LENGTH_LONG).show();
                addCUQuestionFragment();
            break;

            case R.id.nav_admin_edit:
                addQuestionListFragment();
                break;

            case R.id.nav_client_new_test:
                Toast.makeText(this,"Start New Test", Toast.LENGTH_LONG).show();
                addTestLicenseFragment();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
