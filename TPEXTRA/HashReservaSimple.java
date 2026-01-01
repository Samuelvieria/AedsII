import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

// ------------------------------------------------------------
// ---------------------- CLASSE GAMES -------------------------
// ------------------------------------------------------------
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

    static class Data {
        private int dia, mes, ano;
        public Data(int dia, int mes, int ano) {
            this.dia = dia; this.mes = mes; this.ano = ano;
        }
        public String toString() { return String.format("%02d/%02d/%04d", dia, mes, ano); }
    }

    public Games(int id, String nome, Data data, int jogadores, float preco, String[] linguas,
                 int nota, float userScore, int conquistas, String[] publishers,
                 String[] developers, String[] categoria, String[] genero, String[] tags) {
        this.id = id; this.nome = nome; this.data = data; this.jogadores = jogadores;
        this.preco = preco; this.linguas = linguas; this.nota = nota; this.userScore = userScore;
        this.conquistas = conquistas; this.publishers = publishers; this.developers = developers;
        this.categoria = categoria; this.genero = genero; this.tags = tags;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }

    public static String[] LeituraCsv(File arquivo) {
        String[] linhas = new String[15000];
        int i = 0;
        try (Scanner sc = new Scanner(arquivo, "UTF-8")) {
            sc.nextLine();
            while (sc.hasNextLine()) linhas[i++] = sc.nextLine();
        } catch (Exception e) {}
        String[] certo = new String[i];
        System.arraycopy(linhas, 0, certo, 0, i);
        return certo;
    }

    public static Games SetgameLinha(String linha) {
        String[] Campos = new String[14];
        int campoAtual = 0; boolean dentroAspas = false;
        String campo = "";

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '"') dentroAspas = !dentroAspas;
            else if (c == ',' && !dentroAspas) {
                Campos[campoAtual++] = campo.trim(); campo = "";
            } else campo += c;
        }
        Campos[campoAtual] = campo.trim();

        Games g = new Games(0, "", new Data(1, 1, 1900), 0, 0.0f, new String[0],
                -1, -1.0f, 0, new String[0], new String[0],
                new String[0], new String[0], new String[0]);

        if (!Campos[0].isEmpty()) g.id = Integer.parseInt(Campos[0]);
        g.nome = Campos[1];

        return g;
    }

    public static Games[] SetCatalogo(String[] linhas) {
        Games[] catalogo = new Games[linhas.length];
        for (int i = 0; i < linhas.length; i++) catalogo[i] = SetgameLinha(linhas[i]);
        return catalogo;
    }

    public static Games PesquisaSeqMod(Games[] catalogo, int x) {
        for (Games g : catalogo) if (g.id == x) return g;
        return null;
    }
}

class TabelaHash {
    String[] tabela;
    int tamHash = 21;
    int tamTotal = 30;
    int proxReserva = 21;

    public TabelaHash() {
        tabela = new String[tamTotal];
    }

    public int hash(String s) {
        if (s == null) return 0;
        s = s.trim();
        int soma = 0;
        int i = 0;
        while (i < s.length()) {
            soma += (int) s.charAt(i);
            i++;
        }
        return soma % tamHash;
    }

    public void inserir(String nome) {
        int pos = hash(nome);

        if (tabela[pos] == null) {
            tabela[pos] = nome;
        } else {
            if (proxReserva < tamTotal) {
                tabela[proxReserva] = nome;
                proxReserva++;
            }
        }
    }

    public int pesquisar(String nome) {
        int pos = hash(nome);

        if (tabela[pos] != null && tabela[pos].equals(nome)) {
            return pos;
        }

        int i = 21;
        while (i < tamTotal) {
            if (tabela[i] != null && tabela[i].equals(nome)) {
                return i;
            }
            i++;
        }

        return -1;
    }
}


public class HashReservaSimple {

    public static void main(String[] args) throws Exception {

        File arquivo = new File(Games.FILE_PATH);
        String[] linhas = Games.LeituraCsv(arquivo);
        Games[] catalogo = Games.SetCatalogo(linhas);

        Scanner sc = new Scanner(System.in);
        TabelaHash hash = new TabelaHash();

        String entrada = sc.nextLine().trim();
        while (!entrada.equals("FIM")) {
            int id = Integer.parseInt(entrada);
            Games g = Games.PesquisaSeqMod(catalogo, id);
            if (g != null) hash.inserir(g.getNome());
            entrada = sc.nextLine().trim();
        }

        PrintWriter pw = new PrintWriter("800772_hashReserva.txt");

        String nome = sc.nextLine().trim();
        while (!nome.equals("FIM")) {
            int pos = hash.pesquisar(nome);

            if (pos == -1) {
                int theoretical = hash.hash(nome); 
                System.out.println(nome + ":  (Posicao: " + theoretical + ") NAO");
                pw.println(nome + ":  (Posicao: " + theoretical + ") NAO");
            } else {
                System.out.println(nome + ":  (Posicao: " + pos + ") SIM");
                pw.println(nome + ":  (Posicao: " + pos + ") SIM");
            }

            nome = sc.nextLine().trim();
        }

        pw.close();
        sc.close();
    }
}
