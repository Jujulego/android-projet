package net.capellari.julien.projetandroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import net.capellari.julien.projetandroid.db.AppDatabase
import net.capellari.julien.projetandroid.db.Joueur
import net.capellari.julien.projetandroid.db.Match
import net.capellari.julien.projetandroid.db.Score
import org.jetbrains.anko.doAsync

class DataModel(app: Application) : AndroidViewModel(app) {
    // Propriétés
    val app: Application get() = getApplication()

    private val database: AppDatabase by lazy { AppDatabase.database(app) }
    private val joueurDao: Joueur.JoueurDao by lazy { database.getJoueurDao() }
    private val matchDao:  Match.MatchDao   by lazy { database.getMatchDao()  }
    private val scoreDao:  Score.ScoreDao   by lazy { database.getScoreDao()  }

    // Méthodes
    // - joueurs
    fun allJoueurs() = joueurDao.all()
    fun getJoueur(id: Long) = joueurDao.getById(id)

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
    fun getMatch(id: Long) = matchDao.getById(id)

    fun insert(match: Match) {
        doAsync {
            val id = matchDao.insert(match)
            scoreDao.insert(
                    Score(0, 0, id),
                    Score(0, 0, id)
            )
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

    // - scores
    fun allScoresByMatch(match: Match)   = scoreDao.allByMatch(match.id)
    fun allScoresByMatch(match_id: Long) = scoreDao.allByMatch(match_id)
    fun getScoreById(id: Long) = scoreDao.getById(id)
}