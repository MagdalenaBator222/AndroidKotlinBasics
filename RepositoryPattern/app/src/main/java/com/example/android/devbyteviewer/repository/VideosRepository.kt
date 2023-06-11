/*
 * Copyright (C) 2019 Google Inc.
 *gi
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.devbyteviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.example.android.devbyteviewer.network.DevByteNetwork
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for fetching devbyte videos from the network and storing them on disk
 */

// database - parameter for accessing the DAO methods
class VideosRepository(private val database : VideosDatabase){

    // refreshes the offline cache
    suspend fun refreshVideos(){
        // switch the coroutine context to perform network and database operations
        withContext(Dispatchers.IO){
            // fetch the video playlist using the Retrofit service instance
            val playlist = DevByteNetwork.devbytes.getPlaylist()
            // store the playlist in the Room database
            database.videoDao.insertAll(playlist.asDatabaseModel())
        }
    }

    // videos - holds a list of DevByteVideo objects
    // getVideos() - returns a list of database objects, not DevByteVideos objects
    // so we have to use Transformations.map to convert the list to the proper type
    val videos : LiveData<List<DevByteVideo>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    }
}
