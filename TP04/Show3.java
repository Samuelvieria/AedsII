import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

class Tree {
    TreeNode root;

    public Tree() {
        this.root = null;
    }

    private TreeNode insert(String value, TreeNode node) {
        if (node == null) {
            node = new TreeNode(value);
        } else if (Show3.compare(value, node.value) > 0) { // Changed Show to Show3
            node.right = insert(value, node.right);
        } else if (Show3.compare(value, node.value) < 0) { // Changed Show to Show3
            node.left = insert(value, node.left);
        }
        return node;
    }

    public boolean search(String value, TreeNode node) {
        boolean resp = false;

        if (node == null) {
            resp = false;
        } else if (Show3.compare(value, node.value) > 0) { // Changed Show to Show3
            System.out.print("dir ");
            resp = search(value, node.right);
        } else if (Show3.compare(value, node.value) < 0) { // Changed Show to Show3
            System.out.print("esq ");
            resp = search(value, node.left);
        } else {
            resp = true;
        }
        return resp;
    }

    public void insert(String value) {
        // Only insert if the value is not already present to avoid duplicates
        if (!search(value, this.root)) { // Use internal search to check existence
            this.root = insert(value, this.root);
        }
    }

    public boolean search(String value) {
        // This search method is for the inner tree (called by UpperTree)
        // It does not print "=>raiz" or "SIM/NAO" directly, that's for the outer tree's final output
        return search(value, this.root);
    }
}

class UpperTree {
    TreeUpperNode root;

    public UpperTree() {
        this.root = null;
        // Initialize the outer tree with keys 0-14
        int list[] = { 7, 3, 11, 1, 5, 9, 13, 0, 2, 4, 6, 8, 10, 12, 14 };
        for (int i = 0; i < 15; i++) {
            insert(list[i]);
        }
    }

    // Inserts an integer value (releaseYear % 15) into the outer tree
    private TreeUpperNode insert(int value, TreeUpperNode node) {
        if (node == null) {
            node = new TreeUpperNode(value);
        } else if (Show3.compare(value, node.value) > 0) { // Changed Show to Show3
            node.right = insert(value, node.right);
        } else if (Show3.compare(value, node.value) < 0) { // Changed Show to Show3
            node.left = insert(value, node.left);
        }
        return node;
    }

    // This method is called by the constructor to build the outer tree structure
    public void insert(int value) {
        this.root = insert(value, this.root);
    }

    // Inserts a show title into the inner tree associated with the releaseYear % 15
    private TreeUpperNode insert(int value, String secondValue, TreeUpperNode node) {
        if (node == null) {
            // This case should ideally not happen if the outer tree is pre-populated
            node = new TreeUpperNode(value); // Create node if not found
            node.innerTree.insert(secondValue);
        } else if (Show3.compare(value, node.value) > 0) { // Changed Show to Show3
            node.right = insert(value, secondValue, node.right);
        } else if (Show3.compare(value, node.value) < 0) { // Changed Show to Show3
            node.left = insert(value, secondValue, node.left);
        } else {
            // Found the correct outer node, now insert into its inner tree
            node.innerTree.insert(secondValue);
        }
        return node;
    }

    // Public method to insert a show's release year modulo and title
    public void insert(int value, String secondValue) {
        this.root = insert(value, secondValue, this.root);
    }

    // Recursive search for a title across the tree of trees
    private boolean search(String search, TreeUpperNode node) {
        if (node == null) {
            return false;
        }

        boolean resp = false;
        // Search in the current node's inner tree
        resp = node.innerTree.search(search, node.innerTree.root);

        if (resp) {
            return resp; // Found in this inner tree
        }

        // If not found, traverse left or right in the outer tree
        System.out.print(" ESQ ");
        resp = search(search, node.left);

        if (resp) {
            return resp; // Found in left subtree
        }

        System.out.print(" DIR ");
        resp = search(search, node.right);
        return resp; // Found in right subtree or not found at all
    }

