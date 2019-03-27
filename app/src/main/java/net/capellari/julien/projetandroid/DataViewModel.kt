package net.capellari.julien.projetandroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import net.capellari.julien.projetandroid.db.AppDatabase
import net.capellari.julien.projetandroid.db.Joueur
import net.capellari.julien.projetandroid.db.Match
import org.jetbrains.anko.doAsync

class DataViewModel(app: Application) : AndroidViewModel(app) {
    // Propriétés
    val app: Application get() = getApplication()

    private val database: AppDatabase by lazy { AppDatabase.database(app) }
    private val joueurDao: Joueur.JoueurDao by lazy { database.getJoueurDao() }
    private val matchDao:  Match.MatchDao   by lazy { database.getMatchDao()  }

    // Méthodes
    // - joueurs
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

    // - matchs
    fun allMatchs() = matchDao.all()
    fun getMatch(id: Int) = matchDao.getById(id)

    fun insert(match: Match) {
        doAsync {
            matchDao.insert(match)
        }
    }
    fun update(match: Match) {
        doAsync {
            matchDao.update(match)
        }
    }
    fun delete(match: Match) {
        doAsync {
            matchDao.delete(match)
        }
    }
}