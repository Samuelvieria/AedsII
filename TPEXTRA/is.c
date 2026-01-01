#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>

bool isAllVowels(char *str) {
    if (strlen(str) == 0) return false;
    for (int i = 0; str[i] != '\0'; i++) {
        char c = tolower(str[i]);
        if (!isalpha(c) || (c != 'a' && c != 'e' && c != 'i' && c != 'o' && c != 'u')) {
            return false;
        }
    }
    return true;
}

bool isAllConsonants(char *str) {
    if (strlen(str) == 0) return false;
    for (int i = 0; str[i] != '\0'; i++) {
        char c = tolower(str[i]);
        if (!isalpha(c) || (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')) {
            return false;
        }
    }
    return true;
}

bool isInteger(char *str) {
    if (strlen(str) == 0) return false;
    int start = 0;
    if (str[0] == '+' || str[0] == '-') start = 1;
    if (start == 1 && strlen(str) == 1) return false;
    for (int i = start; str[i] != '\0'; i++) {
        if (!isdigit(str[i])) return false;
    }
    return true;
}

bool isReal(char *str) {
    if (strlen(str) == 0) return false;
    int dotCount = 0;
    int start = 0;
    if (str[0] == '+' || str[0] == '-') start = 1;
    if (start == 1 && strlen(str) == 1) return false;
    for (int i = start; str[i] != '\0'; i++) {
        if (str[i] == '.') {
            dotCount++;
            if (dotCount > 1) return false;
            continue;
        }
        if (!isdigit(str[i])) return false;
    }
    return true;
}

int main() {
    char line[1000];
    while (fgets(line, sizeof(line), stdin)) {
        line[strcspn(line, "\n")] = '\0'; // Remove newline
        bool vowels = isAllVowels(line);
        bool consonants = isAllConsonants(line);
        bool integer = isInteger(line);
        bool real = isReal(line);
        printf("%s %s %s %s\n",
               vowels ? "SIM" : "NAO",
               consonants ? "SIM" : "NAO",
               integer ? "SIM" : "NAO",
               real ? "SIM" : "NAO");
    }
    return 0;
}