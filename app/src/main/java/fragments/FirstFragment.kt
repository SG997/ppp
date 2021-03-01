package fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.BaseFragment
import base.UIStep
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import customview.ExpandableView
import kotlinx.android.synthetic.main.first_fragment.*
import java.net.URI


class FirstFragment() : BaseFragment(){

    companion object{
        fun getInstance(uiStep: UIStep) : BaseFragment {
            var frg = FirstFragment()
            frg.uiStep = uiStep
            return frg
        }
    }
    var v : View? = null

    var b : View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.item_text, null)
        b = inflater.inflate(R.layout.expandable_item_bullet, null)
        return inflater.inflate(R.layout.first_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        asd123()
        //asd()

        view.findViewById<ExpandableView>(R.id.asdasdasd).setTopView(v!!)
        view.findViewById<ExpandableView>(R.id.asdasdasd).setBottomView(b!!)
        //view.asdasdasd.topViewExpanadable.addView(v!!)
    }

    private fun asd123(){
        val url = "Images/users"
        val storageRef: StorageReference = FirebaseStorage.getInstance().getReference(url)


        storageRef.downloadUrl.addOnSuccessListener {
            Picasso.with(context).load(it).into(xcvhgfdxcvbn)
        }

    }

    override fun bind(data: Any) {

    }

    fun asd(){
        xcvhgfdxcvbn.setOnClickListener {
            openGallery()
            (activity as MainActivity).imageLoaded = object : MainActivity.OnImageLoaded{
                override fun onImageLoaded(uir: Uri) {
                    xcvhgfdxcvbn.setImageURI(uir)
                    storeImage(uir)
                }

            }
        }
    }

    fun storeImage(uir: Uri){
        val storageRef: StorageReference = FirebaseStorage.getInstance().getReference("Images/users")
        storageRef.putFile(uir)


    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 100)
    }
}