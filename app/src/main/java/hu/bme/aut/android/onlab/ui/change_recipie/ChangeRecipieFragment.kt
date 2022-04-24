package hu.bme.aut.android.onlab.ui.change_recipie

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.group
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import hu.bme.aut.android.onlab.*
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FragmentChangeRecipieBinding
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.*

class ChangeRecipieFragment : Fragment(), MavericksView{

    private val changeRecipieViewModel: ChangeRecipieViewModel by fragmentViewModel()
    private var _binding: FragmentChangeRecipieBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChangeRecipieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun invalidate() = withState(changeRecipieViewModel) { state ->
        binding.ervChangeRecipie.withModels {
            val rec = state.changedRecipie ?: state.recipeRequest() ?: return@withModels

            // Header
            changeRecipieHeader {
                id("header")
                viewmodel(changeRecipieViewModel)
                onClickCancelButton{ _ ->
                    findNavController().navigate(R.id.action_nav_change_recipie_to_nav_recipie)
                }
                onClickFavouriteButton{ _ ->
                    changeRecipieViewModel.checkFavourite()
                }
            }

            // Photo
            // TODO

            //Chipgroup title
            chipgroupText {
                id("chipgroup_title")
            }

            // Chipgroup
            group {
                id("chipgroup")
                layout(R.layout.epoxy_recipie_chigroup)
                if(rec.flags != null){
                    for(flag in rec.flags!!){
                        changeRecipieChipitem {
                            id(flag)
                            title(flag)
                            viewmodel(changeRecipieViewModel)
                        }
                    }
                }
            }

            // Informations
            changeRecipieInformations {
                id("info")
                viewmodel(changeRecipieViewModel)
            }

            // Ingredient items
            rec.ingredients?.forEach{ ingr ->
                newRecipieItem {
                    id(ingr.key)
                    ingredientTextView(ingr.key)
                    quantityTextView(ingr.value)
                    onClickDeleteButton{ _ ->
                        changeRecipieViewModel.deleteIngredient(ingr.key!!)
                    }
                }
            }

            // Add ingredient button
            newRecipieFloatingButton {
                id("add_ingr_btn")
                title(getString(R.string.btn_add_new_ingredient))
                onClickFltButton{ _ ->
                    changeRecipieViewModel.addIngredientDialog(requireContext())
                }
            }

            // Prep text
            recipiePreparationText {
                id("prep_text")
            }

            // Preparation items
            rec.steps?.forEach { step ->
                newRecipiePrepItem {
                    id(step)
                    item(step)
                    onClickDeleteButton{ _ ->
                        changeRecipieViewModel.deleteSteps(step!!)
                    }
                }
            }

            // Add prep item button
            newRecipieFloatingButton {
                id("add_prep_item_btn")
                title(getString(R.string.btn_add_new_step))
                onClickFltButton{ _ ->
                    changeRecipieViewModel.addPrepDialog(requireContext())
                }
            }

            // Save button
            newRecipieSaveBtn {
                id("save")
                onClickSave{ _ ->
                    changeRecipieViewModel.checkChips(requireContext())//saveRecipie(requireContext())
                    findNavController().navigate(
                        R.id.action_nav_change_recipie_to_nav_recipie,
                        changeRecipieViewModel.getArgs()
                    )
                }
            }

            // Delete button - @string/change_rec_delete_recipie
            changeRecipieDeleteBtn {
                id("delete")
                onClickDeleteButton { _ ->
                    changeRecipieViewModel.deleteRecipie(requireContext())
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    fun takePicture(){
//        flag_photo = true
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(takePictureIntent, REQUEST_CODE)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode != Activity.RESULT_OK) {
//            return
//        }
//
//        if (requestCode == REQUEST_CODE) {
//            val imageBitmap = data?.extras?.get("data") as? Bitmap
//            tmp_bitmap = imageBitmap
//            changerecipieController.addPhoto(tmp_bitmap)
//        }
//
//    }
//
//    fun getBitmap(): Bitmap?{
//        return tmp_bitmap
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    fun saveWithPhoto(recipie: Recipie, old_name: String){
//        val bitmap = changerecipieController.getBitmap()
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val imageInBytes = baos.toByteArray()
//
//        val storageReference = FirebaseStorage.getInstance().reference
//        val newImageName = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
//        val newImageRef = storageReference.child("images/$newImageName")
//
//        newImageRef.putBytes(imageInBytes)
//            .addOnFailureListener { exception ->
//                Toast.makeText(this.context, exception.message, Toast.LENGTH_SHORT).show()
//            }
//            .continueWithTask { task ->
//                if (!task.isSuccessful) {
//                    task.exception?.let { throw it }
//                }
//
//                newImageRef.downloadUrl
//            }
//            .addOnSuccessListener { downloadUri ->
//                img_url = downloadUri.toString()
//                uploadRecipie(downloadUri.toString(), recipie, old_name)
//            }
//    }
//
//    fun SaveClick(recipie: Recipie, old_name: String){
//        if(!flag_photo){
//            uploadRecipie(tmp_rec = recipie, old_name = old_name)
//        } else {
//            try {
//                saveWithPhoto(recipie, old_name)
//            } catch (e: Exception){
//                e.printStackTrace()
//            }
//        }
//    }
//
//    fun uploadRecipie(imageUrl: String? = null, tmp_rec: Recipie, old_name: String){
//        if (imageUrl != null) {
//            val images: ArrayList<String> = arrayListOf<String>()
//            images.add(imageUrl)
//            tmp_rec.imageUrls = images
//        }
//
////        for(url in old_photos){
////            if(tmp_rec.imageUrls?.contains(url) == false){
////                val desertRef = storageRef.child(url)
////                desertRef.delete().addOnSuccessListener {
////                    Log.d("PICTURE_DEL: ", "$url deleted")
////                }
////            }
////        }
//
//        db.collection("recipies").whereEqualTo("name", old_name).get().
//        addOnSuccessListener { snapshot ->
//            if(snapshot != null){
//                if(snapshot.documents.isNotEmpty()){
//                    db.collection("recipies").document(snapshot.documents[0].id).
//                    update("abundance", tmp_rec.abundance, "favourite",
//                        tmp_rec.favourite, "flags", tmp_rec.flags, "imageUrls", tmp_rec.imageUrls,
//                        "ingredients", tmp_rec.ingredients, "name", tmp_rec.name, "steps",
//                        tmp_rec.steps, "time", tmp_rec.time)
//                        .addOnSuccessListener {
//                        Toast.makeText(layoutInflater.context, "Changes saved", Toast.LENGTH_SHORT).show()
//                    }.addOnFailureListener { e ->
//                            Toast.makeText(layoutInflater.context, e.toString(), Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//
////        db.collection("recipies").add(recipie)/*.addOnSuccessListener {
////            Toast.makeText(activity?.applicationContext, "Recipie saved", Toast.LENGTH_SHORT).show()
////        }*/.addOnFailureListener { e ->
////            Toast.makeText(activity?.applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
////        }
//
//    }

}