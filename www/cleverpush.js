var CleverPush = function() {
};

CleverPush.prototype.init = function(channelId, notificationOpenedCallback, subscribedCallback) {
  CleverPush._channelId = channelId;

  if (typeof notificationOpenedCallback === 'function') {
    cordova.exec(notificationOpenedCallback, function() {}, 'CleverPush', 'setNotificationOpenedHandler', []);
  }
  if (typeof subscribedCallback === 'function') {
    cordova.exec(subscribedCallback, function() {}, 'CleverPush', 'setSubscribedHandler', []);
  }

  var args = [channelId];
  cordova.exec(function() {}, function() {}, 'CleverPush', 'init', args);
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
