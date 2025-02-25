package TP01_14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;



public class TP01_14 {

    public static void LerArq()throws Exception{

        Scanner scanner = new Scanner(System.in);

        File arquivo = new File("/Users/samuelalves/AedsII/TP01_14/arq.txt");
        
        FileReader fr = new FileReader("arq.txt");
        BufferedReader  br = new BufferedReader(fr);

        String linha;


        while((linha = br.readLine()) !=null){
            System.out.println(linha);
        }



    }

    public static void main(String[] args)throws Exception {

        LerArq();
        
    }
    
}
