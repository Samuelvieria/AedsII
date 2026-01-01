import java.util.Scanner;

public class StringReverse {
    public static String reverseString(String text) {
        char[] chars = text.toCharArray();
        int left = 0;
        int right = chars.length - 1;
        
        while (left < right) {
            
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }
        
        return new String(chars);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Lê entrada até EOF
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String reversed = reverseString(line);
            System.out.println(reversed);
        }
        
        scanner.close();
    }
}