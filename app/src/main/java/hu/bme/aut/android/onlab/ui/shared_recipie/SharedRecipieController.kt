package hu.bme.aut.android.onlab.ui.shared_recipie

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.airbnb.epoxy.EpoxyController
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.*
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.data.ShoppingItem
import hu.bme.aut.android.onlab.ui.epoxy.ViewBindingKotlinModel


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
        saved_rec.ingredients!!.forEach{ item ->
            val new_item = ShoppingItem(item, author = firebaseUser?.email, checked = false)
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
        override fun SharedRecipieInformationsBinding.bind() {
            tvRecipieAuthor.text = saved_rec.author
            tvRecipieTime.text = saved_rec.time
            tvRecipieAbundance.text = saved_rec.abundance
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

    data class PreparationEpoxyModel(val step: String?):
        ViewBindingKotlinModel<ItemBinding>(R.layout.item){
        override fun ItemBinding.bind(){
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

