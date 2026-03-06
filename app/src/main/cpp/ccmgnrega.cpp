#include <jni.h>
#include <string>
#include <unordered_set>

#include <unistd.h>

using namespace std;

size_t pageSize = sysconf(_SC_PAGESIZE);

using namespace std;


const string BASE_URL = "https://nregarep2.nic.in/";


extern "C"
JNIEXPORT jstring
Java_nic_hp_ccmgnrega_common_Helper_getBaseURL(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(BASE_URL.c_str());
}

alignas(16384) char buffer[1024];
