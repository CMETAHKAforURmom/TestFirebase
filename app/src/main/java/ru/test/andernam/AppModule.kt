package ru.test.andernam

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
//    @Singleton
//    @Provides
//    fun provideAuthThingClass(): AuthThingClass{
//        return AuthThingClass()
//    }

//    @Singleton
//    @Provides
//    fun provideTestString() = "This string return by dagger provide"

}

//@Module
//@InstallIn(SingletonComponent::class)
//class ProviderActivity{
//    @Provides
//    fun provideActivity() : Activity {
//        return Activity()
//    }
//}

//@Module
//@InstallIn(SingletonComponent::class)
//class ProviderATC{
//@Singleton
//    @Provides
//    fun provideAuthThingClass(): AuthThingClass{
//        return AuthThingClass()
//    }
//}