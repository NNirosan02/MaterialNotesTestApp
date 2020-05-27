package com.nnirosan.materialnotes.data.note

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.nnirosan.materialnotes.LOG_TAG
import com.nnirosan.materialnotes.WEB_SERVICE_URL
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NoteRepository(val app: Application) {

    val noteData = MutableLiveData<List<Note>>()
    private val noteDao = NoteDatabase.getDatabase(app)
        .noteDao()

    init {
        refreshData()
    }

    private val listType = Types.newParameterizedType(
        List::class.java,
        Note::class.java
    )

    @WorkerThread
    suspend fun callWebService() {
        if (networkAvailable()) {
            val retrofit = Retrofit.Builder()
                .baseUrl(WEB_SERVICE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            val service = retrofit.create(NoteService::class.java)
            val serviceData = service.getNoteData().body() ?: emptyList()
//            Log.i(LOG_TAG, serviceData.toString())
            noteDao.deleteAll()
            noteDao.insertNotes(serviceData)
            var roomData = noteDao.getAll()
            noteData.postValue(roomData)
        }
    }

    @Suppress("DEPRECATION")
    private fun networkAvailable(): Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    fun refreshData() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = getRoomData()
            if (isRoomDataEmpty()) {
                callWebService()
                Log.i(LOG_TAG, "WEB SERVICE WAS CALLED")
            } else {
                noteData.postValue(data)
                Log.i(LOG_TAG, "LOCAL DATA WAS USED")
            }
        }
    }

    private fun getRoomData(): List<Note> {
        return noteDao.getAll()
    }

    private fun isRoomDataEmpty(): Boolean {
        var data = getRoomData()
        return data.isEmpty()
    }

    suspend fun update(note: Note) {
        if (note.noteId == 0) {
            noteDao.insertNote(note)
        } else {
            noteDao.updateNote(note)
        }
    }

    suspend fun delete(note: Note) {
        noteDao.deleteNote(note)
    }
}