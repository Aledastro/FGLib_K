import com.uzery.fglib.core.program.LaunchOptions
import com.uzery.fglib.core.program.Launcher

fun main() {
    Launcher().startProcess(LaunchOptions(700.0, 700.0), Game())
}
