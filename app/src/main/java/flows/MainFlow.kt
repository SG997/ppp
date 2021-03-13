package flows

import base.BaseFlow
import base.BaseFragment
import base.InnerFlowStep
import base.UIStep
import bo.User
import com.google.firebase.auth.FirebaseAuth
import data.Cache
import datamodel.Bank
import fragments.*

class MainFlow : BaseFlow() {

    var selectBusinessPresent = true
    var profilePresent = false
    var c = false
    var productsPresent = false
    var signInPresent = false
    var logInPresent = false
    var productScreenPresent = false
    var purchasePresent = false
    var editProductsPresent = false


    var isBusinessProductsEditable = false



    init {
        if (FirebaseAuth.getInstance().currentUser == null){
            logInPresent = true
            selectBusinessPresent = false

        }

     /*   FireBaseData.observeToUserDataChange(object : FireBaseData.DataStoring{
            override fun onAction(actionCode: FireBaseData.Action, isSuccess: Boolean) {

            }

        })*/
    }

    var user : User? = null
    override fun getAllSteps() {
        addStep(object : UIStep(){

            override fun getFragment(): BaseFragment = FragmentSelectBusiness.getInstance(this)

            override fun shouldPresent(): Boolean = selectBusinessPresent

            override fun onFragmentEnded(data : Any) {
                when(data as FragmentSelectBusiness.SELECT_BUSINESS){
                    FragmentSelectBusiness.SELECT_BUSINESS.PROFILE -> {
                        profilePresent = true
                    }

                    FragmentSelectBusiness.SELECT_BUSINESS.BUSINESS -> {
                        productsPresent = true
                    }
                }

                selectBusinessPresent = false
            }

            override fun getData(): Any = Cache.users

            override fun onBackPressed() {

            }

        })

        addStep(object : UIStep(){
            var model = FragmentProfile.ProfileDataModel(5, "נתי", Bank(12, 455, 609865),"3452")
            override fun getFragment(): BaseFragment = FragmentProfile.getInstance(this, model)

            override fun shouldPresent(): Boolean = profilePresent

            override fun onFragmentEnded(data : Any) {
                profilePresent = false

                when(data as FragmentProfile.Action){
                    FragmentProfile.Action.STARS -> {
                        purchasePresent = true
                    }

                    FragmentProfile.Action.HISTORY -> {

                    }

                    FragmentProfile.Action.EDIT_PRODUCTS -> {
                        productsPresent = true
                        isBusinessProductsEditable = true

                    }

                    FragmentProfile.Action.LOG_OUT -> {
                        logInPresent = true
                    }
                }
            }

            override fun getData(): Any = Any()

            override fun onBackPressed() {
                selectBusinessPresent = true
                profilePresent = false
            }

        })

        addStep(object : UIStep(){

            override fun getFragment(): BaseFragment = FirstFragment.getInstance(this)

            override fun shouldPresent(): Boolean = false

            override fun onFragmentEnded(data : Any) {

            }

            override fun getData(): Any = Any()

            override fun onBackPressed() {

            }
        })

        addStep(object : UIStep(){

            override fun getFragment(): BaseFragment = FragmentBusinessSelectProducts.getInstance(this)

            override fun shouldPresent(): Boolean = productsPresent

            override fun onFragmentEnded(data : Any) {
                productsPresent = false
                productScreenPresent = true
            }

            override fun getData(): Any = isBusinessProductsEditable

            override fun onBackPressed() {
                productsPresent = false
                selectBusinessPresent = true
                isBusinessProductsEditable = false
            }
        })

        addStep(object : UIStep(){
            override fun getFragment(): BaseFragment = FragmentSignIn.getInstance()

            override fun shouldPresent(): Boolean = signInPresent

            override fun onFragmentEnded(data : Any) {
                signInPresent = false
                selectBusinessPresent = true
            }

            override fun getData(): Any = Any()

            override fun onBackPressed() {

            }
        })


        addStep(object : UIStep(){
            override fun getFragment(): BaseFragment = FragmentLogIn()

            override fun shouldPresent(): Boolean = logInPresent

            override fun onFragmentEnded(data : Any) {
                logInPresent = false
                if ((data as FragmentLogIn.LogInData).goToSighIn){
                    signInPresent = true
                } else {
                    selectBusinessPresent = true
                }
            }

            override fun getData(): Any = Any()

            override fun onBackPressed() {

            }
        })

        addStep(object : UIStep(){
            override fun getFragment(): BaseFragment = FragmentProduct.getInstance()

            override fun shouldPresent(): Boolean = productScreenPresent

            override fun onFragmentEnded(data : Any) {
                productScreenPresent = false

                when(data as FragmentProduct.Action){
                    FragmentProduct.Action.GO_TO_STAR_PURCHASE -> {
                        purchasePresent = true
                    }
                }
            }

            override fun getData(): Any = Any()

            override fun onBackPressed() {
                productScreenPresent = false
                productsPresent = true
            }
        })

        addStep(object : UIStep(){
            override fun getFragment(): BaseFragment = FragmentPurchase()

            override fun shouldPresent(): Boolean = purchasePresent

            override fun onFragmentEnded(data: Any) {

            }

            override fun getData(): Any = Any()

            override fun onBackPressed() {
                purchasePresent = false
                productScreenPresent = true
            }

        })

        addStep(object : UIStep(){
            override fun onFragmentEnded(data: Any) {

            }

            override fun getData(): Any = Any()

            override fun onBackPressed() {

            }

            override fun getFragment(): BaseFragment? = FragmentInsertAndEditProducts()

            override fun shouldPresent(): Boolean = editProductsPresent

        })
    }
}