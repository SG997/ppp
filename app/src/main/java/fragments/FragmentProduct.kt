package fragments

import adapters.AdapterBusinessSelectProducts
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.BaseFragment
import com.example.myapplication.R
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import data.Cache
import dialogs.CustomDialog
import firebase.FireBaseData
import firebase.FireBaseImage
import kotlinx.android.synthetic.main.fragment_product.*

class FragmentProduct : BaseFragment(){
    companion object{
        fun getInstance() : BaseFragment{
            return FragmentProduct()
        }
    }

    var product : AdapterBusinessSelectProducts.BusinessProduct? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var storageRef = FirebaseStorage.getInstance().reference

        Picasso.with(context).load(Cache.currentUser?.bannerUrl).into(productImage)

        product = Cache.users[Cache.choosedBusiness].products!![Cache.currenProduct.toString()]

        setTexts()
    }
    fun setStarsBalance(stars : String){
        remainStars.text = context?.getText(R.string.stars_text)?.replace(Regex("xxx"), stars)
    }

    private fun setTexts(){
        product?.let {
            productDetail.text = it.detailes
            starAmount.text = it.amount
            setStarsBalance(Cache.currentUser?.stars.toString())
        }

        continuePaying.setOnClickListener {

            context?.let{
                createPayLogicAndDialog(it)
            }

        }
    }

    override fun bind(data: Any) {
    }

    fun createPayDialog(context: Context){
        val title = context.getString(R.string.alert_title)
        val content = context.getString(R.string.pay_content_alert).replace("$$$", product!!.amount)
        CustomDialog.getAlertDialog(context, title, content,
                DialogInterface.OnClickListener { dialog, which ->
                    var balance : Int? = Cache.currentUser?.stars?.minus(product?.amount?.toInt()!!)

                    FireBaseData.updateStars(context, Cache.currentUser!!.email, balance!!, object : FireBaseData.DataStoring{
                        override fun onAction(actionCode: FireBaseData.Action, isSuccess: Boolean) {
                            setStarsBalance(balance.toString())
                        }

                    })
                    dialog.dismiss()
                },
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                }
        ).show()
    }

    fun createNoStarsBalance(context: Context){
        val title = context.getString(R.string.alert_title)
        val content = context.getString(R.string.purchase_stars)
        CustomDialog.getAlertDialog(context, title, content,
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    notifyFinish(Action.GO_TO_STAR_PURCHASE)
                },
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                }
        ).show()
    }

    private fun createPayLogicAndDialog(context: Context){
        if (Cache.currentUser!!.stars >= product!!.amount.toInt()){
            createPayDialog(context)
        } else{
            createNoStarsBalance(context)
        }
    }

    enum class Action{
        GO_TO_STAR_PURCHASE,
        GO_TO_PAYMENT
    }
}