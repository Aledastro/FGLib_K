package launch

import com.uzery.fglib.core.program.LaunchOptions
import com.uzery.fglib.core.program.Launcher
import game.Game

fun main(args: Array<String>) {
    Launcher().startProcess(LaunchOptions(720.0, 720.0), Game())
}
