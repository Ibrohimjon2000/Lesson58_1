package uz.mobiler.lesson58_1.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import uz.mobiler.lesson58_1.R
import uz.mobiler.lesson58_1.database.AppDatabase
import uz.mobiler.lesson58_1.databinding.FragmentInfoBinding
import java.io.File

private const val ARG_PARAM1 = "id"
private const val ARG_PARAM2 = "param2"

class InfoFragment : Fragment() {
    private var param1: Int = 0
    private var param2: String? = null
    val appDatabase: AppDatabase by lazy {

        AppDatabase.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            val passport = appDatabase.passportDao().getPassportById(param1)
            val r = appDatabase.regionDao().getRegionById(passport.regionId)
            val g = appDatabase.genderDao().getGenderById(passport.genderId)
            actionBar?.title = passport.firstName + " " + passport.lastName
            image.setImageURI(Uri.fromFile(File(passport.imagePath)))
            fullName.text = passport.lastName + " " + passport.firstName + " " + passport.middleName
            city.text = passport.city
            address.text = passport.address
            address.text = passport.address
            date.text = passport.date
            date.text = passport.date
            lifeTime.text = passport.lifeTime
            region.text = r.name
            gender.text = g.name
            series.text = passport.seriesLetter + " " + passport.seriesNumber
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) Navigation.findNavController(binding.root)
            .popBackStack()
        return super.onOptionsItemSelected(item)
    }
}