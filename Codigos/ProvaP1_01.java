import java.util.Arrays;
import java.util.Scanner;

public class ProvaP1_01 {

    public static void Corteobi(){
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int k = scanner.nextInt();

        int[] notas = new int[n];

        for(int i = 0 ; i<n; i++ ){
            notas[i] = scanner.nextInt();
        }
   Arrays.sort(notas);

   for(int j = 0 ; j<n; j++ ){
    System.out.println(notas[j]);
}
   System.out.println(notas[n]);
    }
    
    public static void main(String[] args) {

        Corteobi();

        
    }
    
}
