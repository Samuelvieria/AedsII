
import java.util.PrimitiveIterator;

public class ContarPalavras {
    public static class ArvoreArvore {
        No raiz;
    }

    public class No {
        char letra;
        No esq, dir;
        No2 raiz;
    }

    class No2 {
        String palavra;
        No2 esq, dir;
    }

    int contarPala(char primeiro, char ultimo) {

        No2 i2 = new No2();
        No i = new No();
        i = ArvoreArvore.raiz;

        i2 = pesqArv1(primeiro, i);

        int contador = 0;

        return pesqArv2(i2, contador, ultimo);

    }

    No2 pesqArv1(char primeiro, No i) {

        if (i.letra == primeiro) {
            return i.raiz;
        } else if (i.letra > primeiro) {
            pesqArv1(primeiro, i.dir);

        } else {
            pesqArv1(primeiro, i.esq);
        }

        return null;
    }

    int pesqArv2(No2 i2, int contador, char ultimo) {

        pesqArv2(i2.esq, contador, ultimo);
        pesqArv2(i2.dir, contador, ultimo);
        if (i2.palavra.charAt(i2.palavra.length() - 1) == ultimo) {

            contador++;

        }

        return contador;
    }

}
