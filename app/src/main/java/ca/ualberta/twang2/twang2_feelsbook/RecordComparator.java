package ca.ualberta.twang2.twang2_feelsbook;

import java.util.Comparator;

/*
 * a comparator for sorting records
 */
class RecordComparator implements Comparator<Record> {
    @Override
    public int compare(Record r1, Record r2) {
        return r1.getDate().compareTo(r2.getDate());
    }
}
