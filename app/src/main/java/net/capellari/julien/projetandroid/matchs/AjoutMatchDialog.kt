package net.capellari.julien.projetandroid.matchs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.dialog_ajout_match.view.*
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.LocationModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Match
import net.capellari.julien.utils.success
import java.lang.Exception

class AjoutMatchDialog : DialogFragment() {
    // Companion
    companion object {
        const val TAG = "AjoutMatchDialog"
    }

    // Attributs
    private lateinit var data: DataModel
    private lateinit var location: LocationModel

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // view model !
        data     = ViewModelProviders.of(requireActivity())[DataModel::class.java]
        location = ViewModelProviders.of(requireActivity())[LocationModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate layout
        val inflater = requireActivity().layoutInflater
        val layout = inflater.inflate(R.layout.dialog_ajout_match, null)

        // Dialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.label_ajout_match)

        builder.setView(layout)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            location.getLastLocation(
                success<Location,Exception?> {
                    data.insert(Match(0, layout.titre.text.toString(), "", it.latitude, it.longitude))
                } fail {
                    data.insert(Match(0, layout.titre.text.toString()))
                }
            )
        }

        val dialog = builder.create()

        // Events
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
        }

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                        layout.titre.text.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        layout.titre.addTextChangedListener(watcher)

        return dialog
    }
}