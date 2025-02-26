

import java.io.*;
import java.util.Scanner;

public class TP01_14 {

    public static void LerArq() throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Caminho do arquivo
        String caminho = "numeros.txt";
        File arquivo = new File(caminho);

        // Cria o arquivo se não existir
        if (arquivo.createNewFile()) {
           
        } else {
            System.out.println("Arquivo já existe.");
        }

        // Pede o número de linhas que o usuário deseja escrever
       
        int n = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha após nextInt()

        // Abre o arquivo para escrita no modo de adição (append)
        FileWriter fw = new FileWriter(arquivo, true);

        // Captura e escreve as linhas no arquivo
        for (int i = 0; i < n; i++) {
            String linha = scanner.nextLine();
            fw.write(linha + "\n");
        }

        // Fecha o FileWriter
        fw.close();

        // Reabrindo para leitura reversa com RandomAccessFile
        RandomAccessFile raf = new RandomAccessFile(arquivo, "r");
        long tamanhoArq = raf.length();

        for (long i = tamanhoArq - 1; i >= 0; i--) {
            raf.seek(i);
            char caractere = (char) raf.read();
            System.out.print(caractere);
        }

        // Fecha recursos
        raf.close();
        scanner.close();
    }

    public static void main(String[] args) throws Exception {
        LerArq();
    }
}


/* // Cria o arquivo caso não exista
        if (arquivo.createNewFile()) {
            System.out.println("Arquivo criado com sucesso.");
            
            // Escrevendo conteúdo inicial para evitar erro de leitura
            FileWriter fw = new FileWriter(arquivo);
            fw.write("Digite algo para continuar...\n");
            fw.close();
        } else {
            System.out.println("Arquivo já existe.");
        } */