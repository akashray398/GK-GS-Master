package com.akash.gkgsmaster.data.api.dto

import com.google.gson.annotations.SerializedName

data class CountryDto(
    @SerializedName("name") val name: CountryNameDto,
    @SerializedName("capital") val capital: List<String>?,
    @SerializedName("flags") val flags: CountryFlagDto,
    @SerializedName("population") val population: Long,
    @SerializedName("currencies") val currencies: Map<String, CurrencyDto>?,
    @SerializedName("maps") val maps: MapsDto,
    @SerializedName("region") val region: String
)

data class CountryNameDto(
    @SerializedName("common") val common: String,
    @SerializedName("official") val official: String
)

data class CountryFlagDto(
    @SerializedName("png") val png: String,
    @SerializedName("svg") val svg: String
)

data class CurrencyDto(
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String
)

data class MapsDto(
    @SerializedName("googleMaps") val googleMaps: String
)
