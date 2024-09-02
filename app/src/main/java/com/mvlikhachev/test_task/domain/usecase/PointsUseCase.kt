package com.mvlikhachev.test_task.domain.usecase

import com.mvlikhachev.test_task.data.remote.repository.PointsRepository
import com.mvlikhachev.test_task.domain.model.Point
import com.mvlikhachev.test_task.domain.utils.toPoint
import javax.inject.Inject

class PointsUseCase @Inject constructor(
    private val api: PointsRepository
) {

    suspend operator fun invoke(count: Int): List<Point> {
        return api.getPoints(count).map { point -> point.toPoint() }
    }
}