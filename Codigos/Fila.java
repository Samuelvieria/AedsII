

public class Fila {
    private int array[];
    private int primeiro;
    private int ultimo;

    public Fila(){
        this(10);
    }

    public Fila(int tamanho){
        array = new int[tamanho+1];
        primeiro = ultimo = 0;
    }

    public void Inserir(int x) throws Exception{


      if(((ultimo+1)% array.length) == primeiro){//Essa parte aq faz o modulo da ultima posiçao (extra) que por algum motivo dá a primeira posiçao ( 0 ou 1)

        throw new Exception("FILA CHEIA");

      }

      array[ultimo] = x;

      ultimo = (ultimo +1) % array.length;// esse modulo garante sempre que n vai passar a posiçao, se o ultimo for tres o modulo do proximo é 4

    }

    public int Remover()throws Exception{

        if(ultimo == primeiro){

            throw new Exception("fila vazia");
        }

        int resp = array[primeiro];
        primeiro = (primeiro+1) % array.length;// passa o titulo de primeiro pro proximo.(Fazer tudo normal e colocar esse modulo pra n dar merda)

        return resp;

    }
  //Lembrar que Fila é cheio de porra de modolu  

  public void mostrar() {
    System.out.print("[ ");

    int i = primeiro;
    while (i != ultimo) { // Enquanto não chegar no final da fila
        System.out.print(array[i] + " ");
        i = (i + 1) % array.length; // Avança para o próximo elemento
    }

    System.out.println("]");
}
// verçao mais simples

/*public void mostrar (){
      System.out.print("[ ");

      for(int i = primeiro; i != ultimo; i = ((i + 1) % array.length)) {
         System.out.print(array[i] + " ");
      }

      System.out.println("]");
   } 
   que merda é essa q o max fez em pqp   
   
   */

   public void mostrarRec(){
    System.out.print("[ ");
    mostrarRec(primeiro);
    System.out.println("]");
 }

 public void mostrarRec(int i){
    if(i != ultimo){
       System.out.print(array[i] + " ");
       mostrarRec((i + 1) % array.length);
    }//n vai cair eu acho


    }


    


    public static void main(String[] args) {
        
    }

    
}
