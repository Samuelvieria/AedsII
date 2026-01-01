import java.util.Scanner;

public class SomaDigitos {

    public static int somaDigitos(int n) {
        int soma = 0;
        while (n > 0) {
            soma += n % 10;
            n /= 10;
        }
        return soma;
    }

  
    public static boolean ehFIM(String s) {
        if (s.length() != 3) {
            return false;
        }
        return (s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M');
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String entrada = sc.next();

        
        while (!ehFIM(entrada)) {
            int numero = Integer.parseInt(entrada);
            System.out.println(somaDigitos(numero));

            entrada = sc.next(); 
        }

        sc.close();
    }
}
