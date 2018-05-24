package em.i.di

import dagger.Module
import javax.inject.Singleton
import dagger.Provides
import android.app.Application
import android.content.Context
import em.i.repository.ImeDatabase

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideDatabase(context: Context) = ImeDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }
}