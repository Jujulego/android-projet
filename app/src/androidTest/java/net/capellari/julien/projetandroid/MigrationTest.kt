package net.capellari.julien.projetandroid

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.capellari.julien.projetandroid.db.AppDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationTest {
    // Attributs
    private val TEST_DB = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AppDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory()
    )

    // Tests
    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        // Création db v2
        helper.createDatabase(TEST_DB, 2).apply {
            close()
        }

        // Migrate to v3
        helper.runMigrationsAndValidate(TEST_DB, 3, true, AppDatabase.MIGRATION_2_3)
    }

    @Test
    @Throws(IOException::class)
    fun migrate3To4() {
        // Création db v3
        helper.createDatabase(TEST_DB, 3).apply {
            execSQL("insert into `Match`(id, titre, description, date) values (1, 'Test', 'Test', 0)")
            close()
        }

        // Migrate to v3
        val db = helper.runMigrationsAndValidate(TEST_DB, 4, true, AppDatabase.MIGRATION_3_4)
        val cursor = db.query("select * from `Match` where id = 1")

        cursor.moveToFirst()
        assertEquals(1, cursor.count)
        assertEquals(.0, cursor.getDouble(cursor.getColumnIndex("latitude")), 0.000001)
        assertEquals(.0, cursor.getDouble(cursor.getColumnIndex("longitude")), 0.000001)

        db.close()
    }
}