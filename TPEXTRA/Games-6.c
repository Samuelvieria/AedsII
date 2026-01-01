#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_LINE 16384

/*
                   *
                  /_\
                 /___\
                /_____\
               /_______\
              /_________\
             /___________\
            /_____________\
           /_______________\
          /_________________\
                 |||
                 |||
              FELIZ NATAL!
*/



/* ===================== STRUCTS DO PROJETO ===================== */

typedef struct {
    int dia, mes, ano;
} Data;

typedef struct {
    int id;
    char *name;
    Data date;
    int owners;
    float price;
    char **languages; int languages_count;
    int metacritic;
    float userScore;
    int achievements;
    char **publishers; int publishers_count;
    char **developers; int developers_count;
    char **categories; int categories_count;
    char **genres; int genres_count;
    char **tags; int tags_count;
} Game;

/* ===================== FUNÇÕES BÁSICAS ===================== */

static char *strdup_safe(const char *s) {
    if (!s) return NULL;
    char *d = malloc(strlen(s) + 1);
    strcpy(d, s);
    return d;
}

static void trim(char *s) {
    if (!s) return;
    while (*s == ' ' || *s == '\t') s++;
    int len = strlen(s);
    while (len > 0 && (s[len-1]==' ' || s[len-1]=='\t' || s[len-1]=='\r' || s[len-1]=='\n'))
        s[--len] = '\0';
}

static void free_string_array(char **arr, int n) {
    if (!arr) return;
    for (int i = 0; i < n; i++) free(arr[i]);
    free(arr);
}

static void free_game(Game *g) {
    free(g->name);
    free_string_array(g->languages, g->languages_count);
    free_string_array(g->publishers, g->publishers_count);
    free_string_array(g->developers, g->developers_count);
    free_string_array(g->categories, g->categories_count);
    free_string_array(g->genres, g->genres_count);
    free_string_array(g->tags, g->tags_count);
}

/* ===================== PARSING CSV ===================== */

static char **split_tokens(char *input, int *out_count) {
    *out_count = 0;
    if (!input || strlen(input) == 0) return NULL;

    char tmp[MAX_LINE];
    strcpy(tmp, input);
    for (int i=0; tmp[i]; i++)
        if (tmp[i]=='[' || tmp[i]==']' || tmp[i]=='\'') tmp[i]=' ';

    char *token = strtok(tmp, ",");
    while (token) {
        (*out_count)++;
        token = strtok(NULL,",");
    }

    if (*out_count == 0) return NULL;

    strcpy(tmp, input);
    for (int i=0; tmp[i]; i++)
        if (tmp[i]=='[' || tmp[i]==']' || tmp[i]=='\'') tmp[i]=' ';

    char **arr = malloc(sizeof(char*) * (*out_count));
    int idx = 0;

    token = strtok(tmp, ",");
    while (token) {
        trim(token);
        arr[idx++] = strdup_safe(token);
        token = strtok(NULL,",");
    }

    return arr;
}

static void split_csv_line(const char *line, char fields[][MAX_LINE], int *count) {
    int len = strlen(line);
    int fi=0, ci=0, inq=0;
    char cur[MAX_LINE];

    for (int i=0; i<=len; i++) {
        char c = (i == len ? '\0' : line[i]);
        if (c == '"') inq = !inq;
        else if ((c == ',' || c == '\0') && !inq) {
            cur[ci] = '\0';
            strcpy(fields[fi++], cur);
            ci = 0;
        } else cur[ci++] = c;
    }

    *count = fi;
}

static Data parse_date(const char *field) {
    Data d = {1,1,1900};
    if (!field) return d;

    char tmp[64];
    strcpy(tmp, field);

    trim(tmp);

    const char *months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    char mon[8];
    int day, year;

    if (sscanf(tmp,"%3s %d, %d",mon,&day,&year)==3) {
        for (int i=0;i<12;i++)
            if (strncmp(mon,months[i],3)==0) d.mes = i+1;
        d.dia=day; d.ano=year;
    }

    return d;
}

