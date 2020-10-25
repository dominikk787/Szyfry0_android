package com.example.szyfry0

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.provider.BaseColumns._ID

data class KeyListItem(val id: Int, var name: String, var abId: Int = -1, val key: String = "") {
    //override fun toString(): String = name
}

class PlayfairDBHelper(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, TABLE_VERSION) {
    companion object : BaseColumns {
        const val TABLE_NAME = "PlayfairKeys"
        const val TABLE_VERSION = 1
        const val COLUMN_NAME_KEYNAME = "KeyName"
        const val COLUMN_NAME_ALPHABET = "Alphabet"
        const val COLUMN_NAME_KEYV = "KeyV"
        val CREATE_TABLE = """
            CREATE TABLE $TABLE_NAME (
            $_ID INTEGER PRIMARY KEY,
            $COLUMN_NAME_KEYNAME TEXT,
            $COLUMN_NAME_ALPHABET INTEGER,
            $COLUMN_NAME_KEYV TEXT)
        """.trimIndent()
        val DELETE_TABLE = """
            DROP TABLE IF EXISTS $TABLE_NAME
        """.trimIndent()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DELETE_TABLE)
        onCreate(db)
    }

    fun addKey(key: KeyListItem) {
        val values = ContentValues()
        values.put(COLUMN_NAME_KEYNAME, key.name)
        values.put(COLUMN_NAME_ALPHABET, key.abId)
        values.put(COLUMN_NAME_KEYV, key.key)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getAllKeys(): List<KeyListItem> {
        val cursor = this.readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val list = mutableListOf<KeyListItem>()
        cursor.use { c ->
            c.moveToFirst()
            while(!c.isAfterLast) {
                val id = c.getInt(c.getColumnIndex(_ID))
                val name = c.getString(c.getColumnIndex(COLUMN_NAME_KEYNAME))
                val ab = c.getInt(c.getColumnIndex(COLUMN_NAME_ALPHABET))
                val key = c.getString(c.getColumnIndex(COLUMN_NAME_KEYV))
                println("read db $id $name $ab $key")
                list.add(KeyListItem(id, name, ab, key))
                c.moveToNext()
            }
        }
        return list
    }
}