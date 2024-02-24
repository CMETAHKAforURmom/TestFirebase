package ru.test.andernam

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.test.andernam.navigation.apis.HomeApi
import ru.test.andernam.navigation.apis.MessageApi
import ru.test.andernam.navigation.impls.CurrMessageImpl
import ru.test.andernam.navigation.impls.EnterImpl
import ru.test.andernam.navigation.impls.HomeImpl
import ru.test.andernam.navigation.impls.MessageImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrMessageImpl(): CurrMessageImpl{
        return CurrMessageImpl()
    }

    @Provides
    fun provideEnterImpl(): EnterImpl{
        return EnterImpl()
    }
    @Provides
    fun provideHomeImpl(): HomeApi{
        return HomeImpl()
    }
    @Provides
    fun provideMessageImpl(): MessageApi{
        return MessageImpl()
    }
}