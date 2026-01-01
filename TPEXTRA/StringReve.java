import java.util.Scanner;

public class StringReve {

    // Função recursiva para inverter string
    public static String reverseString(String text) {
        if (text.length() <= 1) {
            return text;
        }
        // pega o último caractere + chama recursivamente o resto
        return text.charAt(text.length() - 1) + reverseString(text.substring(0, text.length() - 1));
    }

    // Função para verificar se é "FIM" manualmente
    public static boolean ehFIM(String s) {
        if (s.length() != 3) return false;
        return (s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M');
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String entrada = sc.nextLine();

        while (!ehFIM(entrada)) {
            String reversed = reverseString(entrada);
            System.out.println(reversed);

            entrada = sc.nextLine(); // lê próxima linha
        }

        sc.close();
    }
}
