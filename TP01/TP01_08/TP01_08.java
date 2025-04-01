package TP01.TP01_08;

import java.util.Scanner;

public class TP01_08 {

    public static int Somarec(char[] numChar,int i){

        if (i == numChar.length) {
            return 0;
        }


        int numero = numChar[i] -'0';

     return numero + Somarec(numChar, i+1);
    }

    public static void Soma(String num ){
        char[] numChar = num.toCharArray();

       int somaN= Somarec(numChar,0);
        

    



        System.out.println(somaN);

    
    }

    public static void main(String[] args) {
        Scanner palavra = new Scanner(System.in);

        while (true) {
            String palavra2 = palavra.nextLine();
            if (palavra2.equals("FIM")) {
                palavra.close();
                return;
            }


            Soma(palavra2);
        }
    }
    
}