    // Public search method, prints the initial "=>raiz" and final "SIM/NAO"
    public boolean search(String search) {
        System.out.print("=>raiz ");
        boolean resp = search(search, this.root);
        if (resp) {
            System.out.println(" SIM");
        } else {
            System.out.println(" NAO");
        }
        return resp;
    }
}

class TreeUpperNode {
    int value;
    Tree innerTree; // Pointer to the inner binary tree
    TreeUpperNode right;
    TreeUpperNode left;

    public TreeUpperNode(int value) {
        this.right = this.left = null;
        innerTree = new Tree(); // Initialize the inner tree
        this.value = value;
    }
}

class TreeNode {
    String value;
    TreeNode right;
    TreeNode left;

    public TreeNode(String value) {
        this.right = this.left = null;
        this.value = value;
    }
}

// RBTree, ReserveHash, HashRehash, Node, LL, IndirectHash classes are not used in this specific problem's solution
// but are kept as per the original provided code. They are not modified.
class RBTree {
    RBTreeNode root;

    public RBTree() {
        this.root = null;
    }

    private RBTreeNode rotacaoDir(RBTreeNode no) {
        RBTreeNode noEsq = no.left;
        RBTreeNode noEsqDir = noEsq.right;

        noEsq.right = no;
        no.left = noEsqDir;

        return noEsq;
    }

    private RBTreeNode rotacaoEsq(RBTreeNode no) {
        RBTreeNode noDir = no.right;
        RBTreeNode noDirEsq = noDir.left;

        noDir.left = no;
        no.right = noDirEsq;
        return noDir;
    }

    private RBTreeNode rotacaoDirEsq(RBTreeNode no) {
        no.right = rotacaoDir(no.right);
        return rotacaoEsq(no);
    }

    private RBTreeNode rotacaoEsqDir(RBTreeNode no) {
        no.left = rotacaoEsq(no.left);
        return rotacaoDir(no);
    }

    private void balance(RBTreeNode bisavo, RBTreeNode avo, RBTreeNode pai, RBTreeNode i) {
        if (pai.color) {
            if (Show3.compare(pai.value, avo.value) > 0) { // Changed Show to Show3
                if (Show3.compare(i.value, pai.value) > 0) { // Changed Show to Show3
                    avo = rotacaoEsq(avo);
                } else {
                    avo = rotacaoDirEsq(avo);
                }
            } else {
                if (Show3.compare(i.value, pai.value) < 0) { // Changed Show to Show3
                    avo = rotacaoDir(avo);
                } else {
                    avo = rotacaoEsqDir(avo);
                }
            }
            if (bisavo == null) {
                root = avo;
            } else if (Show3.compare(avo.value, bisavo.value) < 0) { // Changed Show to Show3
                bisavo.left = avo;
            } else {
                bisavo.right = avo;
            }
            avo.color = false;
            avo.left.color = avo.right.color = true;
        }

    }

    private void insert(String value, RBTreeNode bisavo, RBTreeNode avo, RBTreeNode pai, RBTreeNode i)
            throws Exception {
        if (i == null) {
            if (Show3.compare(value, pai.value) < 0) { // Changed Show to Show3
                i = pai.left = new RBTreeNode(value, true);
            } else {
                i = pai.right = new RBTreeNode(value, true);
            }
            if (pai.color == true) {
                balance(bisavo, avo, pai, i);
            }
        } else {
            if (i.left != null && i.right != null && i.left.color == true && i.right.color == true) {
                i.color = true;
                i.left.color = i.right.color = false;
                if (i == root) {
                    i.color = false;
                } else if (pai.color == true) {
                    balance(bisavo, avo, pai, i);
                }
            }
            if (Show3.compare(value, i.value) < 0) { // Changed Show to Show3
                insert(value, avo, pai, i, i.left);
            } else if (Show3.compare(value, i.value) > 0) { // Changed Show to Show3
                insert(value, avo, pai, i, i.right);
            }
        }
    }

