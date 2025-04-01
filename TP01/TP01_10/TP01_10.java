package TP01.TP01_10;


import java.util.Scanner;

public class TP01_10 {

    public static int QuantPalavras(String p){
    int quant=1;
       char[] palavraChar = p.toCharArray();
        int tam = p.length();

        for(int i = 0 ; i<tam; i++){

            if(palavraChar[i] == ' '){
                quant ++;
            }
        }


      return quant;
    }

    public static void main(String[] args) {

        Scanner palavra = new Scanner(System.in);

        while (true) {
            String palavra2 = palavra.nextLine();
            if (palavra2.equals("FIM")) {
                palavra.close();
                return;
            }

            System.out.println(QuantPalavras(palavra2));
        }
        
    }
    
}
