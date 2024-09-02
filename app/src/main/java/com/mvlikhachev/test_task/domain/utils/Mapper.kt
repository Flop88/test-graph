package com.mvlikhachev.test_task.domain.utils

import com.mvlikhachev.test_task.data.remote.model.PointRemote
import com.mvlikhachev.test_task.domain.model.Point

fun PointRemote.toPoint(): Point {
    return Point(
        x = this.x,
        y = this.y
    )
}