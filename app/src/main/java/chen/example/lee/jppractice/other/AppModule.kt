package chen.example.lee.jppractice.other

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import chen.example.lee.jppractice.db.model.AllWordToneData
import chen.example.lee.jppractice.db.model.JPContract.VOCABULARY_DBNAME
import chen.example.lee.jppractice.db.model.Migrations
import chen.example.lee.jppractice.db.model.SoundHandler
import chen.example.lee.jppractice.db.room.VocabularyDatabase
import chen.example.lee.jppractice.repositories.SharedPreferencesRepository
import chen.example.lee.jppractice.utils.ReadCSVFileUtils
import com.example.lee.jppractice.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Singleton
import kotlin.math.log

@Module
@InstallIn(
    ApplicationComponent::class
)
object AppModule {

    @Singleton
    @Provides
    fun provideJPVocabularyDatabase(
        @ApplicationContext context: Context
    ): VocabularyDatabase {
        return Room.databaseBuilder(
            context,
            VocabularyDatabase::class.java,
            VOCABULARY_DBNAME
        ).addMigrations(
            Migrations(context).v1_2,
            Migrations(context).v2_3,
            Migrations(context).v3_4,
            Migrations(context).v4_5,
            Migrations(context).v5_6,
            Migrations(context).v6_7,
            Migrations(context).v7_8,
            Migrations(context).v8_9,
            Migrations(context).v9_10,
            Migrations(context).v10_11
        ).build().also {db->
            CoroutineScope(Dispatchers.IO).launch(Dispatchers.Default) {
                db.vocabularyDao().getAll().collectLatest {
                    if (it.isEmpty() || db.openHelper.readableDatabase.version != 11){
                        db.vocabularyDao().deleteAll()
                        ReadCSVFileUtils().parseCSV(context).forEach { vocabulay ->
                            db.vocabularyDao().insertVocabulary(vocabulay)
                        }
                    }

                }

            }
        }

    }
    @Singleton
    @Provides
    fun provideVocabularyDao(database: VocabularyDatabase) = database.vocabularyDao()

    @Singleton
    @Provides
    fun provideSoundHandler(@ApplicationContext context: Context) = SoundHandler(context)

    @Singleton
    @Provides
    fun provideSharedPreferencesRepository(@ApplicationContext context: Context) = SharedPreferencesRepository(context)


    @Singleton
    @Provides
    fun provideAllWordToneData(@ApplicationContext context: Context) = AllWordToneData(context)


}

