package com.dolu.mobibus.core.di

import android.app.Application
import androidx.room.Room
import com.dolu.mobibus.core.data.local.Converters
import com.dolu.mobibus.core.data.local.UserDao
import com.dolu.mobibus.core.data.local.entity.BusTicketDatabase
import com.dolu.mobibus.core.data.local.entity.UserDatabase
import com.dolu.mobibus.core.data.local.util.MoshiParser
import com.dolu.mobibus.core.data.remote.BusTicketApi
import com.dolu.mobibus.core.data.remote.util.MockBusTicketApi
import com.dolu.mobibus.core.data.repository.CoreRepositoryImpl
import com.dolu.mobibus.core.domain.model.User
import com.dolu.mobibus.core.domain.repository.CoreRepository
import com.dolu.mobibus.core.domain.use_case.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAddTicketToUserCartUseCase(repository: CoreRepository): AddTicketToUserCart {
        return AddTicketToUserCart(repository)
    }

    @Provides
    @Singleton
    fun provideGetBusTicketInfosUseCase(repository: CoreRepository): GetBusTicketInfos {
        return GetBusTicketInfos(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveTicketFromUserCartUseCase(repository: CoreRepository): RemoveTicketFromUserCart {
        return RemoveTicketFromUserCart(repository)
    }

    @Provides
    @Singleton
    fun provideClearUserCartUseCase(repository: CoreRepository): ClearUserCart {
        return ClearUserCart(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateCartContentUseCase(
        addTicketToUserCart: AddTicketToUserCart,
        removeTicketFromUserCart: RemoveTicketFromUserCart,
        clearUserCart: ClearUserCart
    ): UpdateUserCartContent {
        return UpdateUserCartContent(
            addTicketToUserCart,
            removeTicketFromUserCart,
            clearUserCart
        )
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(repository: CoreRepository): GetUserUseCase {
        return GetUserUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideCoreRepository(
        busTicketDatabase: BusTicketDatabase,
        busTicketApi: BusTicketApi,
        userDatabase: UserDatabase
    ) : CoreRepository{
        return CoreRepositoryImpl(
            busTicketApi,
            busTicketDatabase.dao,
            userDatabase.dao
        )
    }

    @Provides
    @Singleton
    fun provideBusTicketDatabase(
        app: Application,
        moshi: Moshi
    ) : BusTicketDatabase {
        return Room.databaseBuilder(
            app,
            BusTicketDatabase::class.java,
            "bus_ticket_info_db")
            .addTypeConverter(Converters(MoshiParser(moshi)))
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDatabase(
        app: Application,
        moshi: Moshi
    ) : UserDatabase {
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            "user_db")
            .addTypeConverter(Converters(MoshiParser(moshi)))
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())

    @Provides
    @Singleton
    fun provideBusTicketService(): BusTicketApi {
        // TODO replace with remote data fetch
        // => retroBuilder.baseUrl(BusTicketApi.BASE_URL).build().create(BusTicketApi::class.java)
        return MockBusTicketApi()
    }

    @Singleton
    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun providesUser(): User {
        return User()
    }
}