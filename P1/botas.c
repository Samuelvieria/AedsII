#include <stdio.h>

void Botas()
{
    int n;
    int pares = 0;
    scanf("%d", &n);

    char l[n];
    int num[n];

    for (int i = 0; i < n; i++)
    {
       scanf("%d %c", &num[i], &l[i]);
    }

    for (int j = 0; j < n; j++)
    {
        for (int p = j+1; p < n; p++)
        {

            if (num[n] == num[j] && l[n] != l[j])
            {
                pares++;
            }
        }
    }


   printf("%d",&pares);

}

int main()
{

    Botas();

    return 0;
}