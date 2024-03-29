# Yelp Demo App
Basic implementation of [yelp](https://www.yelp.com/developers/documentation/v3/business) business endpoint api.

## Libraries Used
- Dependency Injection
	- [Dagger 2](https://github.com/google/dagger)
- Network
	- [Retrofit](https://square.github.io/retrofit/), [RxJava & RxAndroid](https://github.com/ReactiveX/RxAndroid)
	- OkHttp with caching
- Others
	- [Picasso](https://square.github.io/picasso/)
	- [Timber](https://github.com/JakeWharton/timber) for logs.
	- Google Places API
	- [Lottie](https://lottiefiles.com/)
	- Android Architecture Components

## Testing
I  don't think its necessary to use an espresso testing here so I just provide a unit testing on my ViewModels.

## Continues Integration
* Continuous Integration using CircleCi
![](https://github.com/gilbertparreno/droid-yelp/blob/master/circle_ci_results.png)
