package net.capellari.julien.projetandroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import net.capellari.julien.projetandroid.db.*
import org.jetbrains.anko.doAsync
import java.util.*

class DataModel(app: Application) : AndroidViewModel(app) {
    // Propriétés
    val app: Application get() = getApplication()

    private val database: AppDatabase by lazy { AppDatabase.database(app) }
    private val joueurDao: Joueur.JoueurDao by lazy { database.getJoueurDao() }
    private val matchDao:  Match.MatchDao   by lazy { database.getMatchDao()  }
    private val photoDao:  Photo.PhotoDao   by lazy { database.getPhotoDao()  }
    private val scoreDao:  Score.ScoreDao   by lazy { database.getScoreDao()  }

    // Méthodes
    // - joueurs
    fun allJoueurs() = joueurDao.all()
    fun getJoueur(id: Long) = joueurDao.getById(id)

    fun allJoueursByMatch(match: Match)   = joueurDao.allByMatch(match.id)
    fun allJoueursByMatch(match_id: Long) = joueurDao.allByMatch(match_id)

    fun insert(joueur: Joueur) {
        doAsync {
            joueur.updated_at = Date()
            joueurDao.insert(joueur)
        }
    }
    fun update(joueur: Joueur) {
        doAsync {
            joueur.updated_at = Date()
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
    fun getScore(id: Long) = scoreDao.getById(id)

    fun update(score: Score) {
        doAsync {
            scoreDao.update(score)
        }
    }

    // - photos
    fun allPhotosByMatch(match: Match)   = photoDao.allByMatch(match.id)
    fun allPhotosByMatch(match_id: Long) = photoDao.allByMatch(match_id)
    fun getPhoto(id: Long) = photoDao.getById(id)

    fun insert(photo: Photo) {
        doAsync {
            photoDao.insert(photo)
        }
    }
    fun delete(photo: Photo) {
        doAsync {
            photoDao.delete(photo)
        }
    }
}