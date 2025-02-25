package TP01_11;

import java.util.Scanner;

public class TP01_11 {

    public static void Maiorseq(String p){

        char[] Pchar = p.toCharArray();
        int tamnho = p.length();
        int seq = 0;
        char[] pseq = new char[20];

        for (int i = 0; i<tamnho; i++){
             pseq[i] = p.charAt(i);
             for(int )

        }

    }








    public static void main(String[] args) {

         Scanner scanner = new Scanner(System.in);

        while (true) {
            String entrada = scanner.nextLine();
            if (entrada.equals("FIM")) {
                break;
            }
            Maiorseq(entrada);
        }

        scanner.close();

    }
    
}
