package hu.bme.aut.android.onlab.ui.change_recipie

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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FragmentChangeRecipieBinding
import hu.bme.aut.android.onlab.ui.new_recipie.NewRecipieFragment
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.*

class ChangeRecipieFragment : Fragment(){

    companion object {
        private const val REQUEST_CODE = 101
    }

    private lateinit var changeRecipieViewModel: ChangeRecipieViewModel
    private var _binding: FragmentChangeRecipieBinding? = null

    private lateinit var changerecipieController: ChangeRecipieController

    var btn_ingredient: String = "Add new ingredient"
    var btn_step: String = "Add new step"
    var btn_delete: String = "Delete recipe"
    var btn_save: String = "Save"
    var prep_title: String = "Preparation"

    private val binding get() = _binding!!
    private val db = Firebase.firestore

    var tmp_bitmap: Bitmap? = null
    var img_url: String? = null
    var flag_photo = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeRecipieViewModel =
            ViewModelProvider(this).get(ChangeRecipieViewModel::class.java)

        _binding = FragmentChangeRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rec_name = this.arguments?.get("recipiename").toString()

        db.collection("recipies").whereEqualTo("name", rec_name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot.documents.isNotEmpty()){
                val tmp_data = snapshot.documents[0].data
                val tmp_rec = Recipie(rec_name, tmp_data?.get("favourite") as Boolean,
                    tmp_data.get("flags") as List<String?>?, tmp_data.get("imageUrls") as List<String?>?,
                    tmp_data.get("time").toString(), tmp_data.get("abundance").toString(),
                    tmp_data.get("author").toString(), tmp_data.get("ingredients") as Map<String?, String?>?,
                    tmp_data.get("steps") as List<String?>?, tmp_data.get("shares") as List<String?>?)

                changerecipieController = ChangeRecipieController(this, tmp_rec, btn_ingredient, prep_title,
                    btn_step, btn_delete, btn_save, inflater)
                binding.ervChangeRecipie.setController(changerecipieController)
                changerecipieController.requestModelBuild()
                binding.ervChangeRecipie.addItemDecoration(DividerItemDecoration(requireActivity(),
                    RecyclerView.VERTICAL))
            }
        }
        return root
    }

    fun takePicture(){
        flag_photo = true
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            tmp_bitmap = imageBitmap
            changerecipieController.addPhoto(tmp_bitmap)
        }

    }

    fun getBitmap(): Bitmap?{
        return tmp_bitmap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun saveWithPhoto(recipie: Recipie, old_name: String){
        val bitmap = changerecipieController.getBitmap()
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInBytes = baos.toByteArray()

        val storageReference = FirebaseStorage.getInstance().reference
        val newImageName = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
        val newImageRef = storageReference.child("images/$newImageName")

        newImageRef.putBytes(imageInBytes)
            .addOnFailureListener { exception ->
                Toast.makeText(this.context, exception.message, Toast.LENGTH_SHORT).show()
            }
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }

                newImageRef.downloadUrl
            }
            .addOnSuccessListener { downloadUri ->
                Log.d("downloadURI: ", downloadUri.toString())
                img_url = downloadUri.toString()
                uploadRecipie(downloadUri.toString(), recipie, old_name)
            }
    }

    fun SaveClick(recipie: Recipie, old_name: String){
        if(!flag_photo){
            uploadRecipie(tmp_rec = recipie, old_name = old_name)
        } else {
            try {
                saveWithPhoto(recipie, old_name)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun uploadRecipie(imageUrl: String? = null, tmp_rec: Recipie, old_name: String){
        if (imageUrl != null) {
            val images: ArrayList<String> = arrayListOf<String>()
            images.add(imageUrl)
            tmp_rec.imageUrls = images
        }

        db.collection("recipies").whereEqualTo("name", old_name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                if(snapshot.documents.isNotEmpty()){
                    db.collection("recipies").document(snapshot.documents[0].id).
                    update("abundance", tmp_rec.abundance, "favourite",
                        tmp_rec.favourite, "flags", tmp_rec.flags, "imageUrls", tmp_rec.imageUrls,
                        "ingredients", tmp_rec.ingredients, "name", tmp_rec.name, "steps",
                        tmp_rec.steps, "time", tmp_rec.time)
                        .addOnSuccessListener {
                        Toast.makeText(layoutInflater.context, "Changes saved", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { e ->
                            Toast.makeText(layoutInflater.context, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

//        db.collection("recipies").add(recipie)/*.addOnSuccessListener {
//            Toast.makeText(activity?.applicationContext, "Recipie saved", Toast.LENGTH_SHORT).show()
//        }*/.addOnFailureListener { e ->
//            Toast.makeText(activity?.applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
//        }

    }

}