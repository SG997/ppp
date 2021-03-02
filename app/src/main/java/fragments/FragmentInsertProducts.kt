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
import base.BaseFragment
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_insert_new_products.*
import kotlinx.android.synthetic.main.item_collect_product_data.view.*

class FragmentInsertProducts : BaseFragment(), FragmentSignIn.PopulateData {
    var productImagesList = ArrayList<Uri>()
    var viewList = ArrayList<View>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_insert_new_products, container, false)
    }

    override fun bind(data: Any) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewList = createProductsViews(3)
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

        productImagesList = ArrayList<Uri>()

        var productsDataCollected = ArrayList<AdapterBusinessSelectProducts.BusinessProduct>()

        for (i in 0 until viewList.size){
            productsDataCollected.add(collectData(viewList[i], i))
        }
        return productsDataCollected
    }


    override fun setLocalData(user : FragmentSignIn.CompleteRegistrationData): Boolean {
        user.products = collectData()
        user.productsImages = productImagesList
        return true
    }
}