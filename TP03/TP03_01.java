import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class TP03_01 {

    public static class Show {
        private String showId;
        private String type;
        private String title;
        private String director;
        private String[] cast;
        private String country;
        private Date dataAdded;
        private int releaseYear;
        private String rating;
        private String duration;
        private String listedIn;
        private String description;

        public String getShowId() {
            return showId;
        }

        public void ler(String linha) {
            List<String> campos = parseLinhaCSV(linha);

            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

            this.showId = getOrNaN(campos, 0);
            this.type = getOrNaN(campos, 1);
            this.title = getOrNaN(campos, 2);
            this.director = getOrNaN(campos, 3);

            String elenco = getOrNaN(campos, 4);
            this.cast = elenco.equals("NaN") ? new String[0] : elenco.split(",\\s*");

            this.country = getOrNaN(campos, 5);

            try {
                String data = getOrNaN(campos, 6);
                this.dataAdded = data.equals("NaN") ? new Date(0) : sdf.parse(data);
            } catch (ParseException e) {
                this.dataAdded = new Date(0);
            }

            try {
                this.releaseYear = Integer.parseInt(getOrNaN(campos, 7));
            } catch (NumberFormatException e) {
                this.releaseYear = 0;
            }

            this.rating = getOrNaN(campos, 8);
            this.duration = getOrNaN(campos, 9);
            this.listedIn = getOrNaN(campos, 10);
            this.description = getOrNaN(campos, 11);
        }

        private String getOrNaN(List<String> lista, int index) {
            if (index >= lista.size() || lista.get(index).trim().isEmpty()) {
                return "NaN";
            }
            return lista.get(index).trim();
        }

        public Date getDataAdded() {
            return dataAdded;
        }

        public String getTitle() {
            return title;
        }

        public String formatar() {
            String[] castOrdenado = Arrays.copyOf(cast, cast.length);
            Arrays.sort(castOrdenado);

            return String.format("=> %s ## %s ## %s ## %s ## [%s] ## %s ## %tB %<te, %<tY ## %d ## %s ## %s ## [%s] ##",
                    showId,
                    title,
                    type,
                    director,
                    castOrdenado.length > 0 ? String.join(", ", castOrdenado) : "NaN",
                    country,
                    dataAdded,
                    releaseYear,
                    rating,
                    duration,
                    listedIn
            );
        }

        private List<String> parseLinhaCSV(String linha) {
            List<String> campos = new ArrayList<>();
            boolean emAspas = false;
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < linha.length(); i++) {
                char c = linha.charAt(i);
                if (c == '\"') {
                    emAspas = !emAspas;
                } else if (c == ',' && !emAspas) {
                    campos.add(sb.toString().replaceAll("\"", "").trim());
                    sb.setLength(0);
                } else {
                    sb.append(c);
                }
            }
            campos.add(sb.toString().replaceAll("\"", "").trim());
            return campos;
        }
    }

    public static class Lista {
        private Show[] array;
        private int n;

        public Lista() {
            this(100);
        }

        public Lista(int tamanho) {
            array = new Show[tamanho];
            n = 0;
        }

        public void inserirInicio(Show show) {
            if (n >= array.length) {
                throw new IllegalStateException("Lista cheia");
            }

            for (int i = n; i > 0; i--) {
                array[i] = array[i - 1];
            }

            array[0] = show;
            n++;
        }

        public void inserir(Show show, int pos) {
            if (n >= array.length || pos < 0 || pos > n) {
                throw new IllegalStateException("Erro ao inserir");
            }

            for (int i = n; i > pos; i--) {
                array[i] = array[i - 1];
            }

            array[pos] = show;
            n++;
        }

        public void inserirFim(Show show) {
            if (n >= array.length) {
                throw new IllegalStateException("Lista cheia");
            }

            array[n] = show;
            n++;
        }

        public Show removerInicio() {
            if (n == 0) {
                throw new IllegalStateException("Lista vazia");
            }

            Show resp = array[0];
            n--;

            for (int i = 0; i < n; i++) {
                array[i] = array[i + 1];
            }

            return resp;
        }

        public Show remover(int pos) {
            if (n == 0 || pos < 0 || pos >= n) {
                throw new IllegalStateException("Erro ao remover");
            }

            Show resp = array[pos];
            n--;

            for (int i = pos; i < n; i++) {
                array[i] = array[i + 1];
            }

            return resp;
        }

        public Show removerFim() {
            if (n == 0) {
                throw new IllegalStateException("Lista vazia");
            }

            return array[--n];
        }

        public void mostrar() {
            for (int i = 0; i < n; i++) {
                System.out.println(array[i].formatar());
            }
        }
    }

    public static String[] lerCSV(String caminho) {
        ArrayList<String> linhas = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(caminho));
            String linha = br.readLine(); // pula cabeçalho
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        return linhas.toArray(new String[0]);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] linhas = lerCSV("/tmp/disneyplus.csv");
        Map<String, Show> mapaShows = new HashMap<>();

        for (String linha : linhas) {
            Show s = new Show();
            s.ler(linha);
            mapaShows.put(s.getShowId(), s);
        }

        Lista lista = new Lista();

        while (sc.hasNextLine()) {
            String entrada = sc.nextLine().trim();
            if (entrada.equals("FIM")) break;
            
            Show show = mapaShows.get(entrada);
            if (show != null) {
                lista.inserirFim(show);
            }
        }

        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            String[] partes = sc.nextLine().split(" ");
            String comando = partes[0];

            try {
                switch (comando) {
                    case "II":
                        String idII = partes[1];
                        Show showII = mapaShows.get(idII);
                        if (showII != null) {
                            lista.inserirInicio(showII);
                        }
                        break;
                    case "I*":
                        int pos = Integer.parseInt(partes[1]);
                        String idIStar = partes[2];
                        Show showIStar = mapaShows.get(idIStar);
                        if (showIStar != null) {
                            lista.inserir(showIStar, pos);
                        }
                        break;
                    case "IF":
                        String idIF = partes[1];
                        Show showIF = mapaShows.get(idIF);
                        if (showIF != null) {
                            lista.inserirFim(showIF);
                        }
                        break;
                    case "RI":
                        Show removidoRI = lista.removerInicio();
                        System.out.println("(R) " + removidoRI.getTitle());
                        break;
                    case "R*":
                        int posR = Integer.parseInt(partes[1]);
                        Show removidoRStar = lista.remover(posR);
                        System.out.println("(R) " + removidoRStar.getTitle());
                        break;
                    case "RF":
                        Show removidoRF = lista.removerFim();
                        System.out.println("(R) " + removidoRF.getTitle());
                        break;
                }
            } catch (Exception e) {
                // Ignora operações inválidas
            }
        }

        lista.mostrar();
        sc.close();
    }
}