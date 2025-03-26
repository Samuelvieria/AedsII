#include <stdio.h>

void Cartinhas()
{

    int n;
    int c;
    int m;
    

    scanf("%d", &n);
    scanf("%d", &c);
    scanf("%d", &m);

    int numerocolaods = c;

    int cnc[c];

    for (int x = 0; x < c; x++)
    {
        scanf("%d", &cnc[x]);
    }

    int cn[m];

    for (int w = 0; w < m; w++)
    {
        scanf("%d", &cn[w]);
    }

    for (int i = 0; i < c; i++)
    {
        for (int q = 0; q < m; q++)
        {

            if (cn[q] == cnc[i])
            {
                numerocolaods--;
            }
        }
    }

   

    printf("%d", numerocolaods);
}

int main()
{

    Cartinhas();

    return 0;
}