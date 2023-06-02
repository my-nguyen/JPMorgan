package com.nguyen.jpmorgan.model

data class Temperature(
    val day: Float,
    val min: Float,
    val max: Float,
    val night: Float,
    val eve: Float,
    val morn: Float
): java.io.Serializable
