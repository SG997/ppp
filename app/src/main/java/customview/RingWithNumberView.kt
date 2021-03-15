package customview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import kotlinx.android.synthetic.main.ring_view_layout.view.*

class RingWithNumberView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    init {
        inflate(context, R.layout.ring_view_layout, this)
    }

    fun setNumber(number : Int){
        numberInsideRing.text = number.toString()
    }
}