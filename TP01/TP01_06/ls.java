package TP01.TP01_06;
import java.util.Scanner;

public class ls {

    static boolean vogal(String palavra) {

        for (int i = 0; i < palavra.length(); i++) {

            if (palavra.charAt(i) == 'a' || palavra.charAt(i) == 'A'
                    || palavra.charAt(i) == 'e' || palavra.charAt(i) == 'E'
                    || palavra.charAt(i) == 'i' || palavra.charAt(i) == 'I'
                    || palavra.charAt(i) == 'o' || palavra.charAt(i) == 'O'
                    || palavra.charAt(i) == 'u' || palavra.charAt(i) == 'U') {
            } else {
                return false;
            }
        }
        return true;
    }

    static boolean consoante(String palavra) {

        for (int i = 0; i < palavra.length(); i++) {

            if (palavra.charAt(i) != 'a' && palavra.charAt(i) != 'A'
                    && palavra.charAt(i) != 'e' && palavra.charAt(i) != 'E'
                    && palavra.charAt(i) != 'i' && palavra.charAt(i) != 'I'
                    && palavra.charAt(i) != 'o' && palavra.charAt(i) != 'O'
                    && palavra.charAt(i) != 'u' && palavra.charAt(i) != 'U'
                    && ((palavra.charAt(i) >= 'a' && palavra.charAt(i) <= 'z')
                            || (palavra.charAt(i) >= 'A' && palavra.charAt(i) <= 'Z'))) {
            } else {
                return false;
            }
        }
        return true;
    }

    static boolean inteiro(String palavra) {
        for (int i = 0; i < palavra.length(); i++) {
            if (palavra.charAt(i) >= '0' && palavra.charAt(i) <= '9') {
            } else {
                return false;
            }
        }
        return true;
    }

    static boolean real(String palavra) {
        int cont = 0;
        for (int i = 0; i < palavra.length(); i++) {
            if (palavra.charAt(i) >= '.' || palavra.charAt(i) <= ',') {
                cont++;
                if(cont==2){return false;}
            } else {
                return false;
            }
        }
        return true;
    }

    static void escreva(String palavra) {

        if (vogal(palavra) == true) {
            System.out.print("SIM" + " ");
        } else {
            System.out.print("NAO" + " ");
        }

        if (consoante(palavra) == true) {
            System.out.print("SIM" + " ");
        } else {
            System.out.print("NAO" + " ");
        }

        if (inteiro(palavra) == true) {
            System.out.print("SIM" + " ");
        } else {
            System.out.print("NAO" + " ");
        }

        if (real(palavra) == true) {
            System.out.print("SIM" + " ");
        } else {
            System.out.print("NAO" + " ");
        }

        System.out.println("");

    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            String palavra = sc.nextLine();
            if (palavra.equals("FIM")) {
                sc.close();
                System.exit(0);
            }

            escreva(palavra);

        }

    }

}
