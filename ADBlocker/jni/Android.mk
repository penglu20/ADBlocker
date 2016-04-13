LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := libadblockplus
LOCAL_SRC_FILES := ./libadblockplus-binaries/android_arm/libadblockplus.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := v8-base
LOCAL_SRC_FILES := ./libadblockplus-binaries/android_arm/libv8_base.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := v8-snapshot
LOCAL_SRC_FILES := ./libadblockplus-binaries/android_arm/libv8_snapshot.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := libadblocker
LOCAL_SRC_FILES := JniJsEngine.cpp JniFilterEngine.cpp JniJsValue.cpp
LOCAL_SRC_FILES += JniFilter.cpp JniSubscription.cpp
LOCAL_SRC_FILES += Utils.cpp

LOCAL_CPP_FEATURES := exceptions 
LOCAL_CFLAGS += -std=gnu++0x

LOCAL_C_INCLUDES := jni/libadblockplus-binaries/include/
LOCAL_STATIC_LIBRARIES := libadblockplus v8-base v8-snapshot

include $(BUILD_SHARED_LIBRARY)
