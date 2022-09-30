
# react-native-innopay

## Getting started

`$ npm install react-native-innopay --save`

### Mostly automatic installation

`$ react-native link react-native-innopay`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-innopay` and add `RNInnopay.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNInnopay.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNInnopayPackage;` to the imports at the top of the file
  - Add `new RNInnopayPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-innopay'
  	project(':react-native-innopay').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-innopay/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-innopay')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNInnopay.sln` in `node_modules/react-native-innopay/windows/RNInnopay.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Innopay.RNInnopay;` to the usings at the top of the file
  - Add `new RNInnopayPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNInnopay from 'react-native-innopay';

// TODO: What to do with the module?
RNInnopay;
```
  