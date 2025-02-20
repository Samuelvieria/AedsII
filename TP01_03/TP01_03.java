package TP01_03;
import java.util.Scanner;

public class TP01_03 {

    public static String ciframento(String palavra) {
        int tamanho = palavra.length();
        char[] palavraChar = palavra.toCharArray();

        for (int i = 0; i < tamanho; i++) {
            palavraChar[i] = (char) (palavraChar[i] + 3);
        }

        return new String(palavraChar);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            input = scanner.nextLine();
            if (input.equals("FIM")) {
                break;
            }

           System.out.println(ciframento(input));
        }

        scanner.close();
    }
}
