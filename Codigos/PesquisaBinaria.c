#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>

int main() {
    // Inicializa o gerador de números aleatórios com a semente baseada no tempo atual
    srand(time(NULL));

    int tamanho;
    bool temoun = false;

    // Gera um número aleatório entre 1 e 10
    int numpesq = (rand() % 10) + 1;
    printf("Número a ser pesquisado: %d\n", numpesq);

    // Lê o tamanho do array
    printf("Digite o tamanho do array: ");
    scanf("%d", &tamanho);

    // Verifica se o tamanho é válido
    if (tamanho <= 0) {
        printf("Tamanho inválido!\n");
        return 1;  // Encerra o programa caso o tamanho seja inválido
    }

    // Aloca dinamicamente o array
    int* array = (int*)malloc(tamanho * sizeof(int));
    if (array == NULL) {
        printf("Erro na alocação de memória!\n");
        return 1;  // Encerra o programa caso haja falha na alocação de memória
    }

    // Preenche o array com números aleatórios entre 1 e 10
    for (int i = 0; i < tamanho; i++) {
        array[i] = (rand() % 10) + 1;
    }

    // Exibe o array gerado
    printf("Array gerado: ");
    for (int i = 0; i < tamanho; i++) {
        printf("%d ", array[i]);
    }
    printf("\n");

    // Verifica se o número aleatório gerado está no array
    for (int j = 0; j < tamanho; j++) {
        if (array[j] == numpesq) {
            temoun = true;
            break;  // Não há necessidade de continuar a busca após encontrar o número
        }
    }

    // Exibe o resultado da pesquisa
    if (temoun) {
        printf("Tem\n");
    } else {
        printf("Não tem\n");
    }

    // Libera a memória alocada
    free(array);

    return 0;
}

/*
int main(){

 srand(time(NULL));

    int tamanho;
    bool temoun=false;
    int numpesq = (rand() % 10) + 1;
    scanf("%d", &tamanho);

    int array[tamanho];

//colocando numeros alt no array
    for(int i =0; i<tamanho;  i++){
        array[i] = (rand() % 10) + 1;
    }

    for (int j = 0; j < tamanho; j++){
        if(array[j]==numpesq){
            temoun = true;
        }
    }

    if(temoun == true){
        printf("tem");
      }else{

        printf("Ntem");
    }

return 0;
};
*/