#include <stdio.h>
#include <string.h>
#include <ctype.h>

void placaObi()
{

    char placa[10];
    int i = 0;
    

    while (scanf("%c", &placa[i]) != EOF)
    {
        if (placa[i] == '\n')
            break;
        i++;
    }

    int tam = strlen(placa);

    if (tam == 8)
    {
        int num;
        for(int j = 0 ; j< tam ; j++){
            if(isnumber(placa[j])){
                num ++;
            }

        }
    }
    if(tam == 7)
    {

    }
}

int main()
{

    placaObi();

    return 0;
}