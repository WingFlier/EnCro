#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_wingfly_com_encro_Constants_stringFromJNI(JNIEnv *env, jobject instance)
{
    std::string key = "euAPWIEgufGyylZy";
    return env->NewStringUTF(key.c_str());
}