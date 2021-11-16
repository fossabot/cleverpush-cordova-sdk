var CleverPush = function() {
};

CleverPush.prototype.init = function(channelId, notificationReceivedCallback, notificationOpenedCallback, subscribedCallback) {
  CleverPush._channelId = channelId;

  if (typeof notificationReceivedCallback === 'function') {
    cordova.exec(notificationReceivedCallback, function() {}, 'CleverPush', 'setNotificationReceivedHandler', []);
  }
  if (typeof notificationOpenedCallback === 'function') {
    cordova.exec(notificationOpenedCallback, function() {}, 'CleverPush', 'setNotificationOpenedHandler', []);
  }
  if (typeof subscribedCallback === 'function') {
    cordova.exec(subscribedCallback, function() {}, 'CleverPush', 'setSubscribedHandler', []);
  }

  var args = [channelId];
  cordova.exec(function() {}, function() {}, 'CleverPush', 'init', args);
};

CleverPush.prototype.enableDevelopmentMode = function() {
  cordova.exec(function() {}, function() {}, 'CleverPush', 'enableDevelopmentMode', []);
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
