var CleverPush = function() {
};

CleverPush.prototype.init = function(channelId, notificationReceivedListener, notificationOpenedListener, subscribedListener, autoRegisterParam) {
  CleverPush._channelId = channelId;

  if (typeof notificationReceivedListener === 'function') {
    cordova.exec(notificationReceivedListener, function() {}, 'CleverPush', 'setNotificationReceivedHandler', []);
  }
  if (typeof notificationOpenedListener === 'function') {
    cordova.exec(notificationOpenedListener, function() {}, 'CleverPush', 'setNotificationOpenedHandler', []);
  }
  if (typeof subscribedListener === 'function') {
    cordova.exec(subscribedListener, function() {}, 'CleverPush', 'setSubscribedHandler', []);
  }

  var autoRegister = typeof autoRegisterParam === 'undefined' ? true : autoRegisterParam;

  var args = [channelId, autoRegister];

  cordova.exec(function() {}, function() {}, 'CleverPush', 'init', args);
};

CleverPush.prototype.setNotificationReceivedListener = function(notificationReceivedListener) {
  cordova.exec(notificationReceivedListener, function() {}, 'CleverPush', 'setNotificationReceivedHandler', []);
};

// Only for Android
CleverPush.prototype.setNotificationReceivedCallbackListener = function(notificationReceivedListener) {
  cordova.exec(notificationReceivedListener, function() {}, 'CleverPush', 'setNotificationReceivedCallbackHandler', []);
};
CleverPush.prototype.setNotificationReceivedCallbackResult = function(notificationId, showInForeground) {
  cordova.exec(function() {}, function() {}, 'CleverPush', 'setNotificationReceivedCallbackResult', [notificationId, showInForeground]);
};

CleverPush.prototype.setNotificationOpenedListener = function(notificationOpenedListener) {
  cordova.exec(notificationOpenedListener, function() {}, 'CleverPush', 'setNotificationOpenedHandler', []);
};

CleverPush.prototype.setSubscribedListener = function(subscribedListener) {
  cordova.exec(subscribedListener, function() {}, 'CleverPush', 'setSubscribedHandler', []);
};

CleverPush.prototype.setAppBannerOpenedListener = function(subscribedListener) {
  cordova.exec(subscribedListener, function() {}, 'CleverPush', 'setAppBannerOpenedHandler', []);
};

CleverPush.prototype.enableDevelopmentMode = function() {
  cordova.exec(function() {}, function() {}, 'CleverPush', 'enableDevelopmentMode', []);
};

CleverPush.prototype.showTopicsDialog = function() {
  cordova.exec(function() {}, function() {}, 'CleverPush', 'showTopicsDialog', []);
};

CleverPush.prototype.subscribe = function(subscribeListener) {
  cordova.exec(function(subscriptionId) {
    if (typeof subscribeListener === 'function') {
      subscribeListener(null, subscriptionId);
    }
  }, function(error) {
    if (typeof subscribeListener === 'function') {
      subscribeListener(error);
    }
  }, 'CleverPush', 'subscribe', []);
};

CleverPush.prototype.unsubscribe = function() {
  cordova.exec(function() {}, function() {}, 'CleverPush', 'unsubscribe', []);
};

CleverPush.prototype.isSubscribed = function(callback) {
  cordova.exec(callback, function() {}, 'CleverPush', 'isSubscribed', []);
};

CleverPush.prototype.getSubscriptionId = function (callback) {
  cordova.exec(callback, function () {}, 'CleverPush', 'getSubscriptionId', []);
};

if (!window.plugins) {
  window.plugins = {};
}

if (!window.plugins.CleverPush) {
  window.plugins.CleverPush = new CleverPush();
}

if (typeof module !== 'undefined' && module.exports) {
  module.exports = CleverPush;
}
