#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_LINE 16384

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

/* =================== Funções utilitárias =================== */

static char *strdup_safe(const char *s) {
    if (!s) return NULL;
    char *d = malloc(strlen(s) + 1);
    if (d) strcpy(d, s);
    return d;
}

static void free_string_array(char **arr, int n) {
    if (!arr) return;
    for (int i = 0; i < n; i++) free(arr[i]);
    free(arr);
}

static void free_game(Game *g) {
    if (!g) return;
    free(g->name);
    free_string_array(g->languages, g->languages_count);
    free_string_array(g->publishers, g->publishers_count);
    free_string_array(g->developers, g->developers_count);
    free_string_array(g->categories, g->categories_count);
    free_string_array(g->genres, g->genres_count);
    free_string_array(g->tags, g->tags_count);
}

static void trim(char *s) {
    if (!s) return;
    char *start = s;
    while (*start && (*start == ' ' || *start == '\t' || *start == '\n' || *start == '\r')) start++;
    if (start != s) memmove(s, start, strlen(start) + 1);
    int len = (int)strlen(s);
    while (len > 0 && (s[len - 1] == ' ' || s[len - 1] == '\t' || s[len - 1] == '\n' || s[len - 1] == '\r')) s[--len] = '\0';
}

/* =================== Parsing do CSV =================== */

static char **split_tokens_and_alloc(char *input, int *out_count) {
    *out_count = 0;
    if (!input || strlen(input) == 0) return NULL;

    char tmp[MAX_LINE];
    strncpy(tmp, input, MAX_LINE - 1);
    tmp[MAX_LINE - 1] = '\0';

    for (int i = 0; tmp[i]; i++)
        if (tmp[i] == '[' || tmp[i] == ']' || tmp[i] == '\'') tmp[i] = ' ';

    char *p = tmp, *token, *saveptr;
    token = strtok_r(p, ",", &saveptr);
    while (token) {
        while (*token == ' ' || *token == '\t') token++;
        (*out_count)++;
        token = strtok_r(NULL, ",", &saveptr);
    }

    if (*out_count == 0) return NULL;

    char **arr = malloc(sizeof(char *) * (*out_count));
    strncpy(tmp, input, MAX_LINE - 1);
    tmp[MAX_LINE - 1] = '\0';
    for (int i = 0; tmp[i]; i++)
        if (tmp[i] == '[' || tmp[i] == ']' || tmp[i] == '\'') tmp[i] = ' ';

    int idx = 0;
    token = strtok_r(tmp, ",", &saveptr);
    while (token && idx < *out_count) {
        trim(token);
        arr[idx++] = strdup_safe(token);
        token = strtok_r(NULL, ",", &saveptr);
    }
    return arr;
}

static Data parse_date_field(const char *field) {
    Data d = {1, 1, 1900};
    if (!field) return d;
    char tmp[MAX_LINE];
    strncpy(tmp, field, MAX_LINE - 1);
    tmp[MAX_LINE - 1] = '\0';
    trim(tmp);
    if (strlen(tmp) == 0) return d;

    const char *months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    char mon[16]; int day = 0, year = 0;
    if (sscanf(tmp, "%3s %d, %d", mon, &day, &year) == 3) {
        for (int i = 0; i < 12; i++)
            if (strncmp(mon, months[i], 3) == 0) { d.mes = i + 1; break; }
        d.dia = day; d.ano = year;
        return d;
    }
    if (sscanf(tmp, "%3s %d", mon, &year) == 2) {
        for (int i = 0; i < 12; i++)
            if (strncmp(mon, months[i], 3) == 0) { d.mes = i + 1; break; }
        d.dia = 1; d.ano = year;
        return d;
    }
    if (sscanf(tmp, "%d", &year) == 1) {
        d.ano = year;
        return d;
    }
    return d;
}

static void split_csv_line(const char *line, char fields[][MAX_LINE], int *fields_count) {
    int len = (int)strlen(line);
    int fi = 0, ci = 0, in_quotes = 0;
    char cur[MAX_LINE];
    for (int i = 0; i <= len && fi < 14; i++) {
        char c = (i == len ? '\0' : line[i]);
        if (c == '"') in_quotes = !in_quotes;
        else if ((c == ',' || c == '\0') && !in_quotes) {
            cur[ci] = '\0';
            strcpy(fields[fi++], cur);
            ci = 0;
        } else {
            if (ci < MAX_LINE - 2) cur[ci++] = c;
        }
    }
    *fields_count = fi;
    for (int k = 0; k < fi; k++) trim(fields[k]);
}

