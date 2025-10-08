package com.example.geoblinker.data

import com.example.geoblinker.ui.main.GeoBlinker

enum class SignalType(
    val rawNames: List<String>,

) {
    ShakeAlarm(
        listOf("e_alarm_shake"),

    ),
    InFence(
        listOf("e_alarm_in_fence"),

    ),
    LowPower(
    listOf("e_alarm_lowpower"),

    ),
    OutFence(
        listOf("e_alarm_out_fence"),

    ),
    PowerCut(
        listOf("e_alarm_power_cut_off"),

    ),
    AccOff(
    listOf("e_alarm_acc_off"),

    ),
    LowBat(
        listOf("e_alarm_low_battery"),

    ),
    Speeding(
        listOf("e_alarm_speed"),

    ),
    SpeedingEnd(
        listOf("e_alarm_speeding_end"),

    );

    companion object {
        fun fromRaw(raw: String): SignalType? =
            entries.firstOrNull { raw in it.rawNames }
        fun getScreenName (type:SignalType):String =  GeoBlinker.langData.getNameForEvent(type.rawNames.first())

    }

}