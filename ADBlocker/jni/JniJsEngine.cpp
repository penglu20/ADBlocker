/*
 * This file is part of Adblock Plus <https://adblockplus.org/>,
 * Copyright (C) 2006-2015 Eyeo GmbH
 *
 * Adblock Plus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation.
 *
 * Adblock Plus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Adblock Plus.  If not, see <http://www.gnu.org/licenses/>.
 */

#include <AdblockPlus.h>
#include "JniJsValue.h"
#include "Utils.h"

static void TransformAppInfo(JNIEnv* env, jobject jAppInfo, AdblockPlus::AppInfo& appInfo)
{
  jclass clazz = env->GetObjectClass(jAppInfo);

  appInfo.application = JniGetStringField(env, clazz, jAppInfo, "application");
  appInfo.applicationVersion = JniGetStringField(env, clazz, jAppInfo, "applicationVersion");
  appInfo.id = JniGetStringField(env, clazz, jAppInfo, "id");
  appInfo.locale = JniGetStringField(env, clazz, jAppInfo, "locale");
  appInfo.name = JniGetStringField(env, clazz, jAppInfo, "name");
  appInfo.version = JniGetStringField(env, clazz, jAppInfo, "version");

  appInfo.developmentBuild = JniGetBooleanField(env, clazz, jAppInfo, "developmentBuild");
}

static jlong JNICALL JniCtor(JNIEnv* env, jclass clazz, jobject jAppInfo)
{
  AdblockPlus::AppInfo appInfo;

  TransformAppInfo(env, jAppInfo, appInfo);

  try
  {
    return JniPtrToLong(new AdblockPlus::JsEnginePtr(AdblockPlus::JsEngine::New(appInfo)));
  }
  CATCH_THROW_AND_RETURN(env, 0)
}

static void JNICALL JniDtor(JNIEnv* env, jclass clazz, jlong ptr)
{
  delete JniLongToTypePtr<AdblockPlus::JsEnginePtr>(ptr);
}

static jobject JNICALL JniEvaluate(JNIEnv* env, jclass clazz, jlong ptr, jstring jSource, jstring jFilename)
{
  AdblockPlus::JsEnginePtr& engine = *JniLongToTypePtr<AdblockPlus::JsEnginePtr>(ptr);

  std::string source = JniJavaToStdString(env, jSource);
  std::string filename = JniJavaToStdString(env, jFilename);

  try
  {
    AdblockPlus::JsValuePtr jsValue = engine->Evaluate(source, filename);
    return NewJniJsValue(env, jsValue);
  }
  CATCH_THROW_AND_RETURN(env, 0)
}

static void JNICALL JniTriggerEvent(JNIEnv* env, jclass clazz, jlong ptr, jstring jEventName, jarray jJsPtrs)
{
  AdblockPlus::JsEnginePtr& engine = *JniLongToTypePtr<AdblockPlus::JsEnginePtr>(ptr);
  std::string eventName = JniJavaToStdString(env, jEventName);
  AdblockPlus::JsValueList args;

  if (jJsPtrs)
  {
    jlong* ptrs = (jlong*)env->GetPrimitiveArrayCritical(jJsPtrs, 0);

    jsize length = env->GetArrayLength(jJsPtrs);

    for (jsize i = 0; i < length; i++)
    {
      args.push_back(JniGetJsValuePtr(ptrs[i]));
    }

    env->ReleasePrimitiveArrayCritical(jJsPtrs, ptrs, JNI_ABORT);
  }

  try
  {
    engine->TriggerEvent(eventName, args);
  }
  CATCH_AND_THROW(env)
}

static void JNICALL JniSetDefaultFileSystem(JNIEnv* env, jclass clazz, jlong ptr, jstring jBasePath)
{
  AdblockPlus::JsEnginePtr& engine = *JniLongToTypePtr<AdblockPlus::JsEnginePtr>(ptr);

  try
  {
    AdblockPlus::FileSystemPtr fileSystem(new AdblockPlus::DefaultFileSystem());

    std::string basePath = JniJavaToStdString(env, jBasePath);
    reinterpret_cast<AdblockPlus::DefaultFileSystem*>(fileSystem.get())->SetBasePath(basePath);

    engine->SetFileSystem(fileSystem);
  }
  CATCH_AND_THROW(env)
}

static jobject JNICALL JniNewLongValue(JNIEnv* env, jclass clazz, jlong ptr, jlong value)
{
  AdblockPlus::JsEnginePtr& engine = *JniLongToTypePtr<AdblockPlus::JsEnginePtr>(ptr);

  try
  {
    AdblockPlus::JsValuePtr jsValue = engine->NewValue(static_cast<int64_t>(value));
    return NewJniJsValue(env, jsValue);
  }
  CATCH_THROW_AND_RETURN(env, 0)
}

static jobject JNICALL JniNewBooleanValue(JNIEnv* env, jclass clazz, jlong ptr, jboolean value)
{
  AdblockPlus::JsEnginePtr& engine = *JniLongToTypePtr<AdblockPlus::JsEnginePtr>(ptr);

  try
  {
    AdblockPlus::JsValuePtr jsValue = engine->NewValue(value == JNI_TRUE ? true : false);
    return NewJniJsValue(env, jsValue);
  }
  CATCH_THROW_AND_RETURN(env, 0)
}

static jobject JNICALL JniNewStringValue(JNIEnv* env, jclass clazz, jlong ptr, jstring value)
{
  AdblockPlus::JsEnginePtr& engine = *JniLongToTypePtr<AdblockPlus::JsEnginePtr>(ptr);

  try
  {
    std::string strValue = JniJavaToStdString(env, value);
    AdblockPlus::JsValuePtr jsValue = engine->NewValue(strValue);
    return NewJniJsValue(env, jsValue);
  }
  CATCH_THROW_AND_RETURN(env, 0)
}

// TODO: List of functions that lack JNI bindings
//JsValuePtr NewObject();
//JsValuePtr NewCallback(v8::InvocationCallback callback);
//static JsEnginePtr FromArguments(const v8::Arguments& arguments);
//JsValueList ConvertArguments(const v8::Arguments& arguments);

static JNINativeMethod methods[] =
{
  { (char*)"ctor", (char*)"(" TYP("AppInfo") ")J", (void*)JniCtor },
  { (char*)"dtor", (char*)"(J)V", (void*)JniDtor },

  { (char*)"triggerEvent", (char*)"(JLjava/lang/String;[J)V", (void*)JniTriggerEvent },

  { (char*)"evaluate", (char*)"(JLjava/lang/String;Ljava/lang/String;)" TYP("JsValue"), (void*)JniEvaluate },

  { (char*)"setDefaultFileSystem", (char*)"(JLjava/lang/String;)V", (void*)JniSetDefaultFileSystem },
  { (char*)"newValue", (char*)"(JJ)"TYP("JsValue"), (void*)JniNewLongValue },
  { (char*)"newValue", (char*)"(JZ)"TYP("JsValue"), (void*)JniNewBooleanValue },
  { (char*)"newValue", (char*)"(JLjava/lang/String;)"TYP("JsValue"), (void*)JniNewStringValue }
};

JAVA_PACKAGE_REGISTERNATIVES(JsEngine)
