package fragments

import adapters.AdapterBusinessSelectProducts
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import base.BaseFragment
import base.UIStep
import bo.User
import com.example.myapplication.R
import com.squareup.picasso.Picasso
import data.Cache
import firebase.FireBaseImage
import kotlinx.android.synthetic.main.fragment_business_select_product.*

class FragmentBusinessSelectProducts : BaseFragment() {
    var isEditable : Boolean = false
    companion object{
        fun getInstance(uiStep : UIStep) : BaseFragment{
            var fragment = FragmentBusinessSelectProducts()
            fragment.uiStep = uiStep
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_business_select_product, container, false)

    override fun bind(data: Any) {
        var user : User?
        var products : ArrayList<AdapterBusinessSelectProducts.BusinessProduct>?

        isEditable = data as Boolean
        if (isEditable){
            user = Cache.currentUser

        } else {
            user = Cache.users[Cache.choosedBusiness]
        }

        user?.let{
            loadedAllProductsImagesUris(it)
            downloadAndLoadBannerImage(it)
            initText(it.name, it.explain)
        }

    }

    fun initText(name : String, explain : String){
        businessNameSP.text = name
        contentText.text = explain
    }

    fun configRecycler(user : User){
        businessProductsRecycler.layoutManager = LinearLayoutManager(context)
        businessProductsRecycler.setHasFixedSize(true)

        var adapter = AdapterBusinessSelectProducts(user.products)
        adapter.listener = object : AdapterBusinessSelectProducts.Clicks{
            override fun onClick(data : AdapterBusinessSelectProducts.BusinessProduct) {
                // TODO go to payment screen
                Cache.currenProduct = Cache.users[Cache.choosedBusiness].products.indexOf(data)
                notifyFinish(Any())

            }

        }
        businessProductsRecycler.adapter = adapter
    }

    private fun createFakeData() : ArrayList<AdapterBusinessSelectProducts.BusinessProduct> {
        var lst = ArrayList<AdapterBusinessSelectProducts.BusinessProduct>()
        for (i in 1..10) {
            lst.add(AdapterBusinessSelectProducts.BusinessProduct("~/AndroidStudioProjects/MyApplication2/app/src/main/res/drawable/upnettilogo_icon.jpg", "מפגש חשיבה", "מגוון מוחות שחושבים יחד איך לעשות לך כסף", "30"))
        }
        return lst
    }

    private fun downloadAndLoadBannerImage(user : User){
        FireBaseImage.getBannerImageUri(user.email, object : FireBaseImage.onImageLoaded{
            override fun onImageLoaded(uri: Uri, num: Int) {
                Picasso.with(context).load(uri).into(bannerBusinessSP)
            }
        })
    }

    private fun loadedAllProductsImagesUris(user : User){
        var hash = HashMap<Int, Uri>()
        FireBaseImage.getBusinessProducts(user.email, user.products.size, object : FireBaseImage.onImageLoaded{
            override fun onImageLoaded(uri: Uri, num: Int) {
                hash[num] = uri

                if (hash.keys.size == user.products.size){
                    hash.keys.forEach { user.products[it].uri = hash[it] }
                    configRecycler(user)
                }
            }

        })

    }

}