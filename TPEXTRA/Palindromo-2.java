/*import java.util.Scanner;

public class Palindromo {

    public static boolean ISfim(String palavra) {
        if (palavra.length() == 3 && 
            palavra.charAt(0) == 'F' && 
            palavra.charAt(1) == 'I' && 
            palavra.charAt(2) == 'M') {
            return true;
        }
        return false;

    }

    public static void Ispalindromo() {
        Scanner scanner = new Scanner(System.in);

        String palavra = scanner.nextLine();

        if (ISfim(palavra) == false) {

            int tam = palavra.length();

            boolean eh = true;
            int j = tam - 1;

            for (int i = 0; i < tam / 2; i++) {

                if (palavra.charAt(i) != palavra.charAt(j)) {
                    eh = false;
                }
                j--;

            }

            if (eh == false) {

                System.out.println("NAO");
            } else {

                System.out.println("SIM");

            }

            Ispalindromo();
        }

    }

    public static void main(String[] args) {

        Ispalindromo();

    }

}
 */

 import java.util.Scanner;

public class Palindromo {

    public static boolean ISfim(String palavra) {
        return (palavra.length() == 3 &&
                palavra.charAt(0) == 'F' &&
                palavra.charAt(1) == 'I' &&
                palavra.charAt(2) == 'M');
    }

    public static void Ispalindromo() {
        Scanner scanner = new Scanner(System.in);

        String palavra = scanner.nextLine();

        while (!ISfim(palavra)) {

            int tam = palavra.length();
            boolean eh = true;

            for (int i = 0, j = tam - 1; i < tam / 2; i++, j--) {
                if (palavra.charAt(i) != palavra.charAt(j)) {
                    eh = false;
                    break;
                }
            }

            if (eh) {
                System.out.println("SIM");
            } else {
                System.out.println("NAO");
            }

            palavra = scanner.nextLine(); 
        }

        scanner.close();
    }

    public static void main(String[] args) {
        Ispalindromo();
    }
}
