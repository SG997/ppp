package fragments

import adapters.AdapterBusinessSelectProducts
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.BaseFragment
import com.example.myapplication.R
import dialogs.CustomDialog
import kotlinx.android.synthetic.main.fragment_insert_and_deit_products.*

class FragmentInsertAndEditProducts : BaseFragment() {
    var productImagesList = ArrayList<Uri>()
    var productsDataCollected = ArrayList<AdapterBusinessSelectProducts.BusinessProduct>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_insert_and_deit_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        asd()
    }

    override fun bind(data: Any) {

    }

    fun asd() {
        pressToAddProduct.setOnClickListener {

        }
    }

    fun addProductLogic(){
        CustomDialog.getAddProductDialog(context!!, object : CustomDialog.OnConfirmProduct {
            override fun onProductReady(product: AdapterBusinessSelectProducts.BusinessProduct) {

            }

        })

    }

}