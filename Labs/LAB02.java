import java.util.Arrays;
import java.util.Scanner;

public class LAB02 {

    public static void Espelho(int menor , int maior){

        int tamanho = (maior - menor) + 1;

        int[] cres = new int[tamanho];
        int[] decre = new int[tamanho];
        int[] esp = new int[tamanho * 2];


        for (int i = 0; i < tamanho; i++) {
            cres[i] = menor + i;
        }

        for (int i = 0; i < tamanho; i++) {
            decre[i] = maior - i;
        }

        for (int i = 0; i < tamanho; i++) {
            esp[i] = cres[i];            
            esp[tamanho + i] = decre[i]; 
        }


       System.out.println(Arrays.toString(cres));

       System.out.println(); 

       System.out.println(Arrays.toString(decre));
        


        for (int i = 0; i < esp.length; i++) {
            System.out.print(esp[i]);
        }

        System.out.println(); 
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

       
        while (scanner.hasNextInt()) {
            int menor = scanner.nextInt();
            int maior = scanner.nextInt();

            Espelho(menor, maior);
        }

        scanner.close();
    }
}
