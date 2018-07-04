package em.i.di

import dagger.Component
import em.i.MainActivity
import em.i.ui.InformationsFragment
import em.i.ui.PhotoFragment
import em.i.ui.StatisticsFragment
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ViewModelModule::class))
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(statisticsFragment: StatisticsFragment)
    fun inject(photoFragment: PhotoFragment)
    fun inject(informationsFragment: InformationsFragment)
}