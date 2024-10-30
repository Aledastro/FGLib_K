package com.uzery.fglib.core.component

import com.uzery.fglib.core.component.ability.AbilityBox
import com.uzery.fglib.core.component.ability.GroupAbility
import com.uzery.fglib.core.component.bounds.BoundsBox.Companion.CODE
import com.uzery.fglib.core.component.bounds.BoundsComponent
import com.uzery.fglib.core.component.bounds.BoundsElement
import com.uzery.fglib.core.component.bounds.GroupBounds
import com.uzery.fglib.core.component.controller.Controller
import com.uzery.fglib.core.component.controller.GroupController
import com.uzery.fglib.core.component.controller.TempAction
import com.uzery.fglib.core.component.listener.ActionListener
import com.uzery.fglib.core.component.listener.GroupListener
import com.uzery.fglib.core.component.listener.InputAction
import com.uzery.fglib.core.component.property.GameProperty
import com.uzery.fglib.core.component.property.GroupProperty
import com.uzery.fglib.core.component.reaction.*
import com.uzery.fglib.core.component.reaction.OnLoadComponent
import com.uzery.fglib.core.component.visual.GroupVisualiser
import com.uzery.fglib.core.component.visual.LayerVisualiser
import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape

open class GroupComponent(vararg component: ObjectComponent): ObjectComponent {
    val components = ArrayList<ObjectComponent>()

    init {
        components.addAll(component)
    }

    fun add(vararg component: ObjectComponent) {
        components.addAll(component)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addComponent(vararg component: ObjectComponent) {
        for (c in component) {
            when (c) {
                is GroupComponent -> c.components.forEach { addComponent(it) }

                is BoundsComponent -> addBounds(c.code, c.element)

                is AbilityBox -> addAbility(c)
                is Controller -> addController(c)
                is ActionListener -> addListener(c)
                is GameProperty -> addProperty(c)
                is Visualiser -> addVisual(c)

                is OnInitComponent -> onInit(c)
                is OnLoadComponent -> onLoad(c)
                is OnBirthComponent -> onBirth(c)
                is OnDeathComponent -> onDeath(c)
                is OnGrabComponent -> onGrab(c)

                else -> throw DebugData.error("Wrong Component: $c")
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addBounds(bs: GroupBounds) = add(bs)

    fun addAbility(ability: GroupAbility) = add(ability)
    fun addController(controller: GroupController) = add(controller)
    fun addListener(listener: GroupListener) = add(listener)
    fun addProperty(property: GroupProperty) = add(property)
    fun addVisual(vis: GroupVisualiser) = add(vis)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun addBounds(code: CODE, vararg bs: BoundsElement) {
        bs.forEach {
            add(BoundsComponent(code, it))
        }
    }

    private fun addBounds(code: CODE, shape: () -> Shape?) {
        add(BoundsComponent(code, BoundsElement(shape)))
    }

    private fun addBounds(code: CODE, name: String, shape: () -> Shape?) {
        add(BoundsComponent(code, BoundsElement(name, shape)))
    }

    fun addRedBounds(vararg bs: BoundsElement) = addBounds(CODE.RED, *bs)
    fun addRedBounds(shape: () -> Shape?) = addBounds(CODE.RED, shape)
    fun addRedBounds(name: String, shape: () -> Shape?) = addBounds(CODE.RED, name, shape)

    fun addOrangeBounds(vararg bs: BoundsElement) = addBounds(CODE.ORANGE, *bs)
    fun addOrangeBounds(shape: () -> Shape?) = addBounds(CODE.ORANGE, shape)
    fun addOrangeBounds(name: String, shape: () -> Shape?) = addBounds(CODE.ORANGE, name, shape)

    fun addBlueBounds(vararg bs: BoundsElement) = addBounds(CODE.BLUE, *bs)
    fun addBlueBounds(shape: () -> Shape?) = addBounds(CODE.BLUE, shape)
    fun addBlueBounds(name: String, shape: () -> Shape?) = addBounds(CODE.BLUE, name, shape)

    fun addGreenBounds(vararg bs: BoundsElement) = addBounds(CODE.GREEN, *bs)
    fun addGreenBounds(shape: () -> Shape?) = addBounds(CODE.GREEN, shape)
    fun addGreenBounds(name: String, shape: () -> Shape?) = addBounds(CODE.GREEN, name, shape)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addController(controller: () -> (() -> TempAction)) = add(Controller(controller))
    fun addController(vararg controller: Controller) = add(*controller)

    fun addListener(listener: (InputAction) -> Unit) = add(ActionListener(listener))
    fun addListener(vararg listener: ActionListener) = add(*listener)

    fun addAbility(ability: () -> Unit) = add(AbilityBox(ability))
    fun addAbility(vararg ability: AbilityBox) = add(*ability)

    fun addProperty(vararg property: GameProperty) = add(*property)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addVisual(vararg vis: Visualiser) = add(*vis)

    fun addVisual(layer: DrawLayer, vis: (agc: AffineGraphics, draw_pos: PointN) -> Unit) {
        add(
            object: LayerVisualiser(layer) {
                override fun draw(draw_pos: PointN) {
                    vis(agc, draw_pos)
                }
            }
        )
    }

    fun addVisual(layer: DrawLayer, sort_pos: PointN, vis: (agc: AffineGraphics, draw_pos: PointN) -> Unit) {
        add(
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

    fun onInit(f: () -> Unit) = add(OnInitComponent(f))
    fun onInit(f: OnInitComponent) = add(f)

    fun onLoad(f: () -> Unit) = add(OnLoadComponent(f))
    fun onLoad(f: OnLoadComponent) = add(f)

    fun onBirth(f: () -> Unit) = add(OnBirthComponent(f))
    fun onBirth(f: OnBirthComponent) = add(f)

    fun onDeath(f: () -> Unit) = add(OnDeathComponent(f))
    fun onDeath(f: OnDeathComponent) = add(f)

    fun onGrab(f: () -> Unit) = add(OnGrabComponent(f))
    fun onGrab(f: OnGrabComponent) = add(f)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
