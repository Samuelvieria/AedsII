/*

 

Com String builder
public static Games SetgameLinha(String linha) {
    int tam = linha.length();
    int campoAtual = 0;
    String[] Campos = new String[14];
    StringBuilder sb = new StringBuilder();
    boolean dentroAspas = false;

    for (int i = 0; i < tam; i++) {
        char c = linha.charAt(i);
        if (c == '"') {
            dentroAspas = !dentroAspas;
        } else if (c == ',' && !dentroAspas) {
            Campos[campoAtual] = sb.toString().trim();// nao pode usar StringBuilder ):
            campoAtual++;
            sb.setLength(0);
        } else {
            sb.append(c);
        }
    }
    Campos[campoAtual] = sb.toString().trim();

    Games g = new Games(0, "", "", 0, 0.0f, new String[0], -1, -1.0f, 0,
            new String[0], new String[0], new String[0], new String[0], new String[0]);

    // settando
    if (!Campos[0].isEmpty())
        g.setId(Integer.parseInt(Campos[0]));

    g.setNome(Campos[1]);

    String data = Campos[2];
    if (!data.isEmpty()) {
        String[] partes = data.split("/");
        if (partes.length == 1)
            data = "01/01/" + partes[0];
        else if (partes.length == 2)
            data = "01/" + partes[0] + "/" + partes[1];
    }
    g.setData(data);

    if (!Campos[3].isEmpty()) {
        String numeros = Campos[3].replaceAll("[^0-9]", "");
        if (!numeros.isEmpty())
            g.setJogadores(Integer.parseInt(numeros));
    }

    if (Campos[4].equalsIgnoreCase("Free to Play"))
        g.setPreco(0.0f);
    else if (!Campos[4].isEmpty())
        g.setPreco(Float.parseFloat(Campos[4]));

    if (!Campos[5].isEmpty()) {
        String linguasStr = Campos[5].replaceAll("[\\[\\]]", "");
        g.setLinguas(linguasStr.split(","));
    }
    if (!Campos[6].isEmpty())
        g.setNota(Integer.parseInt(Campos[6]));
    else
        g.setNota(-1);

    if (!Campos[7].isEmpty() && !Campos[7].equalsIgnoreCase("tbd"))
        g.setUserScore(Float.parseFloat(Campos[7]));
    else
        g.setUserScore(-1.0f);

    if (!Campos[8].isEmpty())
        g.setConquistas(Integer.parseInt(Campos[8]));

    if (!Campos[9].isEmpty())
        g.setPublishers(Campos[9].split(","));

    if (!Campos[10].isEmpty())
        g.setDevelopers(Campos[10].split(","));

    if (!Campos[11].isEmpty())
        g.setCategoria(Campos[11].replaceAll("[\\[\\]]", "").split(","));

    if (!Campos[12].isEmpty())
        g.setGenero(Campos[12].replaceAll("[\\[\\]]", "").split(","));

    if (!Campos[13].isEmpty())
        g.setTags(Campos[13].replaceAll("[\\[\\]]", "").split(","));

    return g;
}
*/

