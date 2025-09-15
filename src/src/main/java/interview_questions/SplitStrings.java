package interview_questions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SplitStrings {
    /***
     * Complete the solution so that it splits the string into pairs of two characters. If the string contains an odd
     * number of characters then it should replace the missing second character of the final pair with an underscore ('_').
     */
    public static String[] solution(String s) {
        List<String> result = new ArrayList<>();
        List<String> charArray = s.chars()
                .mapToObj(c -> String.valueOf((char) c)) // FIX: cast to char
                .collect(Collectors.toList());

        boolean isEven = charArray.size() % 2 == 0;
        if (isEven) {
            for (int i = 0; i < charArray.size() - 1; i += 2) {
                String together = charArray.get(i) + charArray.get(i + 1);
                result.add(together);
            }
        } else {
            List<String> oddList = new ArrayList<>(charArray);
            oddList.add("_");
            for (int i = 0; i < oddList.size() - 1; i += 2) {
                String together = oddList.get(i) + oddList.get(i + 1);
                result.add(together);
            }
        }

        return result.toArray(new String[0]); // small cleanup
    }

}

