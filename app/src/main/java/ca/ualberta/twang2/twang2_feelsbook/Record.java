package ca.ualberta.twang2.twang2_feelsbook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Record {
    // data
    private  String feeling;
    private Date date;
    private Comment comment;

    // constructors
    public Record(String feeling, Date date, Comment comment) {
        this.feeling = feeling;
        this.date = date;
        this.comment = comment;
    }

    public Record(String feeling, Date date) {
        this.feeling = feeling;
        this.date = date;
        this.comment = new Comment();
    }

    Record(String feeling, Comment comment) {
        this.feeling = feeling;
        this.date = new Date();
        this.comment = comment;
    }

    public Record(String feeling) {
        this.feeling = feeling;
        this.date = new Date();
        this.comment = new Comment();
    }



    // getters and setters
    String getFeeling() {
        return feeling;
    }

    void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    Comment getComment() {
        return comment;
    }

    void setComment(Comment comment) {
        this.comment = comment;
    }


    //override
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(feeling);
        for (int i=0;i<(18-feeling.length()*2);i++) {
            sb.append(" ");
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = df.format(this.getDate());
        return sb.toString() + " |  " + date + "\n" + comment.getText();
    }
}
