package com.nnirosan.materialnotes.ui.shared

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nnirosan.materialnotes.LOG_TAG
import com.nnirosan.materialnotes.data.note.Note
import com.nnirosan.materialnotes.data.note.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(val app: Application) : AndroidViewModel(app) {
    private val dataRepo = NoteRepository(app)
    val noteData = dataRepo.noteData

    val selectedNote = MutableLiveData<Note>()
    val activityTitle = MutableLiveData<String>()
    val myName = MutableLiveData<String>()

    fun refreshData() {
        dataRepo.refreshData()
    }

    fun updateNoteRecord(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        Log.i(LOG_TAG, "UPDATING NOTE WITH CURRENT DATA")
        Log.i(LOG_TAG, note.title)
        dataRepo.update(note)
    }

    fun deleteNoteRecord(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        Log.i(LOG_TAG, "UPDATING NOTE WITH CURRENT DATA")
        Log.i(LOG_TAG, note.title)
        dataRepo.delete(note)
    }
}