package fragments

import adapters.AdapterBusinessSelectProducts
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import base.BaseFragment
import base.UIStep
import bo.User
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.squareup.picasso.Picasso
import data.Cache
import data.Utils
import dialogs.CustomDialog
import firebase.FireBaseData
import firebase.FireBaseImage
import kotlinx.android.synthetic.main.fragment_business_select_product.*

class FragmentBusinessSelectProducts : BaseFragment() {
    var user: User? = null

    var isEditable : Boolean = false

    var bannerImageUri : Uri? = null
    var adapter : AdapterBusinessSelectProducts? = null

    //var editProducts = ArrayList<AdapterBusinessSelectProducts.BusinessProduct>()
    var editProducts = HashMap<String, AdapterBusinessSelectProducts.BusinessProduct>()

    companion object{
        fun getInstance(uiStep : UIStep) : BaseFragment{
            var fragment = FragmentBusinessSelectProducts()
            fragment.uiStep = uiStep
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_business_select_product, container, false)

    override fun bind(data: Any) {

        isEditable = data as Boolean
        if (isEditable) {
            saveChangedData.visibility = View.VISIBLE
            initListener()
            user = Cache.currentUser

        } else {
            user = Cache.users[Cache.choosedBusiness]
        }

        user?.let {
            loadedAllProductsImagesUris(it)
            downloadAndLoadBannerImage(it)
            initText(it.name, it.explain)
        }

    }

    fun initListener(){
        saveChangedData.setOnClickListener {
            saveLogic()
        }

        bannerBusinessSP.setOnClickListener {
            changeBannerPicture(bannerBusinessSP)
        }

        addProduct.setOnClickListener {
            askForAddProduct()
        }
    }

    private fun loadedAllProductsImagesUris(user : User){
        var hash = HashMap<Int, Uri>()
/*        FireBaseImage.getBusinessProducts(user.email, user.products!!.size, object : FireBaseImage.onImageLoaded{
            override fun onImageLoaded(uri: Uri, num: Int) {
                hash[num] = uri

                if (hash.keys.size == user.products!!.size){
                    hash.keys.forEach { user.products!![it].uri = hash[it] }
                    configRecycler(user)
                }
            }

        })*/

        configRecycler(user)


    }

    fun askForAddProduct(){
        addOrEditProduct()
    }

    fun changeBannerPicture(view : ImageView){
        (context as MainActivity).let {
            it.imageLoaded = object : MainActivity.OnImageLoaded{
                override fun onImageLoaded(uir : Uri){
                    Picasso.with(context).load(uir).into(view)
                    bannerImageUri = uir
                }

            }

            it.openGallery()
        }
    }

    private fun saveLogic(){
        fun saveBanner(){
            if ((Cache.currentUser?.email != null) && (bannerImageUri!=null)){
                FireBaseData.storeBannerViaMail(Cache.currentUser!!.email, bannerImageUri!!)
            }
        }

        saveBanner()
        //FireBaseData.storeUserData(context!!, Cache.currentUser!!, )

        for (i : String in editProducts.keys){
            editProducts[i]?.let {
                Cache.currentUser?.email?.let { mail ->
                    FireBaseData.storeProductToUserViaMail(mail, it, i)
                }
            }
        }
    }

    fun initText(name : String, explain : String){
        businessNameSP.text = name
        contentText.text = explain
    }

    fun configRecycler(user : User){
        businessProductsRecycler.layoutManager = LinearLayoutManager(context)
        businessProductsRecycler.setHasFixedSize(true)

        adapter = AdapterBusinessSelectProducts(ArrayList(user.products?.toList()), isEditable)
        adapter?.listener = object : AdapterBusinessSelectProducts.Clicks{
            override fun onClick(data : Pair<String, AdapterBusinessSelectProducts.BusinessProduct>, position : Int) {
                // TODO go to payment screen

                if (isEditable){
                    addOrEditProduct(data, position)
                } else{
                    Cache.currenProduct = position
                    Cache.currenProductKey = data.first
                    notifyFinish(Any())
                }
            }

            override fun onDelete(key: String) {
                Cache.currentUser?.let {
                    FireBaseData.deleteProductToUserViaMail(it.email, key)
                }
            }

        }
        businessProductsRecycler.adapter = adapter
    }

    fun addOrEditProduct(data : Pair<String, AdapterBusinessSelectProducts.BusinessProduct>? = null, position : Int? = null){

        fun getEditProductListener(position: Int? = null) : CustomDialog.OnConfirmProduct = object : CustomDialog.OnConfirmProduct {
            override fun onProductReady(product: AdapterBusinessSelectProducts.BusinessProduct) {
                when (position){
                    null -> {
                        user?.let {
                            var combinedKeys = ArrayList(editProducts.keys)
                            Cache.currentUser?.products?.keys?.let {
                                combinedKeys.addAll(it)
                            }
                            var uniqueKey = Utils.generateKey(combinedKeys)

                            editProducts[uniqueKey] = product
                            adapter?.addProduct(Pair(position.toString(), product))
                        }
                    }
                    else -> {
                        adapter?.setProductByPosition(product, position)
                        editProducts[data!!.first] = product
                    }
                }

            }
        }

        CustomDialog.getAddProductDialog(context!!, getEditProductListener(position)).show()
    }

    private fun createFakeData() : ArrayList<AdapterBusinessSelectProducts.BusinessProduct> {
        var lst = ArrayList<AdapterBusinessSelectProducts.BusinessProduct>()
        for (i in 1..10) {
            lst.add(AdapterBusinessSelectProducts.BusinessProduct("~/AndroidStudioProjects/MyApplication2/app/src/main/res/drawable/upnettilogo_icon.jpg", "מפגש חשיבה", "מגוון מוחות שחושבים יחד איך לעשות לך כסף", "30"))
        }
        return lst
    }

    private fun downloadAndLoadBannerImage(user : User){
        Picasso.with(context).load(user.bannerUrl).into(bannerBusinessSP)
    }
}