#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>
#include <locale.h>

// --- Show Structure (Identical to previous problem) ---
typedef struct {
    char showId[20];
    char type[20];
    char title[200];
    char director[200];
    char** cast;
    int numCast;
    char country[100];
    time_t dateAdded;
    int releaseYear;
    char rating[20];
    char duration[50];
    char listedIn[200];
    char description[500];
} Show;

// --- Global CSV Data Storage (Identical to previous problem) ---
Show* allShows = NULL;
int numAllShows = 0;
int capacityAllShows = 10000;

// --- Show Helper Functions (Identical to previous problem) ---

// Function to initialize a Show object to default/safe values
void initShow(Show* s) {
    s->cast = NULL;
    s->numCast = 0;
    s->dateAdded = 0;
    s->releaseYear = 0;
    strcpy(s->showId, "NaN");
    strcpy(s->type, "NaN");
    strcpy(s->title, "NaN");
    strcpy(s->director, "NaN");
    strcpy(s->country, "NaN");
    strcpy(s->rating, "NaN");
    strcpy(s->duration, "NaN");
    strcpy(s->listedIn, "NaN");
    strcpy(s->description, "NaN");
}

// Function to free memory allocated for a Show object's dynamic members
void freeShow(Show* s) {
    if (s->cast != NULL) {
        for (int i = 0; i < s->numCast; i++) {
            if (s->cast[i] != NULL) {
                free(s->cast[i]);
            }
        }
        free(s->cast);
        s->cast = NULL;
    }
}

// Function to perform a deep copy of a Show object
Show copyShow(const Show* source) {
    Show dest;
    initShow(&dest);

    strcpy(dest.showId, source->showId);
    strcpy(dest.type, source->type);
    strcpy(dest.title, source->title);
    strcpy(dest.director, source->director);
    strcpy(dest.country, source->country);
    dest.dateAdded = source->dateAdded;
    dest.releaseYear = source->releaseYear;
    strcpy(dest.rating, source->rating);
    strcpy(dest.duration, source->duration);
    strcpy(dest.listedIn, source->listedIn);
    strcpy(dest.description, source->description);

    dest.numCast = source->numCast;
    if (source->numCast > 0) {
        dest.cast = (char**)malloc(source->numCast * sizeof(char*));
        if (dest.cast == NULL) {
            fprintf(stderr, "Memory allocation failed for deep copy cast array\n");
            exit(EXIT_FAILURE);
        }
        for (int i = 0; i < source->numCast; i++) {
            dest.cast[i] = strdup(source->cast[i]);
            if (dest.cast[i] == NULL) {
                fprintf(stderr, "Memory allocation failed for deep copy cast member\n");
                exit(EXIT_FAILURE);
            }
        }
    } else {
        dest.cast = NULL;
    }
    return dest;
}

// Custom CSV parsing function
void parseLinhaCSV(char* linha, char** campos, int* numCampos) {
    bool inQuotes = false;
    int campoIdx = 0;
    int charIdx = 0;
    char temp[1000];

    linha[strcspn(linha, "\r\n")] = 0;

    for (int i = 0; linha[i] != '\0'; i++) {
        char c = linha[i];
        if (c == '\"') {
            inQuotes = !inQuotes;
        } else if (c == ',' && !inQuotes) {
            temp[charIdx] = '\0';
            campos[campoIdx] = strdup(temp);
            campoIdx++;
            charIdx = 0;
            if (campoIdx >= 12) {
                break;
            }
        } else {
            temp[charIdx++] = c;
            if (charIdx >= sizeof(temp) - 1) {
                fprintf(stderr, "Warning: Buffer overflow detected in parseLinhaCSV temp buffer.\n");
                break;
            }
        }
    }
    temp[charIdx] = '\0';
    campos[campoIdx] = strdup(temp);
    *numCampos = campoIdx + 1;
}