    public void insert(String value) throws Exception {
        if (root == null) {
            root = new RBTreeNode(value);
        } else if (root.left == null && root.right == null) {
            if (Show3.compare(value, root.value) < 0) { // Changed Show to Show3
                root.left = new RBTreeNode(value);
            } else {
                root.right = new RBTreeNode(value);
            }
        } else if (root.left == null) {
            if (Show3.compare(value, root.value) < 0) { // Changed Show to Show3
                root.left = new RBTreeNode(value);
            } else if (Show3.compare(value, root.right.value) < 0) { // Changed Show to Show3
                root.left = new RBTreeNode(root.value);
                root.value = value;
            } else {
                root.left = new RBTreeNode(root.value);
                root.value = root.right.value;
                root.right.value = value;
            }
            root.left.color = root.right.color = false;
        } else if (root.right == null) {
            if (Show3.compare(value, root.value) > 0) { // Changed Show to Show3
                root.right = new RBTreeNode(value);
            } else if (Show3.compare(value, root.left.value) > 0) { // Changed Show to Show3
                root.right = new RBTreeNode(root.value);
                root.value = value;
            } else {
                root.right = new RBTreeNode(root.value);
                root.value = root.left.value;
                root.left.value = value;
            }
            root.left.color = root.right.color = false;

        } else {
            insert(value, null, null, null, root);
        }
        root.color = false;
    }

    public boolean search(String value, RBTreeNode node) {
        boolean resp = false;

        if (node == null) {
            resp = false;
        } else if (Show3.compare(value, node.value) > 0) { // Changed Show to Show3
            System.out.print("dir ");
            resp = search(value, node.right);
        } else if (Show3.compare(value, node.value) < 0) { // Changed Show to Show3
            System.out.print("esq ");
            resp = search(value, node.left);
        } else {
            resp = true;
        }
        return resp;
    }

    public boolean search(String value) {
        System.out.print("=>raiz  ");
        boolean resp = search(value, this.root);
        if (resp) {
            System.out.println("SIM");
        } else {
            System.out.println("NAO");
        }
        return resp;
    }
}
class RBTreeNode {
    String value;
    RBTreeNode right;
    RBTreeNode left;
    RBTreeNode parent;
    boolean color = false;

    public RBTreeNode(String value) {
        this.right = this.left = this.parent = null;
        this.value = value;
    }

