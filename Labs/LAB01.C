#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>

#define max_size 100
void contPalavras(char palavra[]){
    int num = 0;
    int tamanho =strlen(palavra);

    for(int i =0; i<tamanho; i++){

        if(isupper(palavra[i])){
            num++;
        }
    }
 printf("%d", num);
}

bool conferirFim(char palavra[]){

    if(strcmp(palavra,"FIM") == 0){
        return true;
    }else{
        return false;
    }
}


int main(){

    char palavra[max_size];


    while(conferirFim(palavra) != true){

        fgets(palavra,sizeof(palavra),stdin);

        palavra[strcspn(palavra, "\n")] = 0;


        contPalavras(palavra);

    }

    return 0;

}