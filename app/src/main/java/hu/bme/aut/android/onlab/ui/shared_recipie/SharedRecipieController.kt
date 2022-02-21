package hu.bme.aut.android.onlab.ui.shared_recipie

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import com.google.android.material.chip.Chip
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.*
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.data.ShoppingItem
import hu.bme.aut.android.onlab.ui.epoxy.ViewBindingKotlinModel
import hu.bme.aut.android.onlab.ui.recipie.RecipieController
import java.util.*
import kotlin.collections.ArrayList


class SharedRecipieController(
    private val context: Context?,
    private val saved_rec: Recipie,
    private var prep_title: String,
    private val inflater: LayoutInflater
) : EpoxyController()
{
    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    fun deleteShare(rec_name: String?){
        db.collection("recipies").whereEqualTo("name", rec_name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                for(doc in snapshot.documents){
                    if(doc["author"] != firebaseUser?.email){
                        val tmp_list = doc["shares"] as ArrayList<*>
                        tmp_list.remove(firebaseUser?.email)
                        db.collection("recipies").document(doc.id).
                        update("shares", tmp_list)
                    }
                    Toast.makeText(inflater.context, "Share deleted successfully",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun saveCart(){

        if(saved_rec.ingredients?.isEmpty()!!){
            return
        }

        var flag_ok = true
        saved_rec.ingredients!!.keys.forEach{ item ->
            val quant_unit = saved_rec.ingredients!![item]?.split(" ")
            val new_item = ShoppingItem(item, quant_unit?.get(0)?.toFloat(),
                quant_unit?.get(1), firebaseUser?.email, false)
            db.collection("shopping_list").add(new_item).addOnFailureListener{
                flag_ok = false
            }
        }
        when {
            flag_ok -> {
                Toast.makeText(this.context, "All successfully added", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this.context, "Add failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun buildModels() {

        HeaderEpoxyModel(this, saved_rec).id(saved_rec.name).addTo(this)
        InformationsEpoxyModel(saved_rec).id(saved_rec.time).addTo(this)

        if(saved_rec.ingredients?.isEmpty()!!){
            return
        }
        saved_rec.ingredients!!.keys.forEach{ item ->
            RecipieController.IngredientEpoxyModel(item, saved_rec.ingredients!![item]).id(item).addTo(this)
        }
        PreparationTextEpoxyModel(prep_title).id(prep_title).addTo(this)

        if(saved_rec.steps?.isEmpty()!!){
            return
        }
        saved_rec.steps!!.forEach { item ->
            PreparationEpoxyModel(item).id(item).addTo(this)
        }
        AddCartEpoxyModel(this).id(this.toString()).addTo(this)
    }

    // Data classes
    data class HeaderEpoxyModel(
        val controller: SharedRecipieController,
        val saved_rec: Recipie,
    ):
        ViewBindingKotlinModel<SharedRecipieHeaderBinding>(R.layout.shared_recipie_header){
        override fun SharedRecipieHeaderBinding.bind() {
            tvRecipieName.text = saved_rec.name
            if(saved_rec.favourite == true){
                imgBtnFavourite.setImageResource(R.drawable.ic_menu_favourites)
            }
            imgBtnUnshare.setOnClickListener {
                controller.deleteShare(saved_rec.name)
                it.findNavController().navigate(R.id.action_nav_shared_recipie_to_nav_shares)
            }
        }
    }

    data class InformationsEpoxyModel(val saved_rec: Recipie):
        ViewBindingKotlinModel<SharedRecipieInformationsBinding>(R.layout.shared_recipie_informations){
        @SuppressLint("SetTextI18n")
        override fun SharedRecipieInformationsBinding.bind() {
            tvRecipieAuthor.text = saved_rec.author
            tvRecipieTime.text = saved_rec.time
            tvRecipieAbundance.text = saved_rec.abundance
//            if(Locale.getDefault().displayLanguage == "en"){
//                if(saved_rec.abundance!! > 1){
//                    tvRecipieAbundance.text = saved_rec.abundance.toString() + " portions"
//                } else{
//                    tvRecipieAbundance.text = saved_rec.abundance.toString() + " portion"
//                }
//            } else {
//                tvRecipieAbundance.text = saved_rec.abundance.toString() + " adag"
//            }
            var chipGroup: ChipGroup = cgRecipieFlags
            chipGroup.removeAllViews()
            if(saved_rec.flags != null){
                for (element in saved_rec.flags!!) {
                    val chip = Chip(chipGroup.context)
                    chip.text = element
                    chip.isChecked = true
                    chip.isClickable = false
                    chip.isCheckable = false
                    chipGroup.addView(chip)
                }
            }
        }
    }

    data class IngredientEpoxyModel(val ingredient: String?):
            ViewBindingKotlinModel<EpoxyRecipeIngredientItemBinding>(R.layout.epoxy_recipe_ingredient_item){
        override fun EpoxyRecipeIngredientItemBinding.bind() {
            tvIngredient.text = ingredient
        }
    }

    data class PreparationTextEpoxyModel(val title: String):
        ViewBindingKotlinModel<EpoxyRecipiePreparationTextBinding>(R.layout.epoxy_recipie_preparation_text){
        override fun EpoxyRecipiePreparationTextBinding.bind() {
            tvRecipiePrepTitle.text = title
        }
    }

    data class PreparationEpoxyModel(val step: String?):
        ViewBindingKotlinModel<EpoxyItemBinding>(R.layout.epoxy_item){
        override fun EpoxyItemBinding.bind(){
            tvTitle.text = step
        }
    }

    data class AddCartEpoxyModel(val controller: SharedRecipieController):
        ViewBindingKotlinModel<SharedRecipieFltBtnBinding>(R.layout.shared_recipie_flt_btn){
        override fun SharedRecipieFltBtnBinding.bind() {
            fltBtnAddCart.setOnClickListener {
                controller.saveCart()
            }
        }
    }}

