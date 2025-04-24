

/* 
import java.util.*;

public class P1 {

    public static void Rank() {

        Scanner scanner = new Scanner(System.in);// N tive tempo de terminar a minha logica estva errada pq eu achei q
                                                 // os numeros eram pontos e n players

        int numeroR;
        int tam;

        numeroR = scanner.nextInt();
        tam = scanner.nextInt();

        if (numeroR == 0 && tam == 0) {
            System.out.println("fim");
        }

        numeroR = scanner.nextInt();
        tam = scanner.nextInt();

        int arrayR[] = new int[tam];
        int arraySec[] = new int[tam];

        for (int i = 0; i < numeroR; i++) {

            for (int j = 0; j < tam; j++) {

                arrayR[j] = scanner.nextInt();

            }

        }

        Arrays.sort(arrayR);
        /*
         * n tive tempo de criar a logica que seleciona o numero que mais apareceu porem
         * seria algo como:
         * 
         * for(int i = 0; i < arrayR; i++){
         * int maior = 0;
         * int numero;
         * int cont;
         * 
         * for (int j = 0; j<arryR, j++){
         * 
         * 
         * 
         * if(arryR[j] == arrayR[j] ){
         * 
         * cont++;
         * if(cont> maior ){
         * 
         * maior = cont;
         * 
         * }
         * numero = arraR[j]
         * }
         * 
         * }
         * 
         * }
         * 
         

        for (int d = 0; d < arrayR.length; d++) {

            System.out.println(arrayR[d]);

        }

        /*
         * 
         * 
         * while (1) {
         * 
         * int numeroR;
         * int tam;
         * 
         * if (numeroR == 0 && tam == 0) {
         * break;
         * }
         * 
         * numeroR = scanner.nextInt();
         * tam = scanner.nextInt();
         * 
         * int arrayR[] = new int[tam];
         * int arraySec[] = new int[tam];
         * 
         * for (int i = 0; i < numeroR; i++) {
         * 
         * for (int j = 0; j < tam; j++) {
         * 
         * arrayR[j] = scanner.nextInt();
         * 
         * }
         * 
         * Arrays.sort(arrayR);
         * 
         * arraySec[i] = arrayR[tam - 1];
         * 
         * }
         * 
         * Arrays.sort(arraySec);
         * 
         

        scanner.close();
    }

    public static void main(String[] args) {

        Rank();

    }
}

*/

import java.util.*;

public class P1 {

    public static void Rank() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int numeroR = scanner.nextInt();
            int tam = scanner.nextInt();

            if (numeroR == 0 && tam == 0) {
                System.out.println("fim");
                break;
            }

            // Vetor para contar votos de cada jogador (assumindo que IDs vão de 1 até tam)
            int[] votos = new int[101]; // assume IDs de 1 até 100 (ajuste se necessário)

            // Ler todos os votos de todas as rodadas
            for (int i = 0; i < numeroR; i++) {
                for (int j = 0; j < tam; j++) {
                    int jogador = scanner.nextInt();
                    votos[jogador]++;
                }
            }

            // Encontrar o segundo maior valor de votos
            int maior = -1, segundoMaior = -1;

            for (int i = 1; i < votos.length; i++) {
                if (votos[i] > maior) {
                    segundoMaior = maior;
                    maior = votos[i];
                } else if (votos[i] > segundoMaior && votos[i] < maior) {
                    segundoMaior = votos[i];
                }
            }

            // Imprimir todos os jogadores com o segundo maior número de votos
            for (int i = 1; i < votos.length; i++) {
                if (votos[i] == segundoMaior) {
                    System.out.println(i);
                }
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        Rank();
    }
}
