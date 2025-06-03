package com.uzery.fglib.core.component

import com.uzery.fglib.core.component.ability.AbilityBox
import com.uzery.fglib.core.component.bounds.BoundsComponent
import com.uzery.fglib.core.component.bounds.BoundsElement
import com.uzery.fglib.core.component.controller.Controller
import com.uzery.fglib.core.component.controller.TempAction
import com.uzery.fglib.core.component.group.*
import com.uzery.fglib.core.component.listener.ActionListener
import com.uzery.fglib.core.component.listener.BoundsInputAction
import com.uzery.fglib.core.component.listener.InputAction
import com.uzery.fglib.core.component.reaction.*
import com.uzery.fglib.core.component.resource.AudioResource
import com.uzery.fglib.core.component.resource.ImageResource
import com.uzery.fglib.core.component.resource.SpriteResource
import com.uzery.fglib.core.component.visual.LayerVisualiser
import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.struct.num.IntI

/**
 * Special component syntax for creating generic [ObjectComponent]
 *
 * for example:
 * ```
 * addAbility {
 *     time++
 * }
 * ```
 **/
abstract class ComponentFunctionality {
    abstract fun addComponent(vararg component: ObjectComponent)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////// Component Functionality ////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addBounds(bs: GroupBounds) = addComponent(bs)

    fun addAbility(ability: GroupAbility) = addComponent(ability)
    fun addController(controller: GroupController) = addComponent(controller)
    fun addListener(listener: GroupListener) = addComponent(listener)
    fun addVisual(vis: GroupVisualiser) = addComponent(vis)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addBounds(code: String, vararg bs: BoundsElement) {
        bs.forEach {
            addComponent(BoundsComponent(code, it))
        }
    }

    fun addBounds(code: String, shape: () -> Shape?) {
        addComponent(BoundsComponent(code, BoundsElement(shape)))
    }

    fun addBounds(code: String, name: String, shape: () -> Shape?) {
        addComponent(BoundsComponent(code, BoundsElement(name, shape)))
    }

    fun addRedBounds(vararg bs: BoundsElement) = addBounds("RED", *bs)
    fun addRedBounds(shape: () -> Shape?) = addBounds("RED", shape)
    fun addRedBounds(name: String, shape: () -> Shape?) = addBounds("RED", name, shape)

    fun addOrangeBounds(vararg bs: BoundsElement) = addBounds("ORANGE", *bs)
    fun addOrangeBounds(shape: () -> Shape?) = addBounds("ORANGE", shape)
    fun addOrangeBounds(name: String, shape: () -> Shape?) = addBounds("ORANGE", name, shape)

    fun addBlueBounds(vararg bs: BoundsElement) = addBounds("BLUE", *bs)
    fun addBlueBounds(shape: () -> Shape?) = addBounds("BLUE", shape)
    fun addBlueBounds(name: String, shape: () -> Shape?) = addBounds("BLUE", name, shape)

    fun addGreenBounds(vararg bs: BoundsElement) = addBounds("GREEN", *bs)
    fun addGreenBounds(shape: () -> Shape?) = addBounds("GREEN", shape)
    fun addGreenBounds(name: String, shape: () -> Shape?) = addBounds("GREEN", name, shape)

    fun addGrayBounds(vararg bs: BoundsElement) = addBounds("GRAY", *bs)
    fun addGrayBounds(shape: () -> Shape?) = addBounds("GRAY", shape)
    fun addGrayBounds(name: String, shape: () -> Shape?) = addBounds("GRAY", name, shape)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addController(controller: () -> (() -> TempAction)?) = addComponent(Controller(controller))
    fun addController(vararg controller: Controller) = addComponent(*controller)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addListener(listener: (InputAction) -> Unit) = addComponent(ActionListener(listener))
    fun addListener(vararg listener: ActionListener) = addComponent(*listener)
    fun addListener(code: String, f: (action: InputAction) -> Unit) {
        addListener { action ->
            if (action.code == code) f(action)
        }
    }

    fun addBoundsListener(
        code: String,
        our: String? = null,
        their: String? = null,
        f: (action: BoundsInputAction) -> Unit
    ) {
        addListener { action ->
            if (action.code != code) return@addListener
            if (action.args.size != 2) throw DebugData.error("Can't cast to BoundsInputAction: $action")

            val bounds_action = BoundsInputAction(action)
            if (our != null && our != bounds_action.our) return@addListener
            if (their != null && their != bounds_action.their) return@addListener

            f(bounds_action)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addAbility(ability: () -> Unit) = addComponent(AbilityBox(ability))
    fun addAbility(vararg ability: AbilityBox) = addComponent(*ability)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addVisual(vararg vis: Visualiser) = addComponent(*vis)

    fun addVisual(layer: DrawLayer, vis: (agc: AffineGraphics, draw_pos: PointN) -> Unit) {
        addComponent(
            object: LayerVisualiser(layer) {
                override fun draw(draw_pos: PointN) {
                    vis(agc, draw_pos)
                }
            }
        )
    }

    fun addVisual(layer: DrawLayer, sort_pos: PointN, vis: (agc: AffineGraphics, draw_pos: PointN) -> Unit) {
        addComponent(
            object: LayerVisualiser(layer) {
                init {
                    sortPOS = sort_pos
                }

                override fun draw(draw_pos: PointN) {
                    vis(agc, draw_pos)
                }
            }
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addImage(name: String, vararg effects: String) = addComponent(ImageResource(name, *effects))
    fun addImage(name: String, size: IntI, vararg effects: String) = addComponent(SpriteResource(name, size, *effects))

    fun addAudio(name: String) = addComponent(AudioResource(name))

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun onInit(f: () -> Unit) = addComponent(OnInitComponent(f))
    fun onInit(f: OnInitComponent) = addComponent(f)

    fun onLoad(f: () -> Unit) = addComponent(OnLoadComponent(f))
    fun onLoad(f: OnLoadComponent) = addComponent(f)

    fun onBirth(f: () -> Unit) = addComponent(OnBirthComponent(f))
    fun onBirth(f: OnBirthComponent) = addComponent(f)

    fun onDeath(f: () -> Unit) = addComponent(OnDeathComponent(f))
    fun onDeath(f: OnDeathComponent) = addComponent(f)

    fun onGrab(f: () -> Unit) = addComponent(OnGrabComponent(f))
    fun onGrab(f: OnGrabComponent) = addComponent(f)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
