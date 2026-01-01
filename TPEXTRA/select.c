#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

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

// Variáveis globais para contagem de comparações e movimentações
static long comparacoes = 0;
static long movimentacoes = 0;

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
    g->name = NULL;
    g->languages = NULL; g->languages_count = 0;
    g->publishers = NULL; g->publishers_count = 0;
    g->developers = NULL; g->developers_count = 0;
    g->categories = NULL; g->categories_count = 0;
    g->genres = NULL; g->genres_count = 0;
    g->tags = NULL; g->tags_count = 0;
}

static void trim(char *s) {
    if (!s) return;
    char *start = s;
    while (*start && (*start == ' ' || *start == '\t' || *start == '\n' || *start == '\r')) start++;
    if (start != s) memmove(s, start, strlen(start) + 1);
    int len = (int)strlen(s);
    while (len > 0 && (s[len - 1] == ' ' || s[len - 1] == '\t' || s[len - 1] == '\n' || s[len - 1] == '\r')) s[--len] = '\0';
}

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

    const char *months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
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

    if (fcount > 3 && strlen(fields[3]) > 0) {
        char nums[MAX_LINE] = {0}; int p = 0;
        for (int i = 0; fields[3][i]; i++)
            if (fields[3][i] >= '0' && fields[3][i] <= '9') nums[p++] = fields[3][i];
        nums[p] = '\0';
        g.owners = (p > 0) ? atoi(nums) : 0;
    }

    if (fcount > 4 && strlen(fields[4]) > 0) {
        if (strcmp(fields[4], "Free to Play") == 0) g.price = 0.0f;
        else g.price = (float)atof(fields[4]);
    }

    if (fcount > 5 && strlen(fields[5]) > 0)
        g.languages = split_tokens_and_alloc(fields[5], &g.languages_count);
    if (fcount > 6 && strlen(fields[6]) > 0)
        g.metacritic = atoi(fields[6]);
    if (fcount > 7 && strlen(fields[7]) > 0 && strcmp(fields[7], "tbd") != 0)
        g.userScore = (float)atof(fields[7]);
    if (fcount > 8 && strlen(fields[8]) > 0)
        g.achievements = atoi(fields[8]);
    if (fcount > 9 && strlen(fields[9]) > 0)
        g.publishers = split_tokens_and_alloc(fields[9], &g.publishers_count);
    if (fcount > 10 && strlen(fields[10]) > 0)
        g.developers = split_tokens_and_alloc(fields[10], &g.developers_count);
    if (fcount > 11 && strlen(fields[11]) > 0)
        g.categories = split_tokens_and_alloc(fields[11], &g.categories_count);
    if (fcount > 12 && strlen(fields[12]) > 0)
        g.genres = split_tokens_and_alloc(fields[12], &g.genres_count);
    if (fcount > 13 && strlen(fields[13]) > 0)
        g.tags = split_tokens_and_alloc(fields[13], &g.tags_count);

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
    printf("=> %d ## %s ## %02d/%02d/%04d ## %d ## %.2f ## ",
           g->id, g->name ? g->name : "",
           g->date.dia, g->date.mes, g->date.ano,
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
    printf(" ##\n");
}

static Game *read_csv_build_catalog(const char *path, int *out_count) {
    FILE *f = fopen(path, "r");
    if (!f) {
        perror("Erro ao abrir CSV");
        return NULL;
    }
    char line[MAX_LINE];
    if (!fgets(line, sizeof(line), f)) {
        fclose(f);
        *out_count = 0;
        return NULL;
    }

    Game *catalog = NULL;
    int capacity = 0, count = 0;

    while (fgets(line, sizeof(line), f)) {
        line[strcspn(line, "\r\n")] = '\0';
        if (strlen(line) == 0) continue;
        if (count >= capacity) {
            int newcap = (capacity == 0) ? 128 : capacity * 2;
            Game *tmp = realloc(catalog, sizeof(Game) * newcap);
            if (!tmp) { perror("realloc"); break; }
            catalog = tmp;
            capacity = newcap;
        }
        Game g = parse_line_to_game(line);
        catalog[count++] = g;
    }
    fclose(f);
    *out_count = count;
    return catalog;
}

