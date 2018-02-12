# Intallation

1. Add the required Cordova plugin

   ```
   cordova plugin add https://github.com/cleverpush/cleverpush-cordova-sdk.git
   ```


2. Add the initialization code to your `index.js` file

   ```javascript
   document.addEventListener('deviceready', function () {
     var notificationOpenedCallback = function(data) {
       console.log('notificationOpenedCallback: ' + JSON.stringify(data));
     };

     window.plugins.CleverPush.init("INSERT_YOUR_CHANNEL_ID", notificationOpenedCallback);
   }, false);
   ```

   Be sure to replace `INSERT_YOUR_CHANNEL_ID` with your CleverPush channel ID (can be found in the channel settings).