// Function to parse a date string
time_t parseDate(const char* dateStr) {
    struct tm tm_obj = {0};
    char monthStr[20];
    int day, year;

    if (sscanf(dateStr, "%s %d, %d", monthStr, &day, &year) != 3) {
        return 0;
    }

    if (strcmp(monthStr, "January") == 0) tm_obj.tm_mon = 0;
    else if (strcmp(monthStr, "February") == 0) tm_obj.tm_mon = 1;
    else if (strcmp(monthStr, "March") == 0) tm_obj.tm_mon = 2;
    else if (strcmp(monthStr, "April") == 0) tm_obj.tm_mon = 3;
    else if (strcmp(monthStr, "May") == 0) tm_obj.tm_mon = 4;
    else if (strcmp(monthStr, "June") == 0) tm_obj.tm_mon = 5;
    else if (strcmp(monthStr, "July") == 0) tm_obj.tm_mon = 6;
    else if (strcmp(monthStr, "August") == 0) tm_obj.tm_mon = 7;
    else if (strcmp(monthStr, "September") == 0) tm_obj.tm_mon = 8;
    else if (strcmp(monthStr, "October") == 0) tm_obj.tm_mon = 9;
    else if (strcmp(monthStr, "November") == 0) tm_obj.tm_mon = 10;
    else if (strcmp(monthStr, "December") == 0) tm_obj.tm_mon = 11;
    else return 0;

    tm_obj.tm_mday = day;
    tm_obj.tm_year = year - 1900;

    return mktime(&tm_obj);
}

#define SAFE_STRCPY(dest, src_field) \
    do { \
        if (src_field == NULL || strlen(src_field) == 0) { \
            strcpy(dest, "NaN"); \
        } else { \
            char* temp_src = strdup(src_field); \
            if (temp_src[0] == '\"' && temp_src[strlen(temp_src) - 1] == '\"') { \
                temp_src[strlen(temp_src) - 1] = '\0'; \
                memmove(temp_src, temp_src + 1, strlen(temp_src)); \
            } \
            strcpy(dest, temp_src); \
            free(temp_src); \
        } \
    } while(0)

// Function to read a line and populate a Show object
void lerShow(Show* s, char* linha) {
    initShow(s);

    char* campos[12];
    int numCampos = 0;
    parseLinhaCSV(linha, campos, &numCampos);

    for (int i = numCampos; i < 12; i++) {
        campos[i] = strdup("");
    }

    SAFE_STRCPY(s->showId, campos[0]);
    SAFE_STRCPY(s->type, campos[1]);
    SAFE_STRCPY(s->title, campos[2]);
    SAFE_STRCPY(s->director, campos[3]);

    char* elenco_raw = campos[4];
    if (elenco_raw == NULL || strlen(elenco_raw) == 0) {
        s->cast = NULL;
        s->numCast = 0;
    } else {
        char* tempElenco = strdup(elenco_raw);
        if (tempElenco[0] == '\"' && tempElenco[strlen(tempElenco) - 1] == '\"') {
            tempElenco[strlen(tempElenco) - 1] = '\0';
            memmove(tempElenco, tempElenco + 1, strlen(tempElenco));
        }

        char* token = strtok(tempElenco, ",");
        s->numCast = 0;
        while (token != NULL) {
            char* trimmed = token;
            while (*trimmed == ' ') trimmed++;
            size_t len = strlen(trimmed);
            while (len > 0 && trimmed[len - 1] == ' ') len--;
            trimmed[len] = '\0';

            s->numCast++;
            s->cast = (char**)realloc(s->cast, s->numCast * sizeof(char*));
            if (s->cast == NULL) {
                fprintf(stderr, "Memory allocation failed for cast array\n");
                exit(EXIT_FAILURE);
            }
            s->cast[s->numCast - 1] = strdup(trimmed);
            if (s->cast[s->numCast - 1] == NULL) {
                fprintf(stderr, "Memory allocation failed for cast member string\n");
                exit(EXIT_FAILURE);
            }
            token = strtok(NULL, ",");
        }
        free(tempElenco);
    }

    SAFE_STRCPY(s->country, campos[5]);

    char* dateStr = campos[6];
    if (dateStr == NULL || strlen(dateStr) == 0) {
        s->dateAdded = 0;
    } else {
        char* cleaned_dateStr = strdup(dateStr);
        if (cleaned_dateStr[0] == '\"' && cleaned_dateStr[strlen(cleaned_dateStr) - 1] == '\"') {
            cleaned_dateStr[strlen(cleaned_dateStr) - 1] = '\0';
            memmove(cleaned_dateStr, cleaned_dateStr + 1, strlen(cleaned_dateStr));
        }
        s->dateAdded = parseDate(cleaned_dateStr);
        free(cleaned_dateStr);
    }

    char* releaseYearStr = campos[7];
    if (releaseYearStr == NULL || strlen(releaseYearStr) == 0) {
        s->releaseYear = 0;
    } else {
        char* cleaned_releaseYearStr = strdup(releaseYearStr);
         if (cleaned_releaseYearStr[0] == '\"' && cleaned_releaseYearStr[strlen(cleaned_releaseYearStr) - 1] == '\"') {
            cleaned_releaseYearStr[strlen(cleaned_releaseYearStr) - 1] = '\0';
            memmove(cleaned_releaseYearStr, cleaned_releaseYearStr + 1, strlen(cleaned_releaseYearStr));
        }
        s->releaseYear = atoi(cleaned_releaseYearStr);
        free(cleaned_releaseYearStr);
    }

    SAFE_STRCPY(s->rating, campos[8]);
    SAFE_STRCPY(s->duration, campos[9]);
    SAFE_STRCPY(s->listedIn, campos[10]);
    SAFE_STRCPY(s->description, campos[11]);

    for (int i = 0; i < numCampos; i++) {
        free(campos[i]);
    }
    for (int i = numCampos; i < 12; i++) {
        free(campos[i]);
    }
}

