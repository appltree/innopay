
#import "RNInnopay.h"

#import <React/RCTConvert.h>
#import <Foundation/Foundation.h>

@interface RNInnopay() {
    RCTResponseSenderBlock callback;
}
@end

@implementation RNInnopay

//결제 결과 키값
NSString *RESULT_PAY_METHOD = @"PayMethod";
NSString *RESULT_MID = @"MID";
NSString *RESULT_TID = @"TID";
NSString *RESULT_MALL_USER_ID = @"mallUserID";
NSString *RESULT_AMT = @"Amt";
NSString *RESULT_NAME = @"name";
NSString *RESULT_GOODS_NAME = @"GoodsName";
NSString *RESULT_OID = @"OID";
NSString *RESULT_MOID = @"MOID";
NSString *RESULT_AUTH_DATE = @"AuthDate";
NSString *RESULT_AUTH_CODE = @"AuthCode";
NSString *RESULT_RESULT_CODE = @"ResultCode";
NSString *RESULT_RESULT_MSG = @"ResultMsg";
NSString *RESULT_MALL_RESERVED = @"MallReserved";
NSString *RESULT_FN_CD = @"fn_cd";
NSString *RESULT_FN_NAME = @"fn_name";
NSString *RESULT_CARD_QUOTA = @"CardQuota";
NSString *RESULT_BUYER_EMAIL = @"BuyerEmail";
NSString *RESULT_ERROR_MSG = @"ErrorMsg";

- (instancetype)init {
    self = [super init];
    if (self) {
    }
    return self;
}

+ (BOOL)requiresMainQueueSetup {
   return YES;
}

- (dispatch_queue_t)methodQueue {
   return dispatch_get_main_queue();
}


RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(pay:(NSDictionary *)params callback:(RCTResponseSenderBlock)cb) {
   
    callback = cb;
    UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
    
    PGViewController *pgViewController = [[PGViewController alloc] init];
    
    NSString *temp = [self makeStringPostParameter:params];
    pgViewController.param = [temp description];
    pgViewController.delegate = self;

    UINavigationController *navController = [[UINavigationController alloc] initWithRootViewController:pgViewController];
    [rootViewController presentViewController:navController animated:YES completion:nil];
}


- (void) dataReceived:(NSDictionary*)data {


    NSString *resultCode = data[RESULT_RESULT_CODE];
    NSString *errorMessage = data[RESULT_ERROR_MSG];

    if(resultCode == nil ) {
        callback(@[@"취소하였습니다.", [NSNull null]]);
    } else if(![resultCode isEqualToString:@"3001"] && ![resultCode isEqualToString:@"4000"] && ![resultCode isEqualToString:@"4100"]) {
        callback(@[errorMessage, [NSNull null]]);
    } else {
        callback(@[[NSNull null], @{
            @"Tid": data[RESULT_TID],
            @"Moid": data[RESULT_MOID],
            @"message": data[RESULT_RESULT_MSG],
        }]);
    }
}


- (void) errorReceived:(NSString*)error {
    callback(@[error, [NSNull null]]);
}


-(NSString*) makeStringPostParameter:(NSDictionary *)params {
    NSString *returnVal = @"";
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"MID=%@" , [params objectForKey:@"MID"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&Moid=%@" , [params objectForKey:@"Moid"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&GoodsName=%@" , [params objectForKey:@"GoodsName"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&Amt=%@" , [params objectForKey:@"Amt"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&DutyFreeAmt=%@" ,[params objectForKey:@"DutyFreeAmt"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&GoodsCnt=%@" , [params objectForKey:@"GoodsCnt"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&BuyerName=%@" , [params objectForKey:@"BuyerName"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&MallUserID=%@" , [params objectForKey:@"MallUserID"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&BuyerTel=%@" , [params objectForKey:@"BuyerTel"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&BuyerEmail=%@" , [params objectForKey:@"BuyerEmail"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&OfferingPeriod=%@" , [params objectForKey:@"OfferingPeriod"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&MerchantKey=%@" , [params objectForKey:@"MerchantKey"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&AppScheme=%@" , [params objectForKey:@"AppScheme"]]];
    returnVal = [returnVal stringByAppendingString : [NSString stringWithFormat: @"&PayMethod=%@" , [params objectForKey:@"PayMethod"]]];    
    return returnVal;
}

@end
  
