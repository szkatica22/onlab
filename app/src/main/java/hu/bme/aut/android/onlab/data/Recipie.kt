package hu.bme.aut.android.onlab.data

data class Recipie(
    var name: String? = null,
    var favourite: Boolean? = null,
    var imageUrls: List<String?>? = null,
    var author: String? = null,
    var ingredients: List<String?>? = null,
    var steps: List<String?>? = null
)