/* com Srting builder 
 *  public static String arrayParaString(String[] arr) {
    if (arr == null || arr.length == 0)
        return "[]";
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < arr.length; i++) {
        sb.append(arr[i].trim());
        if (i != arr.length - 1)
            sb.append(", ");
    }
    sb.append("]");
    return sb.toString();
}
 */

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Games {
    public static final String FILE_PATH = "/tmp/games.csv";
    private static int comparacoes = 0; // Contador de comparações
    private static int movimentacoes = 0; // Contador de movimentações

    private int id;
    private String nome;
    private Data data;
    private int jogadores;
    private float preco;
    private String[] linguas;
    private int nota;
    private float userScore;
    private int conquistas;
    private String[] publishers;
    private String[] developers;
    private String[] categoria;
    private String[] genero;
    private String[] tags;

    // Classe interna Data
    public static class Data {
        private int dia;
        private int mes;
        private int ano;

        public Data(int dia, int mes, int ano) {
            this.dia = dia;
            this.mes = mes;
            this.ano = ano;
        }

        public int getDia() {
            return dia;
        }

        public int getMes() {
            return mes;
        }

        public int getAno() {
            return ano;
        }

        @Override
        public String toString() {
            return String.format("%02d/%02d/%04d", dia, mes, ano);
        }
    }

    public Games(int id, String nome, Data data, int jogadores, float preco, String[] linguas, int nota,
            float userScore, int conquistas, String[] publishers, String[] developers,
            String[] categoria, String[] genero, String[] tags) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.jogadores = jogadores;
        this.preco = preco;
        this.linguas = linguas;
        this.nota = nota;
        this.userScore = userScore;
        this.conquistas = conquistas;
        this.publishers = publishers;
        this.developers = developers;
        this.categoria = categoria;
        this.genero = genero;
        this.tags = tags;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Data getData() {
        return data;
    }

    public int getJogadores() {
        return jogadores;
    }

    public float getPreco() {
        return preco;
    }

    public String[] getLinguas() {
        return linguas;
    }

    public int getNota() {
        return nota;
    }

    public float getUserScore() {
        return userScore;
    }

    public int getConquistas() {
        return conquistas;
    }

    public String[] getPublishers() {
        return publishers;
    }

    public String[] getDevelopers() {
        return developers;
    }

    public String[] getCategoria() {
        return categoria;
    }

    public String[] getGenero() {
        return genero;
    }

    public String[] getTags() {
        return tags;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setJogadores(int jogadores) {
        this.jogadores = jogadores;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public void setLinguas(String[] linguas) {
        this.linguas = linguas;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public void setUserScore(float userScore) {
        this.userScore = userScore;
    }

    public void setConquistas(int conquistas) {
        this.conquistas = conquistas;
    }

    public void setPublishers(String[] publishers) {
        this.publishers = publishers;
    }

    public void setDevelopers(String[] developers) {
        this.developers = developers;
    }

    public void setCategoria(String[] categoria) {
        this.categoria = categoria;
    }

    public void setGenero(String[] genero) {
        this.genero = genero;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    // Leitura do CSV
    public static String[] LeituraCsv(File arquivo) {
        String[] linhas = new String[10000];
        int i = 0;
        try (Scanner scanner = new Scanner(arquivo, "UTF-8")) {
            scanner.nextLine(); // pular cabeçalho
            while (scanner.hasNextLine()) {
                linhas[i] = scanner.nextLine();
                i++;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        String[] certo = new String[i];
        for (int j = 0; j < i; j++) {
            certo[j] = linhas[j];
        }
        return certo;
    }

    // Método SetgameLinha (sem StringBuilder)
    public static Games SetgameLinha(String linha) {
        int tam = linha.length();
        int campoAtual = 0;
        String[] Campos = new String[14];
        String campoTemp = "";
        boolean dentroAspas = false;

        for (int i = 0; i < tam; i++) {
            char c = linha.charAt(i);
            if (c == '"') {
                dentroAspas = !dentroAspas;
            } else if (c == ',' && !dentroAspas) {
                Campos[campoAtual] = campoTemp.trim();
                campoAtual++;
                campoTemp = "";
            } else {
                campoTemp += c;
            }
        }
        Campos[campoAtual] = campoTemp.trim();

        Games g = new Games(0, "", new Data(1, 1, 1900), 0, 0.0f, new String[0], -1, -1.0f, 0,
                new String[0], new String[0], new String[0], new String[0], new String[0]);


                //settando, tive mudar alguma formatacoes
        if (!Campos[0].isEmpty())
            g.setId(Integer.parseInt(Campos[0]));

        g.setNome(Campos[1]);

        String dataStr = Campos[2];
        Data dataObj = new Data(1, 1, 1900);

        if (!dataStr.isEmpty()) {
            String[] meses = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

            int dia = 1;
            int mes = 1;
            int ano = 1900;

            dataStr = dataStr.trim();

            if (dataStr.contains(",")) {
                String[] partes = dataStr.split(",");
                String antesVirgula = partes[0].trim();
                ano = Integer.parseInt(partes[1].trim());
                String[] sub = antesVirgula.split(" ");
                if (sub.length == 2) {
                    String mesStr = sub[0];
                    for (int i = 0; i < meses.length; i++) {
                        if (meses[i].equalsIgnoreCase(mesStr)) {
                            mes = i + 1;
                            break;
                        }
                    }
                    dia = Integer.parseInt(sub[1]);
                }
            } else if (dataStr.contains(" ")) {
                String[] partes = dataStr.split(" ");
                String mesStr = partes[0];
                for (int i = 0; i < meses.length; i++) {
                    if (meses[i].equalsIgnoreCase(mesStr)) {
                        mes = i + 1;
                        break;
                    }
                }
                ano = Integer.parseInt(partes[1]);
            } else {
                ano = Integer.parseInt(dataStr);
            }

            dataObj = new Data(dia, mes, ano);
        }

        g.setData(dataObj);
        
        //N estava funcionando pq causa disso 
        if (!Campos[3].isEmpty()) {
            try {
                String[] partes = Campos[3].split(" - ");
                if (partes.length == 2) {
                    // Remover vírgulas e outros caracteres não numéricos
                    String lowStr = partes[0].replaceAll("[^0-9]", "");
                    String highStr = partes[1].replaceAll("[^0-9]", "");
                    int low = Integer.parseInt(lowStr);
                    int high = Integer.parseInt(highStr);
                    g.setJogadores((low + high) / 2);
                } else {
                    // Caso não seja um intervalo, tentar converter diretamente
                    String numStr = Campos[3].replaceAll("[^0-9]", "");
                    if (!numStr.isEmpty()) {
                        g.setJogadores(Integer.parseInt(numStr));
                    } else {
                        g.setJogadores(0);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro ao processar Estimated_owners: " + Campos[3]);
                g.setJogadores(0);
            }
        } else {
            g.setJogadores(0);
        }

        if (Campos[4].equalsIgnoreCase("Free to Play"))
            g.setPreco(0.0f);
        else if (!Campos[4].isEmpty())
            g.setPreco(Float.parseFloat(Campos[4]));

        if (!Campos[5].isEmpty()) {
            String linguasStr = Campos[5].replaceAll("[\\[\\]]", "").replaceAll("[\"']", "");
            g.setLinguas(linguasStr.split(",\\s*"));
        }
        if (!Campos[6].isEmpty())
            g.setNota(Integer.parseInt(Campos[6]));
        else
            g.setNota(-1);

        if (!Campos[7].isEmpty() && !Campos[7].equalsIgnoreCase("tbd"))
            g.setUserScore(Float.parseFloat(Campos[7]));
        else
            g.setUserScore(-1.0f);

        if (!Campos[8].isEmpty())
            g.setConquistas(Integer.parseInt(Campos[8]));

        if (!Campos[9].isEmpty())
            g.setPublishers(Campos[9].replaceAll("[\\[\\]]", "").replaceAll("[\"']", "").split(",\\s*"));

        if (!Campos[10].isEmpty())
            g.setDevelopers(Campos[10].replaceAll("[\\[\\]]", "").replaceAll("[\"']", "").split(",\\s*"));

        if (!Campos[11].isEmpty())
            g.setCategoria(Campos[11].replaceAll("[\\[\\]]", "").replaceAll("[\"']", "").split(",\\s*"));

        if (!Campos[12].isEmpty())
            g.setGenero(Campos[12].replaceAll("[\\[\\]]", "").replaceAll("[\"']", "").split(",\\s*"));

        if (!Campos[13].isEmpty())
            g.setTags(Campos[13].replaceAll("[\\[\\]]", "").replaceAll("[\"']", "").split(",\\s*"));

        return g;
    }

    // Constrói o catálogo
    public static Games[] SetCatalogo(String[] linhas) {
        int tam = linhas.length;
        Games[] catalogoTemp = new Games[tam];
        for (int i = 0; i < tam; i++) {
            catalogoTemp[i] = SetgameLinha(linhas[i]);
        }
        return catalogoTemp;
    }

    //comStringBuilder
    /*
     *  public static String arrayParaString(String[] arr) {
        if (arr == null || arr.length == 0)
            return "[]";
        StringBuilder resultado = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            // Remove aspas simples ou duplas, se presentes, e faz trim
            String elemento = arr[i].replaceAll("[\"']", "").trim();
            resultado.append(elemento);
            if (i != arr.length - 1)
                resultado.append(", ");
        }
        resultado.append("]");
        return resultado.toString();
    }
     */
    public static String arrayParaString(String[] arr) {
        if (arr == null || arr.length == 0)
            return "[]";
        
        String resultado = "[";
        for (int i = 0; i < arr.length; i++) {
            // Remove aspas simples ou duplas, se presentes, e faz trim
            String elemento = arr[i].replaceAll("[\"']", "").trim();
            resultado += elemento;
            if (i != arr.length - 1)
                resultado += ", ";
        }
        resultado += "]";
        return resultado;
    }
   

    public static void Printar(Games jogo) {
        System.out.println(
                "=> " + jogo.getId() +
                        " ## " + jogo.getNome() +
                        " ## " + jogo.getData().toString() +
                        " ## " + jogo.getJogadores() +
                        " ## " + jogo.getPreco() +
                        " ## " + arrayParaString(jogo.getLinguas()) +
                        " ## " + jogo.getNota() +
                        " ## " + jogo.getUserScore() +
                        " ## " + jogo.getConquistas() +
                        " ## " + arrayParaString(jogo.getPublishers()) +
                        " ## " + arrayParaString(jogo.getDevelopers()) +
                        " ## " + arrayParaString(jogo.getCategoria()) +
                        " ## " + arrayParaString(jogo.getGenero()) +
                        " ## " + arrayParaString(jogo.getTags()) + " ##");
    }

    public static void Swap(int i, int j, Games[] temp) {

        Games tmp = temp[i];
        temp[i] = temp[j];
        temp[j] = tmp;
        movimentacoes += 3;
    }

    public static Games PesquisaSeqMod(Games[] catalogo, int x) {

        for (int i = 0; i < catalogo.length; i++) {
            if (catalogo[i].getId() == x) {
                return catalogo[i];
            }

        }

        return null;
    }

    public static void PesquisaSeq(Games[] catalogo) {
        Scanner scanner = new Scanner(System.in);
        String entrada = scanner.nextLine();

        while (!entrada.equals("FIM")) {
            try {
                int pesq = Integer.parseInt(entrada);
                boolean encontrado = false;

                for (int i = 0; i < catalogo.length; i++) {
                    if (catalogo[i].getId() == pesq) {
                        Printar(catalogo[i]);
                        encontrado = true;
                    }
                }

                if (!encontrado) {
                    System.out.println("Jogo não encontrado!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida!");
            }

            entrada = scanner.nextLine();
        }
    }

    public static Games[] ArrayTemp(Games[] catalogo) {

        Scanner scanner = new Scanner(System.in);
        Games[] temp = new Games[200];
        String id = scanner.nextLine();
        int i = 0;

        while (!id.equals("FIM")) {

            int intid = Integer.parseInt(id);
            temp[i] = PesquisaSeqMod(catalogo, intid);
            i++;

            id = scanner.nextLine();
        }

        Games[] jogostmp = new Games[i];
        for (int k = 0; k < i; k++) {
            jogostmp[k] = temp[k];
        }

        return jogostmp;

    }

    // tp0501
    public static void PesqBinaria(Games[] catalogo) {
        Scanner scanner = new Scanner(System.in);

        Games[] ids = ArrayTemp(catalogo);

        String[] nomestmp = new String[100];
        String nome = scanner.nextLine();
        int j = 0;

        while (!nome.equals("FIM")) {

            nomestmp[j] = nome;
            j++;
            nome = scanner.nextLine();

        }

        String[] nomes = new String[j];

        for (int n = 0; n < j; n++) {
            nomes[n] = nomestmp[n];
        }

        Bolha(ids);

        comparacoes = 0;
        long startTime = System.nanoTime();

        for (int g = 0; g < nomes.length; g++) {
            Pesq(nomes[g], ids);

        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        String matricula = "800772";
        try (PrintWriter writer = new PrintWriter(matricula + "_binaria.txt")) {
            writer.printf("%s\t%.2f\t%d\n", matricula, executionTimeMs, comparacoes);
        } catch (Exception e) {
            System.out.println("Erro ao criar arquivo de log: " + e.getMessage());
        }
    }

    // De fato a pesquisa binaria
    public static void Pesq(String nome, Games[] Ids) {
        int esq = 0;
        int dir = Ids.length - 1;
        int meio;

        boolean resp = false;

        while (esq <= dir) {
            meio = (dir + esq) / 2;
            if (Ids[meio].getNome().equals(nome)) {
                resp = true;
                esq = dir + 1;
            }
            comparacoes++;
            if (nome.compareToIgnoreCase(Ids[meio].getNome()) > 0) {

                esq = meio + 1;

            } else {
                dir = meio - 1;
            }

        }

        if (resp == false) {
            System.out.println(" NAO");
        } else {
            System.out.println(" SIM");
        }

    }

    public static void Bolha(Games[] tmp) {

        Games temp;

        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp.length - 1 - i; j++) {
                if (tmp[j].getNome().compareToIgnoreCase(tmp[j + 1].getNome()) > 0) {
                    temp = tmp[j];
                    tmp[j] = tmp[j + 1];
                    tmp[j + 1] = temp;
                }

            }
        }

    }

    public static void heapSort(Games[] catalogo) {
        Games[] heap = ArrayTemp(catalogo); 
        int n = heap.length;

      
        comparacoes = 0;
        movimentacoes = 0;

        long startTime = System.nanoTime();

       
        for (int i = n / 2 - 1; i >= 0; i--) {
            ReconstruirHeap(i, n, heap);
        }

       
        for (int i = n - 1; i >= 0; i--) {
            Swap(0, i, heap); 
            ReconstruirHeap(0, i, heap); 
        }

       
        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        
        for (int k = 0; k < heap.length; k++) {
            Printar(heap[k]);
        }

        // Gerar arquivo de log
        String matricula = "800772";
        try (PrintWriter writer = new PrintWriter(matricula + "_heapsort.txt")) {
            writer.printf("%s\t%d\t%d\t%.2f\n", matricula, comparacoes, movimentacoes, executionTimeMs);
        } catch (Exception e) {
            System.out.println("Erro ao criar arquivo de log: " + e.getMessage());
        }
    }

    public static void ReconstruirHeap(int i, int tam, Games[] heap) {
        int maior = i; 
        int esquerdo = 2 * i + 1;
        int direito = 2 * i + 2; 

    
        if (esquerdo < tam) {
            comparacoes++;
            if (heap[esquerdo].getJogadores() > heap[maior].getJogadores() ||
                    (heap[esquerdo].getJogadores() == heap[maior].getJogadores() &&
                            heap[esquerdo].getId() > heap[maior].getId())) {
                maior = esquerdo;
            }
        }

     
        if (direito < tam) {
            comparacoes++;
            if (heap[direito].getJogadores() > heap[maior].getJogadores() ||
                    (heap[direito].getJogadores() == heap[maior].getJogadores() &&
                            heap[direito].getId() > heap[maior].getId())) {
                maior = direito;
            }
        }

       
        if (maior != i) {
            Swap(i, maior, heap);
            ReconstruirHeap(maior, tam, heap);
        }
    }

    public static void main(String[] args) {
        File arquivo = new File(FILE_PATH);
        String[] linhas = LeituraCsv(arquivo);
        Games[] catalogo = SetCatalogo(linhas);

        heapSort(catalogo);
    }
}