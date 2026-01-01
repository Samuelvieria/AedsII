import java.io.File;
import java.util.Scanner;

class Games {
    public static final String FILE_PATH = "/tmp/games.csv";

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

    // -------------------- Classe interna Data --------------------
    public static class Data {
        private int dia, mes, ano;

        public Data(int dia, int mes, int ano) {
            this.dia = dia;
            this.mes = mes;
            this.ano = ano;
        }

        public int getDia() { return dia; }
        public int getMes() { return mes; }
        public int getAno() { return ano; }

        @Override
        public String toString() {
            return String.format("%02d/%02d/%04d", dia, mes, ano);
        }
    }

    // -------------------- Construtor --------------------
    public Games(int id, String nome, Data data, int jogadores, float preco,
                 String[] linguas, int nota, float userScore, int conquistas,
                 String[] publishers, String[] developers, String[] categoria,
                 String[] genero, String[] tags) {
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

    // -------------------- Getters e Setters --------------------
    public int getId() { return id; }
    public String getNome() { return nome; }
    public Data getData() { return data; }
    public int getJogadores() { return jogadores; }
    public float getPreco() { return preco; }
    public String[] getLinguas() { return linguas; }
    public int getNota() { return nota; }
    public float getUserScore() { return userScore; }
    public int getConquistas() { return conquistas; }
    public String[] getPublishers() { return publishers; }
    public String[] getDevelopers() { return developers; }
    public String[] getCategoria() { return categoria; }
    public String[] getGenero() { return genero; }
    public String[] getTags() { return tags; }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setData(Data data) { this.data = data; }
    public void setJogadores(int jogadores) { this.jogadores = jogadores; }
    public void setPreco(float preco) { this.preco = preco; }
    public void setLinguas(String[] linguas) { this.linguas = linguas; }
    public void setNota(int nota) { this.nota = nota; }
    public void setUserScore(float userScore) { this.userScore = userScore; }
    public void setConquistas(int conquistas) { this.conquistas = conquistas; }
    public void setPublishers(String[] publishers) { this.publishers = publishers; }
    public void setDevelopers(String[] developers) { this.developers = developers; }
    public void setCategoria(String[] categoria) { this.categoria = categoria; }
    public void setGenero(String[] genero) { this.genero = genero; }
    public void setTags(String[] tags) { this.tags = tags; }

    // -------------------- Leitura e parsing do CSV --------------------
    public static String[] LeituraCsv(File arquivo) {
        String[] linhas = new String[10000];
        int i = 0;
        try (Scanner scanner = new Scanner(arquivo, "UTF-8")) {
            scanner.nextLine(); // pular cabeçalho
            while (scanner.hasNextLine()) {
                linhas[i++] = scanner.nextLine();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        String[] certo = new String[i];
        System.arraycopy(linhas, 0, certo, 0, i);
        return certo;
    }

    public static Games SetgameLinha(String linha) {
        int tam = linha.length();
        String[] Campos = new String[14];
        int campoAtual = 0;
        String campo = "";
        boolean dentroAspas = false;

        for (int i = 0; i < tam; i++) {
            char c = linha.charAt(i);
            if (c == '"') dentroAspas = !dentroAspas;
            else if (c == ',' && !dentroAspas) {
                Campos[campoAtual++] = campo.trim();
                campo = "";
            } else campo += c;
        }
        Campos[campoAtual] = campo.trim();

        Games g = new Games(0, "", new Data(1, 1, 1900), 0, 0.0f, new String[0],
                -1, -1.0f, 0, new String[0], new String[0], new String[0],
                new String[0], new String[0]);

        if (!Campos[0].isEmpty()) g.setId(Integer.parseInt(Campos[0]));
        g.setNome(Campos[1]);

        // Data
        Data dataObj = new Data(1, 1, 1900);
        if (!Campos[2].isEmpty()) {
            String str = Campos[2];
            String[] meses = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
            int dia = 1, mes = 1, ano = 1900;
            if (str.contains(",")) {
                String[] partes = str.split(",");
                ano = Integer.parseInt(partes[1].trim());
                String[] sub = partes[0].trim().split(" ");
                if (sub.length == 2) {
                    String mesStr = sub[0];
                    for (int i = 0; i < 12; i++) if (meses[i].equalsIgnoreCase(mesStr)) mes = i+1;
                    dia = Integer.parseInt(sub[1]);
                }
            } else if (str.contains(" ")) {
                String[] partes = str.split(" ");
                String mesStr = partes[0];
                for (int i = 0; i < 12; i++) if (meses[i].equalsIgnoreCase(mesStr)) mes = i+1;
                ano = Integer.parseInt(partes[1]);
            } else ano = Integer.parseInt(str);
            dataObj = new Data(dia, mes, ano);
        }
        g.setData(dataObj);

        if (!Campos[3].isEmpty()) {
            String numeros = Campos[3].replaceAll("[^0-9]", "");
            g.setJogadores(numeros.isEmpty() ? 0 : Integer.parseInt(numeros));
        }

        if (Campos[4].equalsIgnoreCase("Free to Play")) g.setPreco(0);
        else if (!Campos[4].isEmpty()) g.setPreco(Float.parseFloat(Campos[4]));

        if (!Campos[5].isEmpty()) g.setLinguas(Campos[5].replaceAll("[\\[\\]\"]","").split(",\\s*"));
        if (!Campos[6].isEmpty()) g.setNota(Integer.parseInt(Campos[6]));
        if (!Campos[7].isEmpty() && !Campos[7].equalsIgnoreCase("tbd")) g.setUserScore(Float.parseFloat(Campos[7]));
        if (!Campos[8].isEmpty()) g.setConquistas(Integer.parseInt(Campos[8]));
        if (!Campos[9].isEmpty()) g.setPublishers(Campos[9].replaceAll("[\\[\\]\"]","").split(",\\s*"));
        if (!Campos[10].isEmpty()) g.setDevelopers(Campos[10].replaceAll("[\\[\\]\"]","").split(",\\s*"));
        if (!Campos[11].isEmpty()) g.setCategoria(Campos[11].replaceAll("[\\[\\]\"]","").split(",\\s*"));
        if (!Campos[12].isEmpty()) g.setGenero(Campos[12].replaceAll("[\\[\\]\"]","").split(",\\s*"));
        if (!Campos[13].isEmpty()) g.setTags(Campos[13].replaceAll("[\\[\\]\"]","").split(",\\s*"));
        return g;
    }

    public static Games[] SetCatalogo(String[] linhas) {
        Games[] catalogo = new Games[linhas.length];
        for (int i = 0; i < linhas.length; i++) {
            catalogo[i] = SetgameLinha(linhas[i]);
        }
        return catalogo;
    }

    public static Games PesquisaSeqMod(Games[] catalogo, int x) {
        for (Games g : catalogo)
            if (g.getId() == x) return g;
        return null;
    }

    public static String arrayParaString(String[] arr) {
        if (arr == null || arr.length == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i].trim());
            if (i != arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public static void Printar(Games jogo) {
        System.out.println(
            "=> " + jogo.getId() + " ## " + jogo.getNome() + " ## " + jogo.getData().toString() +
            " ## " + jogo.getJogadores() + " ## " + jogo.getPreco() + " ## " + arrayParaString(jogo.getLinguas()) +
            " ## " + jogo.getNota() + " ## " + jogo.getUserScore() + " ## " + jogo.getConquistas() +
            " ## " + arrayParaString(jogo.getPublishers()) + " ## " + arrayParaString(jogo.getDevelopers()) +
            " ## " + arrayParaString(jogo.getCategoria()) + " ## " + arrayParaString(jogo.getGenero()) +
            " ## " + arrayParaString(jogo.getTags()) + " ##"
        );
    }
}

// --------------------------------------------------------------------
// -------------------- LISTA FLEXÍVEL (Questão 1) --------------------
// --------------------------------------------------------------------

public class ListaFlexivel {
    private static class Celula {
        Games elemento;
        Celula prox;
        Celula(Games elemento) { this.elemento = elemento; }
    }

    private Celula primeiro, ultimo;

    public ListaFlexivel() {
        primeiro = new Celula(null);
        ultimo = primeiro;
    }

    public void inserirInicio(Games game) {
        Celula tmp = new Celula(game);
        tmp.prox = primeiro.prox;
        primeiro.prox = tmp;
        if (ultimo == primeiro) ultimo = tmp;
    }

    public void inserirFim(Games game) {
        ultimo.prox = new Celula(game);
        ultimo = ultimo.prox;
    }

    public void inserir(Games game, int pos) {
        int tam = tamanho();
        if (pos < 0 || pos > tam) { System.out.println("Erro"); return; }
        if (pos == 0) inserirInicio(game);
        else if (pos == tam) inserirFim(game);
        else {
            Celula i = primeiro;
            for (int j = 0; j < pos; j++, i = i.prox);
            Celula tmp = new Celula(game);
            tmp.prox = i.prox;
            i.prox = tmp;
        }
    }

    public Games removerInicio() {
        if (primeiro == ultimo) { System.out.println("Erro"); return null; }
        Celula tmp = primeiro.prox;
        primeiro.prox = tmp.prox;
        if (tmp == ultimo) ultimo = primeiro;
        System.out.println("(R) " + tmp.elemento.getNome());
        return tmp.elemento;
    }

    public Games removerFim() {
        if (primeiro == ultimo) { System.out.println("Erro"); return null; }
        Celula i;
        for (i = primeiro; i.prox != ultimo; i = i.prox);
        Celula tmp = ultimo;
        ultimo = i;
        ultimo.prox = null;
        System.out.println("(R) " + tmp.elemento.getNome());
        return tmp.elemento;
    }

    public Games remover(int pos) {
        int tam = tamanho();
        if (pos < 0 || pos >= tam) { System.out.println("Erro"); return null; }
        if (pos == 0) return removerInicio();
        if (pos == tam - 1) return removerFim();
        Celula i = primeiro;
        for (int j = 0; j < pos; j++, i = i.prox);
        Celula tmp = i.prox;
        i.prox = tmp.prox;
        System.out.println("(R) " + tmp.elemento.getNome());
        return tmp.elemento;
    }

    public int tamanho() {
        int c = 0;
        for (Celula i = primeiro.prox; i != null; i = i.prox) c++;
        return c;
    }

    public void mostrar() {
        for (Celula i = primeiro.prox; i != null; i = i.prox)
            Games.Printar(i.elemento);
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {
        File arquivo = new File(Games.FILE_PATH);
        String[] linhas = Games.LeituraCsv(arquivo);
        Games[] catalogo = Games.SetCatalogo(linhas);
        ListaFlexivel lista = new ListaFlexivel();

        Scanner sc = new Scanner(System.in);
        String entrada;

        // IDs até FIM
        while (!(entrada = sc.nextLine().trim()).equals("FIM")) {
            int id = Integer.parseInt(entrada);
            Games g = Games.PesquisaSeqMod(catalogo, id);
            if (g != null) lista.inserirFim(g);
        }

        int n = Integer.parseInt(sc.nextLine().trim());
        for (int i = 0; i < n; i++) {
            String linha = sc.nextLine().trim();
            String[] partes = linha.split(" ");
            String cmd = partes[0];
            switch (cmd) {
                case "II":
                    lista.inserirInicio(Games.PesquisaSeqMod(catalogo, Integer.parseInt(partes[1])));
                    break;
                case "IF":
                    lista.inserirFim(Games.PesquisaSeqMod(catalogo, Integer.parseInt(partes[1])));
                    break;
                case "I*":
                    lista.inserir(Games.PesquisaSeqMod(catalogo, Integer.parseInt(partes[2])),
                                  Integer.parseInt(partes[1]));
                    break;
                case "RI":
                    lista.removerInicio();
                    break;
                case "RF":
                    lista.removerFim();
                    break;
                case "R*":
                    lista.remover(Integer.parseInt(partes[1]));
                    break;
            }
        }

        lista.mostrar();
        sc.close();
    }
}
