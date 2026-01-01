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

/* ---------------- Funções auxiliares ---------------- */

static char *strdup_safe(const char *s) {
    if (!s) return NULL;
    char *d = malloc(strlen(s) + 1);
    strcpy(d, s);
    return d;
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

static void trim(char *s) {
    if (!s) return;
    char *start = s;
    while (*start && (*start == ' ' || *start == '\t' || *start == '\n' || *start == '\r')) start++;
    if (start != s) memmove(s, start, strlen(start) + 1);
    int len = (int)strlen(s);
    while (len > 0 && (s[len-1] == ' ' || s[len-1] == '\t' || s[len-1] == '\n' || s[len-1] == '\r')) s[--len] = '\0';
}

/* ---------------- Parsing do CSV ---------------- */

static char **split_tokens_and_alloc(char *input, int *out_count) {
    *out_count = 0;
    if (!input || strlen(input) == 0) return NULL;

    char tmp[MAX_LINE];
    strncpy(tmp, input, MAX_LINE - 1);
    tmp[MAX_LINE - 1] = '\0';

    for (int i = 0; tmp[i]; i++)
        if (tmp[i] == '[' || tmp[i] == ']' || tmp[i] == '\'') tmp[i] = ' ';

    char *token = strtok(tmp, ",");
    while (token) {
        (*out_count)++;
        token = strtok(NULL, ",");
    }

    if (*out_count == 0) return NULL;

    strncpy(tmp, input, MAX_LINE - 1);
    tmp[MAX_LINE - 1] = '\0';
    for (int i = 0; tmp[i]; i++)
        if (tmp[i] == '[' || tmp[i] == ']' || tmp[i] == '\'') tmp[i] = ' ';

    char **arr = malloc(sizeof(char*) * (*out_count));
    int idx = 0;
    token = strtok(tmp, ",");
    while (token && idx < *out_count) {
        trim(token);
        arr[idx++] = strdup_safe(token);
        token = strtok(NULL, ",");
    }
    return arr;
}

static Data parse_date_field(const char *field) {
    Data d = {1, 1, 1900};
    if (!field) return d;
    char tmp[64];
    strncpy(tmp, field, 63);
    tmp[63] = '\0';
    trim(tmp);

    const char *months[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    char mon[8]; int day=0, year=0;
    if (sscanf(tmp,"%3s %d, %d",mon,&day,&year)==3){
        for(int i=0;i<12;i++) if(strncmp(mon,months[i],3)==0){d.mes=i+1;break;}
        d.dia=day;d.ano=year;
    } else if (sscanf(tmp,"%3s %d",mon,&year)==2){
        for(int i=0;i<12;i++) if(strncmp(mon,months[i],3)==0){d.mes=i+1;break;}
        d.dia=1;d.ano=year;
    } else if (sscanf(tmp,"%d",&year)==1){
        d.ano=year;
    }
    return d;
}

static void split_csv_line(const char *line, char fields[][MAX_LINE], int *fields_count) {
    int len = strlen(line);
    int fi=0, ci=0, in_quotes=0;
    char cur[MAX_LINE];
    for (int i=0;i<=len;i++){
        char c=(i==len?'\0':line[i]);
        if(c=='"') in_quotes=!in_quotes;
        else if((c==','||c=='\0')&&!in_quotes){
            cur[ci]='\0';
            strcpy(fields[fi++],cur);
            ci=0;
        } else cur[ci++]=c;
    }
    *fields_count=fi;
}

static Game parse_line_to_game(const char *line){
    Game g; memset(&g,0,sizeof(Game));
    char fields[14][MAX_LINE]; int fcount=0;
    split_csv_line(line,fields,&fcount);
    g.id=(fcount>0)?atoi(fields[0]):0;
    g.name=(fcount>1)?strdup_safe(fields[1]):strdup_safe("");
    if(fcount>2) g.date=parse_date_field(fields[2]);
    if(fcount>3) g.owners=atoi(fields[3]);
    if(fcount>4) g.price=atof(fields[4]);
    if(fcount>5) g.languages=split_tokens_and_alloc(fields[5],&g.languages_count);
    if(fcount>6) g.metacritic=atoi(fields[6]);
    if(fcount>7&&strcmp(fields[7],"tbd")!=0) g.userScore=atof(fields[7]);
    if(fcount>8) g.achievements=atoi(fields[8]);
    if(fcount>9) g.publishers=split_tokens_and_alloc(fields[9],&g.publishers_count);
    if(fcount>10) g.developers=split_tokens_and_alloc(fields[10],&g.developers_count);
    if(fcount>11) g.categories=split_tokens_and_alloc(fields[11],&g.categories_count);
    if(fcount>12) g.genres=split_tokens_and_alloc(fields[12],&g.genres_count);
    if(fcount>13) g.tags=split_tokens_and_alloc(fields[13],&g.tags_count);
    return g;
}

static Game *read_csv_build_catalog(const char *path,int *out_count){
    FILE *f=fopen(path,"r");
    if(!f){perror("CSV");exit(1);}
    char line[MAX_LINE]; fgets(line,sizeof(line),f);
    int cap=512,cnt=0;
    Game *arr=malloc(sizeof(Game)*cap);
    while(fgets(line,sizeof(line),f)){
        line[strcspn(line,"\r\n")]='\0';
        if(cnt>=cap){cap*=2;arr=realloc(arr,sizeof(Game)*cap);}
        arr[cnt++]=parse_line_to_game(line);
    }
    fclose(f);
    *out_count=cnt;
    return arr;
}

static Game *find_by_id(Game *catalog,int n,int id){
    for(int i=0;i<n;i++) if(catalog[i].id==id) return &catalog[i];
    return NULL;
}

static char **clone_string_array(char **src,int count){
    if(!src||count<=0)return NULL;
    char **dst=malloc(sizeof(char*)*count);
    for(int i=0;i<count;i++) dst[i]=strdup_safe(src[i]);
    return dst;
}

static Game clone_game(Game *src){
    Game g; memset(&g,0,sizeof(Game));
    g.id=src->id;
    g.name=strdup_safe(src->name);
    g.date=src->date;
    g.owners=src->owners;
    g.price=src->price;
    g.languages_count=src->languages_count;
    g.languages=clone_string_array(src->languages,src->languages_count);
    g.metacritic=src->metacritic;
    g.userScore=src->userScore;
    g.achievements=src->achievements;
    g.publishers_count=src->publishers_count;
    g.publishers=clone_string_array(src->publishers,src->publishers_count);
    g.developers_count=src->developers_count;
    g.developers=clone_string_array(src->developers,src->developers_count);
    g.categories_count=src->categories_count;
    g.categories=clone_string_array(src->categories,src->categories_count);
    g.genres_count=src->genres_count;
    g.genres=clone_string_array(src->genres,src->genres_count);
    g.tags_count=src->tags_count;
    g.tags=clone_string_array(src->tags,src->tags_count);
    return g;
}

/* ---------------- Impressão ---------------- */

static void print_array(char **arr,int n){
    printf("[");
    for(int i=0;i<n;i++){printf("%s",arr[i]);if(i!=n-1)printf(", ");}
    printf("]");
}

static void print_game(Game *g){
    printf("%d ## %s ## %02d/%02d/%04d ## %d ## %.2f ## ",
           g->id,g->name,g->date.dia,g->date.mes,g->date.ano,
           g->owners,g->price);
    print_array(g->languages,g->languages_count);
    printf(" ## %d ## %.1f ## %d ## ",g->metacritic,g->userScore,g->achievements);
    print_array(g->publishers,g->publishers_count);
    printf(" ## ");
    print_array(g->developers,g->developers_count);
    printf(" ## ");
    print_array(g->categories,g->categories_count);
    printf(" ## ");
    print_array(g->genres,g->genres_count);
    printf(" ## ");
    print_array(g->tags,g->tags_count);
    printf(" ##");
}

/* ---------------- Estrutura de Pilha ---------------- */

typedef struct Node{
    Game game;
    struct Node *next;
} Node;

typedef struct{
    Node *top;
    int size;
} Pilha;

static void pilha_init(Pilha *p){p->top=NULL;p->size=0;}

static void push(Pilha *p,Game g){
    Node *n=malloc(sizeof(Node));
    n->game=g;
    n->next=p->top;
    p->top=n;
    p->size++;
}

static Game pop(Pilha *p){
    Node *t=p->top;
    Game g=t->game;
    p->top=t->next;
    free(t);
    p->size--;
    return g;
}

static void printPilha(Pilha *p){
    Node *arr[1000];
    int count=0;
    Node *it=p->top;
    while(it){arr[count++]=it;it=it->next;}
    for(int i=count-1;i>=0;i--){
        printf("[%d] => ",count-1-i);
        print_game(&arr[i]->game);
        printf("\n");
    }
}

/* ---------------- MAIN ---------------- */

int main(){
    int catalog_count=0;
    Game *catalog=read_csv_build_catalog("/tmp/games.csv",&catalog_count);

    Pilha pilha; pilha_init(&pilha);

    char entrada[128];
    while(fgets(entrada,sizeof(entrada),stdin)){
        entrada[strcspn(entrada,"\r\n")]='\0';
        if(strcmp(entrada,"FIM")==0) break;
        int id=atoi(entrada);
        Game *found=find_by_id(catalog,catalog_count,id);
        if(found) push(&pilha,clone_game(found));
    }

    int n; scanf("%d",&n); getchar();
    for(int i=0;i<n;i++){
        fgets(entrada,sizeof(entrada),stdin);
        entrada[strcspn(entrada,"\r\n")]='\0';
        if(entrada[0]=='I'){
            int id=atoi(entrada+2);
            Game *found=find_by_id(catalog,catalog_count,id);
            if(found) push(&pilha,clone_game(found));
        }else if(entrada[0]=='R'){
            Game g=pop(&pilha);
            printf("(R) %s\n",g.name);
            free_game(&g);
        }
    }

    printPilha(&pilha);

    Node *it=pilha.top;
    while(it){free_game(&it->game);Node *tmp=it;it=it->next;free(tmp);}
    for(int i=0;i<catalog_count;i++) free_game(&catalog[i]);
    free(catalog);
    return 0;
}
