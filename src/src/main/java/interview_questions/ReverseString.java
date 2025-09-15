package interview_questions;

public class ReverseString {

    public static String reverse(String s) {
        String response = "";
        StringBuilder stringBuilder = new StringBuilder(s);
        response = stringBuilder.reverse().toString();
        return response;
    }

}
