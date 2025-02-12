package com.uzery.fglib.core.room

import com.uzery.fglib.core.component.bounds.Bounds
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.math.geom.PointN

data class PosBounds(val pos: PointN, val bounds: Bounds, val o: GameObject)
