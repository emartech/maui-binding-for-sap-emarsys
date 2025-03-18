namespace Test;

using EmarsysBinding.Model;

public class TestPredict
{

	private InternalAPIPredict _internal;
	private readonly Mock<IPlatformAPIPredict> _platformMock;

	public TestPredict()
	{
		_platformMock = new Mock<IPlatformAPIPredict>();
		_internal = new InternalAPIPredict(_platformMock.Object);
	}

	[Fact]
	public void TrackCart_ShouldWork()
	{
		_platformMock.Setup(mock => mock.TrackCart(It.IsAny<IList<CartItem>>()));

		List<CartItem> items = new List<CartItem> { new CartItem("testId", 1.1, 2.2) };
		_internal.TrackCart(items);

		_platformMock.Verify(mock => mock.TrackCart(items));
	}

	[Fact]
	public void TrackPurchase_ShouldWork()
	{
		_platformMock.Setup(mock => mock.TrackPurchase(It.IsAny<string>(), It.IsAny<IList<CartItem>>()));

		List<CartItem> items = new List<CartItem> { new CartItem("testId", 1.1, 2.2) };
		_internal.TrackPurchase("test", items);

		_platformMock.Verify(mock => mock.TrackPurchase("test", items));
	}

	[Fact]
	public void TrackItemView_ShouldWork()
	{
		_platformMock.Setup(mock => mock.TrackItemView(It.IsAny<string>()));

		_internal.TrackItemView("test");

		_platformMock.Verify(mock => mock.TrackItemView("test"));
	}

	[Fact]
	public void TrackCategoryView_ShouldWork()
	{
		_platformMock.Setup(mock => mock.TrackCategoryView(It.IsAny<string>()));

		_internal.TrackCategoryView("test");

		_platformMock.Verify(mock => mock.TrackCategoryView("test"));
	}

	[Fact]
	public void TrackSearchTerm_ShouldWork()
	{
		_platformMock.Setup(mock => mock.TrackSearchTerm(It.IsAny<string>()));

		_internal.TrackSearchTerm("test");

		_platformMock.Verify(mock => mock.TrackSearchTerm("test"));
	}

	[Fact]
	public void TrackTag_ShouldWork()
	{
		_platformMock.Setup(mock => mock.TrackTag(It.IsAny<string>(), It.IsAny<Dictionary<string, string>?>()));
		
		Dictionary<string, string> eventAttributes = new Dictionary<string, string>
		{
			{ "key1", "value1" },
			{ "key2", "value2" }
		};
		_internal.TrackTag("test", eventAttributes);

		_platformMock.Verify(mock => mock.TrackTag("test", eventAttributes));
	}

	[Fact]
	public async Task RecommendProducts_ShouldWork()
	{
		List<Product> resultProducts = new List<Product> { new Product(
			emsProduct: "testEMSProduct",
			productId: "testProductId",
			title: "testTitle",
			linkUrl: new Uri("https://testLinkUrl.com"),
			feature: "testFeature",
			cohort: "testCohort",
			customFields: new Dictionary<string, object> { { "testCustomFieldsKey", "testCustomFieldsValue" } }
		) };
		_platformMock.Setup(mock => mock.RecommendProducts(It.IsAny<Logic>(), It.IsAny<IList<Filter>?>(), It.IsAny<int?>(), It.IsAny<string?>(), It.IsAny<Action<IList<Product>?, string?>>()))
			.Callback((Logic _, IList<Filter>? _, int? _, string? _, Action<IList<Product>?, string?> onCompleted) => onCompleted(resultProducts, null));
		
		Logic logic = Logic.Search();
		List<Filter> filters = new List<Filter> { Filter.IncludeIsValue("testField", "testValue") };
		var result = await _internal.RecommendProducts(logic, filters, 3, "testAZ");

		_platformMock.Verify(mock => mock.RecommendProducts(logic, filters, 3, "testAZ", It.IsAny<Action<IList<Product>?, string?>>()));
		Assert.Equal(resultProducts, result.Products);
		Assert.Null(result.Error);
	}

	[Fact]
	public async Task RecommendProducts_ShouldWorkWithNullOptions()
	{
		List<Product> resultProducts = new List<Product> { new Product(
			emsProduct: "testEMSProduct",
			productId: "testProductId",
			title: "testTitle",
			linkUrl: new Uri("https://testLinkUrl.com"),
			feature: "testFeature",
			cohort: "testCohort",
			customFields: new Dictionary<string, object> { { "testCustomFieldsKey", "testCustomFieldsValue" } }
		) };
		_platformMock.Setup(mock => mock.RecommendProducts(It.IsAny<Logic>(), It.IsAny<IList<Filter>?>(), It.IsAny<int?>(), It.IsAny<string?>(), It.IsAny<Action<IList<Product>?, string?>>()))
			.Callback((Logic _, IList<Filter>? _, int? _, string? _, Action<IList<Product>?, string?> onCompleted) => onCompleted(resultProducts, null));
		
		Logic logic = Logic.Search();
		var result = await _internal.RecommendProducts(logic, null, null, null);

		_platformMock.Verify(mock => mock.RecommendProducts(logic, null, null, null, It.IsAny<Action<IList<Product>?, string?>>()));
		Assert.Equal(resultProducts, result.Products);
		Assert.Null(result.Error);
	}

	[Fact]
	public async Task RecommendProducts_ShouldWorkWithError()
	{
		_platformMock.Setup(mock => mock.RecommendProducts(It.IsAny<Logic>(), It.IsAny<IList<Filter>?>(), It.IsAny<int?>(), It.IsAny<string?>(), It.IsAny<Action<IList<Product>?, string?>>()))
			.Callback((Logic _, IList<Filter>? _, int? _, string? _, Action<IList<Product>?, string?> onCompleted) => onCompleted(null, "error"));
		
		Logic logic = Logic.Search();
		List<Filter> filters = new List<Filter> { Filter.IncludeIsValue("testField", "testValue") };
		var result = await _internal.RecommendProducts(logic, filters, 3, "testAZ");

		_platformMock.Verify(mock => mock.RecommendProducts(logic, filters, 3, "testAZ", It.IsAny<Action<IList<Product>?, string?>>()));
		Assert.Null(result.Products);
		Assert.Equal("error", result.Error);
	}

	[Fact]
	public void TrackRecommendationClick_ShouldWork()
	{
		_platformMock.Setup(mock => mock.TrackRecommendationClick(It.IsAny<Product>()));
		
		Product product = new Product(
			emsProduct: "testEMSProduct",
			productId: "testProductId",
			title: "testTitle",
			linkUrl: new Uri("https://testLinkUrl.com"),
			feature: "testFeature",
			cohort: "testCohort",
			customFields: new Dictionary<string, object> { { "testCustomFieldsKey", "testCustomFieldsValue" } }
		);
		_internal.TrackRecommendationClick(product);

		_platformMock.Verify(mock => mock.TrackRecommendationClick(product));
	}

}