    public RBTreeNode(String value, boolean color) {
        this.right = this.left = this.parent = null;
        this.value = value;
        this.color = color;
    }
}
class ReserveHash{
    private int resSize = 9;
    private int tabSize = 21;
    private String[] tab;
    public ReserveHash() {
        this.tab = new String[resSize + tabSize];
    }
    public ReserveHash(int resSize, int tabSize){
        this.resSize = resSize;
        this.tabSize = tabSize;
        this.tab = new String[resSize + tabSize];
    }
    private int hash(String value) {
        int sum = 0;
        for(int i = 0; i<value.length(); i++){
            sum += value.charAt(i);
        }

        return sum%tabSize;
    }
    public boolean search(String value){
        int hash = hash(value);
        int pos = hash;
        boolean res = false;
        if(Show3.compare(tab[hash], value) == 0){ // Changed Show to Show3
            res = true;
        } else {
            for(int i = tabSize; i<(tabSize+resSize); i++){
                if(Show3.compare(tab[i], value) == 0){ // Changed Show to Show3
                    res = true;
                }
            }
        }
        System.out.printf(" (Posicao: %d) %s\n", pos, (res ? "SIM" : "NAO"));
        return res;
    }
    public void insert(String value) {
        int hash = hash(value);
        if(tab[hash] == null){
            tab[hash] = value;
        } else {
            for(int i = tabSize; i<(tabSize+resSize); i++){
                if(tab[i] == null){
                    tab[i] = value;
                    i+=(tabSize+resSize);
                }
            }
        }
    }
}
class HashRehash{
    private int tabSize = 21;
    private String[] tab;
    public HashRehash() {
        this.tab = new String[tabSize];
    }
    public HashRehash(int tabSize){
        this.tabSize = tabSize;
        this.tab = new String[tabSize];
    }
    private int hash(String value) {
        int sum = 0;
        for(int i = 0; i<value.length(); i++){
            sum += value.charAt(i);
        }

        return sum%tabSize;
    }
    private int rehash(String value) {
        int sum = 0;
        for(int i = 0; i<value.length(); i++){
            sum += value.charAt(i);
        }

        return (sum+1)%tabSize;
    }
    public boolean search(String value){
        int hash = hash(value);
        int pos = hash;
        boolean res = Show3.compare(tab[hash], value) == 0 || Show3.compare(tab[rehash(value)], value) == 0; // Changed Show to Show3
        System.out.printf(" (Posicao: %d) %s\n", pos, (res ? "SIM" : "NAO"));
        return res;
    }
    public void insert(String value) {
        int hash = hash(value);
        if(tab[hash] == null){
            tab[hash] = value;
        } else{
            hash = rehash(value);
            if(tab[hash] == null){
                tab[hash] = value;
            }
        }
    }
}
class Node{
    String value;
    Node next;
    public Node(String value){
        this.value = value;
        this.next = null;
    }
}
class LL{
    Node head;
    Node tail;
    public LL(){
        head = tail = null;
    }
    public boolean search(String value){
        boolean resp = false;
        for(Node cur = head; cur != null; cur = cur.next){
            if(Show3.compare(value, cur.value) == 0){ // Changed Show to Show3
                resp = true;
            }
        }
        return resp;
    }
    public void insert(String value) {
        Node newNode = new Node(value);
        if(head == tail && head == null){
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }
}
class IndirectHash{
    private int tabSize = 21;
    private LL[] tab;
    public IndirectHash() {
        this.tab = new LL[tabSize];
        for(int i = 0; i<tabSize; i++){
            this.tab[i] = new LL();
        }
    }
    public IndirectHash(int tabSize){
        this.tabSize = tabSize;
        this.tab = new LL[tabSize];
        for(int i = 0; i<tabSize; i++){
            this.tab[i] = new LL();
        }
    }
    private int hash(String value) {
        int sum = 0;
        for(int i = 0; i<value.length(); i++){
            sum += value.charAt(i);
        }

        return sum%tabSize;
    }
    public boolean search(String value){
        int hash = hash(value);
        int pos = hash;
        boolean res = tab[hash].search(value);

        System.out.printf(" (Posicao: %d) %s\n", pos, (res ? "SIM" : "NAO"));
        return res;
    }
    public void insert(String value) {
        int hash = hash(value);
        if(!tab[hash].search(value)){
            tab[hash].insert(value);
        }
    }
}

public class Show3 { // Renamed class from Show to Show3
    static SimpleDateFormat ddf = new SimpleDateFormat("MMMM dd,yyyy");
    static SimpleDateFormat ddf2 = new SimpleDateFormat("MMMM d,yyyy");
    public static final String FILE_PATH = "/tmp/disneyplus.csv";
    public static ArrayList<Show3> shows = new ArrayList<Show3>(); // Changed Show to Show3
    public static int comp = 0;
    public static int mov = 0;
    private String show_id;
    private String type;
    private String title;
    private String director;
    private String[] cast;
    private String country;
    private Date date_added;
    private int release_year;
    private String range;
    private String duration;
    private String[] listed_in;

    public static void resetCounters() {
        comp = 0;
        mov = 0;
    }

    public Show3 clone() { // Changed Show to Show3
        return new Show3(this.show_id, this.type, this.title, this.director, this.cast, this.country, this.date_added, // Changed Show to Show3
                this.release_year, this.range, this.duration, this.listed_in);
    }

