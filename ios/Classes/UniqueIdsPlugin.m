#import "UniqueIdsPlugin.h"
#import <AdSupport/ASIdentifierManager.h>

@implementation UniqueIdsPlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"unique_ids"
                                     binaryMessenger:[registrar messenger]];
    UniqueIdsPlugin* instance = [[UniqueIdsPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    if ([call.method isEqualToString:@"adId"]) {
        NSUUID *adid = [[ASIdentifierManager sharedManager] advertisingIdentifier];
        result(adid.UUIDString);
    } else if ([call.method isEqualToString:@"uuid"]) {
        result([NSUUID UUID].UUIDString);
    } else {
        result(FlutterMethodNotImplemented);
    }
}


@end
