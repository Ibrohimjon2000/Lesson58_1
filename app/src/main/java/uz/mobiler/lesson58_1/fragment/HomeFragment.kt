package uz.mobiler.lesson58_1.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import uz.mobiler.lesson58_1.R
import uz.mobiler.lesson58_1.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        binding.apply {
            add.setOnClickListener {
                Navigation.findNavController(root).navigate(R.id.addFragment)
            }
            list.setOnClickListener {
                Navigation.findNavController(root).navigate(R.id.listFragment)
            }
        }
        return binding.root
    }
}