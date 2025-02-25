


import java.util.Arrays;
import java.util.Scanner;

public class TP01_09 {

    public static void Anagrama(String palavra) {
       String palavraMin= palavra.toLowerCase();

        String[] palavras = palavraMin.split(" " );


        char[] p1Char = palavras[0].toCharArray();
        char[] p2Char = palavras[2].toCharArray();

        Arrays.sort(p1Char);
        Arrays.sort(p2Char);

        String a = new String(p1Char);
        String b = new String(p2Char);

        if (a.equals(b)) {
            System.out.println("SIM");
        } else {
            System.out.println("N"+'\u00C3'+"O");
        }
        
    }
    
    public static void main(String[] args) {
        Scanner palavra = new Scanner(System.in);

        while (palavra.hasNextLine()) {
            String palavra2 = palavra.nextLine();
            if (palavra2.equals("FIM")) {
                break;  
            }

           
                Anagrama(palavra2);
           
        }

        palavra.close();  
    }
}
