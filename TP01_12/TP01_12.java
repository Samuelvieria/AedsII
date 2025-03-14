
import java.util.Scanner;

public class TP01_12 {

    public static void senha(String senha) {
        if (senha.length() < 8) {
            System.out.println("NAO");
            return;
        }

        boolean temMaiuscula = false;
        boolean temMinusculo = false;
        boolean temNumero = false;
        boolean temEspecial = false;

        for (int i = 0; i < senha.length(); i++) {
            char c = senha.charAt(i);

            if (Character.isUpperCase(c)) {
                temMaiuscula = true;
            } else if (Character.isLowerCase(c)) {
                temMinusculo = true;
            } else if (Character.isDigit(c)) {
                temNumero = true;
            } else {
                temEspecial = true;
            }
        }

        if (temNumero && temMaiuscula && temMinusculo && temEspecial) {
            System.out.println("SIM");
        } else {
            System.out.println("NAO");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String entrada = scanner.nextLine();
            if (entrada.equals("FIM")) {
                break;
            }
            senha(entrada);
        }

        scanner.close();
    }
}
