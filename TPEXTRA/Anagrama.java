import java.util.Scanner;

public class Anagrama {

    public static boolean ehAnagrama(String a, String b) {
        if (a.length() != b.length()) return false;
        char[] ca = a.toLowerCase().toCharArray();
        char[] cb = b.toLowerCase().toCharArray();

        for (int i = 0; i < ca.length - 1; i++) {
            for (int j = i + 1; j < ca.length; j++) {
                if (ca[i] > ca[j]) {
                    char tmp = ca[i];
                    ca[i] = ca[j];
                    ca[j] = tmp;
                }
            }
        }

        for (int i = 0; i < cb.length - 1; i++) {
            for (int j = i + 1; j < cb.length; j++) {
                if (cb[i] > cb[j]) {
                    char tmp = cb[i];
                    cb[i] = cb[j];
                    cb[j] = tmp;
                }
            }
        }

        for (int i = 0; i < ca.length; i++) {
            if (ca[i] != cb[i]) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            String linha = sc.nextLine().trim();
            if (linha.equals("FIM")) break;

            String[] partes = linha.split(" ");
            String a = partes[0];
            String b = partes[partes.length - 1];

            if (ehAnagrama(a, b)) {
                System.out.println("SIM");
            } else {
                System.out.println("N\u00C3O");

            }
        }

        sc.close();
    }
}