static Game parse_game(const char *line) {
    Game g;
    memset(&g, 0, sizeof(Game));

    char fields[14][MAX_LINE];
    int count = 0;

    split_csv_line(line, fields, &count);

    g.id = atoi(fields[0]);
    g.name = strdup_safe(fields[1]);
    g.date = parse_date(fields[2]);
    g.price = atof(fields[4]);

    g.languages = split_tokens(fields[5], &g.languages_count);
    g.publishers = split_tokens(fields[9], &g.publishers_count);
    g.developers = split_tokens(fields[10], &g.developers_count);
    g.categories = split_tokens(fields[11], &g.categories_count);
    g.genres = split_tokens(fields[12], &g.genres_count);
    g.tags = split_tokens(fields[13], &g.tags_count);

    return g;
}

static Game *load_catalog(const char *path, int *out_n) {
    FILE *f = fopen(path, "r");
    if (!f) { perror("CSV"); exit(1); }

    char line[MAX_LINE];
    fgets(line, sizeof(line), f);

    int cap = 1024, n = 0;
    Game *arr = malloc(sizeof(Game)*cap);

    while (fgets(line, sizeof(line), f)) {
        line[strcspn(line,"\r\n")] = '\0';
        if (n == cap) {
            cap *= 2;
            arr = realloc(arr, sizeof(Game)*cap);
        }
        arr[n++] = parse_game(line);
    }

    fclose(f);
    *out_n = n;
    return arr;
}

static Game *find_by_id(Game *catalog, int n, int id) {
    for (int i = 0; i < n; i++)
        if (catalog[i].id == id) return &catalog[i];
    return NULL;
}

/* ===================== HASH INDIRETA (LISTA SIMPLES) ===================== */

typedef struct HNode {
    char *name;
    struct HNode *next;
} HNode;

typedef struct {
    HNode *bucket[21];
} HashIndireta;

static void hash_init(HashIndireta *h) {
    for (int i=0; i<21; i++) h->bucket[i] = NULL;
}

static int hash_ascii(const char *s) {
    int soma=0;
    for (int i=0; s[i]; i++) soma += (unsigned char)s[i];
    return soma % 21;
}

static void hash_insert(HashIndireta *h, const char *name) {
    int pos = hash_ascii(name);
    HNode *n = malloc(sizeof(HNode));
    n->name = strdup_safe(name);
    n->next = h->bucket[pos];
    h->bucket[pos] = n;
}

static int hash_search(HashIndireta *h, const char *name) {
    int pos = hash_ascii(name);
    HNode *cur = h->bucket[pos];

    while (cur) {
        if (strcmp(cur->name, name) == 0) return pos;
        cur = cur->next;
    }
    return -1;
}

static void hash_free(HashIndireta *h) {
    for (int i = 0; i < 21; i++) {
        HNode *cur = h->bucket[i];
        while (cur) {
            HNode *tmp = cur;
            cur = cur->next;
            free(tmp->name);
            free(tmp);
        }
    }
}

/* ===================== MAIN — QUESTÃO 3 COMPLETA ===================== */

int main() {

    int catalog_n = 0;
    Game *catalog = load_catalog("/tmp/games.csv", &catalog_n);

    HashIndireta hash;
    hash_init(&hash);

    char entrada[2048];

    /* -------- INSERÇÃO POR ID -------- */
    while (fgets(entrada, sizeof(entrada), stdin)) {
        entrada[strcspn(entrada,"\r\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;

        int id = atoi(entrada);
        Game *g = find_by_id(catalog, catalog_n, id);
        if (g) hash_insert(&hash, g->name);
    }

    FILE *log = fopen("800772_hashIndireta.txt", "w");

    /* -------- PESQUISA POR NOME -------- */
    while (fgets(entrada, sizeof(entrada), stdin)) {
        entrada[strcspn(entrada,"\r\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;
        trim(entrada);

        int pos = hash_search(&hash, entrada);

        if (pos == -1) {
            int teorica = hash_ascii(entrada);
            printf("%s:  (Posicao: %d) NAO\n", entrada, teorica);
            fprintf(log, "%s:  (Posicao: %d) NAO\n", entrada, teorica);
        } else {
            printf("%s:  (Posicao: %d) SIM\n", entrada, pos);
            fprintf(log, "%s:  (Posicao: %d) SIM\n", entrada, pos);
        }
    }

    fclose(log);

    /* ---- liberar tudo ---- */
    hash_free(&hash);

    for (int i = 0; i < catalog_n; i++)
        free_game(&catalog[i]);
    free(catalog);

    return 0;
}
