#import <UIKit/UIKit.h>
#import <objc/runtime.h>

#import "CleverPushPlugin.h"
#import <CleverPush/CleverPush.h>

NSString* notficationOpenedCallbackId;

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
    if(!jsonError) {
        successCallback(notficationOpenedCallbackId, json);
        notificationOpenedResult = nil;
    }
}

void initCleverPushObject(NSDictionary* launchOptions, const char* channelId, BOOL autoPrompt) {
    NSString* channelIdStr = (channelId ? [NSString stringWithUTF8String:channelId] : nil);

    [CleverPush initWithLaunchOptions:launchOptions channelId:channelIdStr handleNotificationOpened:^(CPNotificationOpenedResult* openResult) {
            notificationOpenedResult = openResult;
            if (pluginCommandDelegate) {
                processNotificationOpened(openResult);
            }
        } settings:@{@"autoPrompt": @(autoPrompt)}];
}

@implementation CleverPushPlugin

- (void)setNotificationOpenedHandler:(CDVInvokedUrlCommand*)command {
    notficationOpenedCallbackId = command.callbackId;
}

- (void)init:(CDVInvokedUrlCommand*)command {
    pluginCommandDelegate = self.commandDelegate;

    NSString* appId = (NSString*)command.arguments[0];
    NSDictionary* settings = command.arguments[2] == [NSNull null] ? @{} : (NSDictionary*)command.arguments[2];

    BOOL autoPrompt = settings[@"autoPrompt"] ? [(NSNumber*)settings[@"autoPrompt"] boolValue] : YES;

    int displayOption = [(NSNumber*)command.arguments[3] intValue];

    initCleverPushObject(nil, [channelId UTF8String], autoPrompt, NO);

    if (notification)
        processNotificationReceived(notification);
    if (notificationOpenedResult)
        processNotificationOpened(notificationOpenedResult);
}

- (void)registerForPushNotifications:(CDVInvokedUrlCommand*)command {
    [CleverPush registerForPushNotifications];
}

@end
