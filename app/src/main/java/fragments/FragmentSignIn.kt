package fragments

import adapters.AdapterBusinessSelectProducts
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import base.BaseFragment
import bo.BankAccount
import bo.User
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import datamodel.ImageAndPath
import dialogs.CustomDialog
import firebase.FireBaseData
import kotlinx.android.synthetic.main.fragment_sign_in.*

class FragmentSignIn : BaseFragment() {
    companion object{
        fun getInstance() : BaseFragment = FragmentSignIn()
    }

    var fragments = ArrayList<BaseFragment>()
    var insertedData = CompleteRegistrationData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun bind(data: Any) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragmentsList()
        setUpViewPager()
        initListener()
    }

    fun initListener(){
        completeSignIn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            handleRegisterLogic()
        }
    }

    fun handleRegisterLogic(){
        try {
            register()
        } catch (e : Exception){
            CustomDialog.getAlertDialog(context!!, "אופס,", "חסר לך מידע, להשלמת התהליך אנה הזן את כלל השדות הדרוים", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss()}, declineButtonView = false).show()
        }
    }

    private fun register(){
        fun createUser(){
            FireBaseData.notify = getFireBaseDataStoringListenere()

            var user = User(insertedData.name!!,
                    insertedData.password,
                    insertedData.email,
                    insertedData.bankAccount!!,
                    insertedData.products!!,
                    insertedData.explain)

            var userImages = ImageAndPath(insertedData.banner!!, insertedData.bannerLogo!!, insertedData.productsImages!!)
            FireBaseData.createUser(context!!, insertedData.email, insertedData.password,
                    user, userImages)
            // FireBaseData.storeUser(it, finalUser, imagesList)
        }

        // Reinit the data
        insertedData = CompleteRegistrationData()

        // Collect all the data
        fragments.forEach { (it as PopulateData).setLocalData(insertedData) }

        context?.let { nonNulContext ->


        }

        createUser()
    }




    fun initFragmentsList(){
        fragments.add(FragmentInsertDetailes())
        fragments.add(FragmentInsertProducts())
    }

    fun setUpViewPager(){

        tabDots.setupWithViewPager(viewPager, true)

        viewPager.adapter = object : FragmentStatePagerAdapter(fragmentManager!!) {
            override fun getItem(position: Int): Fragment = fragments[position]

            override fun getCount(): Int = fragments.size
        }
    }

    var create = false
    var store = false
    private fun getFireBaseDataStoringListenere() : FireBaseData.DataStoring = object : FireBaseData.DataStoring{
        override fun onAction(actionCode: FireBaseData.Action, isSuccess: Boolean) {

            // Make sure one time notify is called
            when (actionCode){
                FireBaseData.Action.USER_CRETE -> {
                    create = true
                    if (isSuccess && store){
                        notifyFinish(Any())
                    }
                }

                FireBaseData.Action.USER_STORE -> {
                    store = true
                    if (isSuccess && create){
                        notifyFinish(Any())
                    }
                }
            }
        }

    }


    interface PopulateData {
        fun setLocalData(user : CompleteRegistrationData) : Boolean
    }

    data class CompleteRegistrationData(var name : String? = "",
                                        var password: String = "",
                                        var email: String = "",
                                        var bankAccount: BankAccount? = null,
                                        var products : ArrayList<AdapterBusinessSelectProducts.BusinessProduct>? = null,
                                        var productsImages : ArrayList<Uri>? = null,
                                        var explain : String = "",
                                        var banner : Uri? = null,
                                        var bannerLogo : Uri? = null)

}