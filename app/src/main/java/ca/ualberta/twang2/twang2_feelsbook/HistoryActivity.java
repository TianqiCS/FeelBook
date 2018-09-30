package ca.ualberta.twang2.twang2_feelsbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

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

public class HistoryActivity extends AppCompatActivity {
    private static final String FILENAME = "history.sav";
    public static final String RECORD_POSITION = "ca.ualberta.twang2.twang2_feelsbook.MESSAGE";
    private RecyclerView recordList;
    private RecyclerView.Adapter recordAdapter;
    private RecordHistory rh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        loadFromFile();
        if (rh==null) {
            rh = new RecordHistory();
            System.out.println("I am empty");
        }

        recordList = findViewById(R.id.recordList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recordList.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        recordList.setLayoutManager(lm);

        // specify an adapter (see also next example)
        recordAdapter = new RecordAdapter(rh);
        recordList.setAdapter(recordAdapter);

        // swipe controller of the history list Reference https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28 BY Artur Grzybowski
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                ArrayList<Record> records = rh.getRecords();
                int pos = viewHolder.getAdapterPosition();  // get the position of the swiped record
                if (i==ItemTouchHelper.LEFT ) {  // LEFT swipe deletes the record
                    records.remove(pos);
                    rh = new RecordHistory(records);
                    recordAdapter.notifyDataSetChanged();
                    saveInFile();
                }
                else {  // Right swipe edits the record
                    openEditor(pos);
                }
            }

        }).attachToRecyclerView(recordList);

    }

    // open the editor activity
    public void openEditor(int pos) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra(RECORD_POSITION, pos);  // pass the target position in the record list
        startActivity(intent);
    }

    // reload record history when resume (such as after editing a record)
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile();
        recordAdapter = new RecordAdapter(rh);
        recordList.setAdapter(recordAdapter);
    }

    // file io with record history
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
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(rh,osw);
            osw.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
