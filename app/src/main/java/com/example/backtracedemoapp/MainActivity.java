package com.example.backtracedemoapp;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import backtraceio.library.BacktraceClient;
import backtraceio.library.BacktraceCredentials;
import backtraceio.library.BacktraceDatabase;
import backtraceio.library.enums.database.RetryBehavior;
import backtraceio.library.enums.database.RetryOrder;
import backtraceio.library.models.BacktraceExceptionHandler;
import backtraceio.library.models.database.BacktraceDatabaseSettings;

public class MainActivity extends AppCompatActivity {

    private BacktraceClient backtraceClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BacktraceCredentials credentials = new BacktraceCredentials("https://cd03.sp.backtrace.io:6098/", "03741e036b52cede4dd9824eaac00d69fc58b857d506d6389fa9774924223c87");

        Context context = getApplicationContext();
        String dbPath = context.getFilesDir().getAbsolutePath(); // any path, eg. absolute path to the internal storage

        BacktraceDatabaseSettings settings = new BacktraceDatabaseSettings(dbPath);
        settings.setMaxRecordCount(100);
        settings.setMaxDatabaseSize(100);
        settings.setRetryBehavior(RetryBehavior.ByInterval);
        settings.setAutoSendMode(true);
        settings.setRetryOrder(RetryOrder.Queue);

        BacktraceDatabase database = new BacktraceDatabase(context, settings);
        backtraceClient = new BacktraceClient(context, credentials, database);

        //backtraceClient = new BacktraceClient(getApplicationContext(), credentials);
        backtraceClient.enableAnr();
        backtraceClient.enableNativeIntegration();
        BacktraceExceptionHandler.enable(backtraceClient);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.button_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Class.forName("dalvik.system.VMDebug")
                            .getMethod("crash")
                            .invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.button_hang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.button_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = 0;
                if (true) {
                    int y = 100 / x;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}