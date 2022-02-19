package hu.bme.aut.android.onlab.ui.new_recipie

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FragmentNewRecipieBinding
import hu.bme.aut.android.onlab.databinding.NewRecipieHeaderBinding
import hu.bme.aut.android.onlab.databinding.PhotoItemBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URLEncoder
import java.util.*

class NewRecipieFragment : Fragment(){

    companion object {
        private const val REQUEST_CODE = 101
    }

    private lateinit var newRecipieViewModel: NewRecipieViewModel
    private var _binding: FragmentNewRecipieBinding? = null

    private lateinit var newrecipieController: NewRecipieController

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val db = Firebase.firestore

    var btn_ingredient: String = "Add new ingredient"
    var btn_step: String = "Add new step"
    var btn_save: String = "Save"
    var prep_title: String = "Preparation"
    var tmp_bitmap: Bitmap? = null
    var img_url: String? = null
    var flag_photo = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newRecipieViewModel =
            ViewModelProvider(this).get(NewRecipieViewModel::class.java)

        _binding = FragmentNewRecipieBinding.inflate(inflater, container, false)

        val root: View = binding.root

        newrecipieController = NewRecipieController(this, btn_ingredient, prep_title, btn_step,
            btn_save, inflater)
        binding.ervChangeRecipie.setController(newrecipieController)

        newrecipieController.requestModelBuild()
        binding.ervChangeRecipie.addItemDecoration(DividerItemDecoration(requireActivity(),
        RecyclerView.VERTICAL))

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
            newrecipieController.addPhoto(tmp_bitmap)
//            photo_binding?.ivPhoto?.setImageBitmap(imageBitmap)
//            photo_binding?.ivPhoto?.visibility = View.VISIBLE
        }

    }

    fun getBitmap(): Bitmap?{
        return tmp_bitmap
    }

    fun saveWithPhoto(recipie: Recipie){
        val bitmap = newrecipieController.getBitmap()
//        val bitmap: Bitmap = (photo_binding?.ivPhoto?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
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
                uploadRecipie(downloadUri.toString(), recipie)
            }
    }

    fun SaveClick(recipie: Recipie){
        if(!flag_photo){
            uploadRecipie(recipie = recipie)
        } else {
            try {
               saveWithPhoto(recipie)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun uploadRecipie(imageUrl: String? = null, recipie: Recipie){
        if (imageUrl != null) {
            val images: ArrayList<String> = arrayListOf<String>()
            images.add(imageUrl)
            recipie.imageUrls = images
        }

//        Log.d("URL: ", img_url.toString())

        db.collection("recipies").add(recipie)/*.addOnSuccessListener {
            Toast.makeText(activity?.applicationContext, "Recipie saved", Toast.LENGTH_SHORT).show()
        }*/.addOnFailureListener { e ->
            Toast.makeText(activity?.applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
        }

    }

//    fun initFlagListener() {
//        db.collection("recipies").
//        addSnapshotListener { snapshots, error ->
//            if (error != null){
//                Toast.makeText(this.context, error.toString(), Toast.LENGTH_SHORT).show()
//                return@addSnapshotListener
//            }
//
//            for (dc in snapshots!!.documentChanges) {
//                when(dc.type) {
//                    com.google.firebase.firestore.DocumentChange.Type.ADDED -> newrecipieController.saveRecipie(/*dc.document.toObject<Recipie>()*/)
////                    com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> Toast.makeText(this.context, dc.document.data.toString(), Toast.LENGTH_SHORT).show()
////                    com.google.firebase.firestore.DocumentChange.Type.REMOVED -> Toast.makeText(this.context, dc.document.data.toString(), Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}