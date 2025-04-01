#include <stdio.h>

void Disco()
{

    int n;
    int d;
    int p;
    int resp;

    scanf("%d", &n);
    scanf("%d", &d);
    scanf("%d", &p);

    if (p > d)
    {
        resp = (n-p)+d;
    }
    else
    {
        resp = d - p ;
    }

    printf("%d", resp);
}

int main()
{
    Disco();

    return 0;
}
