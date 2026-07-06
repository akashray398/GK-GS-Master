package com.akash.gkgsmaster.data.api.dto

import com.google.gson.annotations.SerializedName

data class WikipediaResponse(
    @SerializedName("query") val query: WikipediaQuery?
)

data class WikipediaQuery(
    @SerializedName("pages") val pages: Map<String, WikipediaPage>?
)

data class WikipediaPage(
    @SerializedName("pageid") val pageId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("extract") val extract: String?
)
