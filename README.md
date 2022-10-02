# react-native-innopay

## Getting started

`$ npm install react-native-innopay --save`

## Usage

```javascript
import RNInnopay from "react-native-innopay";
RNInnopay.pay(
  {
    MID: "testpay01m",
    Moid: "moid1234567829012",
    PayMethod: "CARD",
    GoodsName: "테스트상품",
    Amt: "200",
    DutyFreeAmt: "0",
    GoodsCnt: "1",
    MallUserID: "test001",
    BuyerTel: "01012341234",
    BuyerEmail: "noemail@noemail.co.kr",
    BuyerName: "테스트",
    MerchantKey: "...",
    OfferingPeriod: "없음",
    AppScheme: "org.reactjs.native.example.InnopayExample", // only used in iOS
  },
  (err, response) => {
    if (err) {
      Alert.alert("결제오류", err);
    } else {
      const tid = response["Tid"];
      // const moid = response['Moid'];
      // const message = response['message'];
      Alert.alert("결제성공", `tid ${tid}`);
    }
  }
);
```
