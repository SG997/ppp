package customview

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.myapplication.R
import kotlinx.android.synthetic.main.expandable_layout.view.*


class ExpandableView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    var clickListener : Click? = null
    private var topView : View? = null
    private var bottomView : View? = null
    var isOpen : Boolean = false

    init {
        inflate(context, R.layout.expandable_layout, this)
        setExpandableLogic()
    }

    private fun setExpandableLogic() {

        this.setOnClickListener {

            bottomView?.visibility = if (isOpen) View.GONE else View.VISIBLE

            createAndStartChevronAnimation(isOpen)

            isOpen = !isOpen
        }
    }

    fun changeChevronVisibility(shouldView : Boolean){
        expandableChevron.visibility = if (shouldView) View.VISIBLE else View.GONE
    }

    private fun createAndStartChevronAnimation(isOpen : Boolean){
        var START_ANGLE = 0f
        var FINISH_ANGLE = -90f

        var anim: ObjectAnimator? = null
        anim?.duration = 350
        anim = ObjectAnimator.ofFloat(expandableChevron, "rotation", if (!isOpen) START_ANGLE else FINISH_ANGLE, if (isOpen) START_ANGLE else FINISH_ANGLE)
        anim?.start()
    }

    fun setTopView(topView : View){
        this.topView = topView
        addTopView.addView(topView)
        requestLayout()
    }

    fun setBottomView(bottomView : View){
        bottomView.visibility = View.GONE
        this.bottomView = bottomView
        mainLayoutExpandable.addView(bottomView)
    }

    fun setOnClicked(click: Click) {
        this.clickListener = clickListener
    }


    interface Click{
        fun onCLick()
    }
}