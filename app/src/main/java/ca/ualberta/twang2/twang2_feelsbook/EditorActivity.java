package ca.ualberta.twang2.twang2_feelsbook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.Date;
/*
 * EditorActivity is the Activity for editing saved record
 * It can be entered from HistoryActivity
 */
public class EditorActivity extends AppCompatActivity {

    private static final String FILENAME = "history.sav";  // save file name
    private RecordHistory rh;  // Object of the history
    private RadioGroup radioGroup;  // the radioGroup object which contains 6 different feelings
    private TextView comments;  // comments view of the entry
    private static Calendar newTime = Calendar.getInstance();  // Date builder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        loadFromFile();  // load record history from file, if not exist create one

        Intent intent = getIntent();
        int pos = intent.getIntExtra(HistoryActivity.RECORD_POSITION,-1);  // get the target position

        ArrayList<Record> records = rh.getRecords();
        final Record record = records.get(pos);  // get the record
        String feeling = record.getFeeling();

        // check the feeling from record
        // has to use switch, record know what it is but radiobutton does not
        radioGroup = findViewById(R.id.feelingGroup);
        switch (feeling) {
            case "LOVE":
                radioGroup.check(R.id.loveButton);
                break;
            case "JOY":
                radioGroup.check(R.id.joyButton);
                break;
            case "SURPRISE":
                radioGroup.check(R.id.surpriseButton);
                break;
            case "ANGER":
                radioGroup.check(R.id.angerButton);
                break;
            case "SADNESS":
                radioGroup.check(R.id.sadnessButton);
                break;
            case "FEAR":
                radioGroup.check(R.id.fearButton);
                break;
        }

        // set comments textView from the record
        comments = findViewById(R.id.comments);
        comments.setText(record.getComment().getText());

        // set datetime from the record
        Date datetime = record.getDate();
        newTime.setTime(datetime);

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // commit and replace the record with new one which built below
                try {
                    rh.delRecord(record);
                    RadioButton checked = findViewById(radioGroup.getCheckedRadioButtonId());
                    String feeling = (String) checked.getText();
                    Comment comment = new Comment();
                    comment.setText(comments.getText().toString());
                    record.setFeeling(feeling);
                    record.setComment(comment);
                    record.setDate(newTime.getTime());
                    rh.addRecord(record);
                    Toast.makeText(getApplicationContext(), "Record Saved", Toast.LENGTH_SHORT).show();
                    saveInFile();
                    finish();  // return to historyActivity

                } catch (TooLongCommentException e) {  // comments length >100 will stop from committing
                    commentsTooLong();
                    Toast.makeText(getApplicationContext(), "Change not Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Time and Date pickers Reference from android tutorial https://developer.android.com/guide/topics/ui/controls/pickers
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = newTime;
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            int year = newTime.get(Calendar.YEAR);
            int month = newTime.get(Calendar.MONTH);
            int day = newTime.get(Calendar.DAY_OF_MONTH);
            newTime.set(year, month, day, hourOfDay, minute);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = newTime;
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            newTime.set(year, month, day);
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // file i/o with record history
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
            e.printStackTrace();
        }
    }

    private void commentsTooLong() {
        // show the window to tell comments are too long
        AlertDialog.Builder ad = new AlertDialog.Builder(EditorActivity.this);
        ad.setTitle("RECORD");
        ad.setMessage("Comments too long!\nWord count must be under 100");
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });
        ad.show();
    }
}
