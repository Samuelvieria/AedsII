/*
#include <stdio.h>
#include <math.h>

void moedas()

{

    

576.73/100 resto da / = 76.73
76.73/50 resto da / = 26.73
26.7/20 resto da / = 6.73
6,73/10 resto da / = 0
6,73/5 resto da / = 0.73
0,73/1 resto da / = 0
0,73/50 resto da / = 0.20
0,23/0,25 resto da / = 0
0,23/10 resto da / =0.3
0,03/1 resto da / =0




    float valor;
    scanf("%f", &valor);

    int resp[11];

    int num = valor; // a questo era passar de float para int. Pq se fosse apenas isso eu poderia separar os valores inteiros(notas) dos centavos
    float moedas = valor - num;

    // Notas
    resp[0] = % (num / 100);
    num = resp[0] - num;
    resp[1] = % (num / 50);
    num = resp[1] - num;
    resp[2] = % (num / 20);
    num = resp[2] - num;
    resp[3] = % (num / 10);
    num = resp[3] - num;
    resp[4] = % (num / 5);
    num = resp[4] - num;
    resp[5] = % (num / 2);
    num = resp[5] - num;
    resp[6] = % (num / 1);

    // Moedas
    resp[7] = % (moedas / 0.50);
    moedas = moedas - resp[7];
    resp[8] = % (moedas / 0.25);
    moedas = moedas - resp[8];
    resp[9] = % (moedas / 0.10);
    moedas = moedas - resp[9];
    resp[10] = % (moedas / 0.5);
    moedas = moedas - resp[10];
    resp[11] = % (moedas / 0.1);
    moedas = moedas - resp[1];

    for (int i = 0; i < 11; i++)
    {

        printf("%f", resp[i]);
    }
}

int main()
{

    moedas();
}



*/
#include <stdio.h>
#include <math.h>

void moedas() {
    float valor;
    scanf("%f", &valor);

    int notas[] = {100, 50, 20, 10, 5, 2, 1};
    float moedas_val[] = {0.50, 0.25, 0.10, 0.05, 0.01};

    int qtd_notas[7] = {0};
    int qtd_moedas[5] = {0};

    int parte_inteira = (int)valor;
    int parte_centavos = round((valor - parte_inteira) * 100);  // evita erro de precisão com float

    // Calculando as notas
    for (int i = 0; i < 6; i++) {
        qtd_notas[i] = parte_inteira / notas[i];
        parte_inteira %= notas[i];
    }

    qtd_notas[6] = parte_inteira; // restante são moedas de 1 real

    // Calculando as moedas
    int moedas_centavos[] = {50, 25, 10, 5, 1};
    for (int i = 0; i < 5; i++) {
        qtd_moedas[i] = parte_centavos / moedas_centavos[i];
        parte_centavos %= moedas_centavos[i];
    }

    // Imprimindo resultados
    printf("NOTAS:\n");
    for (int i = 0; i < 6; i++) {
        printf("%d nota(s) de R$ %d.00\n", qtd_notas[i], notas[i]);
    }

    printf("MOEDAS:\n");
    printf("%d moeda(s) de R$ 1.00\n", qtd_notas[6]);
    for (int i = 0; i < 5; i++) {
        printf("%d moeda(s) de R$ %.2f\n", qtd_moedas[i], moedas_val[i]);
    }
}

int main() {
    moedas();
    return 0;
}
