#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>

#define max_size 100

void contpalavraRec(char palavra[], int i, int num) {
    if (palavra[i] == '\0') {
        // Caso base: quando a string termina, imprime o número de maiúsculas.
        printf("%d\n", num);
        return;
    }
    // Se o caractere for uma letra maiúscula, incrementa o contador.
    if (palavra[i] >= 'A' && palavra[i] <= 'Z') {
        num++;
    }
    // Chama recursivamente para o próximo caractere.
    contpalavraRec(palavra, i + 1, num);
}

bool conferirFim(char palavra[]) {
    return strcmp(palavra, "FIM") == 0;
}

int main() {
    char palavra[max_size];
    int num = 0;

    while (true) {
        fgets(palavra, sizeof(palavra), stdin);
        palavra[strcspn(palavra, "\n")] = 0; // Remove o '\n' no final da string

        if (conferirFim(palavra)) {
            break; // Encerra o programa se a palavra for "FIM"
        }

        num = 0; // Reset do contador de maiúsculas
        contpalavraRec(palavra, 0, num);
    }

    return 0;
}
