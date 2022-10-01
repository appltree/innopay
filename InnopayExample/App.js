/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  useColorScheme,
  View,
  Button,
  Alert,
} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';

import RNInnopay from 'react-native-innopay';

const App = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        <View
          style={{
            backgroundColor: isDarkMode ? Colors.black : Colors.white,
          }}>
          <Button
            onPress={() => {
              RNInnopay.pay(
                {
                  MID: 'testpay01m',
                  Moid: 'moid12345678901',
                  PayMethod: 'CARD',
                  GoodsName: '테스트상품',
                  Amt: '200',
                  DutyFreeAmt: '0',
                  GoodsCnt: '1',
                  MallUserID: 'test001',
                  BuyerTel: '01012341234',
                  BuyerEmail: 'noemail@noemail.co.kr',
                  BuyerName: '테스트',
                  MerchantKey:
                    'Ma29gyAFhvv/+e4/AHpV6pISQIvSKziLIbrNoXPbRS5nfTx2DOs8OJve+NzwyoaQ8p9Uy1AN4S1I0Um5v7oNUg==',
                  OfferingPeriod: '없음',
                },
                (err, response) => {
                  if (err) {
                    Alert.alert('결제오류', err);
                  } else {
                    const tid = response['Tid'];

                    Alert.alert('결제성공', `tid ${tid}`);
                    // const moid = response['Moid'];
                    // const message = response['message'];
                  }
                },
              );
            }}
            title="Pay"
            color="#841584"
            accessibilityLabel="Learn more about this purple button"
          />
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
