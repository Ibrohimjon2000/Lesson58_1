package uz.mobiler.lesson58_1.fragment

import android.Manifest
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import uz.mobiler.lesson58_1.BuildConfig
import uz.mobiler.lesson58_1.R
import uz.mobiler.lesson58_1.adapter.GenderAdapter
import uz.mobiler.lesson58_1.adapter.RegionAdapter
import uz.mobiler.lesson58_1.database.AppDatabase
import uz.mobiler.lesson58_1.database.entity.Gender
import uz.mobiler.lesson58_1.database.entity.Passport
import uz.mobiler.lesson58_1.database.entity.Region
import uz.mobiler.lesson58_1.databinding.FragmentAddBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddFragment : Fragment() {
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

    private lateinit var binding: FragmentAddBinding
    private lateinit var regionList: ArrayList<Region>
    private lateinit var genderList: ArrayList<Gender>
    private lateinit var passportList: ArrayList<Passport>
    private lateinit var regionAdapter: RegionAdapter
    private lateinit var genderAdapter: GenderAdapter
    private var currentPhotoPath: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val list = ArrayList<String>()
        list.add("AA")
        list.add("AB")
        list.add("AC")
        regionList = ArrayList(appDatabase.regionDao().getRegions())
        genderList = ArrayList(appDatabase.genderDao().getGenders())
        passportList = ArrayList(appDatabase.passportDao().getPassports())
        binding = FragmentAddBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            if (regionList.isEmpty()) {
                appDatabase.regionDao().addRegion(Region(name = "Toshkent"))
                appDatabase.regionDao().addRegion(Region(name = "Farg'ona"))
                appDatabase.regionDao().addRegion(Region(name = "Andijon"))
                appDatabase.regionDao().addRegion(Region(name = "Namangan"))
                appDatabase.regionDao().addRegion(Region(name = "Sirdaryo"))
                appDatabase.regionDao().addRegion(Region(name = "Jizzax"))
                appDatabase.regionDao().addRegion(Region(name = "Samarqand"))
                appDatabase.regionDao().addRegion(Region(name = "Qashqadaryo"))
                appDatabase.regionDao().addRegion(Region(name = "Buxoro"))
                appDatabase.regionDao().addRegion(Region(name = "Navoiy"))
                appDatabase.regionDao().addRegion(Region(name = "Xorazm"))
                appDatabase.regionDao().addRegion(Region(name = "Surxandaryo"))
                appDatabase.regionDao().addRegion(Region(name = "Qoraqalpoqiston"))
                regionList = ArrayList(appDatabase.regionDao().getRegions())
            }
            if (genderList.isEmpty()) {
                appDatabase.genderDao().addGender(Gender(name = "Male"))
                appDatabase.genderDao().addGender(Gender(name = "Female"))
                genderList = ArrayList(appDatabase.genderDao().getGenders())
            }
            regionAdapter = RegionAdapter(requireContext(), regionList)
            region.adapter = regionAdapter

            genderAdapter = GenderAdapter(requireContext(), genderList)
            gender.adapter = genderAdapter

            image.setOnClickListener {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    )
                )
            }

            save.setOnClickListener {
                val lName = lastName.text.toString()
                val fName = firstName.text.toString()
                val mName = middleName.text.toString()
                val region = regionList[region.selectedItemPosition]
                val city = city.text.toString()
                val address = address.text.toString()
                val date = date.text.toString()
                val lifetime = lifetime.text.toString()
                val gender = genderList[gender.selectedItemPosition]
                var seriesNumber = Random.nextInt(9000000) + 1000000
                if (passportList.isNotEmpty()) {
                    for (i in passportList.indices) {
                        if (passportList[i].seriesNumber == seriesNumber) {
                            seriesNumber = Random.nextInt(9000000) + 1000000
                        }
                    }
                }
                val seriesLetterPosition = Random.nextInt(3)
                if (lName.isNotEmpty()
                    && fName.isNotEmpty()
                    && mName.isNotEmpty()
                    && city.isNotEmpty()
                    && address.isNotEmpty()
                    && date.isNotEmpty()
                    && lifetime.isNotEmpty()
                    && currentPhotoPath.isNotEmpty()
                ) {
                    AlertDialog.Builder(requireContext()).setCancelable(false)
                        .setMessage(
                            "Ma’lumotlariningiz to’g’ri ekanligiga \n" +
                                    "ishonchingiz komilmi?"
                        )
                        .setPositiveButton("Ha") { dialog, _ ->
                            val passport = Passport(
                                firstName = fName,
                                lastName = lName,
                                middleName = mName,
                                regionId = region.id,
                                city = city,
                                address = address,
                                date = date,
                                lifeTime = lifetime,
                                genderId = gender.id,
                                imagePath = currentPhotoPath,
                                seriesLetter = list[seriesLetterPosition],
                                seriesNumber = seriesNumber
                            )
                            appDatabase.passportDao().addPassport(passport)
                            Navigation.findNavController(root).popBackStack()
                            dialog.dismiss()
                        }
                        .setNegativeButton("Yo’q") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                } else Toast.makeText(
                    requireContext(),
                    "Ma'lumotlar to'liq kiritilmagan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            Navigation.findNavController(binding.root)
                .popBackStack()
        return super.onOptionsItemSelected(item)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map[Manifest.permission.CAMERA] == true && map[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                val customDialog = layoutInflater.inflate(R.layout.custom_dialog, null)
                val mBuilder = AlertDialog.Builder(requireContext()).setView(customDialog)
                mBuilder.setCancelable(false)
                val mAlertDialog = mBuilder.show()
                mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                customDialog.findViewById<TextView>(R.id.camera).setOnClickListener {
                    takePhotoFromCameraNewMethod()
                    mAlertDialog.dismiss()
                }
                customDialog.findViewById<TextView>(R.id.gallery).setOnClickListener {
                    takePhotoFromGalleryNewMethod()
                    mAlertDialog.dismiss()
                }
            } else {

            }
        }

    private fun takePhotoFromGalleryNewMethod() {
        takePhotoResult.launch("image/*")
    }

    private val takePhotoResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri == null) return@registerForActivityResult
            binding.image.setImageURI(uri)
            val openInputStream = activity?.contentResolver?.openInputStream(uri)
            val file = File(activity?.filesDir, "${System.currentTimeMillis()}.jpg")
            val fileOutputStream = FileOutputStream(file)
            currentPhotoPath = file.absolutePath.toString()
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
        }

    private fun takePhotoFromCameraNewMethod() {
        val file = try {
            createImageFile()
        } catch (e: Exception) {
            null
        }
        val uriForFile = file?.let {
            FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID, it)
        }
        takePhotoFromCameraResultLauncher.launch(uriForFile)
    }

    private val takePhotoFromCameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                binding.image.setImageURI(Uri.fromFile(File(currentPhotoPath)))
            }
        }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val externalFilesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("${System.currentTimeMillis()}", ".jpg", externalFilesDir)
            .apply {
                currentPhotoPath = absolutePath
            }
    }
}