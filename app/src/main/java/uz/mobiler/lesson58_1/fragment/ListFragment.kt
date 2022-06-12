package uz.mobiler.lesson58_1.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import uz.mobiler.lesson58_1.R
import uz.mobiler.lesson58_1.adapter.PassportAdapter
import uz.mobiler.lesson58_1.database.AppDatabase
import uz.mobiler.lesson58_1.database.entity.Passport
import uz.mobiler.lesson58_1.databinding.FragmentListBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ListFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    val appDatabase: AppDatabase by lazy {

        AppDatabase.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    private lateinit var binding: FragmentListBinding
    private lateinit var passportList: ArrayList<Passport>
    private lateinit var passportAdapter: PassportAdapter
    private var bol = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        passportList = ArrayList(appDatabase.passportDao().getPassports())
        binding = FragmentListBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            if (passportList.isNotEmpty()) {
                lottie.visibility = View.INVISIBLE
            } else {
                lottie.visibility = View.VISIBLE
            }
            passportAdapter = PassportAdapter(
                requireContext(),
                lottie,
                passportList,
                object : PassportAdapter.OnItemClickListener {
                    override fun onItemClick(passport: Passport, position: Int) {
                        val bundle = Bundle()
                        bundle.putInt("id", passport.id)
                        Navigation.findNavController(root).navigate(R.id.infoFragment, bundle)
                    }
                })
            if (bol) {
                binding.searchText.visibility = View.VISIBLE
                bol = true
            } else {
                binding.searchText.visibility = View.GONE
                binding.searchText.setText("")
                bol = false
            }
            searchText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    filter(p0.toString())
                }
            })
            rv.adapter = passportAdapter
        }
        return binding.root
    }

    private fun filter(text: String) {
        val filteredList = ArrayList<Passport>()
        for (passport in passportList) {
            if ((passport.lastName + " " + passport.firstName).lowercase()
                    .contains(text.lowercase()) || (passport.seriesLetter + " " + passport.seriesNumber).lowercase()
                    .contains(text.lowercase())
            ) {
                filteredList.add(passport)
            }
        }
        passportAdapter.filterList(filteredList)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            if (!bol) {
                binding.searchText.visibility = View.VISIBLE
                bol = true
            } else {
                binding.searchText.visibility = View.GONE
                binding.searchText.setText("")
                bol = false
            }
        } else if (item.itemId == android.R.id.home) Navigation.findNavController(binding.root)
            .popBackStack()
        return super.onOptionsItemSelected(item)
    }
}