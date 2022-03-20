package hu.bme.aut.android.onlab.data

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import java.io.Serializable

data class RecipeArgs(
    val recipeId: String,
) : Serializable

data class RecipeState(
    val recipeId: String,
    val recipeRequest: Async<Recipie> = Uninitialized
) : MavericksState {
    constructor(args: RecipeArgs) : this(recipeId = args.recipeId)
}