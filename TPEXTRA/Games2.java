import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

class Games2 {
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
    public Games2(int id, String nome, Data data, int jogadores, float preco, String[] linguas,
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

    // ------------------ Funções CSV (parser robusto) ------------------
    public static String[] LeituraCsv(File arquivo) {
        String[] linhas = new String[15000];
        int i = 0;
        try (Scanner sc = new Scanner(arquivo, "UTF-8")) {
            if (sc.hasNextLine()) sc.nextLine(); // pular cabeçalho
            while (sc.hasNextLine()) linhas[i++] = sc.nextLine();
        } catch (Exception e) { System.out.println("Erro: " + e.getMessage()); }
        String[] certo = new String[i];
        System.arraycopy(linhas, 0, certo, 0, i);
        return certo;
    }

    public static Games2 SetgameLinha(String linha) {
        String[] Campos = new String[14];
        int campoAtual = 0; boolean dentroAspas = false;
        StringBuilder campo = new StringBuilder();

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '"') dentroAspas = !dentroAspas;
            else if (c == ',' && !dentroAspas) {
                Campos[campoAtual++] = campo.toString().trim();
                campo.setLength(0);
            } else campo.append(c);
        }
        Campos[campoAtual] = campo.toString().trim();

        Games2 g = new Games2(0, "", new Data(1, 1, 1900), 0, 0.0f, new String[0],
                -1, -1.0f, 0, new String[0], new String[0],
                new String[0], new String[0], new String[0]);

        if (Campos[0] != null && !Campos[0].isEmpty()) {
            try { g.id = Integer.parseInt(Campos[0]); } catch (Exception e) { g.id = 0; }
        }
        g.nome = Campos[1] != null ? Campos[1] : "";

        // Data
        Data d = new Data(1, 1, 1900);
        if (Campos[2] != null && !Campos[2].isEmpty()) {
            String s = Campos[2]; String[] meses = {"Jan","Feb","Mar","Apr","May","Jun",
                    "Jul","Aug","Sep","Oct","Nov","Dec"};
            int dia=1,mes=1,ano=1900;
            if (s.contains(",")) {
                String[] partes = s.split(",");
                try { ano = Integer.parseInt(partes[1].trim()); } catch(Exception ex) { ano = 1900; }
                String[] sub = partes[0].trim().split(" ");
                if (sub.length == 2) {
                    for (int i=0;i<12;i++) if (meses[i].equalsIgnoreCase(sub[0])) mes=i+1;
                    try { dia = Integer.parseInt(sub[1]); } catch(Exception e) { dia = 1; }
                }
            } else if (s.contains(" ")) {
                String[] partes = s.split(" ");
                for (int i=0;i<12;i++) if (meses[i].equalsIgnoreCase(partes[0])) mes=i+1;
                try { ano = Integer.parseInt(partes[1]); } catch(Exception e) { ano = 1900; }
            } else {
                try { ano = Integer.parseInt(s); } catch(Exception e) { ano = 1900; }
            }
            d = new Data(dia,mes,ano);
        }
        g.data = d;

        if (Campos[3] != null && !Campos[3].isEmpty()) {
            String num = Campos[3].replaceAll("[^0-9]", "");
            g.jogadores = num.isEmpty()?0:Integer.parseInt(num);
        }

        if (Campos[4] != null && Campos[4].equalsIgnoreCase("Free to Play")) g.preco = 0;
        else if (Campos[4] != null && !Campos[4].isEmpty()) {
            try { g.preco = Float.parseFloat(Campos[4]); } catch(Exception e) { g.preco = 0f; }
        }

