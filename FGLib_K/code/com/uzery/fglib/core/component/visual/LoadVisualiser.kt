package com.uzery.fglib.core.component.visual

import com.uzery.fglib.core.component.group.GroupVisualiser
import com.uzery.fglib.core.component.reaction.OnLoadComponent

/**
 * [GroupVisualiser] containing [OnLoadComponent] and [Visualiser]
 **/
open class LoadVisualiser(load: OnLoadComponent, vis: Visualiser): GroupVisualiser(load, vis)
