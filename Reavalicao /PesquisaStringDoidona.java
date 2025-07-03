public class PesquisaStringDoidona {

    T1 t1 = new T1();
    T2 t2 = new T2();
    T3 t3 = new T3();

    class T1 {
        String palavras[];
        T2 t2 = new T2();
        T3 t3 = new T3();
    }

    class T2 {
        NO raizes[];
    }

    class NO {
        char letra;
        Celula2 inicio, fim;
        NO esq, dir;
    }

    class Celula2 {
        String palavra;
        Celula2 prox;
    }

    class T3 {
        String palavras[];
        Celula3 inicio;
    }

    class Celula3 {
        String palavra;
        Celula3 prox;
    }

    boolean pesquisar(String palavra) {
        char primeiro = palavra.charAt(0);
        char segundo = palavra.charAt(1);

        int posT1 = hashT1(primeiro);

        if (t1.palavras[posT1].equals(palavra)) {
            return true;
        } else {
            int rehashT1 = hashVirtual(primeiro);

            if (rehashT1 == 0) {
                int posT2 = hashT2(segundo);
                NO raiz = t2.raizes[posT2];
                NO noArvore = pesquisaArvore(raiz, primeiro);
                if (noArvore != null) {
                    return pesquisaLista(noArvore.inicio, palavra);
                }
            } else {
                int posT3 = hashT3(segundo);
                if (t3.palavras[posT3].equals(palavra)) {
                    return true;
                } else {
                    return pesquisaLista3(t3.inicio, palavra);
                }
            }
        }

        return false;
    }

    NO pesquisaArvore(NO no, char letra) {
        if (no == null) return null;

        if (no.letra == letra) {
            return no;
        } else if (letra < no.letra) {
            return pesquisaArvore(no.esq, letra);
        } else {
            return pesquisaArvore(no.dir, letra);
        }
    }

    boolean pesquisaLista(Celula2 cel, String palavra) {
        while (cel != null) {
            if (cel.palavra.equals(palavra)) return true;
            cel = cel.prox;
        }
        return false;
    }

    boolean pesquisaLista3(Celula3 cel, String palavra) {
        while (cel != null) {
            if (cel.palavra.equals(palavra)) return true;
            cel = cel.prox;
        }
        return false;
    }

    int hashT1(char c) {
        return c % 10; // EXEMPLO! Ajuste.
    }

    int hashVirtual(char c) {
        return c % 2; // EXEMPLO! Ajuste.
    }

    int hashT2(char c) {
        return c % 10; // EXEMPLO! Ajuste.
    }

    int hashT3(char c) {
        return c % 10; // EXEMPLO! Ajuste.
    }

    public static void main(String[] args) {
        PesquisaStringDoidona psd = new PesquisaStringDoidona();
        boolean achou = psd.pesquisar("ABACATE");
        System.out.println(achou);
    }
}
