package com.example.android.ecommerceadmin.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.android.ecommerceadmin.R
import com.example.android.ecommerceadmin.adapter.CategoryAdapter
import com.example.android.ecommerceadmin.databinding.FragmentCategoryBinding
import com.example.android.ecommerceadmin.databinding.FragmentSliderBinding
import com.example.android.ecommerceadmin.models.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class CategoryFragment : Fragment() {

    private lateinit var binding : FragmentCategoryBinding
    private var imageUrl : Uri? = null
    private  lateinit var dialog: Dialog

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if ( it.resultCode == Activity.RESULT_OK) {
            imageUrl = it.data!!.data
            binding.imageViewCategory.setImageURI(imageUrl)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        getData()

        binding.apply {
            imageViewCategory.setOnClickListener{
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
            }
            uploadCategoryBtn.setOnClickListener {
                validateData(binding.categoryName.text.toString())
            }
        }

        return binding.root
    }

    private fun getData() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRecyclerView.adapter = CategoryAdapter(requireContext(),list )
            }.addOnFailureListener{

            }
    }

    private fun validateData(categoryName: String) {

        if ( categoryName.isEmpty() && imageUrl == null ){
            Toast.makeText(requireContext(),"Provide All Details",Toast.LENGTH_SHORT).show()
        }
        else if ( categoryName.isEmpty()){
            Toast.makeText(requireContext(),"Provide Category Name",Toast.LENGTH_SHORT).show()
        }else if ( imageUrl == null ){
            Toast.makeText(requireContext(),"Set Image",Toast.LENGTH_SHORT).show()
        }else{
            uploadImage(categoryName)
        }
    }
    private fun uploadImage(CategoryName : String) {
        dialog.show()

        val fileName = UUID.randomUUID().toString()+".jpg"
        val refstorage = FirebaseStorage.getInstance().reference.child("category/$fileName")
        refstorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData (CategoryName, image.toString())
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong with storage", Toast.LENGTH_SHORT).show()

            }
    }
    private fun storeData(categoryName: String,url : String) {
        val db = Firebase.firestore

        val data = hashMapOf<String,Any>(
            "cat" to categoryName,
            "img" to url
        )
        db.collection("categories").add(data)
            .addOnSuccessListener {
                dialog.dismiss()
                binding.imageViewCategory.setImageDrawable(resources.getDrawable(R.drawable.image_preview))
                binding.categoryName.text = null
                getData()
                Toast.makeText(requireContext(),"Category Added",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
            }
    }


}