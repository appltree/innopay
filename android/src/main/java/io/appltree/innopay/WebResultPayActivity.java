package io.appltree.innopay;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URISyntaxException;

public class WebResultPayActivity extends AppCompatActivity {
    private static final String TAG = "WebResultPayActivity";
    private static final String PAY_URL = "https://pg.innopay.co.kr/ipay/appLink.jsp"; // 웹뷰 연동 파일
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_result);

        progressBar = new ProgressBar(this);

        Intent intent = getIntent();
        String params = "&MID=" + intent.getStringExtra("MID") +
                "&Moid=" + intent.getStringExtra("Moid") +
                "&PayMethod=" + intent.getStringExtra("PayMethod") +
                "&GoodsName=" + intent.getStringExtra("GoodsName") +
                "&GoodsCnt=" + intent.getStringExtra("GoodsCnt") +
                "&Amt=" + intent.getStringExtra("Amt") +
                "&DutyFreeAmt=" + intent.getStringExtra("DutyFreeAmt") +
                "&BuyerName=" + intent.getStringExtra("BuyerName") +
                "&BuyerTel=" + intent.getStringExtra("BuyerTel") +
                "&BuyerEmail=" + intent.getStringExtra("BuyerEmail") +
                "&MallUserID=" + intent.getStringExtra("MallUserID") +
                "&OfferingPeriod=" + intent.getStringExtra("OfferingPeriod") +
                "&MerchantKey=" + intent.getStringExtra("MerchantKey");
        initWebView(params.getBytes());


    }

    private void initWebView(byte[] data) {
        webView = (WebView) findViewById(R.id.wv_pay);
        webView.setInitialScale(50);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.addJavascriptInterface(new PayBridge(), "PayAppBridge");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSavePassword(false);
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() + " Webv");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new CustomWebViewClient());
        // Web view cookie 허용 설정
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        setCookieAllow();
        // 디버깅 모드 설정
        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.postUrl(PAY_URL, data);
    }

    private void setCookieAllow() {
        try {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                cookieManager.setAcceptThirdPartyCookies(webView, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private class CustomWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {   //해당 앱 설치 여부 판단 후 미설치 시 해당 마켓 플레이스로 보냄.
            if (url != null && url.startsWith("intent://")) {
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                    if (existPackage != null) {
                        startActivity(intent);
                    } else {
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                        marketIntent.setData(Uri.parse("market://details?id="+intent.getPackage()));
                        startActivity(marketIntent);
                    }
                    return true;
                }catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (url != null && url.startsWith("market://")) {
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    if (intent != null) {
                        startActivity(intent);
                    }
                    return true;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else if (url != null && url != "ansimclick.hyundiacard.co"
                    && (url.contains("http://market.android.com")
                    || url.contains("vguard")
                    || url.contains("droidxantivirus")
                    || url.contains("smhyundaiansimclick://")
                    || url.contains("smshinhanansimclick://")
                    || url.contains("smshinhancardusim://")
                    || url.contains("smartwall://")
                    || url.contains("appfree://")
                    || url.contains("market://")
                    || url.contains("v3mobile") || url.endsWith(".apk")
                    || url.contains("tel:")
                    || url.contains("ansimclick") || url.contains("http://m.ahnlab.com/kr/site/download"))) {
                try {

                    // 인텐트 정합성 체크 : 2014 .01추가
                    Intent intent = null;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        Log.e("intent getScheme     >", intent.getScheme());
                        Log.e("intent getDataString >", intent.getDataString());
                    } catch (URISyntaxException ex) {
                        Log.e("Browser", "Bad URI " + url + ":" + ex.getMessage());
                        return false;
                    }
                    //chrome 버젼 방식 : 2014.01 추가
                    if (url.startsWith("intent")) { // chrome 버젼 방식
                        // 앱설치 체크를 합니다.
                        if (getPackageManager().resolveActivity(intent, 0) == null) {
                            String packagename = intent.getPackage();
                            if (packagename != null) {
                                Uri uri = Uri.parse("market://search?q=pname:"
                                        + packagename);
                                intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                return true;
                            }
                        }

                        //구동방식은 PG:쇼핑몰에서 결정하세요.
                        int runType=1;

                        if (runType == 1) {
                            Uri uri = Uri.parse(intent.getDataString());
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        } else {
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setComponent(null);
                            try {
                                if (startActivityIfNeeded(intent, -1)) {
                                    return true;
                                }
                            } catch (ActivityNotFoundException ex) {
                                return false;
                            }
                        }
                    } else { // 구 방식
                        Uri uri = Uri.parse(url);
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                } catch (ActivityNotFoundException e) {
                    Log.e("error ===>", e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            } else {
                view.loadUrl(url);
                return false;
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(WebResultPayActivity.this ,
                    failingUrl + " 접근시도에 실패하엿습니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            new AlertDialog.Builder(getApplicationContext())
                    .setMessage("유효하지 않는 인증서 입니다." +
                            "\n무시하고 진행을 원하시면 \'진행\'" +
                            "\n취소하고싶으면 취소버튼을 눌러주세요.")
                    .setPositiveButton("진행", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                            handler.proceed();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                            handler.cancel();
                        }
                    })
                    .create().show();
        }
    }

    /**
     * 웹 뷰 브릿지 클래스
     */
    private final class PayBridge {

        @JavascriptInterface
        public void PayResult(final String msg){
            // 결제결과 데이터를 이전 결제 요청 화면으로 돌려줍니다.
            resultCallback(msg);
        }

        @JavascriptInterface
        public void backpage() {
          // 이전 결제 요청 화면으로 되돌아갑니다.
          // startActivity(new Intent(WebResultPayActivity.this, WebPayActivity.class));
          finish();
        }

        /**
         * 결제 결과 데이터를 포함한 이전 결제 요청 화면으로 전환 처리
         * @param resultData 이전 화면으로 전달될 결제 결과 데이터
         */
        public void resultCallback(String resultData) {
            if (resultData != null) {
                try {
                    Intent intent = new Intent();
                    JSONObject jsonObj = new JSONObject(resultData);
                    intent.putExtra(ConstWebResult.RESULT_PAY_METHOD,
                            jsonObj.optString(ConstWebResult.RESULT_PAY_METHOD));
                    intent.putExtra(ConstWebResult.RESULT_MID,
                            jsonObj.optString(ConstWebResult.RESULT_MID));
                    intent.putExtra(ConstWebResult.RESULT_TID,
                            jsonObj.optString(ConstWebResult.RESULT_TID));
                    intent.putExtra(ConstWebResult.RESULT_MALL_USER_ID,
                            jsonObj.optString(ConstWebResult.RESULT_MALL_USER_ID));
                    intent.putExtra(ConstWebResult.RESULT_AMT,
                            jsonObj.optString(ConstWebResult.RESULT_AMT));
                    intent.putExtra(ConstWebResult.RESULT_NAME,
                            jsonObj.optString(ConstWebResult.RESULT_NAME));
                    intent.putExtra(ConstWebResult.RESULT_GOODS_NAME,
                            jsonObj.optString(ConstWebResult.RESULT_GOODS_NAME));
                    intent.putExtra(ConstWebResult.RESULT_OID,
                            jsonObj.optString(ConstWebResult.RESULT_OID));
                    intent.putExtra(ConstWebResult.RESULT_MOID,
                            jsonObj.optString(ConstWebResult.RESULT_MOID));
                    intent.putExtra(ConstWebResult.RESULT_AUTH_DATE,
                            jsonObj.optString(ConstWebResult.RESULT_AUTH_DATE));
                    intent.putExtra(ConstWebResult.RESULT_AUTH_CODE,
                            jsonObj.optString(ConstWebResult.RESULT_AUTH_CODE));
                    intent.putExtra(ConstWebResult.RESULT_RESULT_CODE,
                            jsonObj.optString(ConstWebResult.RESULT_RESULT_CODE));
                    intent.putExtra(ConstWebResult.RESULT_RESULT_MSG,
                            jsonObj.optString(ConstWebResult.RESULT_RESULT_MSG));
                    intent.putExtra(ConstWebResult.RESULT_MALL_RESERVED,
                            jsonObj.optString(ConstWebResult.RESULT_MALL_RESERVED));
                    intent.putExtra(ConstWebResult.RESULT_FN_CD,
                            jsonObj.optString(ConstWebResult.RESULT_FN_CD));
                    intent.putExtra(ConstWebResult.RESULT_FN_NAME,
                            jsonObj.optString(ConstWebResult.RESULT_FN_NAME));
                    intent.putExtra(ConstWebResult.RESULT_CARD_QUOTA,
                            jsonObj.optString(ConstWebResult.RESULT_CARD_QUOTA));
                    intent.putExtra(ConstWebResult.RESULT_BUYER_EMAIL,
                            jsonObj.optString(ConstWebResult.RESULT_BUYER_EMAIL));
                    intent.putExtra(ConstWebResult.RESULT_BUYER_AUTH_NUM,
                            jsonObj.optString(ConstWebResult.RESULT_BUYER_AUTH_NUM));
                    intent.putExtra(ConstWebResult.RESULT_ERROR_CODE,
                            jsonObj.optString(ConstWebResult.RESULT_ERROR_CODE));
                    intent.putExtra(ConstWebResult.RESULT_ERROR_MSG,
                            jsonObj.optString(ConstWebResult.RESULT_ERROR_MSG));
                    setResult(RESULT_OK, intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    setResult(RESULT_CANCELED);
                }
            } else {
                setResult(RESULT_CANCELED);
            }

            finish();
        }
    }
}
