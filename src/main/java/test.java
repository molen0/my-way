import java.util.HashMap;
import java.util.Map;

class Solution {
    public int firstUniqChar(String s) {

        for (int i = 0; i < s.length(); i++) {
            if (s.indexOf(s.charAt(i)) == s.lastIndexOf(s.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public static void change(Integer a) {
        a--;
    }

    public static void main(String[] args) {
        int a = 1;
        Integer b = 1;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        long l = 3L;
        Long g = 3L;
        Long h = 2L;
        System.out.println(b.equals(a));
    }
}