package hu.bme.aut.android.onlab.ui.recipie

import android.view.LayoutInflater
import com.airbnb.epoxy.EpoxyController
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.*
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.ui.epoxy.ViewBindingKotlinModel

class RecipieController (private val saved_rec: Recipie, private val rec_name: String,
                         private var ingredients: ArrayList<Item>,
                        private var steps: ArrayList<Item>, private val recipie_name: String,
                        private var flags: ArrayList<String>, private var time: String,
                        private var abundance: String, private var btn_ingredient: String, private var prep_title: String,
                        private var btn_step: String, private var btn_delete: String,
                        private val inflater: LayoutInflater) : EpoxyController()
{
    private val db = Firebase.firestore
//    private lateinit var saved_rec: Recipie

//    fun getRecipie() {
////        var tmp_rec = Recipie()
//        db.collection("recipies").whereEqualTo("name", rec_name).get().
//        addOnSuccessListener { snapshot ->
//            if(snapshot.documents.isNotEmpty()){
//                val tmp_data = snapshot.documents[0].data
////                Log.d("RECIPIE: ", snapshot.documents[0].data.toString())
////                Log.d("DATA: ", tmp_data?.get("author").toString())
////                val tmp_ingr = tmp_data?.get("ingredients") as List<String?>?
//
////                val flags = tmp_data?.get("flags") as List<String?>?
////                val fav = tmp_data?.get("favourite") as Boolean
////                val urls = tmp_data?.get("imageUrls") as List<String?>?
////                val time = tmp_data?.get("time").toString()
////                val abundance = tmp_data?.get("abundance").toString()
////                val author = tmp_data?.get("author").toString()
////                val ingr = tmp_data?.get("ingredients") as List<String?>?
////                val steps = tmp_data?.get("steps") as List<String?>?
//                // TODO: Ezt megnezni holnap!!!!
////                saved_rec = Recipie(rec_name, fav, flags, urls, time, abundance, author, ingr, steps)
////                Log.d("SAVING: ", saved_rec.ingredients.toString())
////                tmp_rec = Recipie(rec_name, fav, flags, urls, time, abundance, author, ingr, steps)
//                saved_rec = Recipie(rec_name, tmp_data?.get("favourite") as Boolean,
//                    tmp_data?.get("flags") as List<String?>?, tmp_data?.get("imageUrls") as List<String?>?,
//                    tmp_data?.get("time").toString(), tmp_data?.get("abundance").toString(),
//                    tmp_data?.get("author").toString(), tmp_data?.get("ingredients") as List<String?>?,
//                    tmp_data?.get("steps") as List<String?>?)
//                build()
////                Log.d("TMP: ", tmp_rec.toString())
//            }
//        }
////        Log.d("TMP II: ", tmp_rec.toString())
//    }

//    fun build(){
//        HeaderEpoxyModel(rec_name).id(rec_name).addTo(this)
//        InformationsEpoxyModel(flags, time, abundance).id(time).addTo(this)
//
//        if(ingredients.isEmpty()){
//            return
//        }
//        ingredients.forEach{ item ->
//            IngredientEpoxyModel(item).id(item.title).addTo(this)
//        }
//        PreparationTextEpoxyModel(prep_title).id(prep_title).addTo(this)
//
//        if(steps.isEmpty()){
//            return
//        }
//        steps.forEach { item ->
//            PreparationEpoxyModel(item).id(item.title).addTo(this)
//        }
//    }

    override fun buildModels() {
        // II.
//        db.collection("recipies").whereEqualTo("name", rec_name).
//        addSnapshotListener { snapshot, error ->
//            if(error != null){
//                Toast.makeText(inflater.context, error.toString(), Toast.LENGTH_SHORT).show()
//            }
//            if(snapshot != null && snapshot.documents.isNotEmpty()){
//                val tmp_data = snapshot.documents[0].data
//                val saved_rec = Recipie(rec_name, tmp_data?.get("favourite") as Boolean,
//                    tmp_data?.get("flags") as List<String?>?, tmp_data?.get("imageUrls") as List<String?>?,
//                    tmp_data?.get("time").toString(), tmp_data?.get("abundance").toString(),
//                    tmp_data?.get("author").toString(), tmp_data?.get("ingredients") as List<String?>?,
//                    tmp_data?.get("steps") as List<String?>?)
//                Log.d("TMP: ", saved_rec.toString())
//            }
//            HeaderEpoxyModel(rec_name).id(rec_name).addTo(this)
//            InformationsEpoxyModel(flags, time, abundance).id(time).addTo(this)
//
//            if(ingredients.isNotEmpty()){
//                ingredients.forEach{ item ->
//                    IngredientEpoxyModel(item).id(item.title).addTo(this)
//                }
//            }
//            PreparationTextEpoxyModel(prep_title).id(prep_title).addTo(this)
//
//            if(steps.isNotEmpty()){
//                steps.forEach { item ->
//                    PreparationEpoxyModel(item).id(item.title).addTo(this)
//                }
//            }
//
//
//        }

        // III.

//        when(::saved_rec.isInitialized){
//            true -> {
//                HeaderEpoxyModel(rec_name).id(rec_name).addTo(this)
//                InformationsEpoxyModel(flags, time, abundance).id(time).addTo(this)
//
//                if(ingredients.isEmpty()){
//                    return
//                }
//                ingredients.forEach{ item ->
//                    IngredientEpoxyModel(item).id(item.title).addTo(this)
//                }
//                PreparationTextEpoxyModel(prep_title).id(prep_title).addTo(this)
//
//                if(steps.isEmpty()){
//                    return
//                }
//                steps.forEach { item ->
//                    PreparationEpoxyModel(item).id(item.title).addTo(this)
//                }
//            }
//        }

        // I.
        // Get Recipie from Firestore
//        getRecipie()
//        Log.d("RECIPIE: ", saved_rec.toString())

        HeaderEpoxyModel(saved_rec).id(saved_rec.name).addTo(this)
        InformationsEpoxyModel(saved_rec/*, flags, time, abundance*/).id(saved_rec.time).addTo(this)

        if(saved_rec.ingredients?.isEmpty()!!){
            return
        }
        saved_rec.ingredients!!.forEach{ item ->
            IngredientEpoxyModel(item).id(item).addTo(this)
        }
        PreparationTextEpoxyModel(prep_title).id(prep_title).addTo(this)

        if(saved_rec.steps?.isEmpty()!!){
            return
        }
        saved_rec.steps!!.forEach { item ->
            PreparationEpoxyModel(item).id(item).addTo(this)
        }
    }

    // Data classes
    data class HeaderEpoxyModel(val saved_rec: Recipie):
        ViewBindingKotlinModel<RecipieHeaderBinding>(R.layout.recipie_header){
        override fun RecipieHeaderBinding.bind() {
            tvRecipieName.text = saved_rec.name
            imgBtnEdit.setOnClickListener {
                it.findNavController().navigate(R.id.action_nav_recipie_to_nav_change_recipie)
            }
        }
    }

    data class InformationsEpoxyModel(val saved_rec: Recipie/*, val flags: ArrayList<String>, val time: String, val abundance: String*/):
        ViewBindingKotlinModel<RecipieInformationsBinding>(R.layout.recipie_informations){
        override fun RecipieInformationsBinding.bind() {
            tvRecipieTime.text = saved_rec.time
            tvRecipieAbundance.text = saved_rec.abundance
            var chipGroup: ChipGroup = cgRecipieFlags
            chipGroup.removeAllViews()
            if(saved_rec.flags != null){
                for (i in 0 until saved_rec.flags!!.size) {
                    val chip = Chip(chipGroup.context)
                    chip.text = saved_rec.flags!![i]
                    chip.isClickable = false
                    chip.isCheckable = false
                    chipGroup.addView(chip)
                }
            }
        }
    }

    data class IngredientEpoxyModel(val ingredient: String?):
            ViewBindingKotlinModel<RecipeIngredientItemBinding>(R.layout.recipe_ingredient_item){
        override fun RecipeIngredientItemBinding.bind() {
            tvIngredient.text = ingredient
        }
    }

    data class PreparationTextEpoxyModel(val title: String):
        ViewBindingKotlinModel<RecipiePreparationTextBinding>(R.layout.recipie_preparation_text){
        override fun RecipiePreparationTextBinding.bind() {
            tvRecipiePrepTitle.text = title
        }
    }

    data class PreparationEpoxyModel(val step: String?): //tv_recipie_ingredient_item
        ViewBindingKotlinModel<ItemBinding>(R.layout.item){
        override fun ItemBinding.bind(){
            tvTitle.text = step
        }
    }

}

