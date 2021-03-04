package dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import com.example.myapplication.R
import kotlinx.android.synthetic.main.dialog_layout.view.*

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
    
}