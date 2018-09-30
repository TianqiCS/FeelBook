package ca.ualberta.twang2.twang2_feelsbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

class RecordHistory {
    // data
    private ArrayList<Record> records;

    // constructors
    RecordHistory() {
        this.records = new ArrayList<>();
    }

    RecordHistory(ArrayList<Record> records) {
        this.records = records;
    }

    // methods
    void addRecord(Record record) {
        this.records.add(record);
    }

    void delRecord(Record record) {
        if (!this.records.remove(record)) {
            System.out.println("Remove record unsuccessfully");
        }
        System.out.println("record removed");

    }

    private void sortRecord() {
        Collections.sort(this.records,new RecordComparator());
    }

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
