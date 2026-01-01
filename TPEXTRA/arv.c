#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define FILE_PATH "/tmp/games.csv"

// ------------------------------------------------------------
// ---------------------- STRUCT DATA --------------------------
// ------------------------------------------------------------
typedef struct {
    int dia, mes, ano;
} Data;

// ------------------------------------------------------------
// ---------------------- STRUCT GAMES -------------------------
// ------------------------------------------------------------
typedef struct {
    int id;
    char nome[300];
    Data data;
    int jogadores;
    float preco;
} Games;

// CSV armazenado na memória
Games catalogo[15000];
int totalCatalogo = 0;

// ------------------------------------------------------------
// ------------------- Função: Parse da DATA ------------------
// ------------------------------------------------------------
Data parseData(char *s) {
    Data d = {1,1,1900};
    if (strlen(s)==0) return d;

    char meses[12][4] = {"Jan","Feb","Mar","Apr","May","Jun",
                         "Jul","Aug","Sep","Oct","Nov","Dec"};

    int dia=1, mes=1, ano=1900;

    if (strchr(s, ',')) {
        char mesDia[20], anoStr[10];
        sscanf(s, "%[^,], %s", mesDia, anoStr);
        ano = atoi(anoStr);

        char m[5]; int dd;
        sscanf(mesDia, "%s %d", m, &dd);
        dia = dd;

        for (int i=0;i<12;i++)
            if (strcasecmp(m, meses[i])==0) mes = i+1;

    } else if (strchr(s,' ')) {
        char m[5]; int yy;
        sscanf(s, "%s %d", m, &yy);
        ano = yy;

        for (int i=0;i<12;i++)
            if (strcasecmp(m, meses[i])==0) mes = i+1;

    } else ano = atoi(s);

    d.dia = dia;
    d.mes = mes;
    d.ano = ano;
    return d;
}

// ------------------------------------------------------------
// ------------------- LEITURA DO CSV -------------------------
// ------------------------------------------------------------
void lerCSV() {
    FILE *f = fopen(FILE_PATH, "r");
    if (!f) {
        printf("Erro abrindo CSV.\n");
        exit(1);
    }

    char linha[2000];
    fgets(linha, 2000, f); // pular cabeçalho

    while (fgets(linha, 2000, f)) {
        char *campos[15];
        int qtd = 0;
        char *p = strtok(linha, ",");
        while (p && qtd < 15) {
            campos[qtd++] = p;
            p = strtok(NULL, ",");
        }

        Games g;
        g.id = atoi(campos[0]);
        strcpy(g.nome, campos[1]);
        campos[2][strcspn(campos[2], "\n")] = 0;

        g.data = parseData(campos[2]);
        g.jogadores = atoi(campos[3]);
        g.preco = atof(campos[4]);

        catalogo[totalCatalogo++] = g;
    }

    fclose(f);
}

// Busca sequencial por id
Games* pesquisaCatalogo(int id) {
    for (int i=0;i<totalCatalogo;i++)
        if (catalogo[i].id == id)
            return &catalogo[i];
    return NULL;
}

// ------------------------------------------------------------
// ------------------ ÁRVORE ALVINEGRA ------------------------
// ------------------------------------------------------------
typedef enum {VERMELHO, PRETO} Cor;

typedef struct No {
    Games *jogo;
    struct No *esq, *dir;
    Cor cor;
} No;

No *raiz = NULL;
long comparacoes = 0;

// ----------- Funções auxiliares -----------------
Cor cor(No *n) { return n == NULL ? PRETO : n->cor; }

void setCor(No *n, Cor c) { if (n) n->cor = c; }

// ----------- Rotações ----------------------------
No* rotacaoEsq(No *n) {
    No *x = n->dir;
    n->dir = x->esq;
    x->esq = n;
    x->cor = n->cor;
    n->cor = VERMELHO;
    return x;
}

No* rotacaoDir(No *n) {
    No *x = n->esq;
    n->esq = x->dir;
    x->dir = n;
    x->cor = n->cor;
    n->cor = VERMELHO;
    return x;
}

void trocarCor(No *n) {
    setCor(n, VERMELHO);
    if (n->esq) setCor(n->esq, PRETO);
    if (n->dir) setCor(n->dir, PRETO);
}

// ----------- Inserção (rec) -----------------------
No* inserirRec(Games *g, No *n) {
    if (n == NULL) {
        No *novo = malloc(sizeof(No));
        novo->jogo = g;
        novo->esq = novo->dir = NULL;
        novo->cor = VERMELHO;
        return novo;
    }

    if (strcasecmp(g->nome, n->jogo->nome) < 0)
        n->esq = inserirRec(g, n->esq);
    else if (strcasecmp(g->nome, n->jogo->nome) > 0)
        n->dir = inserirRec(g, n->dir);

    if (cor(n->dir) == VERMELHO && cor(n->esq) == PRETO)
        n = rotacaoEsq(n);

    if (cor(n->esq) == VERMELHO && cor(n->esq->esq) == VERMELHO)
        n = rotacaoDir(n);

    if (cor(n->esq) == VERMELHO && cor(n->dir) == VERMELHO)
        trocarCor(n);

    return n;
}

void inserir(Games *g) {
    raiz = inserirRec(g, raiz);
    raiz->cor = PRETO;
}

// ------------------------------------------------------------
// ----------------------- PESQUISA ---------------------------
// ------------------------------------------------------------
int pesquisarRec(char *nome, No *n) {
    if (n == NULL) {
        printf("NAO\n");
        comparacoes++;
        return 0;
    }

    comparacoes++;
    int cmp = strcasecmp(nome, n->jogo->nome);

    if (cmp == 0) {
        printf("SIM\n");
        return 1;
    }

    if (cmp < 0) {
        printf("esq ");
        return pesquisarRec(nome, n->esq);
    } else {
        printf("dir ");
        return pesquisarRec(nome, n->dir);
    }
}

void pesquisar(char *nome) {
    printf("%s raiz ", nome);
    pesquisarRec(nome, raiz);
}

// ------------------------------------------------------------
// ------------------------------ MAIN ------------------------
// ------------------------------------------------------------
int main() {
    lerCSV();

    char entrada[300];

    // Inserção de IDs
    while (1) {
        fgets(entrada, 300, stdin);
        entrada[strcspn(entrada, "\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;

        int id = atoi(entrada);
        Games *g = pesquisaCatalogo(id);
        if (g) inserir(g);
    }

    // Pesquisa por nome
    while (1) {
        fgets(entrada, 300, stdin);
        entrada[strcspn(entrada, "\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;
        pesquisar(entrada);
    }

    // LOG
    FILE *logf = fopen("800772_arvoreAlvinegra.txt", "w");
    fprintf(logf, "800772\t%ld\n", comparacoes);
    fclose(logf);

    return 0;
}