// Comparison function for qsort (sorting cast members)
int compareStrings(const void* a, const void* b) {
    return strcmp(*(const char**)a, *(const char**)b);
}

// Function to format and print a Show object
void formatarShow(const Show* s) {
    printf("=> %s ## %s ## %s ## %s ## [", s->showId, s->title, s->type, s->director);

    if (s->numCast > 0) {
        char** sortedCast = (char**)malloc(s->numCast * sizeof(char*));
        if (sortedCast == NULL) {
            fprintf(stderr, "Memory allocation failed for sortedCast\n");
            exit(EXIT_FAILURE);
        }
        for (int i = 0; i < s->numCast; i++) {
            sortedCast[i] = s->cast[i];
        }
        qsort(sortedCast, s->numCast, sizeof(char*), compareStrings);

        for (int i = 0; i < s->numCast; i++) {
            printf("%s%s", sortedCast[i], (i == s->numCast - 1) ? "" : ", ");
        }
        free(sortedCast);
    } else {
        printf("NaN");
    }
    printf("] ## %s ## ", s->country);

    if (s->dateAdded != 0) {
        char buffer[80];
        struct tm* tm_info = localtime(&s->dateAdded);
        strftime(buffer, sizeof(buffer), "%B %d, %Y", tm_info);
        printf("%s", buffer);
    } else {
        printf("NaN");
    }

    printf(" ## %d ## %s ## %s ## [%s] ##\n",
           s->releaseYear, s->rating, s->duration, s->listedIn);
}

// --- PilhaShow Structure (Dynamic Array Implementation) ---
typedef struct {
    Show* array;
    int topo; // Index of the top element (-1 if empty)
    int capacity;
} PilhaShow;

// Function to initialize PilhaShow
void initPilhaShow(PilhaShow* pilha, int tamanho) {
    pilha->array = (Show*)malloc(tamanho * sizeof(Show));
    if (pilha->array == NULL) {
        fprintf(stderr, "Memory allocation failed for PilhaShow array\n");
        exit(EXIT_FAILURE);
    }
    pilha->topo = -1; // Stack is empty
    pilha->capacity = tamanho;
}

// Function to free memory used by PilhaShow
void freePilhaShow(PilhaShow* pilha) {
    while (pilha->topo != -1) {
        freeShow(&pilha->array[pilha->topo]); // Free dynamic members of each Show
        pilha->topo--;
    }
    free(pilha->array); // Free the array itself
    pilha->array = NULL;
    pilha->capacity = 0;
}

// Empilhar (Push): Inserts a show onto the top of the stack
void empilhar(PilhaShow* pilha, Show show) {
    if (pilha->topo == pilha->capacity - 1) {
        fprintf(stderr, "Error: Pilha cheia (stack full) - cannot push.\n");
        return;
    }
    pilha->topo++;
    pilha->array[pilha->topo] = copyShow(&show); // Deep copy the incoming show
}

// Desempilhar (Pop): Removes and returns the show from the top of the stack
Show desempilhar(PilhaShow* pilha) {
    if (pilha->topo == -1) {
        fprintf(stderr, "Error: Pilha vazia (stack empty) - cannot pop.\n");
        Show emptyShow;
        initShow(&emptyShow);
        return emptyShow; // Return an empty/invalid Show
    }
    Show resp = pilha->array[pilha->topo]; // Get the Show object
    pilha->topo--;
    // The 'resp' Show now owns the memory that was at the top of the stack.
    // It's the caller's responsibility to free it.
    return resp;
}

