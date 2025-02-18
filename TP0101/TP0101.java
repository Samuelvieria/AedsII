import java.util.Scanner;

public class TP0101 {

    public static boolean Palindromo(String palavra) {
        String palavraMinu = palavra;
        int tamanho = palavraMinu.length();

        for (int i = 0; i < tamanho / 2; i++) {
            int j = tamanho - 1 - i; // Índice do outro lado da string
            if (palavraMinu.charAt(i) != palavraMinu.charAt(j)) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            input = scanner.nextLine();
            if (input.equals("FIM")) {
                break;
            }

            if (Palindromo(input)) {
                System.out.println("SIM");
            } else {
                System.out.println("NAO");
            }
        }

        scanner.close();
    }
}
