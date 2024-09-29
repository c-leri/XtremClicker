package uqac.dim.xtremclicker

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uqac.dim.xtremclicker.save.BoughtUpgradeDao
import uqac.dim.xtremclicker.save.Save
import uqac.dim.xtremclicker.save.SaveDao
import uqac.dim.xtremclicker.sound.SoundsManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideXtremClickerDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        XtremClickerDatabase::class.java,
        "xtrem_clicker"
    ).build()

    @Singleton
    @Provides
    fun provideSaveDao(db: XtremClickerDatabase) = db.getSaveDao()

    @Singleton
    @Provides
    fun provideBoughtUpgradeDao(db: XtremClickerDatabase) = db.getBoughtUpgradeDao()

    @Singleton
    @Provides
    fun provideSave(
        application: Application,
        saveDao: SaveDao,
        boughtUpgradeDao: BoughtUpgradeDao
    ) =
        Save(application, saveDao, boughtUpgradeDao)

    @Singleton
    @Provides
    fun provideSoundsManager(
        @ApplicationContext applicationContext: Context
    ) = SoundsManager(applicationContext)
}