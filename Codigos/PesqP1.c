# include <stdio.h>
# include <string.h>


void printArray(int array[], int size) {
    for (int i = 0; i < size; i++) {
        printf("%d ", array[i]);
    }
    printf("\n");
}



void selectionSort(int array[],int tam){

   for(int i = 0; i<tam-1; i++){

    int min = i;

    for(int j = i+1 ; j<tam ; j++){

         if(array[j]<array[min]){
      
         min = j;
         
    }

   }

   if(min != i){
      
      int temp = array[i];
      array[i] = array[min];
      array[min] = temp;
    
    }

   }

   printArray(array,tam);
}


int main(){

int array[10] = {5,4,6,765,32,5,43,2,1,4};

int tam = sizeof(array)/sizeof(array[0]);

selectionSort(array,tam);


    return 0;
}