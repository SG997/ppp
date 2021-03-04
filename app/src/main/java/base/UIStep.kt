package base

abstract class UIStep : Base() {
    var frag : BaseFragment? = null
    init {
    }

    fun getFragmentInstance() : BaseFragment{
        if (frag == null){
            frag = getFragment()
        }
        return frag!!
    }

    abstract fun onFragmentEnded(data : Any)
    abstract fun getData() : Any
    abstract fun onBackPressed()
}