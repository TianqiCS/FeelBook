package ca.ualberta.twang2.twang2_feelsbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

/*
 * the RecordHistory contains records and provide several way to access them
 */
class RecordHistory {
    // data
    private ArrayList<Record> records;  // how it is implemented

    // constructors
    RecordHistory() {
        this.records = new ArrayList<>();
    }

    RecordHistory(ArrayList<Record> records) {
        this.records = records;
    }

    // add a record to the history
    void addRecord(Record record) {
        this.records.add(record);
    }

    // remove a record from history
    void delRecord(Record record) {
        if (!this.records.remove(record)) {
            System.out.println("Remove record unsuccessfully");
        }
        System.out.println("record removed");

    }

    // sort itself with RecordComparator (by time)
    private void sortRecord() {
        Collections.sort(this.records,new RecordComparator());
    }

    // get the actual numbers for how many feelings recorded
    Hashtable<String, Integer> getStats() {
        Hashtable<String, Integer> stats = new Hashtable<>();

        String[] feelings = {"LOVE", "JOY", "SURPRISE", "ANGER", "SADNESS", "FEAR"}; // all feelings
        for (String i : feelings) {
            stats.put(i,0);
        }

        for (Record r : this.records) {
            if (stats.containsKey(r.getFeeling())) {
                stats.put(r.getFeeling(),stats.get(r.getFeeling())+1);
            }
        }

        return stats;
    }




    // getters
    ArrayList<Record> getRecords() {
        sortRecord();
        return records;
    }


}
