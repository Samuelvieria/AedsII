public class Lista {
    
    private int[] array;
    private int n;

// Construtor iniciando a lista com tamanho 10
    public Lista() {
        this(10);
    }

    public Lista(int tamanho) { 
        array = new int[tamanho];
        n = 0; // Contador pra saber quantos itens tem na lista
    }

    public void InserirInicio(int x) throws Exception {
        
        if (n >= array.length) {
            throw new Exception("Erro: Lista cheia"); 
        }

        // Move para o final do array
        for (int i = n; i > 0; i--) {
    array[i] = array[i - 1]; 
        }
        
        array[0] = x;
        n++;
    }

    public void InserirFim(int x) throws Exception {

        if (n >= array.length) {
            throw new Exception("ERRO");
        }

        // Só acrescenta se tiver espaço no array
        array[n] = x;
        n++;
    }

public void InserirPos(int x, int pos) throws Exception { 

        // Vê se tem espaço e se a posição é maior que zero e menor que o fim do array
        if (n >= array.length || pos < 0 || pos > n) { 
            throw new Exception("ERRO");
        }

        // Leva os elementos para o fim do array exatamente igual a outra porém a partir da posição
        for (int i = n; i > pos; i--) {
            array[i] = array[i - 1]; 
        }

        // Decorar esse padrão de for para inserção 
        array[pos] = x;
        n++;
    }

    public int RemocaoInicio() throws Exception {  

        if (n == 0) {
            throw new Exception("Lista vazia");
        }

        int resp = array[0]; // Por algum motivo fizeram pra retornar o número removido
        n--;

        // Esse aqui movimenta pra esquerda
        for (int i = 0; i < n; i++) {
            array[i] = array[i + 1]; 
        }

        return resp;
    }

    public int removerFim() throws Exception { 

        if (n == 0) {
            throw new Exception("Lista vazia"); 
        }

        return array[--n]; 
    } // Só tirar no final 

    public int RemoverPosicao(int pos) throws Exception { 

        if (pos < 0 || pos >= n) { 
            throw new Exception("Erro");
        }

        int resp = array[pos];
        n--;

        // For ++ pra esquerda, -- pra direita
        for (int i = pos; i < n; i++) { 
            array[i] = array[i + 1]; 
        }
        return resp;
    }

    public void mostrar() {
        System.out.print("[ ");
        for (int i = 0; i < n; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println("]");
    }

    public boolean pesquisar(int x) {
        boolean retorno = false;
        for (int i = 0; i < n && retorno == false; i++) {
            retorno = (array[i] == x);
        }
        return retorno;
    }
    public boolean isPalindromo() {
        for (int i = 0; i < n / 2; i++) {
            if (array[i] != array[n - 1 - i]) {
                return false;
            }
        }
        return true;

        //n é quntidade de elementos n tamanho do array 
    }

    public static void main(String[] args) {
        
    }
}
