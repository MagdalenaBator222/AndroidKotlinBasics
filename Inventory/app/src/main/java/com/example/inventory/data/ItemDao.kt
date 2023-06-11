package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    // suspend function - can be accessed from a coroutine
    // conflict strategy = tells room what to do in case of a new item with an id that's already
    // in the database - it will be ignored (not added to the database)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    // select item by id
    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id : Int) : Flow<Item>

    // show a list of all items, sorted by name in ascending order
    @Query("SELECT * from item ORDER BY name ASC")
    fun getItems() : Flow<List<Item>>
}