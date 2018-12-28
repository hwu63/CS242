/**
 * Machine Problem: Malloc
 * CS 241 - Spring 2017
 */

#include "tester-utils.h"

#define NUM_CYCLES 10000000

int main() {
  int i;
  for (i = 0; i < NUM_CYCLES; i++) {
    int *ptr = malloc(sizeof(int));
    free(ptr);
  }

  fprintf(stderr, "Memory was allocated, used, and freed!\n");
  return 0;
}
