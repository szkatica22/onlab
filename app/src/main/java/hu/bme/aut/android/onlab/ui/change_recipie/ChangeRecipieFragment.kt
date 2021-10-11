package hu.bme.aut.android.onlab.ui.change_recipie

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.FragmentChangeRecipieBinding
import hu.bme.aut.android.onlab.ui.recipie.IngredientItemAdapter
import hu.bme.aut.android.onlab.ui.recipie.Item
import hu.bme.aut.android.onlab.ui.recipie.PreparationItemAdapter

class ChangeRecipieFragment : Fragment(){
    private lateinit var changeRecipieViewModel: ChangeRecipieViewModel
    private var _binding: FragmentChangeRecipieBinding? = null

    private lateinit var ingredientItemAdapter: IngredientItemAdapter
    private lateinit var preparationItemAdapter: PreparationItemAdapter

//    private lateinit var adds_btn: FloatingActionButton
//    private lateinit var rec_v: RecyclerView
    var ingredients_list = mutableListOf(Item("1 cup espresso"), Item("6 egg yolks"), Item("6 Tbsp rum"), Item("30 ladyfingers"))
    var preparation_list = mutableListOf(Item("step1"), Item("step2"), Item("step3"), Item("step4"))

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeRecipieViewModel =
            ViewModelProvider(this).get(ChangeRecipieViewModel::class.java)

        _binding = FragmentChangeRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Teszt gomb az elozo fragmentre valtashoz
        binding.imgBtnEdit.setOnClickListener{
            findNavController().navigate(R.id.action_nav_change_recipie_to_nav_recipie)
        }

        //Teszt hozzavalokra es elkeszitesi lepesekre

        ingredientItemAdapter = IngredientItemAdapter(ingredients_list)
        binding.rvRecipieIngredients.adapter = ingredientItemAdapter
        binding.rvRecipieIngredients.layoutManager = LinearLayoutManager(this.context)

        preparationItemAdapter = PreparationItemAdapter(preparation_list)
        binding.rvRecipiePreparation.adapter = preparationItemAdapter
        binding.rvRecipiePreparation.layoutManager = LinearLayoutManager(this.context)

        //Add Ingredient

        binding.rvRecipieIngredients.adapter = preparationItemAdapter
        binding.rvRecipieIngredients.layoutManager = LinearLayoutManager(this.context)

        //recyclerview id: rv_recipie_ingredients

        binding.addingBtnRecipieIngredients.setOnClickListener {

            val add_dialog = AlertDialog.Builder(this.context)

//            add_dialog.setView()

            add_dialog.setPositiveButton("Ok"){
                dialog,_ ->
//            val title = binding.etNewRecipieIngredient.text.toString()
//                ingredients_list.add(Item("Teszt"))
            }
            add_dialog.setNegativeButton("Cancel"){
                dialog,_ ->
            }
            add_dialog.create()
            add_dialog.show()



//            val title = binding.etNewRecipieIngredient //.text.toString()
//            if(etNewRecipieIngredient.isNotEmpty()){
////                val list_item = Item(etNewRecipieIngredient)
//                ingredientItemAdapter.addItem(Item(etNewRecipieIngredient))
//            }
        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}