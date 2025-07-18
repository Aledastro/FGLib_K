package com.uzery.fglib.core.room.entry

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.RoomLoadUtils
import com.uzery.fglib.core.room.mask.RoomMask
import com.uzery.fglib.utils.FGUtils
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.entry.FGEntry
import com.uzery.fglib.utils.data.entry.FGFormat
import com.uzery.fglib.utils.data.getter.AbstractClassGetter
import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.file.FileUtils
import com.uzery.fglib.utils.file.FilenameUtils
import com.uzery.fglib.utils.math.geom.shape.RectN

object FGRoomFormat: RoomSerialization() {
    override fun writeTo(filepath: String, entry: FGRoomLoadEntry, getter: AbstractClassGetter<GameObject>) {
        val dir = getDedicatedDir(filepath)
        writeRoom(filepath, entry.room, getter)
        entry.masks.forEach { mask -> writeMask(dir+mask.name+".mask", mask) }
    }

    private fun writeRoom(filename: String, entry: FGRoomEntry, getter: AbstractClassGetter<GameObject>) {
        val room = RoomLoadUtils.makeRoom(getter, entry).toString()

        FileUtils.write(filename, room, true)
    }

    private fun writeMask(filename: String, mask: RoomMask) {
        FileUtils.write(filename, mask.toString(), true)
    }

    /////////////////////////////////////////////////////////////////////////////////////

    override fun readFrom(filepath: String): FGRoomLoadEntry {
        val files = FileUtils.filesFrom(getDedicatedDir(filepath))

        val room_filename = filepath //files.find { FileUtils.extensionOf(it) == "room" }
            ?: throw DebugData.error("room file not found in: $filepath")

        val mask_filenames = files.filter { filename -> FilenameUtils.extensionOf(filename) == "mask" }
        val masks = List(mask_filenames.size) { i -> getMask(mask_filenames[i]) }

        return FGRoomLoadEntry(getRoomEntry(room_filename), masks)
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

        return FGRoomEntry(FilenameUtils.nameOf(filename), room_info.pos, room_info.size, objects)
    }

    /////////////////////////////////////////////////////////////////////////////////////

    private fun getMask(filename: String): RoomMask {
        val lines = getLines(filename)
        val mask = RoomMask(FilenameUtils.nameOf(filename))

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

    private fun getDedicatedDir(filepath: String): String {
        val prev = FilenameUtils.dirOf(filepath)
        val name = FilenameUtils.nameOf(filepath)
        return "$prev/$name/"
    }

    private fun getLines(filename: String): ArrayList<String> {
        val lines = FileUtils.readLines(filename)

        val list = ArrayList<String>()
        list.addAll(lines)

        list.removeIf { FGUtils.isComment(it) }

        return list
    }
}
