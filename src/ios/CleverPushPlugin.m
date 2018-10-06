#import <UIKit/UIKit.h>
#import <objc/runtime.h>

#import "CleverPushPlugin.h"
#import <CleverPush/CleverPush.h>

NSString* notficationOpenedCallbackId;
NSString* subscribedCallbackId;

CPNotificationOpenedResult* notificationOpenedResult;

id <CDVCommandDelegate> pluginCommandDelegate;

void successCallback(NSString* callbackId, NSDictionary* data) {
    CDVPluginResult* commandResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:data];
    commandResult.keepCallback = @1;
    [pluginCommandDelegate sendPluginResult:commandResult callbackId:callbackId];
}

void failureCallback(NSString* callbackId, NSDictionary* data) {
    CDVPluginResult* commandResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:data];
    commandResult.keepCallback = @1;
    [pluginCommandDelegate sendPluginResult:commandResult callbackId:callbackId];
}

void processNotificationOpened(CPNotificationOpenedResult* result) {
    NSString * data = [result stringify];
    NSError *jsonError;
    NSData *objectData = [data dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:objectData
                                                         options:NSJSONReadingMutableContainers
                                                           error:&jsonError];
    if (!jsonError) {
        successCallback(notficationOpenedCallbackId, json);
        notificationOpenedResult = nil;
    }
}

void initCleverPushObject(NSDictionary* launchOptions, const char* channelId) {
    NSString* channelIdStr = (channelId ? [NSString stringWithUTF8String:channelId] : nil);

    [CleverPush initWithLaunchOptions:launchOptions channelId:channelIdStr handleNotificationOpened:^(CPNotificationOpenedResult* openResult) {
        notificationOpenedResult = openResult;
        if (pluginCommandDelegate && notficationOpenedCallbackId != nil) {
            processNotificationOpened(openResult);
        }
    } handleSubscribed:^(NSString *subscriptionId) {
        if (pluginCommandDelegate && subscribedCallbackId != nil) {
            successCallback(subscribedCallbackId, subscriptionId);
        }
    }];
}

@implementation CleverPushPlugin

- (void)setNotificationOpenedHandler:(CDVInvokedUrlCommand*)command {
    notficationOpenedCallbackId = command.callbackId;
}

- (void)setSubscribedHandler:(CDVInvokedUrlCommand*)command {
    subscribedCallbackId = command.callbackId;
}

- (void)init:(CDVInvokedUrlCommand*)command {
    pluginCommandDelegate = self.commandDelegate;

    NSString* channelId = (NSString*)command.arguments[0];

    initCleverPushObject(nil, [channelId UTF8String]);

    if (notificationOpenedResult) {
        processNotificationOpened(notificationOpenedResult);
    }
}

@end
