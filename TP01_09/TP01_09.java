package TP01_09;

import java.util.Arrays;
import java.util.Scanner;


public class TP01_09 {

    public static void Anagrama(String p1, String p2){

        char[] p1Char = p1.toCharArray();
        char[] p2Char = p2.toCharArray();
    
        Arrays.sort(p1Char);
        Arrays.sort(p2Char);
    
        
        String a = new String(p1Char);
        String b = new String(p2Char);
    
        if(a.equals(b)){
            System.out.println("SIM");
        }else{
            System.out.println("NÃO");
        }
    
       
    
    
    }
    

    public static void main(String[] args) {
        Scanner palavra = new Scanner(System.in);

        while (true) {
            String palavra2 = palavra.nextLine();
            String palavra3 = palavra.nextLine();
            if (palavra2.equals("FIM")) {
                palavra.close();
                return;
            }

            Anagrama(palavra2,palavra3);
        }
        
    }

    
}
