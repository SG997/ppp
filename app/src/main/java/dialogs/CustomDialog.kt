package dialogs

import adapters.AdapterBusinessSelectProducts
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
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
        }

        view.negative.visibility = if (declineButtonView) View.VISIBLE else View.GONE

        return dialog
    }


    fun getAddProductDialog(context: Context, listener : OnConfirmProduct) : Dialog{
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_collect_product_data, null)

        var dialog = AlertDialog.Builder(context).setView(view).create()

        view.confirmProduct.setOnClickListener {
            var name = it.productName.toString()
            var explain = it.productExplain.toString()
            var amount = it.productPrice.toString()

            var product = AdapterBusinessSelectProducts.BusinessProduct("a", name, explain, amount)

            listener.onProductReady(product)
        }
        return dialog
    }


    interface OnConfirmProduct{
        fun onProductReady(product : AdapterBusinessSelectProducts.BusinessProduct)
    }
}