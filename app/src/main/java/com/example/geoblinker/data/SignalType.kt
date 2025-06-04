package com.example.geoblinker.data

import androidx.annotation.StringRes
import com.example.geoblinker.R

enum class SignalType(
    @StringRes val description: Int
) {
    MovementStarted(R.string.movement_started),
    Stop(R.string.stop),
    LowCharge(R.string.low_charge),
    DoorOpen(R.string.door_open),
    ReachedLocation(R.string.reached_location)
}