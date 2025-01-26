package com.uzery.fglib.core.room.entry

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.RoomLoadUtils
import com.uzery.fglib.core.room.mask.RoomMask
import com.uzery.fglib.utils.FGUtils
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.entry.FGEntry
import com.uzery.fglib.utils.data.entry.FGFormat
import com.uzery.fglib.utils.data.file.TextData
import com.uzery.fglib.utils.data.getter.AbstractClassGetter
import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.math.geom.shape.RectN
import java.util.*
import kotlin.collections.ArrayList

object FGRoomFormat: FGRoomSerialization() {
    override fun writeTo(filepath: String, entry: FGRoomLoadEntry, getter: AbstractClassGetter<GameObject>) {
        writeRoom(filepath, entry.room, getter)
        val dir = getDir(filepath)
        entry.masks.forEach { mask -> writeMask(dir+mask.name+".mask", mask) }
    }

    private fun writeRoom(filename: String, entry: FGRoomEntry, getter: AbstractClassGetter<GameObject>) {
        val room = RoomLoadUtils.makeRoom(getter, entry).toString()

        TextData.write(filename, room, true)
    }

    private fun writeMask(filename: String, mask: RoomMask) {
        TextData.write(filename, mask.toString(), true)
    }

    /////////////////////////////////////////////////////////////////////////////////////

    override fun readFrom(filepath: String): FGRoomLoadEntry {
        val room = getRoomEntry(filepath)
        val masks = getAllMasks(filepath)

        return FGRoomLoadEntry(room, masks)
    }

    /////////////////////////////////////////////////////////////////////////////////////

    private val room_info_cg = object: ClassGetter<RectN>() {
        override fun addAll() {
            add("room", 2) { RectN(pos, size) }
        }
    }

    private fun getRoomEntry(filename: String): FGRoomEntry {
        val lines = getLines(filename)

        val objects = ArrayList<FGEntry>()
        var next = lines.removeFirst()

        val room_info = room_info_cg[next]

        while (lines.isNotEmpty()) {
            next = lines.removeFirst()
            if (FGUtils.isComment(next)) continue
            objects.add(FGFormat.entryFrom(next))
        }

        return FGRoomEntry(room_info.pos, room_info.size, objects)
    }

    /////////////////////////////////////////////////////////////////////////////////////

    private fun getAllMasks(filepath: String): ArrayList<RoomMask> {
        val masks = ArrayList<RoomMask>()
        val dir = getDir(filepath)
        val files = TextData.filesFrom(dir)
        val mask_filenames = files.filter { filename -> filename.endsWith(".mask") }

        mask_filenames.forEach { filename ->
            masks.add(getMask(filename))
        }

        return masks
    }

    private fun getMask(filename: String): RoomMask {
        val lines = getLines(filename)
        val name = filename.substring(filename.lastIndexOf("/")+1, filename.lastIndexOf("."))
        val mask = RoomMask(name)

        while (lines.isNotEmpty()) {
            val next = lines.removeFirst()
            if (FGUtils.isComment(next)) continue

            val sign = next[0]
            val action = next.substring(2)
            when (sign) {
                '+' -> mask.toAdd(FGFormat.entryFrom(action))
                '-' -> mask.toRemove(FGFormat.entryFrom(action))
                else -> throw DebugData.error("mask sign: $sign")
            }
        }

        return mask
    }

    /////////////////////////////////////////////////////////////////////////////////////

    private fun getDir(filepath: String): String {
        val prev = filepath.substring(0, filepath.lastIndexOf("/")+1)
        val name = filepath.substring(filepath.lastIndexOf("/")+1)
        return prev+name.substring(0, name.lastIndexOf("."))+"/"
    }

    private fun getLines(filename: String): LinkedList<String> {
        val lines = TextData.readLines(filename)

        val list = LinkedList<String>()
        list.addAll(lines)

        list.removeIf { FGUtils.isComment(it) }

        return list
    }
}
