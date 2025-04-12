package ru.test.andernam

import android.content.Context
import androidx.credentials.CredentialManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.test.andernam.navigation.apis.HomeApi
import ru.test.andernam.navigation.impls.CurrMessageImpl
import ru.test.andernam.navigation.impls.EnterImpl
import ru.test.andernam.navigation.impls.HomeImpl
import ru.test.andernam.navigation.impls.SplashImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSplashImpl(): SplashImpl{
        return SplashImpl()
    }

    @Singleton
    @Provides
    fun provideCurrMessageImpl(): CurrMessageImpl{
        return CurrMessageImpl()
    }
    @Singleton
    @Provides
    fun provideEnterImpl(): EnterImpl{
        return EnterImpl()
    }
    @Singleton
    @Provides
    fun provideHomeImpl(): HomeApi{
        return HomeImpl()
    }
    @Singleton
    @Provides
    fun provideCredentialManager(@ApplicationContext context: Context) = CredentialManager.create(context)
}