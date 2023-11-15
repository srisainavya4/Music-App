package com.example.musicv1.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "SONG")
data class Song(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val title: String,
	val imageUrl: String,
	val songUrl: String
)


