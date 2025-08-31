package com.example.geoblinker.data

import com.example.geoblinker.ui.main.GeoBlinker

enum class SignalType(
    val rawNames: List<String>,
    val description: String
) {
    ShakeAlarm(
        listOf("e_alarm_shake"),
        GeoBlinker.constants.getNameForEvent("e_alarm_shake")
    ),
    InFence(
        listOf("e_alarm_in_fence"),
        GeoBlinker.constants.getNameForEvent("e_alarm_in_fence")
    ),
    LowPower(
    listOf("e_alarm_lowpower"),
    GeoBlinker.constants.getNameForEvent("e_alarm_lowpower")
    ),
    OutFence(
        listOf("e_alarm_out_fence"),
        GeoBlinker.constants.getNameForEvent("e_alarm_out_fence")
    ),
    PowerCut(
        listOf("e_alarm_power_cut_off"),
        GeoBlinker.constants.getNameForEvent("e_alarm_power_cut_off")
    ),
    AccOff(
    listOf("e_alarm_acc_off"),
    GeoBlinker.constants.getNameForEvent("e_alarm_acc_off")
    ),
    LowBat(
        listOf("e_alarm_low_battery"),
        GeoBlinker.constants.getNameForEvent("e_alarm_low_battery")
    ),
    Speeding(
        listOf("e_alarm_speed"),
        GeoBlinker.constants.getNameForEvent("e_alarm_speed")
    ),
    SpeedingEnd(
        listOf("e_alarm_speeding_end"),
        GeoBlinker.constants.getNameForEvent("e_alarm_speeding_end")
    );

    companion object {
        fun fromRaw(raw: String): SignalType? =
            entries.firstOrNull { raw in it.rawNames }
    }
}