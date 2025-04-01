#include <stdio.h>
#include <string.h>

int somaRecursiva(char *numChar, int i) {
    if (i == strlen(numChar)) {
        return 0;
    }
    
    int numero = numChar[i] - '0';
    return numero + somaRecursiva(numChar, i + 1);
}

void soma(char *num) {
    int somaN = somaRecursiva(num, 0);
    printf("%d\n", somaN);
}

int main() {
    char palavra[100];
    
    while (1) {
        scanf("%s", palavra);
        
        if (strcmp(palavra, "FIM") == 0) {
            break;
        }
        
        soma(palavra);
    }
    
    return 0;
}
