package com.plugin.gcm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

import com.cleverpush.CleverPush;
import com.cleverpush.NotificationOpenedResult;
import com.cleverpush.listener.NotificationOpenedListener;

public class CleverPushPlugin extends CordovaPlugin {
  public static final String TAG = "CleverPushPlugin";

  public static final String INIT = "init";

  private static CallbackContext notificationOpenedCallbackContext;

  private static void callbackSuccess(CallbackContext callbackContext, JSONObject jsonObject) {
    if (jsonObject == null) {
      jsonObject = new JSONObject();
    }

    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, jsonObject);
    pluginResult.setKeepCallback(true);
    callbackContext.sendPluginResult(pluginResult);
  }

  private static void callbackError(CallbackContext callbackContext, JSONObject jsonObject) {
    if (jsonObject == null) {
      jsonObject = new JSONObject();
    }

    PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, jsonObject);
    pluginResult.setKeepCallback(true);
    callbackContext.sendPluginResult(pluginResult);
  }

  private static void callbackError(CallbackContext callbackContext, String str) {
    PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, str);
    pluginResult.setKeepCallback(true);
    callbackContext.sendPluginResult(pluginResult);
  }

  @Override
  public boolean execute(String action, JSONArray data, CallbackContext callbackContext) {
    if (action.equals(INIT)) {
      try {
        String channelId = data.getString(0);
        notificationOpenedCallbackContext = callbackContext;

        CleverPush.init(this.cordova.getActivity(), channelId, new CordovaNotificationOpenedHandler(notificationOpenedCallbackContext));
        return true;
      } catch (JSONException e) {
        Log.e(TAG, "execute: Got JSON Exception " + e.getMessage());
        return false;
      }
    } else {
      Log.e(TAG, "Invalid action: " + action);
      callbackError(callbackContext, "Invalid action : " + action);
      return false;
    }
  }

  private class CordovaNotificationOpenedHandler implements NotificationOpenedHandler {

    private CallbackContext callbackContext;

    public CordovaNotificationOpenedHandler(CallbackContext callbackContext) {
      this.callbackContext = callbackContext;
    }

    @Override
    public void notificationOpened(NotificationOpenedResult result) {
      try {
        callbackSuccess(callbackContext, new JSONObject(result.getData()));
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
  }

  @Override
  public void onDestroy() {
    CleverPush.removeNotificationOpenedListener();
  }
}