#include <stdio.h> 
#include <stdlib.h>
#include <stdbool.h>

void PesqBinaria(int arry[], int tam){
    int inicio = 0;
    int fim = tam - 1;
    int meio;
    int chave = 5;
    bool temOUn = false;

    while (inicio <= fim) {
        meio = (inicio + fim) / 2;

        if (arry[meio] == chave) { 
            temOUn = true;
            break;  
        } else if (arry[meio] > chave) {
            fim = meio - 1;  
        } else {
            inicio = meio + 1;  
        }
    }

    
    if (temOUn) {
        printf("tem\n");
    } else {
        printf("ntem\n");
    }
}

int main(){
    int array[12] = {1,2,3,4,5,6,7,8,9,10,11,12};
    int tam = 12;

    PesqBinaria(array, tam);

    return 0;
}