static Game parse_line_to_game(const char *line) {
    Game g;
    memset(&g, 0, sizeof(Game));
    g.metacritic = -1; g.userScore = -1.0f;

    char fields[14][MAX_LINE];
    int fcount = 0;
    split_csv_line(line, fields, &fcount);

    g.id = (fcount > 0) ? atoi(fields[0]) : 0;
    g.name = (fcount > 1) ? strdup_safe(fields[1]) : strdup_safe("");
    if (fcount > 2) g.date = parse_date_field(fields[2]);
    if (fcount > 3) g.owners = atoi(fields[3]);
    if (fcount > 4) g.price = atof(fields[4]);
    if (fcount > 5) g.languages = split_tokens_and_alloc(fields[5], &g.languages_count);
    if (fcount > 6) g.metacritic = atoi(fields[6]);
    if (fcount > 7 && strcmp(fields[7], "tbd") != 0) g.userScore = atof(fields[7]);
    if (fcount > 8) g.achievements = atoi(fields[8]);
    if (fcount > 9) g.publishers = split_tokens_and_alloc(fields[9], &g.publishers_count);
    if (fcount > 10) g.developers = split_tokens_and_alloc(fields[10], &g.developers_count);
    if (fcount > 11) g.categories = split_tokens_and_alloc(fields[11], &g.categories_count);
    if (fcount > 12) g.genres = split_tokens_and_alloc(fields[12], &g.genres_count);
    if (fcount > 13) g.tags = split_tokens_and_alloc(fields[13], &g.tags_count);

    return g;
}

static Game *read_csv_build_catalog(const char *path, int *out_count) {
    FILE *f = fopen(path, "r");
    if (!f) { perror("CSV"); return NULL; }

    char line[MAX_LINE];
    fgets(line, sizeof(line), f); // cabeçalho

    int cap = 256, count = 0;
    Game *arr = malloc(sizeof(Game) * cap);

    while (fgets(line, sizeof(line), f)) {
        line[strcspn(line, "\r\n")] = '\0';
        if (count >= cap) {
            cap *= 2;
            arr = realloc(arr, sizeof(Game) * cap);
        }
        arr[count++] = parse_line_to_game(line);
    }
    fclose(f);
    *out_count = count;
    return arr;
}

static Game *pesquisa_seq_mod(Game *catalog, int n, int id) {
    for (int i = 0; i < n; i++)
        if (catalog[i].id == id) return &catalog[i];
    return NULL;
}

static char **clone_string_array(char **src, int count) {
    if (!src || count <= 0) return NULL;
    char **dst = malloc(sizeof(char*) * count);
    for (int i = 0; i < count; i++) dst[i] = strdup_safe(src[i]);
    return dst;
}

static Game clone_game(const Game *src) {
    Game g;
    memset(&g, 0, sizeof(Game));
    g.id = src->id;
    g.name = strdup_safe(src->name);
    g.date = src->date;
    g.owners = src->owners;
    g.price = src->price;
    g.languages_count = src->languages_count;
    g.languages = clone_string_array(src->languages, src->languages_count);
    g.metacritic = src->metacritic;
    g.userScore = src->userScore;
    g.achievements = src->achievements;
    g.publishers_count = src->publishers_count;
    g.publishers = clone_string_array(src->publishers, src->publishers_count);
    g.developers_count = src->developers_count;
    g.developers = clone_string_array(src->developers, src->developers_count);
    g.categories_count = src->categories_count;
    g.categories = clone_string_array(src->categories, src->categories_count);
    g.genres_count = src->genres_count;
    g.genres = clone_string_array(src->genres, src->genres_count);
    g.tags_count = src->tags_count;
    g.tags = clone_string_array(src->tags, src->tags_count);
    return g;
}

static void print_array(char **arr, int count) {
    printf("[");
    for (int i = 0; i < count; i++) {
        printf("%s", arr[i]);
        if (i != count - 1) printf(", ");
    }
    printf("]");
}

static void print_game(const Game *g) {
    printf("%d ## %s ## %02d/%02d/%04d ## %d ## %.2f ## ",
           g->id, g->name, g->date.dia, g->date.mes, g->date.ano,
           g->owners, g->price);
    print_array(g->languages, g->languages_count);
    printf(" ## %d ## %.1f ## %d ## ", g->metacritic, g->userScore, g->achievements);
    print_array(g->publishers, g->publishers_count);
    printf(" ## ");
    print_array(g->developers, g->developers_count);
    printf(" ## ");
    print_array(g->categories, g->categories_count);
    printf(" ## ");
    print_array(g->genres, g->genres_count);
    printf(" ## ");
    print_array(g->tags, g->tags_count);
    printf(" ##");
}

/* =================== Estrutura da Lista Flexível =================== */

typedef struct Node {
    Game game;
    struct Node *next;
} Node;

