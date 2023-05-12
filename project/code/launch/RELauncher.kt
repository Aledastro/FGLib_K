package launch

import com.uzery.fglib.core.program.LaunchOptions
import com.uzery.fglib.core.program.Launcher
import com.uzery.fglib.extension.room_editor.RoomEditor
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.ClassGetter
import game.ClassGetterX
import game.Game

fun main(args: Array<String>) {
    Launcher().startProcess(
        LaunchOptions(PointN(1920, 1080), fullscreen = true),
        RoomEditor(Game.current_filename, ClassGetter(ClassGetterX())))
}
