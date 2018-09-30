package ca.ualberta.twang2.twang2_feelsbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Hashtable;

public class StatsActivity extends AppCompatActivity {

    private static final String FILENAME = "history.sav";
    private RecordHistory rh;
    private TextView loveCount;
    private TextView joyCount;
    private TextView surpriseCount;
    private TextView angerCount;
    private TextView sadnessCount;
    private TextView fearCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        loveCount = findViewById(R.id.loveCount);
        joyCount = findViewById(R.id.joyCount);
        surpriseCount = findViewById(R.id.surpriseCount);
        angerCount = findViewById(R.id.angerCount);
        sadnessCount = findViewById(R.id.sadnessCount);
        fearCount = findViewById(R.id.fearCount);

        loadFromFile();
        update();


    }

    // update the stats when resume
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile();
        update();
    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type typeRecordHistory = new TypeToken<RecordHistory>() {}.getType();
            rh = gson.fromJson(reader, typeRecordHistory);
            if (rh==null) {
                rh = new RecordHistory();
            }
        } catch (FileNotFoundException e) {
            try {
                FileOutputStream fos = openFileOutput(FILENAME,0);
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
        }
    }

    // update the counts of each feelings
    private void update() {
        Hashtable<String,Integer> stats = rh.getStats();
        loveCount.setText(stats.get("LOVE").toString());
        joyCount.setText(stats.get("JOY").toString());
        surpriseCount.setText(stats.get("SURPRISE").toString());
        angerCount.setText(stats.get("ANGER").toString());
        sadnessCount.setText(stats.get("SADNESS").toString());
        fearCount.setText(stats.get("FEAR").toString());
    }

}