    public Show3() { // Changed Show to Show3
    }

    public Show3(final String show_id, final String type, final String title, final String director, final String[] cast, // Changed Show to Show3
            final String country, final Date date_added, final int release_year, final String range,
            final String duration, final String[] listed_in) {
        this.show_id = show_id;
        this.type = type;
        this.title = title;
        this.director = director;
        this.cast = cast;
        this.country = country;
        this.date_added = date_added;
        this.release_year = release_year;
        this.range = range;
        this.duration = duration;
        this.listed_in = listed_in;
    }

    public String getShow_id() {
        return show_id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String[] getCast() {
        return cast;
    }

    public String getCountry() {
        return country;
    }

    public Date getDate_added() {
        return date_added;
    }

    public int getRelease_year() {
        return release_year;
    }

    public String getRange() {
        return range;
    }

    public String getDuration() {
        return duration;
    }

    public String[] getListed_in() {
        return listed_in;
    }

    public Show3 setShow_id(String show_id) { // Changed Show to Show3
        this.show_id = show_id;
        return this;
    }

    public Show3 setType(String type) { // Changed Show to Show3
        this.type = type;
        return this;
    }

    public Show3 setTitle(String title) { // Changed Show to Show3
        this.title = title;
        return this;
    }

    public Show3 setDirector(String director) { // Changed Show to Show3
        this.director = director;
        return this;
    }

    public Show3 setCast(String[] cast) { // Changed Show to Show3
        this.cast = cast;
        return this;
    }

    public Show3 setCountry(String country) { // Changed Show to Show3
        this.country = country;
        return this;
    }

    public Show3 setDate_added(Date date_added) { // Changed Show to Show3
        this.date_added = date_added;
        return this;
    }

    public Show3 setRelease_year(int release_year) { // Changed Show to Show3
        this.release_year = release_year;
        return this;
    }

    public Show3 setRange(String range) { // Changed Show to Show3
        this.range = range;
        return this;
    }

    public Show3 setDuration(String duration) { // Changed Show to Show3
        this.duration = duration;
        return this;
    }

    public Show3 setListed_in(String[] listed_in) { // Changed Show to Show3
        this.listed_in = listed_in;
        return this;
    }

    public void heapSort(String[] v) {
        for (int i = 1; i < v.length; i++) {
            for (int j = i; j > 0 && v[j].trim().compareTo(v[(j - 1) / 2].trim()) > 0; j = (j - 1) / 2) {
                String temp = v[j];
                v[j] = v[(j - 1) / 2];
                v[(j - 1) / 2] = temp;
            }
        }
        for (int i = v.length - 1; i >= 0;) {
            String temp = v[i];
            v[i] = v[0];
            v[0] = temp;
            i--;
            int j = 0;
            while (j < i) {
                if (j * 2 + 1 <= i || j * 2 + 2 <= i) {
                    int ji = j * 2 + 2 <= i && v[j * 2 + 2].trim().compareTo(v[j * 2 + 1].trim()) > 0 ? j * 2 + 2
                            : j * 2 + 1;
                    if (v[j].trim().compareTo(v[ji].trim()) < 0) {
                        String temp2 = v[j];
                        v[j] = v[ji];
                        v[ji] = temp2;
                        j = ji;
                    } else {
                        j = i;
                    }
                } else {
                    j = i;
                }
            }
        }
    }

    public static Show3 searchById(String id, ArrayList<Show3> shows) { // Changed Show to Show3
        for (int i = 0; i < shows.size(); i++) {
            if (shows.get(i).getShow_id().equals(id))
                return shows.get(i);
        }
        return null;
    }

    public void read(String line) {
        try {
            ArrayList<String> parts = new ArrayList<>();
            boolean inQuotes = false;
            StringBuilder sb = new StringBuilder();

            for (char c : line.toCharArray()) {
                if (c == '"') {
                    inQuotes = !inQuotes;
                } else if (c == ',' && !inQuotes) {
                    parts.add(sb.toString().trim());
                    sb = new StringBuilder();
                } else {
                    sb.append(c);
                }
            }
            parts.add(sb.toString().trim());
            this.show_id = !parts.get(0).isEmpty() ? parts.get(0) : "NaN";
            this.type = !parts.get(1).isEmpty() ? parts.get(1) : "NaN";
            this.title = !parts.get(2).isEmpty() ? parts.get(2) : "NaN";
            this.director = !parts.get(3).isEmpty() ? parts.get(3) : "NaN";

            this.cast = !parts.get(4).isEmpty()
                    ? parts.get(4).split(",\\s*")
                    : new String[] { "NaN" };
            this.heapSort(this.cast);

            this.country = !parts.get(5).isEmpty() ? parts.get(5) : "NaN";

            if (parts.get(6) != null && !parts.get(6).isEmpty()) {
                try {
                    this.date_added = ddf.parse(parts.get(6));
                } catch (ParseException e) {
                    this.date_added = null;
                }
            } else {
                this.date_added = ddf.parse("March 1, 1900");
            }

            this.release_year = !parts.get(7).isEmpty()
                    ? Integer.parseInt(parts.get(7))
                    : -1;

            this.range = !parts.get(8).isEmpty() ? parts.get(8) : "NaN";
            this.duration = !parts.get(9).isEmpty() ? parts.get(9) : "NaN";

            this.listed_in = !parts.get(10).isEmpty()
                    ? parts.get(10).split(",\\s*")
                    : new String[] { "NaN" };
            this.heapSort(this.listed_in);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.print("=> " + show_id + " ## " + title + " ## " + type + " ## " + director + " ## [");
        System.out.print(String.join(", ", cast));
        System.out.print("] ## " + country + " ## ");

        System.out.print(date_added != null ? ddf2.format(date_added) : "NaN");
        System.out.print(" ## " + release_year + " ## " + range + " ## " + duration + " ## [");
        System.out.print(String.join(", ", listed_in));
        System.out.println("] ##");
    }

    public static void printVerde(long init, String filename) throws FileNotFoundException {
        long end = System.currentTimeMillis();
        PrintWriter log = new PrintWriter("867936_" + filename + ".txt");
        log.println("867936\t" + (end - init) + "\t" + comp);
        log.close();
    }

    public static void startShows() {
        try {
            FileInputStream fstream = new FileInputStream(FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String line = br.readLine(); // Read header

            while ((line = br.readLine()) != null) {
                Show3 show = new Show3(); // Changed Show to Show3
                show.read(line);
                shows.add(show);
            }

            fstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int compare(String a, String b) {
        comp++;
        return a.trim().compareTo(b.trim());
    }

    public static int compare(Integer a, Integer b) {
        comp++;
        return a.compareTo(b);
    }

    public static void main(String[] args) throws Exception {
        startShows();
        Scanner sc = new Scanner(System.in);

        // Instanciação da Árvore de Árvores
        UpperTree upperTree = new UpperTree(); 

        String line = sc.nextLine();

        // Leitura e inserção dos registros na árvore de árvores
        while (!line.equals("FIM")) {
            Show3 show = searchById(line, shows); // Changed Show to Show3
            if (show != null) {
                // Inserir releaseYear % 15 e o título do show na árvore de árvores
                upperTree.insert(show.release_year % 15, show.title); 
            } else {
                System.out.println("x Show not found!");
            }
            line = sc.nextLine();
        }

        long init = System.currentTimeMillis();
        resetCounters();
        line = sc.nextLine();

        // Pesquisa dos registros na árvore de árvores
        while (!line.equals("FIM")) {
            // Pesquisar o título na árvore de árvores
            upperTree.search(line); 
            line = sc.nextLine();
        }
        // Geração do arquivo de log com o tempo e número de comparações
        printVerde(init, "arvoreDeArvore"); // Changed filename for the log
        sc.close();
    }
}