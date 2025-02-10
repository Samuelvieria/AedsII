import java.util.Scanner;

public class LAB1 {

    // Método para contar o número de caracteres maiúsculos em uma string
    public static int Contagem(String palavra) {
        int quant = 0;
        for (int i = 0; i < palavra.length(); i++) {
            if (Character.isUpperCase(palavra.charAt(i))) {
                quant++;
            }
        }
        return quant;
    }

    // Método para verificar se a palavra é "FIM"
    public static boolean Fim (String palavra){

        if(palavra.length() == 3 && palavra.charAt(0) == 'F' && palavra.charAt(1) == 'I' && palavra.charAt(2) == 'M'){
            return true;
        }else{
            return false;
        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String palavra;

        // Leitura 
        while (true) {
            palavra = scanner.nextLine();
            if (Fim(palavra)) {
                break;
            }
            int contagem = Contagem(palavra);
            System.out.println(contagem);
        }
        scanner.close();
    }
}
