package com.nnirosan.materialnotes.data.note

import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * from notes")
    fun getAll(): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Insert
    suspend fun insertNotes(notes: List<Note>)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE from notes")
    suspend fun deleteAll()

    @Query("UPDATE notes SET title=:title WHERE note_id = :id")
    fun update(title: Float?, id: Int)

}