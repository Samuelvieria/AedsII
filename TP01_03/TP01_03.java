
package TP01_03;

import java.util.Scanner;

public class TP01_03 {

    public static String ciframento(String palavra) {
        int tamanho = palavra.length();
        char[] palavraChar = palavra.toCharArray();

        for (int i = 0; i < tamanho; i++) {
            
            palavraChar[i] = (char)(palavra.charAt(i) + 3);
        }

        return new String(palavraChar);
    }

    public static void main(String[] args) {
        Scanner palavra = new Scanner(System.in);

        while (true) {
            String palavra2 = palavra.nextLine();
            if (palavra2.equals("FIM")) {
                palavra.close();
                return;
            }

            System.out.println(ciframento(palavra2));
        }
    }
}
