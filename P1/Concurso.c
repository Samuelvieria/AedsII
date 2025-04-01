#include <stdio.h>


void Conc()
{
    int n;
    int k;

    scanf("%d", &n);
    scanf("%d", &k);

    int candi[n];

    for (int i = 0; i < n; i++)
    {
        scanf("%d", &candi[i]);
    }

    int temp;

    for (int k = 0; k < n - 1; k++)
    {
        for (int j = 0; j < n - k - 1; j++)
        {
            if (candi[j] > candi[j + 1])
            {
                temp = candi[j];
                candi[j] = candi[j + 1];
                candi[j + 1] = temp;
            }
        }
    }


    printf("%d",candi[k]);



}

int main()
{

    Conc();
}