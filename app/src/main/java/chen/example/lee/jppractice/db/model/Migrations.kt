package chen.example.lee.jppractice.db.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import chen.example.lee.jppractice.utils.ReadCSVFileUtils

data class Migrations(val context: Context) {
        val v1_2 = object : Migration(1,2){ override fun migrate(database: SupportSQLiteDatabase) {} }
        val v2_3 = object : Migration(2,3){ override fun migrate(database: SupportSQLiteDatabase) {} }
        val v3_4 = object : Migration(3,4){ override fun migrate(database: SupportSQLiteDatabase) {} }
        val v4_5 = object : Migration(4,5){ override fun migrate(database: SupportSQLiteDatabase) {} }
        val v5_6 = object : Migration(5,6){ override fun migrate(database: SupportSQLiteDatabase) {} }
        val v6_7 = object : Migration(6,7){ override fun migrate(database: SupportSQLiteDatabase) {} }
        val v7_8 = object : Migration(7,8){  override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE 'Vocabulary' ADD COLUMN 'wordPlayResourceName' TEXT NOT Null DEFAULT '' ")
            database.execSQL("ALTER TABLE 'Vocabulary' ADD COLUMN 'examplePlayResourceName' TEXT NOT Null DEFAULT ''")
        } }
        val v8_9 = object : Migration(8,9){ override fun migrate(database: SupportSQLiteDatabase) {} }
        val v9_10 = object : Migration(9,10){  override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE 'Vocabulary' ADD COLUMN 'wrongWord' TEXT NOT Null DEFAULT '' ")
        } }
        val v10_11 = object : Migration(10,11){  override fun migrate(database: SupportSQLiteDatabase) {} }
}