package hu.bme.aut.android.onlab.data

import android.graphics.Bitmap
import com.airbnb.mvrx.MavericksState

data class Recipie(
    var name: String? = null,
    var favourite: Boolean? = null,
    var flags: List<String?>? = null,
    var imageUrls: List<String?>? = null,
    var time: String? = null,
    var abundance: String? = null,
    var author: String? = null,
    var ingredients: Map<String?, String?>? = null,
    var steps: List<String?>? = null,
    var shares: List<String?>? = null
) : MavericksState