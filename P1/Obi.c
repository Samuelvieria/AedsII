#include <stdio.h>
#include <string.h>

void Placas()
{

    char placa[10];
    int i = 0;

    scanf("%9s", placa);

    int tam = strlen(placa);

    if (tam == 8)
    {
        printf("1");
    }
    else if (tam == 7)
    {
        printf("2");
    }
    else
    {
        printf("0");
    }
}

int main()
{
    Placas();

    return 0;
}