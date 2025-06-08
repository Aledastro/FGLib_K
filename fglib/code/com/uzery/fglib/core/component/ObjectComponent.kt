package com.uzery.fglib.core.component

import com.uzery.fglib.core.component.ability.AbilityBox
import com.uzery.fglib.core.component.bounds.BoundsComponent
import com.uzery.fglib.core.component.controller.Controller
import com.uzery.fglib.core.component.group.*
import com.uzery.fglib.core.component.listener.ActionListener
import com.uzery.fglib.core.component.reaction.*
import com.uzery.fglib.core.component.resource.AudioResource
import com.uzery.fglib.core.component.resource.ImageResource
import com.uzery.fglib.core.component.resource.SpriteResource
import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.GameObject

/**
 * Base class for component in *Entity-Component System*
 *
 * Group components:
 * - [GroupComponent]
 * - [GroupAbility], [GroupBounds], [GroupController], [GroupListener], [GroupVisualiser]
 *
 * Basic components:
 * - [AbilityBox], [BoundsComponent], [Controller], [ActionListener], [Visualiser]
 *
 * Reaction components:
 * - [OnBirthComponent],  [OnDeathComponent], [OnGrabComponent], [OnInitComponent], [OnLoadComponent]
 *
 * Resource components:
 * - [AudioResource], [ImageResource], [SpriteResource]
 *
 * @see [GameObject]
 **/
interface ObjectComponent
