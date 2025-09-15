package interview_questions;

public class IsPalindrome {

    public static boolean isPalindrome(String s) {
        int i = 0;
        int j = s.length() - 1;
        while (i < j) {
            char a = s.charAt(i);
            char b = s.charAt(j);
            if (!Character.isLetterOrDigit(a)) {
                //for ignoring punctuation
                i++;
                continue;
            }
            if (!Character.isLetterOrDigit(b)) {
                //for ignoring punctuation
                j--;
                continue;
            }
            if (Character.toLowerCase(a) != Character.toLowerCase(b)) {
                return false;
            }
            i++;
            j--;
        }

        return true;
    }

}
