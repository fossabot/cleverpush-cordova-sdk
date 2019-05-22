var CleverPush = function() {
};

CleverPush.prototype.init = function(channelId, notificationOpenedCallback, subscribedCallback) {
  CleverPush._channelId = channelId;

  var args = [channelId];
  if (typeof notificationOpenedCallback === 'function') {
    args.push(notificationOpenedCallback);
  } else {
    args.push(function() {});
  }
  if (typeof subscribedCallback === 'function') {
    args.push(subscribedCallback);
  } else {
    args.push(function() {});
  }

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