typedef struct {
    Node *head;
    int size;
} Lista;

static void lista_init(Lista *l) { l->head = NULL; l->size = 0; }

static void inserirInicio(Lista *l, Game g) {
    Node *n = malloc(sizeof(Node));
    n->game = g;
    n->next = l->head;
    l->head = n;
    l->size++;
}

static void inserirFim(Lista *l, Game g) {
    Node *n = malloc(sizeof(Node));
    n->game = g; n->next = NULL;
    if (!l->head) l->head = n;
    else {
        Node *p = l->head;
        while (p->next) p = p->next;
        p->next = n;
    }
    l->size++;
}

static void inserirPos(Lista *l, Game g, int pos) {
    if (pos <= 0) { inserirInicio(l, g); return; }
    if (pos >= l->size) { inserirFim(l, g); return; }
    Node *p = l->head;
    for (int i = 0; i < pos - 1; i++) p = p->next;
    Node *n = malloc(sizeof(Node));
    n->game = g;
    n->next = p->next;
    p->next = n;
    l->size++;
}

static Game removerInicio(Lista *l) {
    Node *n = l->head;
    Game g = n->game;
    l->head = n->next;
    free(n);
    l->size--;
    return g;
}

static Game removerFim(Lista *l) {
    if (l->size == 1) return removerInicio(l);
    Node *p = l->head;
    while (p->next->next) p = p->next;
    Node *ultimo = p->next;
    Game g = ultimo->game;
    p->next = NULL;
    free(ultimo);
    l->size--;
    return g;
}

static Game removerPos(Lista *l, int pos) {
    if (pos <= 0) return removerInicio(l);
    if (pos >= l->size - 1) return removerFim(l);
    Node *p = l->head;
    for (int i = 0; i < pos - 1; i++) p = p->next;
    Node *rem = p->next;
    Game g = rem->game;
    p->next = rem->next;
    free(rem);
    l->size--;
    return g;
}

static void printLista(const Lista *l) {
    Node *p = l->head; int i = 0;
    while (p) {
        printf("[%d] => ", i++);
        print_game(&p->game);
        printf("\n");
        p = p->next;
    }
}

/* =================== MAIN =================== */

int main() {
    int catalog_count = 0;
    Game *catalog = read_csv_build_catalog("/tmp/games.csv", &catalog_count);
    Lista lista; lista_init(&lista);

    char entrada[256];
    while (fgets(entrada, sizeof(entrada), stdin)) {
        entrada[strcspn(entrada, "\r\n")] = '\0';
        if (strcmp(entrada, "FIM") == 0) break;
        int id = atoi(entrada);
        Game *found = pesquisa_seq_mod(catalog, catalog_count, id);
        if (found) inserirFim(&lista, clone_game(found));
    }

    int n;
    scanf("%d", &n);
    getchar();

    for (int i = 0; i < n; i++) {
        fgets(entrada, sizeof(entrada), stdin);
        entrada[strcspn(entrada, "\r\n")] = '\0';

        if (strncmp(entrada, "II", 2) == 0) {
            int id = atoi(entrada + 3);
            Game *found = pesquisa_seq_mod(catalog, catalog_count, id);
            if (found) inserirInicio(&lista, clone_game(found));
        } else if (strncmp(entrada, "IF", 2) == 0) {
            int id = atoi(entrada + 3);
            Game *found = pesquisa_seq_mod(catalog, catalog_count, id);
            if (found) inserirFim(&lista, clone_game(found));
        } else if (strncmp(entrada, "I*", 2) == 0) {
            int pos, id;
            sscanf(entrada + 2, "%d %d", &pos, &id);
            Game *found = pesquisa_seq_mod(catalog, catalog_count, id);
            if (found) inserirPos(&lista, clone_game(found), pos);
        } else if (strcmp(entrada, "RI") == 0) {
            Game g = removerInicio(&lista);
            printf("(R) %s\n", g.name);
            free_game(&g);
        } else if (strcmp(entrada, "RF") == 0) {
            Game g = removerFim(&lista);
            printf("(R) %s\n", g.name);
            free_game(&g);
        } else if (strncmp(entrada, "R*", 2) == 0) {
            int pos;
            sscanf(entrada + 2, "%d", &pos);
            Game g = removerPos(&lista, pos);
            printf("(R) %s\n", g.name);
            free_game(&g);
        }
    }

    printLista(&lista);

    Node *p = lista.head;
    while (p) {
        free_game(&p->game);
        Node *tmp = p;
        p = p->next;
        free(tmp);
    }
    for (int i = 0; i < catalog_count; i++) free_game(&catalog[i]);
    free(catalog);
    return 0;
}
