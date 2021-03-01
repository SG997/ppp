package base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.myapplication.MainActivity

abstract class BaseFragment : Fragment() {
    var uiStep: UIStep? = null

    fun notifyFinish(data : Any){
        println("notifyFinish is called")
        uiStep?.onFragmentEnded(data)
        (activity as MainActivity).searchAndViewScreen()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiStep?.let {
            bind(it.getData())
        }
    }
    abstract fun bind(data : Any)
}