package base

abstract class UIStep{
    var frag : BaseFragment? = null
    init {
    }

    fun getFragmentInstance() : BaseFragment{
        if (frag == null){
            frag = getFragment()
        }
        return frag!!
    }
    protected abstract fun getFragment() : BaseFragment
    abstract fun shouldPresent() : Boolean
    abstract fun onFragmentEnded(data : Any)
    abstract fun getData() : Any
    abstract fun onBackPressed()
}