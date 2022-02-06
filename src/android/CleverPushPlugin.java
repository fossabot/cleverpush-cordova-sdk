package com.cleverpush.cordova;

import android.util.Log;

import com.cleverpush.CleverPush;
import com.cleverpush.NotificationOpenedResult;
import com.cleverpush.listener.NotificationOpenedListener;
import com.cleverpush.listener.NotificationReceivedCallbackListener;
import com.cleverpush.listener.NotificationReceivedListener;
import com.cleverpush.listener.SubscribedListener;
import com.google.gson.Gson;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

public class CleverPushPlugin extends CordovaPlugin {
  public static final String TAG = "CleverPushPlugin";

  private static CallbackContext receivedCallbackContext;
  private static CallbackContext openedCallbackContext;
  private static CallbackContext subscribedCallbackContext;

  private static final HashMap<String, Boolean> notificationReceivedCallbackResults = new HashMap<>();
  private static final HashMap<String, CountDownLatch> notificationReceivedCallbackLocks = new HashMap<>();
  private CordovaNotificationReceivedCallbackHandler notificationReceivedCallbackListener;

  private static void callbackSuccess(CallbackContext callbackContext, JSONObject jsonObject) {
    if (jsonObject == null) {
      jsonObject = new JSONObject();
    }

    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, jsonObject);
    pluginResult.setKeepCallback(true);
    callbackContext.sendPluginResult(pluginResult);
  }

  private static void callbackSuccess(CallbackContext callbackContext, String resultString) {
    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, resultString);
    pluginResult.setKeepCallback(true);
    callbackContext.sendPluginResult(pluginResult);
  }

  private static void callbackSuccess(CallbackContext callbackContext, boolean resultBoolean) {
    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, resultBoolean);
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
    Log.d(TAG, "CleverPush CDV execute: " + action);
    switch (action) {
      case "init":
        try {
          String channelId = data.getString(0);
          boolean autoRegister = data.optBoolean(1, true);

          NotificationReceivedListener receivedListener = null;
          if (receivedCallbackContext != null) {
            receivedListener = new CordovaNotificationReceivedHandler(receivedCallbackContext);
          }
          NotificationOpenedListener openedListener = null;
          if (openedCallbackContext != null) {
            openedListener = new CordovaNotificationOpenedHandler(openedCallbackContext);
          }
          SubscribedListener subscribedListener = null;
          if (subscribedCallbackContext != null) {
            subscribedListener = new CordovaSubscribedHandler(subscribedCallbackContext);
          }

          CleverPush.getInstance(this.cordova.getActivity()).init(
            channelId,
            receivedListener,
            openedListener,
            subscribedListener,
            autoRegister
          );
          return true;
        } catch (Exception e) {
          Log.e(TAG, "execute: Got Exception: " + e.getMessage());
          return false;
        }
      case "setNotificationReceivedHandler":
        receivedCallbackContext = callbackContext;
        return true;
      case "setNotificationReceivedCallbackHandler":
        this.notificationReceivedCallbackListener = new CordovaNotificationReceivedCallbackHandler(callbackContext);
        CleverPush.getInstance(this.cordova.getActivity()).setNotificationReceivedListener(
                this.notificationReceivedCallbackListener
        );
        return true;
      case "setNotificationReceivedCallbackResult":
        this.setNotificationReceivedCallbackResult(data.optString(0), data.optBoolean(1));
        return true;
      case "setNotificationOpenedHandler":
        openedCallbackContext = callbackContext;
        return true;
      case "setSubscribedHandler":
        subscribedCallbackContext = callbackContext;
        return true;
      case "enableDevelopmentMode":
        CleverPush.getInstance(this.cordova.getActivity()).enableDevelopmentMode();
        return true;
      case "subscribe":
        CleverPush.getInstance(this.cordova.getActivity()).subscribe();
        return true;
      case "unsubscribe":
        CleverPush.getInstance(this.cordova.getActivity()).unsubscribe();
        return true;
      case "isSubscribed":
        boolean isSubscribed = CleverPush.getInstance(this.cordova.getActivity()).isSubscribed();
        callbackSuccess(callbackContext, isSubscribed);
        return true;
      default:
        Log.e(TAG, "Invalid action: " + action);
        callbackError(callbackContext, "Invalid action: " + action);
        return false;
    }
  }

  private void setNotificationReceivedCallbackResult(String notificationId, Boolean showInForeground) {
    notificationReceivedCallbackResults.put(notificationId, showInForeground);
    CountDownLatch latch = notificationReceivedCallbackLocks.get(notificationId);
    if (latch != null) {
      latch.countDown();
    }
  }

  private class CordovaNotificationReceivedHandler implements NotificationReceivedListener {
    private final CallbackContext callbackContext;

    public CordovaNotificationReceivedHandler(CallbackContext callbackContext) {
      this.callbackContext = callbackContext;
    }

    @Override
    public void notificationReceived(NotificationOpenedResult result) {
      try {
        Gson gson = new Gson();

        JSONObject resultObj = new JSONObject();
        resultObj.put("notification", new JSONObject(gson.toJson(result.getNotification())));
        resultObj.put("subscription", new JSONObject(gson.toJson(result.getSubscription())));

        callbackSuccess(callbackContext, resultObj);
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
  }

  private class CordovaNotificationReceivedCallbackHandler extends NotificationReceivedCallbackListener {
    private final CallbackContext callbackContext;

    public CordovaNotificationReceivedCallbackHandler(CallbackContext callbackContext) {
      this.callbackContext = callbackContext;
    }

    @Override
    public boolean notificationReceivedCallback(NotificationOpenedResult result) {
      try {
        Gson gson = new Gson();

        JSONObject resultObj = new JSONObject();
        resultObj.put("notification", new JSONObject(gson.toJson(result.getNotification())));
        resultObj.put("subscription", new JSONObject(gson.toJson(result.getSubscription())));

        callbackSuccess(callbackContext, resultObj);

        CountDownLatch latch = new CountDownLatch(1);
        notificationReceivedCallbackLocks.put(result.getNotification().getId(), latch);

        latch.await();

        Boolean notificationCallbackResult = notificationReceivedCallbackResults.get(result.getNotification().getId());

        Log.d(TAG, "notificationCallbackResult: " + notificationCallbackResult.toString());

        notificationReceivedCallbackResults.remove(result.getNotification().getId());

        return notificationCallbackResult;
      } catch (Throwable t) {
        t.printStackTrace();
        return false;
      }
    }
  }

  private class CordovaNotificationOpenedHandler implements NotificationOpenedListener {
    private final CallbackContext callbackContext;

    public CordovaNotificationOpenedHandler(CallbackContext callbackContext) {
      this.callbackContext = callbackContext;
    }

    @Override
    public void notificationOpened(NotificationOpenedResult result) {
      try {
        Gson gson = new Gson();

        JSONObject resultObj = new JSONObject();
        resultObj.put("notification", new JSONObject(gson.toJson(result.getNotification())));
        resultObj.put("subscription", new JSONObject(gson.toJson(result.getSubscription())));

        callbackSuccess(callbackContext, resultObj);
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
  }

  private class CordovaSubscribedHandler implements SubscribedListener {
    private final CallbackContext callbackContext;

    public CordovaSubscribedHandler(CallbackContext callbackContext) {
      this.callbackContext = callbackContext;
    }

    @Override
    public void subscribed(String subscriptionId) {
      try {
        callbackSuccess(callbackContext, subscriptionId);
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
  }
}
