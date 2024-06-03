#include <jni.h>
#include <string>
#include <thread>

extern "C"
JNIEXPORT jstring JNICALL
Java_github_kutouzi_actassistant_network_JsonFileNIO_receiveSwipeUpJsonFromQt(JNIEnv* env, jclass clazz) {
    // TODO:接受json数据文件，记得计算哈希验证
    std::string jsonResponse = "";

    return env->NewStringUTF(jsonResponse.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_github_kutouzi_actassistant_network_JsonFileNIO_sendSwipeUpJsonToQt(JNIEnv *env, jclass thiz, jstring json_data) {
    const char *json = env->GetStringUTFChars(json_data, 0);
    // TODO:寻找连接服务端，并且传输，记得计算哈希一起传输
    env->ReleaseStringUTFChars(json_data, json);
}