        if (Campos[5] != null && !Campos[5].isEmpty()) g.linguas = Campos[5].replaceAll("[\\[\\]\"]", "").split(",\\s*");
        if (Campos[6] != null && !Campos[6].isEmpty()) try { g.nota = Integer.parseInt(Campos[6]); } catch(Exception e){ g.nota = -1; }
        if (Campos[7] != null && !Campos[7].isEmpty() && !Campos[7].equals("tbd")) try { g.userScore = Float.parseFloat(Campos[7]); } catch(Exception e){ g.userScore = -1f; }
        if (Campos[8] != null && !Campos[8].isEmpty()) try { g.conquistas = Integer.parseInt(Campos[8]); } catch(Exception e){ g.conquistas = 0; }
        if (Campos[9] != null && !Campos[9].isEmpty()) g.publishers = Campos[9].replaceAll("[\\[\\]\"]", "").split(",\\s*");
        if (Campos[10] != null && !Campos[10].isEmpty()) g.developers = Campos[10].replaceAll("[\\[\\]\"]", "").split(",\\s*");
        if (Campos[11] != null && !Campos[11].isEmpty()) g.categoria = Campos[11].replaceAll("[\\[\\]\"]", "").split(",\\s*");
        if (Campos[12] != null && !Campos[12].isEmpty()) g.genero = Campos[12].replaceAll("[\\[\\]\"]", "").split(",\\s*");
        if (Campos[13] != null && !Campos[13].isEmpty()) g.tags = Campos[13].replaceAll("[\\[\\]\"]", "").split(",\\s*");
        return g;
    }

    public static Games2[] SetCatalogo(String[] linhas) {
        Games2[] catalogo = new Games2[linhas.length];
        for (int i = 0; i < linhas.length; i++) catalogo[i] = SetgameLinha(linhas[i]);
        return catalogo;
    }

    public static Games2 PesquisaSeqMod(Games2[] catalogo, int x) {
        for (Games2 g : catalogo) if (g.id == x) return g;
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

    public static void Printar(Games2 g) {
        System.out.println("=> " + g.id + " ## " + g.nome + " ## " + g.data.toString() + " ## " +
                g.jogadores + " ## " + g.preco + " ## " + arrayParaString(g.linguas) +
                " ## " + g.nota + " ## " + g.userScore + " ## " + g.conquistas +
                " ## " + arrayParaString(g.publishers) + " ## " + arrayParaString(g.developers) +
                " ## " + arrayParaString(g.categoria) + " ## " + arrayParaString(g.genero) +
                " ## " + arrayParaString(g.tags) + " ##");
    }

    // ==================== ARVORE PRINCIPAL (NÓS DO TIPO 1) ====================
    static class NO1 {
        int chave;       // jogadores % 15
        NO1 esq, dir;
        NO2 raizSegunda; // raiz da árvore associada

        NO1(int chave) {
            this.chave = chave;
            this.esq = this.dir = null;
            this.raizSegunda = null;
        }
    }

    // ==================== ARVORE SECUNDÁRIA (NÓS DO TIPO 2) ====================
    static class NO2 {
        String chave; // nome do jogo
        NO2 esq, dir;

        NO2(String chave) {
            this.chave = chave;
            this.esq = this.dir = null;
        }
    }

    // ==================== ÁRVORE DE ÁRVORES ====================
    static class ArvoreDeArvores {
        private NO1 raiz1;
        private int comparacoesSegundas; // contador de comparações realizadas nas buscas das segundas árvores

        public ArvoreDeArvores() {
            raiz1 = null;
            comparacoesSegundas = 0;
        }

        // Cria a primeira árvore com nodes nas chaves fornecidas (na ordem dada)
        public void criarPrimeiraArvoreComOrdem(int[] ordem) {
            for (int k : ordem) {
                raiz1 = inserirNO1(raiz1, k);
            }
        }

        // Inserção padrão na primeira árvore (por chave int)
        private NO1 inserirNO1(NO1 i, int chave) {
            if (i == null) {
                return new NO1(chave);
            }
            if (chave < i.chave) i.esq = inserirNO1(i.esq, chave);
            else if (chave > i.chave) i.dir = inserirNO1(i.dir, chave);
            return i;
        }

        // Encontrar nó NO1 por chave (busca padrão)
        private NO1 buscarNO1PorChave(int chave) {
            NO1 atual = raiz1;
            while (atual != null) {
                if (chave == atual.chave) return atual;
                else if (chave < atual.chave) atual = atual.esq;
                else atual = atual.dir;
            }
            return null;
        }

        // Inserir um Games2 na segunda árvore associada ao nó cujo chave = jogos.getJogadores() % 15
        public void inserirNaSegundaPorGame(Games2 g) {
            int chave = Math.floorMod(g.getJogadores(), 15); // jogadores % 15
            NO1 no1 = buscarNO1PorChave(chave);
            if (no1 == null) {
                raiz1 = inserirNO1(raiz1, chave);
                no1 = buscarNO1PorChave(chave);
            }
            no1.raizSegunda = inserirNO2(no1.raizSegunda, g.getNome());
        }

        // Inserção padrão na segunda árvore (por nome, sem duplicatas)
        private NO2 inserirNO2(NO2 raiz, String nome) {
            if (raiz == null) return new NO2(nome);
            int cmp = nome.compareToIgnoreCase(raiz.chave);
            if (cmp < 0) raiz.esq = inserirNO2(raiz.esq, nome);
            else if (cmp > 0) raiz.dir = inserirNO2(raiz.dir, nome);
            return raiz;
        }

        private boolean buscarNomePreorder(NO1 no, String nome, StringBuilder sbFirstTokens,
                                           StringBuilder sbSecondTokens) {
            return buscarNomePreorder(no, nome, sbFirstTokens, sbSecondTokens, "");
        }

        private boolean buscarNomePreorder(NO1 no, String nome, StringBuilder sbFirstTokens,
                                           StringBuilder sbSecondTokens, String dirTokenFromParent) {
            if (no == null) return false;

            // append dir token from parent when visiting this node (for root, dirTokenFromParent = "")
            if (!dirTokenFromParent.isEmpty()) {
                if (sbFirstTokens.length() > 0) sbFirstTokens.append(" ");
                sbFirstTokens.append(dirTokenFromParent);
            }

            // if this node has a second tree, attempt search there
            if (no.raizSegunda != null) {
                // try search, but we must collect only direction tokens (esq/dir) for second tree
                StringBuilder tmpSecond = new StringBuilder();
                boolean found = buscarEmSegundaColetando(no.raizSegunda, nome, tmpSecond);
                if (tmpSecond.length() > 0) {
                    if (sbSecondTokens.length() > 0) sbSecondTokens.append(" ");
                    sbSecondTokens.append(tmpSecond.toString());
                }
                if (found) return true;
            }

            // visit left subtree (token "ESQ")
            if (buscarNomePreorder(no.esq, nome, sbFirstTokens, sbSecondTokens, "ESQ")) return true;
            // visit right subtree (token "DIR")
            if (buscarNomePreorder(no.dir, nome, sbFirstTokens, sbSecondTokens, "DIR")) return true;

            return false;
        }

        private boolean buscarEmSegundaColetando(NO2 raiz, String nome, StringBuilder sbPath) {
            NO2 atual = raiz;
          
            while (atual != null) {
                comparacoesSegundas++;
                int cmp = nome.compareToIgnoreCase(atual.chave);
                if (cmp == 0) {
                    return true;
                } else if (cmp < 0) {
                    // move left => append "esq"
                    if (sbPath.length() > 0) sbPath.append(" ");
                    sbPath.append("esq");
                    atual = atual.esq;
                } else {
                    if (sbPath.length() > 0) sbPath.append(" ");
                    sbPath.append("dir");
                    atual = atual.dir;
                }
            }
            return false;
        }

    
        public boolean pesquisarNomeLinhaFormatada(String nome) {
            StringBuilder sbFirst = new StringBuilder();  // tokens maiúsculos ("ESQ"/"DIR")
            StringBuilder sbSecond = new StringBuilder(); // tokens minúsculos ("esq"/"dir")

            boolean found = buscarNomePreorder(raiz1, nome, sbFirst, sbSecond);

            // Monta a linha exatamente como no pub.out:
            // => <nome> => raiz  [<primeira tokens, separados por ' '] [<segunda tokens>]  SIM/NAO
            StringBuilder line = new StringBuilder();
            line.append("=> ").append(nome).append(" => ");
            line.append("raiz");
            if (sbFirst.length() > 0) {
                // add two spaces before the first-tree tokens block to match sample
                line.append("  ").append(sbFirst.toString());
            }
            if (sbSecond.length() > 0) {
                // ensure a space between first tokens block and second tokens
                line.append(" ").append(sbSecond.toString());
            }
            // two spaces before result as in sample
            line.append("  ").append(found ? "SIM" : "NAO");

            System.out.println(line.toString());
            return found;
        }

        public int getComparacoesSegundas() { return comparacoesSegundas; }
    }

    // ----------------------------- MAIN -----------------------------
    public static void main(String[] args) throws Exception {

        File arquivo = new File(FILE_PATH);
        String[] linhas = LeituraCsv(arquivo);
        Games2[] catalogo = SetCatalogo(linhas);

        Scanner sc = new Scanner(System.in);
        ArvoreDeArvores arv = new ArvoreDeArvores();

        // Ordem fixa para criar a primeira árvore (conforme enunciado)
        int[] ordem = new int[] {7, 3, 11, 1, 5, 9, 13, 0, 2, 4, 6, 8, 10, 12, 14};
        arv.criarPrimeiraArvoreComOrdem(ordem);

        // ---------- Parte 1: Inserções por ID (até "FIM") ----------
        String linha = sc.nextLine().trim();
        while (!linha.equals("FIM")) {
            try {
                int id = Integer.parseInt(linha);
                Games2 g = PesquisaSeqMod(catalogo, id);
                if (g != null) {
                    arv.inserirNaSegundaPorGame(g);
                }
            } catch (Exception e) {
                // ignora linha inválida
            }
            linha = sc.nextLine().trim();
        }

        // ---------- Parte 2: Pesquisas por Nome (até "FIM") ----------
        String nome = sc.nextLine().trim();
        while (!nome.equals("FIM")) {
            arv.pesquisarNomeLinhaFormatada(nome);
            nome = sc.nextLine().trim();
        }

        // ---------- Log ----------
        PrintWriter pw = new PrintWriter("800772_arvoreBinaria.txt");
        pw.println("800772\t" + arv.getComparacoesSegundas());
        pw.close();

        sc.close();
    }
}
