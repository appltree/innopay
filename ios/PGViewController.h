#import <UIKit/UIKit.h>
#import <WebKit/WebKit.h>

@protocol sendBackDelegate
@required
- (void)dataReceived:(NSDictionary *)data;
- (void)errorReceived:(NSString *)error;
@end

@interface PGViewController : UIViewController <WKNavigationDelegate, WKUIDelegate, WKScriptMessageHandler>

@property(copy, nonatomic) NSString *param;
@property(weak, nonatomic) id<sendBackDelegate> delegate;

@end
