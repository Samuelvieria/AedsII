package SORTS;
import java.util.Random;

public class ExchangeSort {

    // Gera um array aleatório de 10 números entre 0 e 100
    public static int[] arrayAleatorio() {
        int tamanho = 10;
        int[] array = new int[tamanho];
        Random random = new Random();

        for (int i = 0; i < tamanho; i++) {
            array[i] = random.nextInt(100);
        }

        return array;
    }

    // Função para imprimir arrays
    public static void Print(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }

    // Método para trocar elementos no array
    public static void Swap(int[] array, int a, int b) {
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    // Implementação do Exchange Sort
    public static void Ssort(int[] array) {
        for(int i = 0; i<array.length; i++){
            for(int j = i +1 ; i< array.length; i++){
                if(array[i]>array[j]){
                    Swap(array, i, j);
                }

            }
        }
    }

    public static void main(String[] args) {
        int[] array = arrayAleatorio(); // Gera o array aleatório

        System.out.println("Array antes da ordenação:");
        Print(array);

        Ssort(array); // Ordena o array

        System.out.println("Array depois da ordenação:");
        Print(array);
    }
}