// Mostrar: Displays all shows in the stack from top to bottom
void mostrarPilha(const PilhaShow* pilha) {
    // Iterate from topo down to 0
    for (int i = pilha->topo; i >= 0; i--) {
        formatarShow(&pilha->array[i]);
    }
}

// --- CSV Reading (Global Map Equivalent - Identical to previous problem) ---
void lerCSV(const char* caminho) {
    FILE* file = fopen(caminho, "r");
    if (file == NULL) {
        perror("Error opening file");
        exit(EXIT_FAILURE);
    }

    char linha[1024];
    fgets(linha, sizeof(linha), file); // Skip header

    allShows = (Show*)malloc(capacityAllShows * sizeof(Show));
    if (allShows == NULL) {
        fprintf(stderr, "Memory allocation failed for allShows array\n");
        exit(EXIT_FAILURE);
    }

    while (fgets(linha, sizeof(linha), file) != NULL) {
        if (numAllShows >= capacityAllShows) {
            capacityAllShows *= 2;
            allShows = (Show*)realloc(allShows, capacityAllShows * sizeof(Show));
            if (allShows == NULL) {
                fprintf(stderr, "Memory reallocation failed for allShows\n");
                exit(EXIT_FAILURE);
            }
        }
        lerShow(&allShows[numAllShows], linha);
        numAllShows++;
    }
    fclose(file);
}

// Helper function to find a Show by showId
Show* findShowById(const char* showId) {
    for (int i = 0; i < numAllShows; i++) {
        if (strcmp(allShows[i].showId, showId) == 0) {
            return &allShows[i];
        }
    }
    return NULL;
}

// Function to read a specific line from the CSV given a showId
char* lerLinhaPorArquivo(const char* showId) {
    FILE* file = fopen("/tmp/disneyplus.csv", "r");
    if (file == NULL) {
        perror("Error opening file");
        return NULL;
    }

    char linha[1024];
    fgets(linha, sizeof(linha), file); // Skip header

    char* foundLine = NULL;
    while (fgets(linha, sizeof(linha), file) != NULL) {
        char tempShowId[20];
        if (sscanf(linha, "%19[^,]", tempShowId) == 1) {
            if (strcmp(tempShowId, showId) == 0) {
                foundLine = strdup(linha);
                break;
            }
        }
    }
    fclose(file);
    return foundLine;
}


int main() {
    setlocale(LC_ALL, "en_US.UTF-8");

    lerCSV("/tmp/disneyplus.csv");

    PilhaShow pilha;
    initPilhaShow(&pilha, 1000); // Initialize stack with capacity

    char id[20];
    // First part: read IDs and push onto the stack
    while (scanf("%19s", id) == 1 && strcmp(id, "FIM") != 0) {
        Show* s = findShowById(id);
        if (s != NULL) {
            empilhar(&pilha, *s); // Push a deep copy onto the stack
        }
    }

    int n_commands;
    scanf("%d", &n_commands);
    getchar(); // Consume the leftover newline

    for (int i = 0; i < n_commands; i++) {
        char commandLine[256];
        if (fgets(commandLine, sizeof(commandLine), stdin) == NULL) {
            break;
        }
        commandLine[strcspn(commandLine, "\n")] = 0;

        char cmd[2]; // "I" or "R"
        char arg[20]; // For showId in "I" command

        sscanf(commandLine, "%s", cmd);

        if (strcmp(cmd, "I") == 0) { // Push command
            sscanf(commandLine, "%*s %19s", arg);
            char* lineFromFile = lerLinhaPorArquivo(arg);
            if (lineFromFile != NULL) {
                Show newShow;
                lerShow(&newShow, lineFromFile);
                empilhar(&pilha, newShow); // Deep copy and push
                freeShow(&newShow); // Free original newShow's dynamic members
                free(lineFromFile); // Free line read from file
            } else {
                fprintf(stderr, "Error: Show '%s' not found for insertion.\n", arg);
            }
        } else if (strcmp(cmd, "R") == 0) { // Pop command
            Show removed = desempilhar(&pilha);
            printf("(R) %s\n", removed.title);
            freeShow(&removed); // Free dynamic members of the removed Show
        }
    }

    mostrarPilha(&pilha);

    // Clean up all dynamically allocated memory
    freePilhaShow(&pilha); // Frees elements in the stack and the stack array itself

    if (allShows != NULL) {
        for (int i = 0; i < numAllShows; i++) {
            freeShow(&allShows[i]); // Frees dynamic members of each Show in the global array
        }
        free(allShows); // Frees the global array itself
        allShows = NULL;
    }

    return 0;
}