namespace EmarsysBinding;

public partial class Emarsys
{

	private static InternalAPI _internal = new InternalAPI(new PlatformAPI());

	public static void Setup(EMSConfig config)
	{
		_internal.Setup(config);
		
		TrackCustomEvent("wrapper:init", new Dictionary<string, string>
		{
			{ "type", "maui" },
			{ "version", Global.packageVersion },
			{ "frameworkVersion" , Global.frameworkVersion }
		});
	}

	public static Task<ErrorType?> SetContact(int contactFieldId, string contactFieldValue)
	{
		return _internal.SetContact(contactFieldId, contactFieldValue);
	}

	public static Task<ErrorType?> ClearContact()
	{
		return _internal.ClearContact();
	}

	public static Task<ErrorType?> TrackCustomEvent(string eventName, Dictionary<string, string>? eventAttributes = null)
	{
		return _internal.TrackCustomEvent(eventName, eventAttributes);
	}

	public static EmarsysConfig Config = new EmarsysConfig();

	public static EmarsysPush Push = new EmarsysPush();

	public static EmarsysInApp InApp = new EmarsysInApp();

	public static EmarsysInbox Inbox = new EmarsysInbox();

	public static EmarsysGeofence Geofence = new EmarsysGeofence();

	public static EmarsysPredict Predict = new EmarsysPredict();

}
