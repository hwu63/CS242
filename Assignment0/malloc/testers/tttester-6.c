/**
 * Machine Problem: Malloc
 * CS 241 - Spring 2017
 */

#include "tester-utils.h"

#define NUM_CYCLES 10000000

int main() {
int *ptr = malloc(1000);
free(ptr);

int *ptr5 = malloc(500);
int *ptr3 = malloc(300);
int *ptr2 = malloc(200);
free(ptr5);
free(ptr3);
free(ptr2);
  fprintf(stderr, "Memory was allocated, used, and freed!\n");
  return 0;
}
