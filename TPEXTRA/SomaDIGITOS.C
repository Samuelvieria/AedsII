
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int somaDigitos(int n) {
    if (n == 0) {
        return 0;
    }
    return (n % 10) + somaDigitos(n / 10);
}

int main() {
    char entrada[100];
    while (scanf("%s", entrada) != EOF) {
        if (strcmp(entrada, "FIM") == 0) {
            break;
        }
        int numero = atoi(entrada);
        printf("%d\n", somaDigitos(numero));
    }
    return 0;
}
/*

int somaNum(int numero){

    int qnt = 0;
    int num[10];
    int soma = 0;

   

    while (numero > 0)
    {
        num[qnt] = numero % 10;
        numero = numero / 10;
        qnt++;
    }

    for (int i = 0; i < qnt; i++)
    {

        soma = soma + num[i];
    }




    return soma;
}


int main()
{
    int numero;
 

   while( scanf("%d", &numero) != 'FIM'){

   int soma = somaNum(numero);

    printf("%d", soma);
   }
}
*/

