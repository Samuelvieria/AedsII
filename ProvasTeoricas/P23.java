


public class P23 {


void selecao() {
Celula i = ultimo;

for (i ; i != primeiro; i = i.ant) {
Celula maior = i;

for (Celula j = i; j != null; j = j.ant) { // o correto seria j = i.ant
if (j.elemento > maior.elemento) {
maior = j; // foi aq onde eu errei colocando ( celula maior = celula i)
}
}

//Vou fazer a representação visual que eu fiz na prova dessa parte embaixo

celula tmp = maior.prox;
maior.ant.prox = tmp;
maior.prox.ant = maior.ant;


maior.ant = null;
maior.prox = null;

inserirAntesDeOrdenado(maior); // Insere o nó 'maior' imediatamente antes de 'ultimoOrdenado' na lista duplamente encadeada.

i = i.ant;
}
}
maior
[ ] <-->[ ]<-->[ ]

maior tmp
celula tmp = maior.prox - - - - [ ] <--> [ ]<--> [ ]

maior tmp
maior.ant.prox = tmp - - - - - [ ]< - - [ ] - ->[ ]
| ↑
|_____________ |

_____________
↓ m |tmp
maior.prox.ant = maior.ant - - - [ ]< - - [ ] - -> [ ]
| ↑
|_____________|

maior.prox =null m
maior. ant = null. null--[ ]--null

    
}
