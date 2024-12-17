package com.uzery.fglib.core.component.bounds

import com.uzery.fglib.core.component.ObjectComponent

/**
 * One of basic [ObjectComponent]
 *
 * Adds [BoundsElement] to [BoundsBox] based on `code`
 **/
data class BoundsComponent(val code: BoundsBox.Companion.CODE, val element: BoundsElement): ObjectComponent
