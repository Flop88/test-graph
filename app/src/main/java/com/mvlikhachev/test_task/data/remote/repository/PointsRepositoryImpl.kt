package com.mvlikhachev.test_task.data.remote.repository

import com.mvlikhachev.test_task.data.remote.PointsApiService
import com.mvlikhachev.test_task.data.remote.model.PointRemote
import javax.inject.Inject

class PointsRepositoryImpl @Inject constructor(
    private val pointsApiService: PointsApiService
): PointsRepository {

    override suspend fun getPoints(count: Int): List<PointRemote> {
        return pointsApiService.getPoints(count = count).points
    }
}