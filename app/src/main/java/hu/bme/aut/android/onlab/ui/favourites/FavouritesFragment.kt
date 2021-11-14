package hu.bme.aut.android.onlab.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.FragmentFavouritesBinding
import hu.bme.aut.android.onlab.ui.flag.RecipieItem

class FavouritesFragment : Fragment() {

    private lateinit var favouritesViewModel: FavouritesViewModel
    private var _binding: FragmentFavouritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var favouriteItemAdapter: FavouriteItemAdapter
    var favourite_list = mutableListOf(
        FavouriteItem("Favourite1"), FavouriteItem("Favourite2"),
        FavouriteItem("Favourite3"), FavouriteItem("Favourite2"),
        FavouriteItem("Favourite5"), FavouriteItem("Favourite6"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favouritesViewModel =
            ViewModelProvider(this).get(FavouritesViewModel::class.java)

        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textFavourites
        favouritesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        favouriteItemAdapter = FavouriteItemAdapter(favourite_list)
        binding.rvFavourites.adapter = favouriteItemAdapter
        binding.rvFavourites.layoutManager = LinearLayoutManager(this.context)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}