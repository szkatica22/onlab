package hu.bme.aut.android.onlab.ui.recipie

import android.content.Context
import android.os.Bundle
import android.util.Log
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


class RecipieController(
    private val context: Context?,
    private val saved_rec: Recipie,
    private var prep_title: String,
    private var other_users: List<String>,
    private val inflater: LayoutInflater
) : EpoxyController()
{
    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    fun deleteRecipie(rec_name: String?, inflater: LayoutInflater){

        db.collection("recipies").whereEqualTo("name", rec_name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                for(doc in snapshot.documents){
                    db.collection("recipies").document(doc.id).delete()
                    Toast.makeText(inflater.context, "Recipie deleted", Toast.LENGTH_SHORT).show()
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

    fun saveShares(tmp_users: List<String>) {

        // Recept: saved_rec

        db.collection("recipies").whereEqualTo("name", saved_rec.name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                if(snapshot.documents.isNotEmpty()){
                    db.collection("recipies").document(snapshot.documents[0].id).
                    update("shares", tmp_users)
                    Toast.makeText(this.context, "Recipe Shared Successfully",
                        Toast.LENGTH_SHORT).show()
                }
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
            val idx = saved_rec.ingredients!!.indexOf(item)
            IngredientEpoxyModel(item, saved_rec.ingr_quantities?.get(idx)).id(item).addTo(this)
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
        val controller: RecipieController,
        val saved_rec: Recipie,
    ):
        ViewBindingKotlinModel<RecipieHeaderBinding>(R.layout.recipie_header){
        override fun RecipieHeaderBinding.bind() {
            tvRecipieName.text = saved_rec.name
            imgBtnEdit.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("recipiename", saved_rec.name)
                it.findNavController().navigate(R.id.action_nav_recipie_to_nav_change_recipie, bundle)
            }
            if(saved_rec.favourite == true){
                imgBtnFavourite.setImageResource(R.drawable.ic_menu_favourites)
            }
            imgBtnDelete.setOnClickListener {
                controller.deleteRecipie(saved_rec.name, controller.inflater)
                it.findNavController().navigate(R.id.action_nav_recipie_to_nav_recipies)
            }
        }
    }

    data class InformationsEpoxyModel(val saved_rec: Recipie):
        ViewBindingKotlinModel<RecipieInformationsBinding>(R.layout.recipie_informations){
        override fun RecipieInformationsBinding.bind() {
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

    data class IngredientEpoxyModel(val ingredient: String?, val quantity: String?):
            ViewBindingKotlinModel<RecipeIngredientItemBinding>(R.layout.recipe_ingredient_item){
        override fun RecipeIngredientItemBinding.bind() {
            tvIngredient.text = ingredient
            tvQuantity.text = quantity
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

    data class AddCartEpoxyModel(val controller: RecipieController):
            ViewBindingKotlinModel<AddShopListFltBtnBinding>(R.layout.add_shop_list_flt_btn){
        override fun AddShopListFltBtnBinding.bind() {
            fltBtnAddCart.setOnClickListener {
                controller.saveCart()
            }
            // Share button
            var all_user = controller.other_users.toTypedArray() // arrayOf(/*"valami@valami.hu",*/ "s@s.hu", "teszt@nev.com", "meg@egy.com")
            var chose_users = booleanArrayOf()
            for(itm in all_user){
                if(controller.saved_rec.shares?.contains(itm) == true){
                    chose_users += true
                } else {
                    chose_users += false
                }
            }
//            var chose_users = booleanArrayOf(false, false, false)
            fltBtnShareRecipe.setOnClickListener {
                val add_dialog = AlertDialog.Builder(controller.inflater.context)

                add_dialog.setTitle("Share with:")
                add_dialog.setMultiChoiceItems(all_user, chose_users){ dialog, position, isChecked ->
                    chose_users[position] = isChecked
                }
                add_dialog.setPositiveButton("Ok"){
                        dialog,_->
                    // Save chose users
                    var tmp_users = emptyList<String>()
                    for (idx in chose_users.indices){
                        if(chose_users[idx]){
                            tmp_users += all_user[idx]
                        }
                    }
                    controller.saveShares(tmp_users)
                    dialog.dismiss()
                }
                add_dialog.setNegativeButton("Cancel"){
                        dialog,_->
                    Toast.makeText(controller.inflater.context, "Cancel", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                add_dialog.create()
                add_dialog.show()
            }
        }
    }
}

