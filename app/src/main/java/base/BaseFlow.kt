package base

import android.app.Activity

open class BaseFlow(){
    var mActivity : Activity? = null
    var list = ArrayList<UIStep>()

    init {
        print("Hello 1234567890")
        list = ArrayList()
        //mActivity = activity
        getAllSteps()
    }

    open fun getAllSteps(){}

    fun addStep(step : UIStep){
        // Letting the Fragment having a pointer to his wrapper
        step.getFragmentInstance().uiStep = step
        list.add(step)
    }

    fun currentStep() : BaseFragment?{
        for (step in list){
            if (step.shouldPresent()){
                return step.getFragmentInstance()
            }
        }
        return null
    }

    fun currentUIStep() : UIStep?{
        for (step in list){
            if (step.shouldPresent()){
                return step
            }
        }
        return null
    }
}