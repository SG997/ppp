package base

abstract class Base {
    protected abstract fun getFragment() : BaseFragment?
    abstract fun shouldPresent() : Boolean

}