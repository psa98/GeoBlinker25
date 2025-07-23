package com.example.geoblinker.data

import androidx.annotation.StringRes
import com.example.geoblinker.R

enum class SignalType(
    val rawNames: List<String>,
    @StringRes val description: Int
) {
    MovementStarted(
        listOf("e_alarm_speed", "e_alarm_displacement"),
        R.string.movement_started
    ),
    Stop(
        listOf("e_alarm_speeding_end"),
        R.string.stop
    ),
    LowCharge(
        listOf("e_alarm_low_battery"),
        R.string.low_charge
    ),
    DoorOpen(
        listOf("e_alarm_illegal_open", "e_alarm_door"),
        R.string.door_open
    ),
    VibrationAlarm(
        listOf("e_alarm_shake"),
        R.string.vibration_alarm
    );

    companion object {
        fun fromRaw(raw: String): SignalType? =
            entries.firstOrNull { raw in it.rawNames }
    }
}