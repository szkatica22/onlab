package hu.bme.aut.android.onlab.data

import com.airbnb.mvrx.MavericksState

data class Recipie(
    var name: String? = null,
    var favourite: Boolean? = null,
    var flags: List<String?>? = emptyList(),
    var imageUrls: List<String?>? = emptyList(),
    var time: String? = null,
    var abundance: String? = null,
    var author: String? = null,
    var ingredients: Map<String?, String?>? = emptyMap(),
    var steps: List<String?>? = emptyList(),
    var shares: List<String?>? = emptyList()
) : MavericksState