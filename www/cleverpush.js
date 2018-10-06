var CleverPush = function() {
};

CleverPush.prototype.init = function(channelId, notificationOpenedCallback, iOSSettings) {
  CleverPush._channelId = channelId;

  var args = [channelId];
  if (typeof notificationOpenedCallback === 'function') {
    args.push(notificationOpenedCallback);
  } else {
    args.push(function() {});
  }
  if (typeof iOSSettings === 'object') {
    args.push(iOSSettings);
  }

  cordova.exec(function() {}, function() {}, 'CleverPush', 'init', args);
};

// For iOS if autoPrompt is false
CleverPush.prototype.registerForPushNotifications = function() {
  cordova.exec(function() {}, function() {}, 'CleverPush', 'registerForPushNotifications', []);
};


if (!window.plugins) {
  window.plugins = {};
}

if (!window.plugins.CleverPush) {
  window.plugins.CleverPush = new CleverPush();
}

if (typeof module != 'undefined' && module.exports) {
  module.exports = CleverPush;
}
