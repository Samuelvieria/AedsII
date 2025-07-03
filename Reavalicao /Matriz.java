

public class Matriz {
    CelulaMatriz inicio;
    int linhas, coluna;

   
    CelulaDupla diagunificada(){

        CelulaMatriz i = new CelulaMatriz();
        i = inicio;
        Celula j = new  Celula();
       
        CelulaDupla inicioDuplo = new CelulaDupla();
     
       CelulaDupla fimDuplo = new CelulaDupla(); ;
        
        fimDuplo  = inicioDuplo;

    

        for(; i.inf != null && i.dir != null; i=i.dir.inf){

            j = i.inicio.prox;

            while(j != null){
               fimDuplo = inserirCelulaDupla(j.elemento,fimDuplo);
                j = j.prox;
            }
        }

        return inicioDuplo;
    }
    CelulaDupla inserirCelulaDupla(int ele , CelulaDupla fimDuplo){

    CelulaDupla tmp = new CelulaDupla();
    fimDuplo.prox = tmp;
    tmp.ant = fimDuplo;
    tmp.elemento = ele;
    fimDuplo= fimDuplo.prox;

    return fimDuplo;

     }


}

class CelulaMatriz {
    CelulaMatriz esq, dir, inf, sup;
    Celula inicio, fim;
}

class Celula {
    int elemento;
    Celula prox;
}

class CelulaDupla {
    int elemento;
    CelulaDupla prox, ant;
}
