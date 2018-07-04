package em.i.di

import dagger.Module
import javax.inject.Singleton
import dagger.Provides
import android.app.Application
import android.content.Context
import em.i.repository.ImeDatabase
import em.i.repository.local.InformationsStore
import em.i.viewmodels.PhotoViewModel
import em.i.viewmodels.StatisticsViewModel

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideDatabase(context: Context) = ImeDatabase.getInstance(context)

    @Provides
    fun provideStatisticsViewModel(database: ImeDatabase) = StatisticsViewModel(database)

    @Provides
    fun providePhotoViewModel() = PhotoViewModel()

    @Provides
    fun provideInformationsStore(context: Context) = InformationsStore(context)

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }
}