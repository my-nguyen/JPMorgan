package com.nguyen.jpmorgan.model

data class Temperature(
    val day: Float,
    val min: Float,
    val max: Float,
    val night: Float = 0F,
    val eve: Float = 0F,
    val morn: Float = 0F
): java.io.Serializable
