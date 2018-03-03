package com.example.zaheenkhan.kitchenassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Pair;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.facebook.FacebookSdk;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG ="MainActivity";

    private MobileServiceClient mClient;
    LoginButton loginButton;
    CallbackManager callbackManager;
    Context context;

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

        try {
            mClient = new MobileServiceClient(
                    "https://kitchenmanager.azurewebsites.net",
                    this
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        FacebookSdk.sdkInitialize(getApplicationContext());

        final Button button = findViewById(R.id.button);
        final List<Pair<String,String>> myList = new ArrayList<>();
        myList.add((new Pair<String,String>("name" , "jithin")));
        final TextView tt = findViewById(R.id.tt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ListenableFuture<String> result = mClient.invokeApi( "checkUserID", "GET" , myList );
                final ListenableFuture<com.google.gson.JsonElement> result = mClient.invokeApi("checkUserID","GET",myList);
                Futures.addCallback(result, new FutureCallback<JsonElement>() {
                    @Override
                    public void onSuccess(@Nullable JsonElement jsonElement) {

                        try {
                            tt.setText(result.get().toString());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        TextView tt = findViewById(R.id.tt);
                        tt.setText(throwable.toString());
                    }
                });

            }
        });

        callbackManager = CallbackManager.Factory.create();

        final Button db_button = findViewById(R.id.button2);
        db_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(MainActivity.this,DashboardActivity.class);
                startActivity(i1);
            }
        });


//        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//        builder1.setMessage("Log-In Successful.");
//        builder1.setCancelable(true);
//
//        builder1.setPositiveButton(
//                "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        final AlertDialog alert11 = builder1.create();
//
//        final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
//        builder2.setMessage("Log-In UnSuccessful.");
//        builder2.setCancelable(true);
//
//        builder2.setPositiveButton(
//                "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        final AlertDialog alert22 = builder2.create();
        final TextView logbox = findViewById(R.id.logbox);
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        JSONObject payload = new JSONObject();
                        try {
                            payload.put("access_token", AccessToken.getCurrentAccessToken().getToken());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tt.setText(payload.toString());
                        ListenableFuture<MobileServiceUser> mLogin = mClient.login("facebook", payload.toString());
                        Futures.addCallback(mLogin, new FutureCallback<MobileServiceUser>() {
                            @Override
                            public void onFailure(Throwable exc) {
                                exc.printStackTrace();
                            }
                            @Override
                            public void onSuccess(MobileServiceUser user) {
//                                alert11.show();
                                logbox.setText("Login Success");
                                Intent i1 = new Intent(MainActivity.this,DashboardActivity.class);
                                startActivity(i1);

                            }
                        });

                    }

                    @Override
                    public void onCancel() {
                        logbox.setText("Login Cancelled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
//                        alert11.show();
                        logbox.setText("Login Error :" + exception.getMessage() );
                    }
                });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
