package ca.ualberta.twang2.twang2_feelsbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/*
 * MainActivity of the App
 * can record feels with only one click
 * provide two buttons for going to check history and statistics
 */

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "history.sav";  // save file
    private TextView comments;  // get user comments input
    private RecordHistory rh;  // the object of the record history

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize view variables
        Button loveButton = findViewById(R.id.loveButton);
        Button joyButton = findViewById(R.id.joyButton);
        Button surpriseButton = findViewById(R.id.surpriseButton);
        Button angerButton = findViewById(R.id.angerButton);
        Button sadnessButton = findViewById(R.id.sadnessButton);
        Button fearButton = findViewById(R.id.fearButton);
        Button statsButton = findViewById(R.id.statsButton);
        Button historyButton = findViewById(R.id.historyButton);
        comments = findViewById(R.id.comments);


        ArrayList<Button> buttonList = new ArrayList<>();
        buttonList.add(loveButton);
        buttonList.add(joyButton);
        buttonList.add(surpriseButton);
        buttonList.add(angerButton);
        buttonList.add(sadnessButton);
        buttonList.add(fearButton);

        loadFromFile();  // load record history from file

        for (Button b:buttonList) {
            // setup feeling buttons' onclick
            b.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    String feeling = (String) ((Button)v).getText();
                    String comment = comments.getText().toString();
                    Record record = makeRecord(feeling, comment);
                    if (record!=null) {
                        Toast.makeText(getApplicationContext(), "Record Saved", Toast.LENGTH_SHORT).show();
                        rh.addRecord(record);
                        saveInFile();  // save changes
                        //recordSucceed()
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Record Dismissed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // navigation buttons
        statsButton.setOnClickListener(new View.OnClickListener() {
            public void  onClick(View v) {
                openStats(v);
            }
        });
        historyButton.setOnClickListener(new View.OnClickListener() {
            public void  onClick(View v) {
                openHistory(v);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile();
    }

    // open Stats activity
    public void  openStats(View v) {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    // open history activity
    public void openHistory(View v) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    // load record history from file
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

    // save record history to file
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(rh,osw);
            osw.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // helper function to make a record
    private Record makeRecord(String feeling, String comments) {
        try {
            Comment comment = new Comment();
            comment.setText(comments);
            return new Record(feeling,comment);
        } catch (TooLongCommentException e) {
            commentsTooLong();
            return null;
        }
    }

    // helper function when comments are too long
    private void commentsTooLong() {
        // show the window to tell comments are too long
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("RECORD");
        ad.setMessage("Comments too long!\nWord count must be under 100");
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });
        ad.show();
    }

    private void recordSucceed(final String feeling, final String comments) {
        // NOT USED BECAUSE OF INSTRUCTIONS  reference from: https://stackoverflow.com/a/45797332 BY Redman
        // show a confirmation window
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("RECORD");
        ad.setMessage("Feeling: "+ feeling + "\n" + "Comments: " + "\n" + comments);
        // set Yes Button
        ad.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Record Saved", Toast.LENGTH_SHORT).show();
                Record record = makeRecord(feeling, comments);
                assert record != null;
                System.out.println(record.getFeeling());
            }
        });

        // set Cancel Button
        ad.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Record Dismissed", Toast.LENGTH_SHORT).show();
            }
        });
        ad.show();
    }
}
