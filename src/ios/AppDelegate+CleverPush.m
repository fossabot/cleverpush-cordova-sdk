#import "AppDelegate.h"
#import <CleverPush/CleverPush.h>

@implementation AppDelegate (CleverPush)

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    [CleverPush didRegisterForRemoteNotifications:application deviceToken:deviceToken];
}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
    [CleverPush handleDidFailRegisterForRemoteNotification:error];
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
    [CleverPush handlePushReceived:userInfo isActive:[application applicationState] == UIApplicationStateActive];
}

@end
