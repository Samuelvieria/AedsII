
#include <stdio.h>

int main(){
    FILE *file;
    char texto[250];
    int n;

    scanf("%i",n);

    // Abre o arquivo para escrita (cria se não existir, sobrescreve se existir)
    file = fopen("/Users/samuelalves/AedsII/TP01_15/arq.txt", "w");

    if (file == NULL) {
        printf("Erro ao abrir o arquivo!\n");
        return 1; 
    }


    while (1) {
        fgets(texto, sizeof(texto), stdin); // Lê a entrada do usuário

        // Se o usuário digitar "sair", interrompe o loop
        if (strncmp(texto, "sair", 4) == 0)
            break;

        fprintf(file, "%s", texto); // Escreve no arquivo
    }

    fclose(file); // Fecha o arquivo

    printf("Texto salvo em arq.txt!\n");

    return 0;
}
