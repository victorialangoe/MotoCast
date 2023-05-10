package com.example.motocast.data_injection

import android.app.Application
import android.content.Context
import com.example.motocast.BuildConfig
import com.example.motocast.data.api.*
import com.example.motocast.data.remote.RemoteDataSource
import com.example.motocast.data.repository.MotoCastRepository
import com.example.motocast.domain.*
import com.example.motocast.domain.use_cases.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * This is the module where we provide all the dependencies for the app to use.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMotoCastRepository(
        addressesApi: AddressesApi,
        directionsApi: DirectionsApi,
        metAlertsApi: MetAlertsApi,
        nowCastApi: NowCastApi,
        reverseGeocodingApi: ReverseGeocodingApi,
        locationForecastApi: LocationForecastApi,
        @ApplicationContext appContext: Context
    ): MotoCastRepository {
        return MotoCastRepository(
            remoteDataSource = RemoteDataSource(
                addressesApi = addressesApi,
                directionsApi = directionsApi,
                metAlertsApi = metAlertsApi,
                nowCastApi = nowCastApi,
                reverseGeocodingApi = reverseGeocodingApi,
                locationForecastApi = locationForecastApi
            ),
            appContext = appContext,
        )
    }

    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext app: Context): Application {
        return app.applicationContext as Application
    }

    @Provides
    @Singleton
    fun provideGetResoursesUseCase(
        motoCastRepository: MotoCastRepository
    ): GetResourcesUseCase {
        return GetResourcesUseCase(motoCastRepository)
    }
    @Provides
    @Singleton
    fun provideRemoteDataSource(
        addressesApi: AddressesApi,
        directionsApi: DirectionsApi,
        metAlertsApi: MetAlertsApi,
        nowCastApi: NowCastApi,
        reverseGeocodingApi: ReverseGeocodingApi,
        locationForecastApi: LocationForecastApi
    ): RemoteDataSource {
        return RemoteDataSource(
            addressesApi = addressesApi,
            directionsApi = directionsApi,
            metAlertsApi = metAlertsApi,
            nowCastApi = nowCastApi,
            reverseGeocodingApi = reverseGeocodingApi,
            locationForecastApi = locationForecastApi
        )
    }

    @Provides
    @Singleton
    fun provideAddressApi(): AddressesApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.ADDRESS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(AddressesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNowCastApi(): NowCastApi {

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.MET_API_BASE_URL)
            .client(metClientBuilder)
            .addConverterFactory(GsonConverterFactory.create()).build()

        return retrofit.create(NowCastApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReverseGeocodingApi(): ReverseGeocodingApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.MAPBOX_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

        return retrofit.create(ReverseGeocodingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationForecastApi(): LocationForecastApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.MET_API_BASE_URL)
            .client(metClientBuilder)
            .addConverterFactory(GsonConverterFactory.create()).build()

        return retrofit.create(LocationForecastApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDirectionsApi(): DirectionsApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.MAPBOX_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

        return retrofit.create(DirectionsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMetAlertsApi(): MetAlertsApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.MET_API_BASE_URL)
            .client(metClientBuilder)
            .addConverterFactory(GsonConverterFactory.create()).build()

        return retrofit.create(MetAlertsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFetchAddressesUseCase(
        repository: MotoCastRepository,
        getLocationUseCase: LocationUseCase,
    ) = FetchAddressesUseCase(
        repository,
        getLocationUseCase,
    )

    @Provides
    @Singleton
    fun provideGetGeocodedNameUseCase(
        repository: MotoCastRepository,
    ) = GetGeocodedNameUseCase(
        repository
    )

    @Provides
    @Singleton
    fun provideGetSystemDarkModeEnabledUseCase(
        repository: MotoCastRepository
    ) = GetSystemDarkModeEnabledUseCase(repository)

    @Provides
    @Singleton
    fun provideFetchNowCastDataUseCase(
        repository: MotoCastRepository
    ) = FetchNowCastDataUseCase(repository)

    @Provides
    @Singleton
    fun provideFetchDirectionsDataUseCase(
        repository: MotoCastRepository
    ) = FetchDirectionsDataUseCase(repository)

    @Provides
    @Singleton
    fun provideFetchLocationForecastDataUseCase(
        repository: MotoCastRepository
    ) = FetchLocationForecastDataUseCase(repository)


    @Provides
    @Singleton
    fun provideGetWeatherDataUseCase(
        repository: MotoCastRepository,
        fetchNowCastDataUseCase: FetchNowCastDataUseCase,
        fetchLocationForecastDataUseCase: FetchLocationForecastDataUseCase,
    ) = GetWeatherDataUseCase(
        repository,
        fetchLocationForecastDataUseCase,
        fetchNowCastDataUseCase,
    )

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context,
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideLocationUseCase(
        repository: MotoCastRepository,
        fusedLocationProviderClient: FusedLocationProviderClient
    ): LocationUseCase {
        return LocationUseCase(
            repository,
            fusedLocationProviderClient
        )
    }


    private val metClientBuilder = OkHttpClient.Builder().addInterceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.header("X-Gravitee-API-Key", BuildConfig.MET_API_KEY)
        chain.proceed(requestBuilder.build())
    }.build()

}