// Função para comparar dois jogos por nome com desempate por AppID
static int comparar_games(const Game *a, const Game *b) {
    comparacoes++;
    int cmp = strcmp(a->name ? a->name : "", b->name ? b->name : "");
    if (cmp != 0) return cmp;
    return a->id - b->id;
}

static void swap(Game *a, Game *b) {
    Game temp = *a;
    *a = *b;
    *b = temp;
    movimentacoes++;
}

// Implementação do Selection Sort
static void selectionsort(Game *array, int n) {
    for (int i = 0; i < n - 1; i++) {
        int min_idx = i;
        for (int j = i + 1; j < n; j++) {
            if (comparar_games(&array[j], &array[min_idx]) < 0) {
                min_idx = j;
            }
        }
        if (min_idx != i) {
            swap(&array[i], &array[min_idx]);
        }
    }
}

// Função para buscar um jogo por ID no catálogo
static Game *pesquisa_seq_mod(Game *catalog, int catalog_count, int id) {
    for (int i = 0; i < catalog_count; i++) {
        if (catalog[i].id == id) {
            return &catalog[i];
        }
    }
    return NULL;
}

// Funções auxiliares para clonar (deep copy) arrays de strings e um Game
static char **clone_string_array(char **src, int count) {
    if (!src || count <= 0) return NULL;
    char **dst = malloc(sizeof(char*) * count);
    if (!dst) return NULL;
    for (int i = 0; i < count; i++) {
        dst[i] = strdup_safe(src[i]);
    }
    return dst;
}

static Game clone_game(const Game *src) {
    Game g;
    memset(&g, 0, sizeof(Game));
    g.id = src->id;
    g.name = src->name ? strdup_safe(src->name) : NULL;
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

// Função para criar um array temporário com jogos selecionados por ID
static Game *array_temp(Game *catalog, int catalog_count, int *out_count) {
    char entrada[256];
    Game *temp = malloc(sizeof(Game) * 200);
    if (!temp) {
        perror("malloc temp");
        *out_count = 0;
        return NULL;
    }
    int i = 0;

    while (fgets(entrada, sizeof(entrada), stdin)) {
        entrada[strcspn(entrada, "\r\n")] = '\0';
        if (strcmp(entrada, "FIM") == 0) break;
        trim(entrada);
        if (strlen(entrada) == 0) continue;

        int id = atoi(entrada);
        Game *found = pesquisa_seq_mod(catalog, catalog_count, id);
        if (found && i < 200) {
            temp[i++] = clone_game(found);
        }
    }

    Game *jogos_tmp = malloc(sizeof(Game) * i);
    if (!jogos_tmp) {
        perror("malloc jogos_tmp");
        for (int k = 0; k < i; k++) free_game(&temp[k]);
        free(temp);
        *out_count = 0;
        return NULL;
    }
    for (int k = 0; k < i; k++) {
        jogos_tmp[k] = temp[k];
    }
    free(temp);
    *out_count = i;
    return jogos_tmp;
}

int main(int argc, char **argv) {
    const char *csv_path = "/tmp/games.csv";
    if (argc > 1) csv_path = argv[1];

    int catalog_count = 0;
    Game *catalog = read_csv_build_catalog(csv_path, &catalog_count);
    if (!catalog) {
        fprintf(stderr, "Nenhum jogo lido.\n");
        return 1;
    }

    int temp_count = 0;
    Game *jogos_tmp = array_temp(catalog, catalog_count, &temp_count);

    clock_t inicio = clock();
    selectionsort(jogos_tmp, temp_count);
    clock_t fim = clock();
    double tempo_execucao = ((double)(fim - inicio)) / CLOCKS_PER_SEC;

    for (int i = 0; i < temp_count; i++) {
        print_game(&jogos_tmp[i]);
    }

    FILE *log_file = fopen("800772_selectionsort.txt", "w");
    if (log_file) {
        fprintf(log_file, "800772\t%ld\t%ld\t%.6f\n", comparacoes, movimentacoes, tempo_execucao);
        fclose(log_file);
    } else {
        perror("Erro ao criar arquivo de log");
    }

    for (int i = 0; i < temp_count; i++) {
        free_game(&jogos_tmp[i]);
    }
    free(jogos_tmp);

    for (int i = 0; i < catalog_count; i++) {
        free_game(&catalog[i]);
    }
    free(catalog);

    return 0;
}