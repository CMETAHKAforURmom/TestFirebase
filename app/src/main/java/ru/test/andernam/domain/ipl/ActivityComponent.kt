package ru.test.andernam.domain.ipl

import android.app.Activity

//@Component(modules = [ProviderActivity::class])
interface ActivityComponent {
    fun inject(activity: Activity)
}