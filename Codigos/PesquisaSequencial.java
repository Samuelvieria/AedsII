import java.util.Random;

public class PesquisaSequencial {

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

    //função de pesquisa sequencial 
    public static boolean pesquisaSeq(int[] array, int num){

        boolean resp = false;
        for(int i = 0; i<array.length;i++){
            if(array[i] == num){
                resp = true;
                return resp;
            }
        }
        return resp;
    }




    public static void main(String[] args) {
        Random random = new Random();

        int[] array = arrayAleatorio(); // Gera o array aleatório
        int num = random.nextInt(25); //Numero aleatório 

        System.out.println("numero pesquisado \n" + num);
        System.out.println("Array");
        Print(array);

        if(pesquisaSeq(array,num) == true){
            System.out.println("numero encontrado!!");
        }else{
            System.out.println("Numero n encontrado ):");
        }
        

        
    }
    
}
