
import java.util.Random;

public class MergeSort {

    // Gera um array aleatório de 10 números entre 0 e 25
    public static int[] arrayAleatorio() {
        int tamanho = 10;
        int[] array = new int[tamanho];
        Random random = new Random();

        for (int i = 0; i < tamanho; i++) {
            array[i] = random.nextInt(25);
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

    // Recebe array , inicio , fim e meio
    public static void Mege(array[], int I, int F, int M) {

        //acha o tamanho dos dois sub arrys 
        int N1 = M - I + 1;
        int N2 = F - M;

        int I[] = new int[N1];
        int F[] = new int[N2];

    }

    static void Sort(int array[], int I, int F) {

        if (I < F) {

            //acha o meio 
            int M = I + (F - I) / 2;

        }

    }

    public static void main(String[] args) {

        Random random = new Random();

        int[] array = arrayAleatorio(); // Gera o array aleatório

        int F = array.length - 1;
        int I = 0;

        Sort(array, I, F);

    }

}
