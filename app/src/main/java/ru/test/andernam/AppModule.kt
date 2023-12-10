package ru.test.andernam

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.test.andernam.domain.UserClass
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

//    @Binds
//    @ActivityScope
//    abstract fun bindLifecycleOwner(activity: AppCompatActivity): LifecycleOwner
//
//    @Singleton
//    @Provides
//    fun provideUserLiveData(): LiveUserData{
//        return LiveUserData(provideUserClass())
//    }


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