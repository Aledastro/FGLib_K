import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.math.getter.ClassGetterInstance

class ClassGetterX: ClassGetterInstance<GameObject>() {
    override fun addAll() {
        add("snake", 1) { Snake(pos) }
        add("wall", 1) { Wall(pos) }
    }
}