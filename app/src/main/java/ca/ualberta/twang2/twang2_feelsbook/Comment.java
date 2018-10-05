package ca.ualberta.twang2.twang2_feelsbook;

/*
 * Comment is the class for comment entities
 */
public class Comment {
    // data
    private String text;  // the actual words inside the comment

    //constructors
    Comment() {
        this.text = "";
    }

    // getters and setters
    public void setText(String text) throws TooLongCommentException {
        if (text.length()>100)
            throw new TooLongCommentException();  // in case user enter too long
        this.text = text;
    }

    public String getText() {
        return text;
    }  // return the text

}
