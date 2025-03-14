import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class TP01_13 {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                String nomePagina = reader.readLine();
                if (nomePagina.equals("FIM")) {
                    break;
                }
                String enderecoPagina = reader.readLine();
                String html = fetchHTML(enderecoPagina);
                int[] contagem = contarCaracteres(html);
                exibirResultado(contagem, nomePagina);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String fetchHTML(String enderecoPagina) throws IOException {
        StringBuilder html = new StringBuilder();
        URL url = new URL(enderecoPagina);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }
        }
        return html.toString();
    }

    private static int[] contarCaracteres(String html) {
        // Array com 22 vogais + 1 consoante + 1 <br> + 1 <table> = 24
        int[] contagem = new int[24]; 
        String vogais = "aeiouáéíóúàèìòùãõâêîôû";
        String consoantes = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ";

        for (char c : html.toCharArray()) {
            // Verifica se é uma vogal
            int indexVogal = vogais.indexOf(c);
            if (indexVogal != -1) {
                contagem[indexVogal]++; // Conta vogais
            } else if (consoantes.indexOf(c) != -1) {
                contagem[22]++; // Conta consoantes
            }
        }

        // Conta as ocorrências de <br> e <table>
        contagem[23] = contarOcorrencias(html, "<br>");
        contagem[24] = contarOcorrencias(html, "<table>");

        return contagem;
    }

    private static int contarOcorrencias(String html, String padrao) {
        int count = 0;
        int index = html.indexOf(padrao);
        while (index != -1) {
            count++;
            index = html.indexOf(padrao, index + padrao.length());
        }
        return count;
    }

    private static void exibirResultado(int[] contagem, String nomePagina) {
        String[] vogais = {"a", "e", "i", "o", "u", "á", "é", "í", "ó", "ú", "à", "è", "ì", "ò", "ù", "ã", "õ", "â", "ê", "î", "ô", "û"};
        for (int i = 0; i < vogais.length; i++) {
            System.out.print(vogais[i] + "(" + contagem[i] + ") ");
        }
        System.out.print("consoante(" + contagem[22] + ") ");
        System.out.print("<br>(" + contagem[23] + ") ");
        System.out.print("<table>(" + contagem[24] + ") ");
        System.out.println(nomePagina);
    }
}
