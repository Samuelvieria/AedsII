package SORTS;
import java.util.Random;

public class Bolha {

    public static int[] arrayAleatorio() {
        int tamanho = 10;
        int[] array = new int[tamanho];
        Random random = new Random();

        for (int i = 0; i < tamanho; i++) {
            array[i] = random.nextInt(25);
        }

        return array;
    }

    public static void print(int[] array) {
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    public static int[] sort(int[] array) {
        int tam = array.length;


        
        /* 
        Fiz um selection sort sem querer
         *  for(int i = 0; i<tam ; i++){
            for(int j = i + 1; j<tam; j++){
              if(array[i]>array[j]){
                int tmp;
                tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
              }

            }
        }
        */

        for(int i = 0; i<tam-1 ; i++){
            for(int j = 0; j<tam - 1 - i; j++){
              if(array[j]>array[j+1]){
                int tmp;
                tmp = array[j];
                array[j] = array[j+1];
                array[j+1] = tmp;
              }

            }
        }
       

        
        return array;
    }

    public static void main(String[] args) {
        int[] array = arrayAleatorio();

        System.out.println("Array antes da ordenação:");
        print(array);

        sort(array);

        System.out.println("Array depois da ordenação:");
        print(array);
    }

    
}
// Custo de comparacao sempre é O(nˆ2) mesmo com a implementacao pra reduzir comparacoes.

//Custo de troca: Melhor caso quando o array já esta ordenado, n tem nenhuma troca O(0). Pior quando se tem que trocar todos ou muitos O(nˆ2).