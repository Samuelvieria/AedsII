
import java.util.Scanner;

public class contrato {

    public static void numeros() {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            int d = scanner.nextInt();
            int n = scanner.nextInt();

            if (d == 0 && n == 0) {
                return;
            }

            String ns = Integer.toString(n);
            int tamanho = ns.length();
            int remove = (char)(d);
            int[] resp = new int[tamanho];

            for(int p = 0; p<tamanho;p++){

               
                    resp[p] = ns.charAt(p);
                

            }
            

            

            System.out.println(resp);
        }
        
        
       
    }

    public static void main(String[] args) {

        numeros();

        return;
    }

}
