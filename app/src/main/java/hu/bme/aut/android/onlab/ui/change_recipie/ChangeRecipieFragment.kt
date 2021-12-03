package hu.bme.aut.android.onlab.ui.change_recipie

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FragmentChangeRecipieBinding

class ChangeRecipieFragment : Fragment(){
    private lateinit var changeRecipieViewModel: ChangeRecipieViewModel
    private var _binding: FragmentChangeRecipieBinding? = null

    private lateinit var changerecipieController: ChangeRecipieController

    var btn_ingredient: String = "Add new ingredient"
    var btn_step: String = "Add new step"
    var btn_delete: String = "Delete recipie"
    var btn_save: String = "Save"
    var prep_title: String = "Preparation"

    private val binding get() = _binding!!
    private val rec_name = this.arguments?.get("recipiename").toString()
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeRecipieViewModel =
            ViewModelProvider(this).get(ChangeRecipieViewModel::class.java)

        _binding = FragmentChangeRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db.collection("recipies").whereEqualTo("name", rec_name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot.documents.isNotEmpty()){
                Log.d("CHANGE_REC_FRAGMENT: ", rec_name)
                val tmp_data = snapshot.documents[0].data
                val tmp_rec = Recipie(rec_name, tmp_data?.get("favourite") as Boolean,
                    tmp_data?.get("flags") as List<String?>?, tmp_data?.get("imageUrls") as List<String?>?,
                    tmp_data?.get("time").toString(), tmp_data?.get("abundance").toString(),
                    tmp_data?.get("author").toString(), tmp_data?.get("ingredients") as List<String?>?,
                    tmp_data?.get("steps") as List<String?>?)

                changerecipieController = ChangeRecipieController(tmp_rec, btn_ingredient, prep_title,
                    btn_step, btn_delete, btn_save, inflater)
                binding.ervChangeRecipie.setController(changerecipieController)
                changerecipieController.requestModelBuild()
                binding.ervChangeRecipie.addItemDecoration(DividerItemDecoration(requireActivity(),
                    RecyclerView.VERTICAL))
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}