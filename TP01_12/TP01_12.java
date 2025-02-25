package TP01_12;

import java.util.Scanner;

//Uma senha é considerada válida se contém pelo menos 8 caracteres, incluindo pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especia

public class TP01_12 {

    public static boolean EhsenhaRec(Char[] senha ,int i){
        
        if(senha[i]> 'a' && senha[i]< 'z' || senha[i]> '0' && senha[i]< '9' || senha[i]> '!' && senha[i]< '~' || senha[i]> 'A'&& senha ){

        }

    }

    public static void senha(String senha) {
      
        char[] senhaChar = senha.toCharArray();
        boolean ehsenha = false;
        int i = 0;

        EhsenhaRec(senhaChar,i);

      
        
        
       

      }


        
  } 
}

    

    public static void main(String[] args) {
        Scanner palavra = new Scanner(System.in);

        while (true) {
            String palavra2 = palavra.nextLine();
            if (palavra2.equals("FIM")) {
                palavra.close();
                return;
            }


            senha(palavra2);
        }
        
    }
    
}


/*public class VerificarCaracteres {
    public static void main(String[] args) {
        String texto = "Exemplo123!";

        boolean temNumero = false;
        boolean temMaiuscula = false;
        boolean temEspecial = false;

        // Percorre cada caractere da string
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);

            if (Character.isDigit(c)) {
                temNumero = true;
            } else if (Character.isUpperCase(c)) {
                temMaiuscula = true;
            } else if (!Character.isLetterOrDigit(c)) {
                temEspecial = true;
            }

            // Se todos os tipos já foram encontrados, podemos parar
            if (temNumero && temMaiuscula && temEspecial) {
                break;
            }
        }

        // Verificando resultados
        if (temNumero) {
            System.out.println("A string contém um número.");
        } else {
            System.out.println("A string NÃO contém um número.");
        }

        if (temMaiuscula) {
            System.out.println("A string contém uma letra maiúscula.");
        } else {
            System.out.println("A string NÃO contém uma letra maiúscula.");
        }

        if (temEspecial) {
            System.out.println("A string contém um caractere especial.");
        } else {
            System.out.println("A string NÃO contém um caractere especial.");
        }
    }
}
 */