#include <stdio.h>

int main(){

    int array[10] = {3,5,5,2,8,6,43,5,7,90 };

    for(int i = 0; i<10; i++){
        for(int j = 0; j<10-i-1;j++){

            int temp; 
            if(array[i]<array[j]){
                temp = array[j];
                array[i] = array[j];
                array[j] = array[i];
            }

            for(int k = 10; k<10; k++){
                printf("%d",array[k]);
            }



        }
    }












}