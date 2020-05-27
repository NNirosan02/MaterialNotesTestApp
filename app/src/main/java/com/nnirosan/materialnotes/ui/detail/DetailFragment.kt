package com.nnirosan.materialnotes.ui.detail

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nnirosan.materialnotes.LOG_TAG
import com.nnirosan.materialnotes.R
import com.nnirosan.materialnotes.data.note.Note
import com.nnirosan.materialnotes.databinding.FragmentDetailBinding
import com.nnirosan.materialnotes.ui.shared.SharedViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.android.ext.android.get


/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment(), View.OnClickListener {

    val viewModel = get<SharedViewModel>()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setHasOptionsMenu(true)
        navController = Navigation.findNavController(
            requireActivity(), R.id.nav_host
        )

        val binding = FragmentDetailBinding.inflate(
            inflater, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var fabSave = binding.root.findViewById<FloatingActionButton>(R.id.saveNoteButton)
        fabSave?.setOnClickListener(this)

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navController.navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveData() {
        if (TextUtils.isEmpty(bodyFullText.text)) {
            Log.i(LOG_TAG, "IS EMPTY")
        } else {
            Log.i(LOG_TAG, "BEGIN SAVE NOTE")
            var currentNewNote = buildNote()
            viewModel.updateNoteRecord(currentNewNote)
        }
    }


    fun buildNote(): Note {
        var currentNoteId = Integer.parseInt(selectedNoteId.text.toString())
        var currentNoteTitle = titleFullText.text.toString()
        var currentNoteBody = bodyFullText.text.toString()
        return Note(
            currentNoteId,
            currentNoteTitle,
            currentNoteBody
        )
    }

    override fun onClick(v: View?) {
        Log.i(LOG_TAG, "SOMETHING WAS CLICKED")
        when (v?.id) {
            R.id.saveNoteButton -> {
                Log.i(LOG_TAG, "FAB PRESSED")
                saveData()
            }
            else -> {
                Log.i(LOG_TAG, "NOT MATCHING ANYTHING")
            }
        }
    }

}