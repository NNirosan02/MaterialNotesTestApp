package com.nnirosan.materialnotes.data.note

import com.nnirosan.materialnotes.WEB_SERVICE_PARAMS
import retrofit2.Response
import retrofit2.http.GET

interface NoteService {
    @GET(WEB_SERVICE_PARAMS)
    suspend fun getNoteData(): Response<List<Note>>
}