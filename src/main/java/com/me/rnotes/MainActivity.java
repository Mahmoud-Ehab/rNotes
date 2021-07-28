package com.me.rnotes;

import android.Manifest;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.CDATASection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<RecordedNote> RecordedNotes;
    private static JSONObject database;
    private static File database_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] permissions = { Manifest.permission.RECORD_AUDIO };
        ActivityCompat.requestPermissions(this, permissions, 0);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        database_file = new File(getExternalCacheDir().getAbsolutePath() + "/Database");
        try {

            RetrieveData();
            RecordedNotes = new ArrayList<>();
            LoadData();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Passing each menu ID as a set of Ids because each
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void RetrieveData() throws Exception {

        if (database_file.exists()) {

            FileInputStream inputStream = new FileInputStream(database_file);
            String db_file = convertStreamToString(inputStream);
            inputStream.close();

            database = new JSONObject(db_file);
        }
        else {
            Log.d("test", "{}");
            database = new JSONObject("{}");
        }
    }


    private void LoadData() throws JSONException {
        int len = database.getInt("len");

        for (int i = 0; i < len; i++) {
            JSONObject rnObject = (JSONObject) database.get(String.valueOf(i));
            RecordedNote rn = new RecordedNote(rnObject.getString("title"), rnObject.getInt("dur"),
                    rnObject.getString("path"));

            RecordedNotes.add(rn);
        }
    }

    public String convertStreamToString(InputStream is) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        reader.close();
        return sb.toString();

    }

    public static void NotifyDataChanged() throws IOException, JSONException {

        database = new JSONObject("{}");
        for (int i = 0; i < RecordedNotes.size(); i++) {
            RecordedNote rn = RecordedNotes.get(i);

            JSONObject rnObject = new JSONObject("{}");
            rnObject.put("title", rn.getTitle());
            rnObject.put("dur", rn.getDuration());
            rnObject.put("path", rn.getPath());

            database.put(String.valueOf(i), rnObject);
        }
        database.put("len", RecordedNotes.size());

        String newDatabase = database.toString();

        FileOutputStream fos = new FileOutputStream(database_file, false);
        fos.write(newDatabase.getBytes(), 0, newDatabase.length());
        fos.close();

    }

}