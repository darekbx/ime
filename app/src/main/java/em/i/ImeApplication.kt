package em.i

import android.app.Application
import em.i.di.AppComponent
import em.i.di.AppModule
import em.i.di.DaggerAppComponent

class ImeApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}