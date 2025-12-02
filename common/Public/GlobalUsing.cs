#if ANDROID
global using EmarsysAndroid;
global using EventHandlerAction = System.Action<Android.Content.Context, string, Org.Json.JSONObject?>;
global using OnCompletedAction = System.Action<System.Exception?>;
global using ErrorType = System.Exception;
#elif IOS
global using EmarsysiOS;
global using EventHandlerAction = System.Action<Foundation.NSString, Foundation.NSDictionary<Foundation.NSString, Foundation.NSObject>?>;
global using OnCompletedAction = System.Action<System.Exception?>;
global using ErrorType = System.Exception;
#endif
global using EmarsysBinding.Internal;
global using System.Reflection;
global using System.Runtime.Versioning;

public static class Global
{
    public static string packageVersion = "0.1.3";
    public static string frameworkVersion = GetFrameworkVersion();

    private static string GetFrameworkVersion()
    {
        try
        {
            var mauiAssembly = typeof(Microsoft.Maui.Hosting.MauiApp).Assembly;

            var fullVersion =
                mauiAssembly.GetCustomAttribute<AssemblyInformationalVersionAttribute>()?.InformationalVersion ??
                mauiAssembly.GetCustomAttribute<AssemblyFileVersionAttribute>()?.Version ??
                mauiAssembly.GetName().Version?.ToString();

            if (fullVersion is null)
                return "unknown";

            // Strip commit hash suffix (anything after '+')
            var cleanVersion = fullVersion.Split('+')[0];

            return cleanVersion;
        }
        catch
        {
            var tfa = Assembly.GetExecutingAssembly()
                .GetCustomAttributes(typeof(TargetFrameworkAttribute), false)
                .OfType<TargetFrameworkAttribute>()
                .SingleOrDefault();

            return tfa?.FrameworkDisplayName ?? "unknown";
        }
    }
}
