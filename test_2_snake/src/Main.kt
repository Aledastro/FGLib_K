import com.uzery.fglib.core.program.LaunchOptions
import com.uzery.fglib.core.program.Launcher

fun main() {
    Launcher().startProcess(LaunchOptions(720.0, 720.0), Game())
}
