package SORTS;

import java.util.Random;

public class QuikSort {

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
    // Método para trocar elementos no array
    public static void Swap(int[] array, int a, int b) {
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    public static int[] sort(int[] array, int esq, int dir) {

        int i = esq;
        int j = dir;
        int pivo = (esq+dir)/2;

        while(i<=j){
            while(array[i]<array[j])i++;
            while(array[j]>array[i])j--;
            if(i<j){
                Swap(array,i,j);
                i++;
                j--;
            }
            if (esq < j)  sort(array,esq, j);
            if (i < dir)  sort(array,i, dir);


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