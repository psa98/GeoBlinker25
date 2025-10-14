package com.example.geoblinker.data

import com.example.geoblinker.R


object MarkersRepository {
    val markerImageList = listOf(
        R.drawable.m_0,
        R.drawable.m_1,
        R.drawable.m_2,
        R.drawable.m_3,
        R.drawable.m_4,
        R.drawable.m_5,
        R.drawable.m_6,
        R.drawable.m_7,
        R.drawable.m_8,
        R.drawable.m_9,
        R.drawable.m_10,
        R.drawable.m_11,
        R.drawable.m_12,
        R.drawable.m_13,
        R.drawable.m_14,
        )

    val markerChoiceImageList = listOf(
        R.drawable.m_0_round_example,
        R.drawable.m_1_car_example,
        R.drawable.m_2_moto_example,
        R.drawable.m_3_lorry_example,
        R.drawable.m_4_camion_example,
        R.drawable.m_5_bus_example,
        R.drawable.m_6_bulldoxer_example,
        R.drawable.m_7_tractor_example,
        R.drawable.m_8_pricep_example,
        R.drawable.m_9_human_example,
        R.drawable.m_10_child_example,
        R.drawable.m_11_cat_example,
        R.drawable.m_12_dog_example,
        R.drawable.m_13_horse_example,
        R.drawable.m_14_cow_example,
    )
    fun getFilename(id:Int): String {
        return "m_$id.svg"
    }

}