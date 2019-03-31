package net.capellari.julien.projetandroid.matchs

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.db.Match
import net.capellari.julien.projetandroid.db.Score

class MatchModel : ViewModel() {
    // Attributs
    lateinit var data: DataModel
        private set

    var match_id: Long = 0
        private set

    var match: LiveData<Match>? = null
        private set

    var scores: LiveData<Array<Score>>? = null
        private set

    // Méthodes
    fun setMatchId(match_id: Long, data: DataModel) {
        // Save values
        this.data = data
        this.match_id = match_id

        // Start queries
        match  = data.getMatch(match_id)
        scores = data.allScoresByMatch(match_id)
    }
}