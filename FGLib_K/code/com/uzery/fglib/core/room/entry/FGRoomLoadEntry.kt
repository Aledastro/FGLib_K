package com.uzery.fglib.core.room.entry

import com.uzery.fglib.core.room.mask.RoomMask

data class FGRoomLoadEntry(
    val room: FGRoomEntry,
    val masks: List<RoomMask>
)
