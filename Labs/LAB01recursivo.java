package LABS

import java.util.Scanner;

public class LAB01recursivo {


    public static int ContagemRecursiva(String palavra, int i) {
        if (i == palavra.length()) {
            return 0;
        }
        int count = 0;
        if (Character.isUpperCase(palavra.charAt(i))) {
            count = 1;
        }
        return count + ContagemRecursiva(palavra, i + 1);
    }
    
    // Método para verificar se a palavra é "FIM"
    public static boolean Fim (String palavra){

        if(palavra.length() == 3 && palavra.charAt(0) == 'F' && palavra.charAt(1) == 'I' && palavra.charAt(2) == 'M'){
            return true;
        }else{
            return false;
        }

    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String palavra;
            while (true) {
                palavra = scanner.nextLine();
                if (Fim(palavra)) {
                    break;
                }
                int contagem = ContagemRecursiva(palavra, 0);
                System.out.println(contagem);
            }
        }
    }
}
