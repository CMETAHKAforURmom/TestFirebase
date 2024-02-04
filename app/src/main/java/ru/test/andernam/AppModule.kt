package ru.test.andernam

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import ru.test.andernam.domain.AuthThingClass
import ru.test.andernam.domain.DBFirstStep
import ru.test.andernam.domain.old.UserClass
import ru.test.andernam.domain.repository.LiveUserData
import ru.test.andernam.navigation.HomeImpl
import ru.test.andernam.navigation.MessageImpl
import ru.test.andernam.navigation.apis.FeatureApi
import ru.test.andernam.navigation.apis.HomeApi
import ru.test.andernam.navigation.apis.MessageApi
import ru.test.andernam.view.MainActivity
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUserClass(): UserClass {
        return UserClass()
    }

    @Provides
    fun provideHomeImpl(): HomeApi{
        return HomeImpl()
    }
    @Provides
    fun provideMessageImpl(): MessageApi{
        return MessageImpl()
    }

    @Singleton
    @Provides
    fun provideAuthClass(): AuthThingClass{
        return AuthThingClass(provideActivity())
    }

    @Provides
    fun provideUserLiveData(): LiveUserData{
        return LiveUserData()
    }
    @Singleton
    @Provides
    fun provideDB(): DBFirstStep{
        return DBFirstStep()
    }

    @Singleton
    @Provides
    fun provideActivity(): Activity {
        return Activity()
    }

    @Singleton
    @Provides
    fun provideMainActivity(): MainActivity{
        return MainActivity()
    }

//    @Singleton
//    @Provides
//    fun provideLifecycleOwner(): LifecycleOwner{
//        return provideMainActivity().get
//    }

//    @Singleton
//    @Provides
//    fun provideLifecycleOwner (): ApplicationContext{
//        return ApplicationContext()
//    }

//    @Singleton
//    @Provides
//    fun provideLifecycleOwner(): LifecycleOwner{
//        return LifecycleOwner()
//    }
//    @Singleton
//    @Provides
//    fun provideAuthThingClass(): AuthThingClass {
//        return AuthThingClass(provideActivity(), provideUserLiveData())
//    }
//
//    @Singleton
//    @Provides
//    fun provideCommonViewModel(): CommonViewModel{
//        return CommonViewModel(provideAuthThingClass())
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