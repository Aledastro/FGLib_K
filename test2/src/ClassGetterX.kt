import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.math.getter.ClassGetterInstance

class ClassGetterX: ClassGetterInstance<GameObject>() {
    override fun addAll() {
        add("player", 1) { Snake(pos) }

    }
}