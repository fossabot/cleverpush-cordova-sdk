#import <Foundation/Foundation.h>
#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>

@interface CleverPushPlugin : CDVPlugin {}

- (void)setNotificationOpenedHandler:(CDVInvokedUrlCommand*)command;
- (void)setNotificationReceivedHandler:(CDVInvokedUrlCommand*)command;
- (void)setSubscribedHandler:(CDVInvokedUrlCommand*)command;
- (void)setAppBannerOpenedHandler:(CDVInvokedUrlCommand*)command;
- (void)init:(CDVInvokedUrlCommand*)command;
- (void)enableDevelopmentMode:(CDVInvokedUrlCommand*)command;
- (void)showTopicsDialog:(CDVInvokedUrlCommand*)command;
- (void)subscribe:(CDVInvokedUrlCommand*)command;
- (void)unsubscribe:(CDVInvokedUrlCommand*)command;
- (void)isSubscribed:(CDVInvokedUrlCommand*)command;
- (void)getSubscriptionId:(CDVInvokedUrlCommand*)command;

@end
