package TP01_06;

import java.util.Scanner;

public class TP01_06 {

    public static void Is(String p){





        

    }
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String entrada = scanner.nextLine();
            if (entrada.equals("FIM")) {
                break;
            }
            Is(entrada);
        }

        scanner.close();



        
    }
    
}
