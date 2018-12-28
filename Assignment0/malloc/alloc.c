#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

typedef struct metadata_t{

	void *ptr;
	size_t size;
	int free;
	void *end;
	struct metadata_t *next;
	struct metadata_t *prev;
	struct metadata_t *fnext;

}metadata_t;


void * start;
void * end;
size_t fn;
//int flag = 0;
//int list=0;


metadata_t * head = NULL;
metadata_t * fhead = NULL;


void *malloc(size_t size) {
//	printf("%d: malloc size:%zu\n",__LINE__, size);
	//printf("%d: fhead:%p\n",__LINE__, fhead );
	if(end-start ==sizeof(int)) return start;
	if(size == sizeof(int) && !head){
		start = sbrk(0);
		sbrk(4);
		end = sbrk(0);
		return start;
	}
	if(size == 1 && !head){
		start = sbrk(0);
		sbrk(1);
		end = sbrk(0);
		return start;
	}
	/*if(!flag && size == TOTAL_ALLOCS * sizeof(int *)){
		flag = 1;
	}
	if((flag ==1 ||flag == 2)&& size == sizeof(int)){
	flag = 2;
	int * ret = end;
	sbrk(sizeof(int));
	end = sbrk(0);
	//printf(" sbrk(0): %p, int: %d,  return: %p\n",  end, *ret, ret);
	return ret;
	}*/
	if(fhead){//freelist head
		metadata_t * fp = fhead;
		metadata_t * fprev =NULL;
		metadata_t * fphead =NULL;
		while(fp){
		//printf("%d:fp\n", __LINE__);
			if(fp->size >= size){
				fp->free = 0;
				int rest = fp->size - size - (int)sizeof(metadata_t);
			//	printf("restsize:%d\n", rest);
			//	printf("fhead: %p fp->fnext:%p\n", fhead, fp->fnext);
				if(rest <=0){////////////////no rest
					fn--;
					if(fprev){fprev->fnext = fp->fnext;}
					else if (fhead == fp){ fhead = fp->fnext;} 
					fp->fnext = NULL;}
				else{////////////////have rest
					//fp reset
				//	printf("%d: og: %p, ptr: %p, end: %p, real size: %ld, size: %zu, free:%d,fnext: %p, next: %p, prev: %p \n",__LINE__, fp,  fp->ptr, fp->end, fp->end-fp->ptr, fp->size, fp->free, fp->fnext, fp->next, fp->prev);
					fp->size = size;
					fp->end = fp->ptr + size;
					//printf("%d: fp: %p, ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, fnext: %p, next: %p, prev: %p \n",__LINE__, fp,  fp->ptr, fp->end, fp->end-fp->ptr, fp->size, fp->free, fp->fnext, fp->next, fp->prev);
					///new re
					metadata_t *re = fp->end;
					re->ptr = (void*)(re + 1);
					re->size = rest;
					re->free = 1;
					re->end = re->ptr+ rest;
					//printf("%d: re:%p, ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, fnext: %p, next: %p, prev: %p \n", __LINE__, re, re->ptr, re->end, re->end-re->ptr, re->size, re->free, re->fnext, re->next, re->prev);
					////to whole listfnext
			//		printf("head: %p, fp: %p\n", head, fp);
					if(fp == head) {head = re; re->prev = NULL;}
					if(fp->prev) fp->prev->next = re;
					re->prev = fp->prev;
					fp->prev = fp->end;
					re->next = fp;
					/////to free list
					if(fprev)fprev->fnext = re;
					re->fnext = fp->fnext;	
					if(!fhead || fp == fhead) fhead = re; 
					fp->fnext = NULL;
					//printf("%d: re:%p, ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, fnext: %p, next: %p, prev: %p \n", __LINE__, re, re->ptr, re->end, re->end-re->ptr, re->size, re->free, re->fnext, re->next, re->prev);
					//printf("%d: fp: %p, ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, fnext: %p, next: %p, prev: %p \n",__LINE__, fp,  fp->ptr, fp->end, fp->end-fp->ptr, fp->size, fp->free, fp->fnext, fp->next, fp->prev);
					}
		/***********************************************
		metadata_t * p= head;
		printf("malloc rest: \n");
		int i=0;
		while(p){
		printf("%d: %dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p,fnext: %p\n",__LINE__, i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev, p->fnext);
		i++;
		p=p->next;
		}
		printf("freelist: \n");
					p= fhead;
					i=0;
					while(p){
							printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev);
						i++;
					 p=p->fnext;
					}
					printf("start: %p, end: %p, size: %ld, list num: %d, fhead: %p\n\n\n", start, end, end-start, i, fhead);
		printf("%d:hedblock: %p, ptr: %p size: %zu, free:%d, prev: %p, next:%p\n",__LINE__, head, head->ptr, head->size, head->free, head->prev, head->next);
		printf("start: %p, end: %p, size: %ld, list num: %d, fhead: %p\n", start, end, end-start, i, fhead);
		printf("return: %p\n\n", fp->ptr);
		**********************************************************/
				return fp->ptr;
			}
			if(fp == head ) fphead = fprev;
			fprev = fp;
			fp=fp->fnext;
		}//whileend
		//	if(head)	printf("%d:head:%p, head->free:%d\n", __LINE__, head, head->free);
	
	
	
	
	
	
	
	
	
	
	//if enough space for head
	if(size>40){
		//////////extend head/////////////
		if(head->free == 1){
				head->free = 0;
				int more= size - head->size;
			//	printf("%d:more: %d\n", __LINE__, more);
			//	printf("%d:head:%p, size - head->size:%ld, size: %zu, head->size: %zu\n",__LINE__,head, size - head->size, size, head->size);
				if(more < 0) more = size;
				if(sbrk(more)==(void *)-1) return NULL;
					head->end = sbrk(0);
					head->size = head->end - head->ptr;		
						
					if(head != fhead && fphead)fphead->fnext = head->fnext;
					else fhead = fhead->fnext;
					head->fnext = NULL;
					head->prev = NULL;
						end = sbrk(0);
						fn--;
	/***********************************************************
	metadata_t * p= head;
		printf("%dmalloc head == free: \n", __LINE__);
		int i=0;
		while(p){
		printf("%d: %dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p, fnext:%p\n",__LINE__, i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev,p->fnext);
		i++;
		p=p->next;
		}
		printf("freelist: \n");
					p= fhead;
					i=0;
					while(p){
							printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev);
						i++;
					 p=p->fnext;
					}
					printf("start: %p, end: %p, size: %ld, list num: %d, fhead: %p\n\n\n", start, end, end-start, i, fhead);
		printf("%d:hedblock: %p, ptr: %p size: %zu, free:%d, prev: %p, next:%p\n",__LINE__, head, head->ptr, head->size, head->free, head->prev, head->next);
		printf("start: %p, end: %p, size: %ld, list num: %d, fhead: %p\n", start, end, end-start, i, fhead);printf("return: %p\n\n", head->ptr);	
		***********************************************************/
						return head->ptr;}
		}//if fhead
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//set new list
	if(!head) start = sbrk(0);
	metadata_t * new = NULL;
	new = sbrk(0);
	sbrk(sizeof(metadata_t));
	new->ptr = sbrk(0);
	if(sbrk(size)==(void *)-1) return NULL;
	new->end = sbrk(0);
				
	new->size = size;
	new->free = 0;
	new->fnext = NULL;		
	if(head)head->prev = new;
	new->next = head;
	new->prev = NULL;
	head = new;
	//end = sbrk(0);
	/**********************************************************
metadata_t * p= head;
		printf("%d:malloc no head: \n", __LINE__);
		int i=0;
		while(p){
		printf("%d: %dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p\n",__LINE__, i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev);
		i++;
		p=p->next;
		}
		printf("freelist: \n");
					p= fhead;
					i=0;
					while(p){
							printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev);
						i++;
					 p=p->fnext;
					}
					printf("start: %p, end: %p, size: %ld, list num: %d, fhead: %p\n\n\n", start, end, end-start, i, fhead);
		printf("%d:hedblock: %p, ptr: %p size: %zu, free:%d, prev: %p, next:%p\n",__LINE__, head, head->ptr, head->size, head->free, head->prev, head->next);
		printf("start: %p, end: %p, size: %ld,  list num: %d\n", start, end, end-start, i);printf("return: %p\n\n", new->ptr);	
		**********************************************************/
	return new->ptr;
	
}

void *calloc(size_t num, size_t size) {

	void * ptr = malloc(num*size);
	if(ptr){memset(ptr, 0, num*size);}
	return ptr;
}
////////////////////////////////////////////////////////////free
void free(void *ptr) {
	if(end-start ==4 || end-start ==1 ) return;
	metadata_t * des = (metadata_t * )ptr -1;
//	printf("%d: des: %p, ptr: %p, head: %p, fhead: %p, prev: %p, next: %p \n",__LINE__, des, ptr, head, fhead, des->prev, des->next);
	//printf("head: %p, ptr: %p size: %zu, free:%d, prev: %p, next:%p\n", head, head->ptr, head->size, head->free, head->prev, head->next);
		
	des->free = 1;
	
	if(!fhead) {
	fn++;
		fhead = des; 
		des->fnext = NULL; 
		des->free = 1;
		/**********************************************************
	printf("%d: free no fhead: \n", __LINE__);
	metadata_t * p= head;
					int i=0;
					while(p){
							printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p, fnext: %p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev, p->fnext);
						i++;
					 p=p->next;
					}
					printf("start: %p, end: %p, size: %ld, list num: %d\n", start, end, end-start, i);
					printf("freelist: \n");
					p= fhead;
					i=0;
					while(p){
							printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev);
						i++;
					 p=p->fnext;
					}
					printf("start: %p, end: %p, size: %ld, list num: %d, fhead: %p\n\n\n", start, end, end-start, i, fhead);
					**********************************************************/
		return;
	}

	
	metadata_t * prev = des->prev;
	metadata_t * next = des->next;
		if(!((next && next->free == 1 )|| (prev && prev->free==1))){
		des ->fnext = fhead;
			fhead = des; 
			fn++;
			/***************** debugging *****************************************
				printf("%d: free no merge: \n", __LINE__);
						metadata_t * p= head;
					int i=0;
					printf("whole: \n");
					while(p){
						printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p, fnext: %p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev, p->fnext);
						i++;
					 p=p->next;
					}
					printf("freelist: \n");
					p= fhead;
					i=0;
					while(p){
							printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev);
						i++;
					 p=p->fnext;
					}
					printf("start: %p, end: %p, size: %ld, list num: %d, fhead: %p\n\n\n", start, end, end-start, i, fhead);
				**********************************************************/
			return;}
	metadata_t * p= fhead;
	int flag=0;
	if(prev){
		if(prev->free==1){
		//	printf("%d: prevfree\n", __LINE__);
			des->size = des->size+ prev->size+ (int)sizeof(metadata_t);
			des->end = prev->end;
		//	printf("des->prev:%p, des:%p, des->ptr: %p \n", des->prev, des, des->ptr);
			if(head == prev){head = des; des->prev = NULL;}
			if(prev->prev) prev->prev->next = des; prev->prev = NULL;
			if(fhead == prev){fhead = des;}
			else{
				for(size_t i=0; i<fn; i++){
				if(!p) break;
				if(p->fnext == prev){p->fnext = des;break;}
				p=p->fnext ;
			}
			}
			des->fnext = prev->fnext;
			des->prev = prev->prev;
			prev->prev = NULL;
			flag = 1;
		/**********************************************************
		p= head;
	int i=0;
	printf("whole: \n");
	while(p){
		printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p, fnext: %p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev, p->fnext);
		i++;
	 p=p->next;
	}
	printf("freelist: \n");
	p= fhead;
	i=0;
//	while(p){
			printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p, fnext:%p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev, p->fnext);
//		i++;
//	 p=p->fnext;
//	}
	printf("start: %p, end: %p, size: %ld, list num: %d\n", start, end, end-start, i);
	printf("prevfree end\n\n\n");
	**********************************************************/
		}
	}
	p= fhead;
	if(next){
		if(next->free==1){
	//	printf("%d: nextfree\n",__LINE__);
			next->size = next->size+des->size+ (int)sizeof(metadata_t);
			next->end  = des->end;
			next->prev = prev;
			if(des == head) {head = next; next->prev = NULL;}
			if(des == fhead || fhead == next) {fhead = next;}
			else {
				for(size_t i=0; i<fn; i++){
				if(!p) break;
				if(p->fnext== des){p->fnext = next;break;}
				p=p->fnext;
			}
			}
			if(flag == 1)next->fnext = des->fnext;
			if(prev) prev->next = next;
			/**********************************************************
			printf("freenext:\n des %p, ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p, fnext: %p\n", des, des->ptr, des->end, des->end-des->ptr, des->size, des->free, des->prev, des->fnext);
				printf("%d: next free: \n", __LINE__);
	p= head;
	int i=0;
	printf("whole: \n");
	while(p){
		printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p, fnext:%p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev, p->fnext);
		i++;
	 p=p->next;
	}
	printf("freelist: \n");
	p= fhead;
	i=0;
	while(p){
			printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev);
		i++;
	 p=p->fnext;
	}
	printf("start: %p, end: %p, size: %ld, list num: %d, fhead: %p\n\n\n", start, end, end-start, i, fhead);
	**********************************************************/
	
		}
	}
	

	/////////////////////free print
	/*********************************************************
		printf("%d: next prev free: \n", __LINE__);
	 p= head;
	int i=0;
	printf("whole: \n");
	while(p){
		printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p, fnext:%p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev, p->fnext);
		i++;
	 p=p->next;
	}
	printf("freelist: \n");
	p= fhead;
	i=0;
	while(p){
			printf("%dth block is %p,ptr: %p, end: %p, real size: %ld, size: %zu, free:%d, prev: %p\n", i, p, p->ptr,p->end, p->end-p->ptr, p->size, p->free, p->prev);
		i++;
	 p=p->fnext;
	}
	printf("start: %p, end: %p, size: %ld, list num: %d, fhead:%p\n\n\n", start, end, end-start, i, fhead);
	**********************************************************/
//////////////////////////free print
}


void *realloc(void *ptr, size_t size) {
  // implement realloc!	
	if (size == 0 && ptr == NULL){
		return NULL;
	}else if (ptr == NULL){
		return malloc(size);
	}else if (size == 0){
		free(ptr); return NULL;
	}
		metadata_t * p = (metadata_t *)ptr - 1;
		void* re = NULL;
	if (p->size >= size) {// if whole block is bigger than request size
		return ptr; 
	}else{// if whole block is smaller than request size
		
		//printf("afterfreerealloc\n");
		re = malloc(size);
		memcpy(re, p->ptr, p->size);
		free(ptr); 
		return re;
		}
		
	return NULL;
}

