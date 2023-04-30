package launch

import com.uzery.fglib.core.program.LaunchOptions
import com.uzery.fglib.core.program.Launcher
import com.uzery.fglib.utils.math.geom.PointN
import game.Game

fun main(args: Array<String>) {
    Launcher().startProcess(LaunchOptions(PointN(780.0 + 360.0, 780.0)), Game())
}
