package net.capellari.julien.projetandroid.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import net.capellari.julien.projetandroid.R

@Database(
    entities = [Joueur::class, Match::class, Photo::class, Score::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // Companion
    companion object {
        // Méthodes
        fun database(context: Context): AppDatabase {
            val ctx = context.applicationContext

            return Room.databaseBuilder(ctx, AppDatabase::class.java, ctx.getString(R.string.database))
                    .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigrationFrom(1)
                    .build()
        }

        // Migrations
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Création de la table photo
                database.execSQL("""
                    create table Photo (
                        id integer primary key autoincrement not null,
                        match_id integer not null,
                        photo text not null,
                        date integer not null,

                        foreign key (match_id) references `Match`(id) on delete cascade
                    )
                """)

                // Création d'index
                database.execSQL("create index index_Photo_match_id  on Photo(match_id)")
                database.execSQL("create index index_Score_match_id  on Score(match_id)")
                database.execSQL("create index index_Score_joueur_id on Score(joueur_id)")
            }
        }
    }

    // Méthodes
    abstract fun getJoueurDao(): Joueur.JoueurDao
    abstract fun getMatchDao():  Match.MatchDao
    abstract fun getPhotoDao():  Photo.PhotoDao
    abstract fun getScoreDao():  Score.ScoreDao
}