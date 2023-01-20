/*
 * @author: Sasikumar Bharanikumar
 * @version: 1.0
 */

#include "MemOpUtils.h"
#include "stdlib.h"
#include "stdio.h"
#include "string.h"
#include <unistd.h>
#include "android/log.h"

#define MEMOP_LOG_TAG "libjnimemop"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, MEMOP_LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, MEMOP_LOG_TAG, __VA_ARGS__)

#define MAX_SIZE 2000
static int *p[MAX_SIZE];
static int count = 0;

/*
 * @author: Sasikumar Bharanikumar
 * Method:    malloc
 * Signature: ()I
 */

JNIEXPORT jint JNICALL Java_com_discovery_thunderapp_MemOpUtils_malloc
        (JNIEnv *, jclass, jint size) {
    if (count < MAX_SIZE) {
        p[count] = (int *) malloc(256 * 1024 * size * sizeof(int));
        if (p[count]) {
            memset(p[count], 2, 256 * 1024 * size * sizeof(int));
            count++;
            LOGI("memfill, malloc memory size is %d", size);
            return size;
        } else {
            LOGI("memfill, malloc memory error");
        }
    } else {
        LOGE("memfill, count is MAX_SIZE");
    }
    return 0;
}

/*
 * Method:    free
 * Signature: ()I
 */
JNIEXPORT void JNICALL Java_com_ayst_stresstest_util_MemOpUtils_free
        (JNIEnv *, jclass) {
    for (int i = 0; i < MAX_SIZE; i++) {
        if (NULL != p[i]) {
            free(p[i]);
            p[i] = NULL;
        }
    }
    count = 0;
    LOGI("memfree, free memory");
    return;
}
