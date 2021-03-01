package fragments

import adapters.AdapterBusinessSelectProducts
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import base.BaseFragment
import base.UIStep
import bo.BankAccount
import bo.User
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import datamodel.ImageAndPath
import firebase.FireBaseData
import kotlinx.android.synthetic.main.fragment_sigh_in.*
import kotlinx.android.synthetic.main.item_collect_product_data.view.*


class FragmentSighIn : BaseFragment() {
    companion object{
        val DATA = "data"

        fun getInstance(uiStep : UIStep) : BaseFragment{
            var fragment = FragmentSighIn()
            fragment.uiStep = uiStep
            return fragment
        }
    }

    var viewList = ArrayList<View>()

    var productImagesList = ArrayList<Uri>()

    var create  = false
    var store = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sigh_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListener()
        viewList = createProductsViews(3)
    }

    override fun bind(data: Any) {

    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 100)
    }

    private fun collectData() : ArrayList<AdapterBusinessSelectProducts.BusinessProduct>{
        fun collectData(view: View, number : Int) : AdapterBusinessSelectProducts.BusinessProduct{
            var name = view.productName.text.toString()
            var description = view.productExplain.text.toString()
            var price = view.productPrice.text.toString()

            productImagesList.add(view.importImage.tag as Uri)

            return AdapterBusinessSelectProducts.BusinessProduct("a", name, description, price)
        }

        var productsDataCollected = ArrayList<AdapterBusinessSelectProducts.BusinessProduct>()

        for (i in 0 until viewList.size){
            productsDataCollected.add(collectData(viewList[i], i))
        }
        return productsDataCollected
    }

    private fun createProductsViews(amount : Int) : ArrayList<View>{
        var productsView = ArrayList<View>()
        for (i in 0 until amount){
            var layoutInflater = LayoutInflater.from(context)
            var product: View = layoutInflater.inflate(R.layout.item_collect_product_data, null)
            product.importImage.setOnClickListener {
              importImage(it.importImage)
            }
            productsView.add(product)
            productsDataLayout.addView(product)
        }
        return productsView
    }

    fun importImage(view : ImageButton){
        openGallery()
        (activity as MainActivity).imageLoaded = object : MainActivity.OnImageLoaded{
            override fun onImageLoaded(uir: Uri) {
                view.setImageURI(uir)
                view.tag = uir
            }
        }
    }

    fun storeImage(path : String, uir: Uri){
        val storageRef: StorageReference = FirebaseStorage.getInstance().getReference(path)
        storageRef.putFile(uir).addOnSuccessListener { Toast.makeText(context, "Image upload success", Toast.LENGTH_LONG).show() }
    }

    fun collectBankAccountData() : BankAccount = BankAccount(bankNumber.text.toString().toInt(), branchNumber.text.toString().toInt(), accountNumber.text.toString().toInt())

    fun setUpListener(){
        banner.setOnClickListener {
            importImage(banner)
        }

        bannerLogo.setOnClickListener {
            importImage(bannerLogo)
        }


        FirebaseAuth.getInstance().signOut()
        completeSignIn.setOnClickListener {

            // User properties
            var name = businessName.text.toString()
            var email = businessMail.text.toString()
            var password = password.text.toString()
            var businessExplain = businessExplain.text.toString()

            var finalUser = User(name, password, email, collectBankAccountData(), collectData(), businessExplain)

            // User images
            val bannerUri = banner.tag as Uri
            val iconUri = bannerLogo.tag as Uri

            context?.let {

                FireBaseData.notify = getFireBaseDataStoringListenere()
                FireBaseData.createUser(it, email, password, finalUser, ImageAndPath(bannerUri, iconUri, productImagesList))
               // FireBaseData.storeUser(it, finalUser, imagesList)
            }

        }
    }


    private fun getFireBaseDataStoringListenere() : FireBaseData.DataStoring = object : FireBaseData.DataStoring{
        override fun onAction(actionCode: FireBaseData.Action, isSuccess: Boolean) {

            // Make sure one time notify is called
            when (actionCode){
                FireBaseData.Action.USER_CRETE -> {
                    create = true
                    if (isSuccess && store){
                        notifyFinish(Any())
                    }
                }

                FireBaseData.Action.USER_STORE -> {
                    store = true
                    if (isSuccess && create){
                        notifyFinish(Any())
                    }
                }
            }
        }

    }
}