package P3;

public class Agenda {

    public class Contato {
        String nome;
        String telefone;
        String email;
        int cpf;

        public Contato(String nome, String telefone, String email, int cpf) {
            this.nome = nome;
            this.telefone = telefone;
            this.email = email;
            this.cpf = cpf;
        }
    }

    public class Celula {
        Contato contato;
        Celula prox;

        public Celula(Contato contato) {
            this.contato = contato;
            this.prox = null;
        }
    }

    public class No {
        char letra;
        No esq;
        No dir;
        Celula primeiro;
        Celula ultimo;

        public No(char letra) {
            this.letra = letra;
            this.esq = null;
            this.dir = null;
            this.primeiro = null;
            this.ultimo = null;
        }

        public void inserirNaLista(Contato contato) {
            Celula nova = new Celula(contato);
            if (primeiro == null) {
                primeiro = ultimo = nova;
            } else {
                ultimo.prox = nova;
                ultimo = nova;
            }
        }
    }

    private No raiz;

    public Agenda() {
        raiz = null;
    }

    public void inserir(Contato contato) {
        raiz = inserir(contato, raiz);
    }

    private No inserir(Contato contato, No i) {
        if (i == null) {
            i = new No(contato.nome.charAt(0));
            i.inserirNaLista(contato);
        } else if (i.letra == contato.nome.charAt(0)) {
            i.inserirNaLista(contato);
        } else if (contato.nome.charAt(0) < i.letra) {
            i.esq = inserir(contato, i.esq);
        } else {
            i.dir = inserir(contato, i.dir);
        }
        return i;
    }
}
