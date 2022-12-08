package com.example.android.ecommerceadmin.fragments

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.android.ecommerceadmin.R
import com.example.android.ecommerceadmin.databinding.FragmentProductBinding
import com.example.android.ecommerceadmin.databinding.FragmentSliderBinding

class ProductFragment : Fragment() {

    private lateinit var binding : FragmentProductBinding
    private var imageUrl : Uri? = null
    private  lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(layoutInflater)
        binding.floatingActionButton.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_productFragment_to_addProductFragment)

        }
        return binding.root
    }

}