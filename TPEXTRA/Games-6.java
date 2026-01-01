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

    // ------------------ Classe interna Data ------------------
    static class Data {
        private int dia, mes, ano;
        public Data(int dia, int mes, int ano) {
            this.dia = dia; this.mes = mes; this.ano = ano;
        }
        public String toString() { return String.format("%02d/%02d/%04d", dia, mes, ano); }
    }

    // ------------------ Construtor ------------------
    public Games(int id, String nome, Data data, int jogadores, float preco, String[] linguas,
                 int nota, float userScore, int conquistas, String[] publishers,
                 String[] developers, String[] categoria, String[] genero, String[] tags) {
        this.id = id; this.nome = nome; this.data = data; this.jogadores = jogadores;
        this.preco = preco; this.linguas = linguas; this.nota = nota; this.userScore = userScore;
        this.conquistas = conquistas; this.publishers = publishers; this.developers = developers;
        this.categoria = categoria; this.genero = genero; this.tags = tags;
    }

    // ------------------ Getters ------------------
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

    // ------------------ Funções CSV ------------------
    public static String[] LeituraCsv(File arquivo) {
        String[] linhas = new String[15000];
        int i = 0;
        try (Scanner sc = new Scanner(arquivo, "UTF-8")) {
            sc.nextLine(); // pular cabeçalho
            while (sc.hasNextLine()) linhas[i++] = sc.nextLine();
        } catch (Exception e) { System.out.println("Erro: " + e.getMessage()); }
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

        // Data
        Data d = new Data(1, 1, 1900);
        if (!Campos[2].isEmpty()) {
            String s = Campos[2]; String[] meses = {"Jan","Feb","Mar","Apr","May","Jun",
                    "Jul","Aug","Sep","Oct","Nov","Dec"};
            int dia=1,mes=1,ano=1900;
            if (s.contains(",")) {
                String[] partes = s.split(",");
                ano = Integer.parseInt(partes[1].trim());
                String[] sub = partes[0].trim().split(" ");
                if (sub.length == 2) {
                    for (int i=0;i<12;i++) if (meses[i].equalsIgnoreCase(sub[0])) mes=i+1;
                    dia = Integer.parseInt(sub[1]);
                }
            } else if (s.contains(" ")) {
                String[] partes = s.split(" ");
                for (int i=0;i<12;i++) if (meses[i].equalsIgnoreCase(partes[0])) mes=i+1;
                ano = Integer.parseInt(partes[1]);
            } else ano = Integer.parseInt(s);
            d = new Data(dia,mes,ano);
        }
        g.data = d;

        if (!Campos[3].isEmpty()) {
            String num = Campos[3].replaceAll("[^0-9]", "");
            g.jogadores = num.isEmpty()?0:Integer.parseInt(num);
        }

        if (Campos[4].equalsIgnoreCase("Free to Play")) g.preco = 0;
        else if (!Campos[4].isEmpty()) g.preco = Float.parseFloat(Campos[4]);

        if (!Campos[5].isEmpty()) g.linguas = Campos[5].replaceAll("[\\[\\]\"]", "").split(",\\s*");
        if (!Campos[6].isEmpty()) g.nota = Integer.parseInt(Campos[6]);
        if (!Campos[7].isEmpty() && !Campos[7].equals("tbd")) g.userScore = Float.parseFloat(Campos[7]);
        if (!Campos[8].isEmpty()) g.conquistas = Integer.parseInt(Campos[8]);
        if (!Campos[9].isEmpty()) g.publishers = Campos[9].replaceAll("[\\[\\]\"]", "").split(",\\s*");
        if (!Campos[10].isEmpty()) g.developers = Campos[10].replaceAll("[\\[\\]\"]", "").split(",\\s*");
        if (!Campos[11].isEmpty()) g.categoria = Campos[11].replaceAll("[\\[\\]\"]", "").split(",\\s*");
        if (!Campos[12].isEmpty()) g.genero = Campos[12].replaceAll("[\\[\\]\"]", "").split(",\\s*");
        if (!Campos[13].isEmpty()) g.tags = Campos[13].replaceAll("[\\[\\]\"]", "").split(",\\s*");
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

    public static void Printar(Games g) {
        System.out.println("=> " + g.id + " ## " + g.nome + " ## " + g.data.toString() + " ## " +
                g.jogadores + " ## " + g.preco + " ## " + arrayParaString(g.linguas) +
                " ## " + g.nota + " ## " + g.userScore + " ## " + g.conquistas +
                " ## " + arrayParaString(g.publishers) + " ## " + arrayParaString(g.developers) +
                " ## " + arrayParaString(g.categoria) + " ## " + arrayParaString(g.genero) +
                " ## " + arrayParaString(g.tags) + " ##");
    }


    // -------------------- ÁRVORE BINÁRIA DENTRO DA GAMES --------------------
    static class NoAB {
        private Games jogo;
        private NoAB esq;
        private NoAB dir;

        public NoAB(Games jogo) {
            this.jogo = jogo;
            this.esq = null;
            this.dir = null;
        }

        public Games getJogo() { return jogo; }
        public void setJogo(Games jogo) { this.jogo = jogo; }
        public NoAB getEsq() { return esq; }
        public void setEsq(NoAB esq) { this.esq = esq; }
        public NoAB getDir() { return dir; }
        public void setDir(NoAB dir) { this.dir = dir; }
    }

    static class ArvoreBinaria {
        private NoAB raiz;
        private int comparacoes;

        public ArvoreBinaria() {
            raiz = null;
            comparacoes = 0;
        }

        public int getComparacoes() { return comparacoes; }

        public void inserir(Games g) {
            raiz = inserir(g, raiz);
        }

        private NoAB inserir(Games g, NoAB i) {
            if (i == null) return new NoAB(g);

            int cmp = g.getNome().compareToIgnoreCase(i.getJogo().getNome());

            if (cmp < 0) i.setEsq(inserir(g, i.getEsq()));
            else if (cmp > 0) i.setDir(inserir(g, i.getDir()));

            return i;
        }

        public boolean pesquisar(String nome) {
            System.out.print(nome + " raiz ");
            return pesquisar(nome, raiz);
        }

        private boolean pesquisar(String nome, NoAB i) {
            if (i == null) {
                System.out.println("NAO");
                comparacoes++;
                return false;
            }

            comparacoes++;

            int cmp = nome.compareToIgnoreCase(i.getJogo().getNome());

            if (cmp == 0) {
                System.out.println("SIM");
                return true;
            }

            if (cmp < 0) {
                System.out.print("esq ");
                return pesquisar(nome, i.getEsq());
            } else {
                System.out.print("dir ");
                return pesquisar(nome, i.getDir());
            }
        }
    }



    // ----------------------------- MAIN -----------------------------
    public static void main(String[] args) throws Exception {

        File arquivo = new File(FILE_PATH);
        String[] linhas = LeituraCsv(arquivo);
        Games[] catalogo = SetCatalogo(linhas);

        Scanner sc = new Scanner(System.in);
        ArvoreBinaria arvore = new ArvoreBinaria();

        String entrada = sc.nextLine().trim();
        while (!entrada.equals("FIM")) {
            int id = Integer.parseInt(entrada);
            Games g = PesquisaSeqMod(catalogo, id);
            if (g != null) arvore.inserir(g);
            entrada = sc.nextLine().trim();
        }

        String nome = sc.nextLine().trim();
        while (!nome.equals("FIM")) {
            arvore.pesquisar(nome);
            nome = sc.nextLine().trim();
        }

        PrintWriter pw = new PrintWriter("800772_arvoreBinaria.txt");
        pw.println("800772\t" + arvore.getComparacoes());
        pw.close();

        sc.close();
    }

}
