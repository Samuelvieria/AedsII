package TP01_04;

import java.util.Random;
import java.util.Scanner;

public class TP01_04 {

    public static String Aleatorio(String palavra,Random gerador){
        
        int tamanho = palavra.length();
        char[] palavraChar = palavra.toCharArray();

        char letra1 = (char)('a' + (Math.abs(gerador.nextInt()) % 26));
        char letra2 = (char)('a' + (Math.abs(gerador.nextInt()) % 26));

       for(int i = 0; i<tamanho; i++){

        if(palavraChar[i] == letra1){

            palavraChar[i] = letra2;
        }
       }

    
      
       return new String(palavraChar);
    }




    public static void main(String[] args) {

        Scanner palavra = new Scanner(System.in);
        Random gerador = new Random();
        gerador.setSeed(4);

        while (true) {
            String palavra2 = palavra.nextLine();
            if (palavra2.equals("FIM")) {
                palavra.close();
                return;
            }

            System.out.println(Aleatorio(palavra2,gerador));
        }
        
    }
    
}
