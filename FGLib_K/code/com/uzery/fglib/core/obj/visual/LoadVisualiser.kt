package com.uzery.fglib.core.obj.visual

import com.uzery.fglib.core.obj.GroupComponent
import com.uzery.fglib.core.obj.component.OnLoadComponent

open class LoadVisualiser(load: OnLoadComponent, vis: Visualiser): GroupComponent(load, vis)
