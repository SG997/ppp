package base

abstract class InnerFlowStep : Base() {

    override fun getFragment(): BaseFragment? = getFlow().currentStep()

    abstract fun getFlow() : BaseFlow
}