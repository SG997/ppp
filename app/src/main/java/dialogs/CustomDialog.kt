package dialogs

import adapters.AdapterBusinessSelectProducts
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.dialog_layout.view.*
import kotlinx.android.synthetic.main.item_collect_product_data.view.*

object CustomDialog {
    fun getAlertDialog(context: Context, title: String, content: String, confirmListener: DialogInterface.OnClickListener? = null, cancelListener: DialogInterface.OnClickListener? = null, declineButtonView : Boolean = true) : AlertDialog{
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.dialog_layout, null)

        view.title.text = title
        view.content.text = content

        var dialog = AlertDialog.Builder(context).setView(view).create()

        view.positive.setOnClickListener {
            confirmListener?.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
        }
        
        view.negative.setOnClickListener {
            cancelListener?.onClick(dialog, DialogInterface.BUTTON_NEGATIVE)
            if (cancelListener == null) { dialog.dismiss() }
        }

        view.negative.visibility = if (declineButtonView) View.VISIBLE else View.GONE

        return dialog
    }


    fun getAddProductDialog(context: Context, listener : OnConfirmProduct) : Dialog{
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_collect_product_data, null)

        var dialog = AlertDialog.Builder(context).setView(view).create()

        view.confirmProduct.visibility = View.VISIBLE

        view.importImage.setOnClickListener {
            imagePressLogic(context, view.importImage)
        }

        view.confirmProduct.setOnClickListener {
            var name = view.productName.text.toString()
            var explain = view.productExplain.text.toString()
            var amount = view.productPrice.text.toString()
            var imageUri = view.importImage.tag as Uri

            var product = AdapterBusinessSelectProducts.BusinessProduct(imageUri.toString(), name, explain, amount, imageUri)

            dialog.dismiss()
            listener.onProductReady(product)
        }
        return dialog
    }

    private fun imagePressLogic(context : Context, view : ImageView){
        (context as MainActivity).let {
            it.imageLoaded = object : MainActivity.OnImageLoaded{
                override fun onImageLoaded(uir: Uri) {
                    view.importImage.setImageURI(uir)
                    view.tag = uir
                }
            }

            it.openGallery()
        }
    }


    interface OnConfirmProduct{
        fun onProductReady(product : AdapterBusinessSelectProducts.BusinessProduct)
    }
}