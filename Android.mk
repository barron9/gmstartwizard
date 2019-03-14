LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_PACKAGE_NAME := GMSetupWizard
LOCAL_PRIVILEGED_MODULE := true
LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res
LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_CERTIFICATE := platform
LOCAL_PRIVATE_PLATFORM_APIS := true

include $(LOCAL_PATH)/setupwizard/library/common.mk
LOCAL_PROGUARD_ENABLED := disabled 
LOCAL_JAVA_LIBRARIES := telephony-common

LOCAL_STATIC_JAVA_LIBRARIES := \
	volley \
	gson \
	json \
	setup-wizard-lib 


include $(BUILD_PACKAGE)


include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES=gson:lib/gson.jar
include $(BUILD_MULTI_PREBUILT)



