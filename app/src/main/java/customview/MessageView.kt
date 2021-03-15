package customview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import kotlinx.android.synthetic.main.message_view_layout.view.*

class MessageView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    init {
        inflate(context, R.layout.message_view_layout, this)
    }

    fun setNumber(number : Int){
        ringWithNumber.setNumber(number)
    }
}