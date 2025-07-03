import P3.Agenda.No;

public class Doidonap4 {

  boolean buscarVeiculo (Veiculo veiculo){

  boolean esta = false; // seria correto usar porém só retornei true ou false 

  if (hashT1(veiculo.placa) == veiculo.placa){ 
    /*
    
    está errado!

    int pos = t1.hashT1(veiculo.placa);
    if (t1.tabela[pos] != null && t1.tabela[pos].placa.equals(veiculo.placa))

*/
    esta = true;

    return esta;
  }
  else if (veiculo.tipo == "carro"){ //veiculo.tipo.equals("carro")

    int pos = t2.hashT2(veiculo.chassi);
    No i = t2.raizes[pos];
    char P1 = veiculo.placa.charAt(0);
    return buscaArvore(char P1, No i, Veiculo veiculo);
  }
  else if (veiculo.tipo == "moto"){
    int pos2 = t3.hashT3(veiculo.chassi);
    if (t3.tabela[pos2] == veiculo){
      return true;
    }
    else {
      while (inicio.prox != null){
        if (inicio.veiculo == veiculo){
          return true;
        }
      }
      return false;
    }
  }
}

    boolean busca(char P1, No i) {
        if (i.letra == P1) {

        return PesquisaLista(inicio, fim, veiculo);
        
        } else if (i.letra > P1) {
            busca(P1, no1.esq);
        } else if (i.letra < P1) {
            busca(P1, no1.dir);
        }
    }

    // Por n ter tempo nem espaço só indiquei a exeistencia dessa funcao, mas praticamente é so uma pesquisa boolean em lista
    boolean pesquisaLista(CelulaLista inicio, CelulaLista fim, Veiculo veiculo) {
        boolean resp = false;

        for (CelulaLista atual = inicio; atual != null && !resp; atual = atual.prox) {
            if (atual.veiculo.placa.equals(placa)) {
                resp = true;
            }
        }

        return resp;
    }

}
