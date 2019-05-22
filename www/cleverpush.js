var CleverPush = function() {
};

CleverPush.prototype.init = function(channelId, notificationOpenedCallback, subscribedCallback) {
  CleverPush._channelId = channelId;

  if (typeof notificationOpenedCallback === 'function') {
    cordova.exec(function() {}, function() {}, 'CleverPush', 'setNotificationOpenedHandler', args);
  } else {
    args.push(function() {});
  }
  if (typeof subscribedCallback === 'function') {
    cordova.exec(function() {}, function() {}, 'CleverPush', 'setSubscribedHandler', args);
  } else {
    args.push(function() {});
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
