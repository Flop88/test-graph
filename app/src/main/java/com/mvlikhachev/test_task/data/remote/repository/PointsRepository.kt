package com.mvlikhachev.test_task.data.remote.repository

import com.mvlikhachev.test_task.data.remote.model.PointRemote

interface PointsRepository {

    suspend fun getPoints(count: Int): List<PointRemote>
}