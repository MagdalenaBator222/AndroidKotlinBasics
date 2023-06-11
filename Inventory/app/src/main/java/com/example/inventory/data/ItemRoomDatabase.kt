package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// abstract class: Room creates the implementation for you
// entities: Item is the only class with the list of entities
// version number: increases whenever the database schema changes
// exportSchema: schema version history backups are not being kept

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {
    abstract fun itemDao() : ItemDao // returns the data access object (DAO) for the Item class

    companion object {
        @Volatile // the value of a volatile variable will never be cached -> the value is always up-to-date
        private var INSTANCE : ItemRoomDatabase? = null
        fun getDatabase(context: Context): ItemRoomDatabase {
            // return the instance object or (if it's null) - initialise it inside a synchronised{} block
            // -> only one thread of execution can enter this block of code -> database gets initialised only once!
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                ) // builder -> gets the database (parameters: application context, database class and its name)
                    .fallbackToDestructiveMigration() // migration strategy -> if the schema changes, the database is destroyed and rebuilt
                    .build()
                INSTANCE = instance
                return instance
            }

        }
    }
}