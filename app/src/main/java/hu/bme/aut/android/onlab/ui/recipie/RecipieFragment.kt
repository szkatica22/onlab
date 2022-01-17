package hu.bme.aut.android.onlab.ui.recipie

import android.os.Bundle
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
import hu.bme.aut.android.onlab.databinding.FragmentRecipieBinding

class RecipieFragment: Fragment(){
    private lateinit var recipieViewModel: RecipieViewModel
    private var _binding: FragmentRecipieBinding? = null

    private val binding get() = _binding!!
    private var db = Firebase.firestore

    private lateinit var recipieController: RecipieController
    private var prep_title: String = "Preparation"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recipieViewModel =
            ViewModelProvider(this).get(RecipieViewModel::class.java)

        _binding = FragmentRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rec_name = this.arguments?.get("recipiename").toString()

        db.collection("recipies").whereEqualTo("name", rec_name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot.documents.isNotEmpty()){
                val tmp_data = snapshot.documents[0].data
                val tmp_rec = Recipie(rec_name, tmp_data?.get("favourite") as Boolean,
                    tmp_data?.get("flags") as List<String?>?, tmp_data?.get("imageUrls") as List<String?>?,
                    tmp_data?.get("time").toString(), tmp_data?.get("abundance").toString(),
                    tmp_data?.get("author").toString(), tmp_data?.get("ingredients") as List<String?>?,
                    tmp_data?.get("steps") as List<String?>?)

                recipieController = RecipieController(this.context, tmp_rec, prep_title, inflater)
                binding.ervRecipie.setController(recipieController)

                recipieController.requestModelBuild()
                binding.ervRecipie.addItemDecoration(DividerItemDecoration(requireActivity(),
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