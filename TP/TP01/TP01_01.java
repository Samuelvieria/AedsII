

import java.util.Scanner;


public class TP01_01 {

    public static void Palindromo(String palavra) {
        String palavraMinu = palavra.toLowerCase(); 
        boolean ehPali = true; 

        for (int i = 0; i < palavraMinu.length(); i++) {
  
            for (int j = palavraMinu.length() - 1; j >= 0; j--) {
               
                if (palavraMinu.charAt(i) != palavraMinu.charAt(j)) {
                    ehPali = false; 
                    break; 
                }
            }
            if (!ehPali) {
                break; // Sai do laço externo também
            }
        }

        if (ehPali) {
            System.out.println("SIM");
        } else {
            System.out.println("NAO");
        }
    }

    // Função de leitura
    public static String leitura() {
        Scanner scanner = new Scanner(System.in);
        String palavra = scanner.nextLine();
        return palavra;
    }

    public static void main(String[] args) {
        while (true) {
            String input = leitura(); 
            if (input.equals("FIM")) { // Verifica se a palavra é "FIM"
                break;
            }
            Palindromo(input);
        }
    }
}
