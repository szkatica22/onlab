package hu.bme.aut.android.onlab.ui.new_recipie

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import hu.bme.aut.android.onlab.*
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FragmentNewRecipieBinding
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.*

class NewRecipieFragment : Fragment(), MavericksView{

    companion object {
        private const val REQUEST_CODE = 101
    }

    private val newRecipieViewModel: NewRecipieViewModel by fragmentViewModel()
    private var _binding: FragmentNewRecipieBinding? = null

    private val binding get() = _binding!!
    val db = Firebase.firestore

//    var tmp_bitmap: Bitmap? = null
//    var img_url: String? = null
//    var flag_photo = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
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
//            newrecipieController.addPhoto(tmp_bitmap)
//        }
//
//    }
//
//    fun getBitmap(): Bitmap?{
//        return tmp_bitmap
//    }
//
//    fun saveWithPhoto(recipie: Recipie){
//        val bitmap = newrecipieController.getBitmap()
//        val baos = ByteArrayOutputStream()
//        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
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
//                uploadRecipie(downloadUri.toString(), recipie)
//            }
//    }
//
//    fun SaveClick(recipie: Recipie){
//        if(!flag_photo){
//            uploadRecipie(recipie = recipie)
//        } else {
//            try {
//               saveWithPhoto(recipie)
//            } catch (e: Exception){
//                e.printStackTrace()
//            }
//        }
//    }
//
//    fun uploadRecipie(imageUrl: String? = null, recipie: Recipie){
//        if (imageUrl != null) {
//            val images: ArrayList<String> = arrayListOf<String>()
//            images.add(imageUrl)
//            recipie.imageUrls = images
//        }
//
//        db.collection("recipies").add(recipie).addOnSuccessListener {
//            Toast.makeText(layoutInflater.context, "Recipie saved", Toast.LENGTH_SHORT).show()
//        }.addOnFailureListener { e ->
//            Toast.makeText(layoutInflater.context, e.toString(), Toast.LENGTH_SHORT).show()
//        }
//
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun invalidate() = withState(newRecipieViewModel) { state ->
        binding.ervChangeRecipie.withModels {
            val rec = state.recipeRequest() ?: return@withModels

            // Header
            newRecipieHeader {
                id("header")
                recipie(rec)
                viewmodel(newRecipieViewModel)

                onClickCancelButton { _ ->
                    findNavController().navigate(R.id.action_nav_new_recipie_to_nav_recipies)
                }
                onClickFavouriteButton { _ ->
                    newRecipieViewModel.checkFavourite()
                }
            }

            // Photos
            // TODO

            // Chipgroup title
            chipgroupText {
                id("chipgroup_title")
            }

            // Chipgroup
            group {
                id("chipgroup")
                layout(R.layout.epoxy_recipie_chigroup)
                if(rec.flags != null) {
                    for (flag in rec.flags!!) {
                        newRecipieChipitem {
                            id(flag)
                            title(flag)
                            viewmodel(newRecipieViewModel)
                        }
                    }
                }
            }

            // Informations
            newRecipieInformations {
                id("info")
                viewmodel(newRecipieViewModel)
            }

            // Ingredient items
            rec.ingredients?.forEach { ingr ->
                newRecipieItem {
                    id(ingr.key)
                    ingredientTextView(ingr.key)
                    quantityTextView(ingr.value)
                    onClickDeleteButton { _ ->
                        newRecipieViewModel.deleteIngredient(ingr.key!!)
                    }
                }
            }

            // Add ingredient button
            newRecipieFloatingButton {
                id("add_ingr_btn")
                title(getString(R.string.btn_add_new_ingredient))
                onClickFltButton { _ ->
                    newRecipieViewModel.addIngredientDialog(requireContext())
                }
            }

            // Prep text
            recipiePreparationText {
                id("prep_text")
            }

            // Preparation items
            rec.steps?.forEach{ step ->
                newRecipiePrepItem {
                    id(step)
                    item(step)
                    onClickDeleteButton { _ ->
                        newRecipieViewModel.deleteSteps(rec.steps?.indexOf(step)!!)
                    }
                }
            }

            // Add prep item button
            newRecipieFloatingButton {
                id("add_prep_item_btn")
                title(getString(R.string.btn_add_new_step))
                onClickFltButton { _ ->
                    newRecipieViewModel.addPrepDialog(requireContext())
                }
            }

            // Save button
            newRecipieSaveBtn {
                id("save")
                onClickSave { _ ->
                    newRecipieViewModel.saveRecipie(requireContext())
                    findNavController().navigate(R.id.action_nav_new_recipie_to_nav_recipies)
                }
            }
        }
    }
}