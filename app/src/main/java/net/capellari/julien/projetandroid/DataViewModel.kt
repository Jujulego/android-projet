package net.capellari.julien.projetandroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import net.capellari.julien.projetandroid.db.AppDatabase
import net.capellari.julien.projetandroid.db.Joueur
import org.jetbrains.anko.doAsync

class DataViewModel(app: Application) : AndroidViewModel(app) {
    // Propriétés
    val app: Application get() = getApplication()

    private val database: AppDatabase by lazy { AppDatabase.database(app) }
    private val joueurDao: Joueur.JoueurDao by lazy { database.getJoueurDao() }

    // Méthodes
    fun allJoueurs() = joueurDao.all()
    fun getJoueur(id: Int) = joueurDao.getById(id)

    fun insert(joueur: Joueur) {
        doAsync {
            joueurDao.insert(joueur)
        }
    }
    fun update(joueur: Joueur) {
        doAsync {
            joueurDao.update(joueur)
        }
    }
    fun delete(joueur: Joueur) {
        doAsync {
            joueurDao.delete(joueur)
        }
    }
}