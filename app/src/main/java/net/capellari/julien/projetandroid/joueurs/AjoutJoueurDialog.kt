package net.capellari.julien.projetandroid.joueurs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.dialog_ajout_joueur.view.*
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Joueur

class AjoutJoueurDialog : DialogFragment() {
    // Companion
    companion object {
        const val TAG = "AjoutJoueurDialog"
    }

    // Attributs
    private lateinit var data: DataModel

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // view model !
        data = ViewModelProviders.of(requireActivity())[DataModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate layout
        val inflater = requireActivity().layoutInflater
        val layout = inflater.inflate(R.layout.dialog_ajout_joueur, null)

        // Dialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.label_ajout_joueur)

        builder.setView(layout)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            data.insert(Joueur(0, layout.nom.text.toString(), layout.prenom.text.toString()))
        }

        val dialog = builder.create()

        // Events
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
        }

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                        layout.nom.text.isNotEmpty() && layout.prenom.text.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        layout.nom.addTextChangedListener(watcher)
        layout.prenom.addTextChangedListener(watcher)

        return dialog
    }
}