package TP01.TP01_07;


import java.util.Scanner;

public class TP01_07 {

    public static String Invertido(String palavra){

        int tamanho = palavra.length();
        char[] palavraChar = palavra.toCharArray();

        for (int i = 0; i<tamanho ; i++){

            palavraChar[tamanho-i-1] = palavra.charAt(i);

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

            System.out.println(Invertido(palavra2));
        }
        
    }
    
}
