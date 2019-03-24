package net.capellari.julien.projetandroid.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.capellari.julien.projetandroid.R

@Database(
    entities = [Joueur::class, Match::class, Score::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // Companion
    companion object {
        // Méthodes
        fun database(context: Context): AppDatabase {
            val ctx = context.applicationContext

            return Room.databaseBuilder(ctx, AppDatabase::class.java, ctx.getString(R.string.database))
                    .build()
        }
    }

    // Méthodes
    abstract fun getJoueurDao(): Joueur.JoueurDao
}