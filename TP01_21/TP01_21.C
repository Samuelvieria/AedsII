#include <stdio.h>
#include <string.h>

void inverterRecursivo(char *palavra, int inicio, int fim) {
    if (inicio >= fim) {
        return;
    }
    
    char temp = palavra[inicio];
    palavra[inicio] = palavra[fim];
    palavra[fim] = temp;
    
    inverterRecursivo(palavra, inicio + 1, fim - 1);
}

int main() {
    char palavra[100];
    
    while (1) {
        scanf("%s", palavra);
        
        if (strcmp(palavra, "FIM") == 0) {
            break;
        }
        
        int tamanho = strlen(palavra);
        inverterRecursivo(palavra, 0, tamanho - 1);
        printf("%s\n", palavra);
    }
    
    return 0;
}