package ca.ualberta.twang2.twang2_feelsbook;

public class Comment {
    // data
    private String text;

    //constructors
    Comment() {
        this.text = "";
    }

    // getters and setters
    public void setText(String text) throws TooLongCommentException {
        if (text.length()>100)
            throw new TooLongCommentException();
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
