# Android:

### 1. Add plugin
cordova plugin add https://github.com/mightymop/cordova-plugin-openfile.git
### 2. For Typescript add following code to main ts file: 
/// &lt;reference types="cordova-plugin-openfile" /&gt;<br/>
### 3. Usage:
```
window.openfile.open(filename: string,callback(res:string)=>any);

Example result:
result === null = OK else errormessage...
```
