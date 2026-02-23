#if ANDROID
global using EmarsysAndroid;
global using EmarsysAndroid.Model;
global using EventHandlerAction = System.Action<Android.Content.Context, string, Org.Json.JSONObject?>;
global using OnCompletedAction = System.Action<System.Exception?>;
global using ErrorType = System.Exception;
#elif IOS
global using EmarsysiOS;
global using EventHandlerAction = System.Action<Foundation.NSString, Foundation.NSDictionary<Foundation.NSString, Foundation.NSObject>?>;
global using OnCompletedAction = System.Action<System.Exception?>;
global using ErrorType = System.Exception;
#else
global using EventHandlerAction = System.Action<string>;
global using OnCompletedAction = System.Action<string>;
global using ErrorType = string;
#endif
