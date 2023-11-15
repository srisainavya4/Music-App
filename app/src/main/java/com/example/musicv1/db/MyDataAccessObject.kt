package com.example.musicv1.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.UUID


@Dao
interface MyDataAccessObject {

	@Query("DELETE FROM SONG")
	fun deleteAllSongs()

	@Insert
	fun insertSong(urls: Song)

	@Update
	fun updateSong(urls: Song)

	@Delete
	fun deleteSong(urls: Song)

	@Query("SELECT * FROM SONG WHERE id = :id")
	fun getSong(id: Int): Song

	@Query("SELECT * FROM SONG")
	fun getSongs(): List<Song>
}


