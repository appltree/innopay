package io.appltree.innopay;

import android.content.Context;
import android.widget.Toast;
import android.content.Intent;
import android.app.Activity;
// import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ActivityEventListener;

public class RNInnopayModule extends ReactContextBaseJavaModule implements ActivityEventListener {

  private final ReactApplicationContext reactContext;

  private static final int ACTIVITY_PAY = 1;
  
  private Callback callback;


  public RNInnopayModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    reactContext.addActivityEventListener(this); 
  }

  @Override
  public String getName() {
    return "RNInnopay";
  }

  @ReactMethod
  public void show(String text) {
    Context context = getReactApplicationContext();
    Toast.makeText(context,text, Toast.LENGTH_LONG).show();
  }

  @ReactMethod
  public void pay(ReadableMap params, final Callback cb) {

    callback = cb;
    Activity currentActivity = getCurrentActivity();

    if (currentActivity == null) {      
      callback.invoke("Activity doesn't exist", null);
      return;
    }

    Intent intent = new Intent(currentActivity, WebResultPayActivity.class);

    String MID = params.getString("MID");

    intent.putExtra("MID", params.getString("MID"));   // 발급 받은 mid 입력
    intent.putExtra("Moid", params.getString("Moid"));
    intent.putExtra("PayMethod", params.getString("PayMethod"));
    intent.putExtra("GoodsName", params.getString("GoodsName"));
    intent.putExtra("Amt", params.getString("Amt"));
    intent.putExtra("DutyFreeAmt", params.getString("DutyFreeAmt"));
    intent.putExtra("GoodsCnt", params.getString("GoodsCnt"));
    intent.putExtra("MallUserID", params.getString("MallUserID"));
    intent.putExtra("BuyerName", params.getString("BuyerName"));
    intent.putExtra("BuyerTel", params.getString("BuyerTel"));
    intent.putExtra("BuyerEmail", params.getString("BuyerEmail"));
    intent.putExtra("OfferingPeriod", params.getString("OfferingPeriod"));

    String merchantKey = params.getString("MerchantKey"); // 발급받은 라이센스키 입력
    merchantKey = merchantKey.replace("+", "%2B"); // + 기호 치환
    merchantKey = merchantKey.replace(" ", "+"); // + 기호 치환
    intent.putExtra("MerchantKey", merchantKey);
    
    currentActivity.startActivityForResult(intent, ACTIVITY_PAY);  
  }

  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == ACTIVITY_PAY && data != null) {
        /*
          * 사용자 환경에 따라 결제 결과를 받지 못하는 경우가 발생될 수 있으니
          * "InnopayPgNoti 메뉴얼 가이드" 문서를 참조하여 결제 결과 수신을 필수로 구현하여야 합니다.
          */

        // 결과코드
        // Log.d( TAG, "[Sample] resultCode: "
        //         + data.getStringExtra(ConstWebResult.RESULT_RESULT_CODE));
        // // 결과메시지
        // Log.d( TAG, "[Sample] resultMsg: "
        //         + data.getStringExtra(ConstWebResult.RESULT_RESULT_MSG));
        // // 상점 ID
        // Log.d( TAG, "[Sample] mid: "
        //         + data.getStringExtra(ConstWebResult.RESULT_MID));
        // // 주문번호
        // Log.d( TAG, "[Sample] moid: "
        //         + data.getStringExtra(ConstWebResult.RESULT_MOID));
        // // 주문번호(OID, MOID 동일)
        // Log.d( TAG, "[Sample] oid: "
        //         + data.getStringExtra(ConstWebResult.RESULT_OID));
        // // 거래번호
        // Log.d( TAG, "[Sample] tid: "
        //         + data.getStringExtra(ConstWebResult.RESULT_TID));
        // // 결제카드사코드
        // Log.d( TAG, "[Sample] fnCd: "
        //         + data.getStringExtra(ConstWebResult.RESULT_FN_CD));
        // // 결제카드사명
        // Log.d( TAG, "[Sample] fnName: "
        //         + data.getStringExtra(ConstWebResult.RESULT_FN_NAME));
        // // 승인일자(yymmddhhmmss)
        // Log.d( TAG, "[Sample] authDate: "
        //         + data.getStringExtra(ConstWebResult.RESULT_AUTH_DATE));
        // // 승인번호
        // Log.d( TAG, "[Sample] authCode: "
        //         + data.getStringExtra(ConstWebResult.RESULT_AUTH_CODE));
        // // 지불수단 [CARD:신용카드, BANK:계좌이체, VBANK:가상계좌]
        // Log.d( TAG, "[Sample] payMethod: "
        //         + data.getStringExtra(ConstWebResult.RESULT_PAY_METHOD));
        // //할부개월수
        // Log.d( TAG, "[Sample] cardQuota: "
        //         + data.getStringExtra(ConstWebResult.RESULT_CARD_QUOTA));
        // // 금액
        // Log.d( TAG, "[Sample] goods Amt: "
        //         + data.getStringExtra(ConstWebResult.RESULT_AMT));
        // // 상품명
        // Log.d( TAG, "[Sample] goods Name: "
        //         + data.getStringExtra(ConstWebResult.RESULT_GOODS_NAME));
        // // 결제자명
        // Log.d( TAG, "[Sample] buyer name: "
        //         + data.getStringExtra(ConstWebResult.RESULT_NAME));
        // // 상점예비정보
        // Log.d( TAG, "[Sample] mallReserved: "
        //         + data.getStringExtra(ConstWebResult.RESULT_MALL_RESERVED));
        // // 고객사회원 ID
        // Log.d( TAG, "[Sample] mallUserId: "
        //         + data.getStringExtra(ConstWebResult.RESULT_MALL_USER_ID));
        // // 구매자 이메일주소
        // Log.d( TAG, "[Sample] buyerEmail: "
        //         + data.getStringExtra(ConstWebResult.RESULT_BUYER_EMAIL));
        // // 구매자 인증번호
        // Log.d( TAG, "[Sample] buyerAuthNum: "
        //         + data.getStringExtra(ConstWebResult.RESULT_BUYER_AUTH_NUM));
        // // 결과 에러코드
        // Log.d( TAG, "[Sample] errorCode: "
        //         + data.getStringExtra(ConstWebResult.RESULT_ERROR_CODE));
        // // 결제 에러메시지
        // Log.d( TAG, "[Sample] errorMsg: "
        //         + data.getStringExtra(ConstWebResult.RESULT_ERROR_MSG));

        String payResultCode = data.getStringExtra(ConstWebResult.RESULT_RESULT_CODE);

        if (!payResultCode.equals("3001") && !payResultCode.equals("4000") && !payResultCode.equals("4100")) {
            String errorMsg = data.getStringExtra(ConstWebResult.RESULT_ERROR_MSG);
            callback.invoke(errorMsg, null);
        } else {
            String message = data.getStringExtra(ConstWebResult.RESULT_RESULT_MSG);
            WritableMap response = Arguments.createMap();
            response.putString("Tid", data.getStringExtra(ConstWebResult.RESULT_TID));
            response.putString("Moid", data.getStringExtra(ConstWebResult.RESULT_MOID));
            response.putString("message", message);
            callback.invoke(null, response);
        }
      }
      // 결제 결과를 받지 못할 경우
      else {
        callback.invoke("결과 알수 없음 (취소 또는 오류 발생)", null);
      }
    }
  }

  @Override
  public void onNewIntent(Intent intent) {
  }

}