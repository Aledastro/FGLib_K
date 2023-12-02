package com.uzery.fglib.javafx_rl

import com.uzery.fglib.core.realisation.FGListener
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Program
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.input.KeyActivator
import com.uzery.fglib.utils.input.MouseActivator
import com.uzery.fglib.utils.input.data.FGKey
import com.uzery.fglib.utils.input.data.FGMouseKey
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.input.KeyCode
import javafx.stage.Stage

internal object ListenerFX: FGListener() {
    private val pressed = Array(KeyCode.values().size) { false }
    private val char_pressed = Array(Platform.charArray.size) { -1 }
    private val char_pressedID = HashMap<Char, Int>()
    private val mouse_pressed = Array(KeyCode.values().size) { false }
    private var mouseP = PointN.ZERO

    private var scrollP = PointN.ZERO
        get() = if (last_scroll_time == Program.program_time) field else PointN.ZERO
    private var last_scroll_time = 0


    private fun char_pressed(code: Int): Boolean {
        return char_pressed[code] == Program.program_time
    }

    fun initListeners(stage: Stage){
        stage.scene.setOnMousePressed { e ->
            mouseP = PointN(e.x, e.y)
            mouse_pressed[e.button.ordinal] = true
        }
        stage.scene.setOnMouseReleased { e ->
            mouseP = PointN(e.x, e.y)
            mouse_pressed[e.button.ordinal] = false
        }
        stage.scene.setOnMouseMoved { e ->
            mouseP = PointN(e.x, e.y)
            mouse_pressed[e.button.ordinal] = false
        }
        stage.scene.setOnMouseDragged { e ->
            mouseP = PointN(e.x, e.y)
            mouse_pressed[e.button.ordinal] = true
        }
        stage.scene.setOnScroll { e ->
            scrollP = PointN(e.deltaX, e.deltaY)
            last_scroll_time = Program.program_time
        }

        Platform.charArray.forEachIndexed { i, value -> char_pressedID[value] = i }
        stage.scene.setOnKeyPressed { key -> pressed[key.code.ordinal] = true }
        stage.scene.setOnKeyReleased { key -> pressed[key.code.ordinal] = false }

        stage.scene.setOnKeyTyped { key ->
            key.character.forEach { ch ->
                if (char_pressedID[ch] == null) throw DebugData.error("type key: $key, char: ${key.character}")
                char_pressed[char_pressedID[ch]!!] = Program.program_time
            }
        }
    }


    override val keyboard = object: KeyActivator<FGKey>(FGKey.values()) {
        override fun pressed0(code: Int): Boolean = pressed[code]
        override fun fromKey(key: FGKey): Int = key.id
    }
    override val char_keyboard = object: KeyActivator<Char>(Platform.charArray) {
        override fun pressed0(code: Int): Boolean = char_pressed(code)
        override fun fromKey(key: Char): Int = char_pressedID[key]!!
    }

    override val mouse = object: MouseActivator(RectN(PointN.ZERO, Platform.CANVAS)) {
        override fun pos0(): PointN = mouseP/Platform.graphics_ut.scale
        override fun scroll0() = scrollP

        override val keys = object: KeyActivator<FGMouseKey>(FGMouseKey.values()) {
            override fun pressed0(code: Int): Boolean = mouse_pressed[code]
            override fun fromKey(key: FGMouseKey): Int = key.id
        }
    }
}
