package fragments

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.BaseFragment
import base.UIStep
import com.example.myapplication.R
import datamodel.Bank
import kotlinx.android.synthetic.main.bank_account_card.view.*
import kotlinx.android.synthetic.main.credit_card.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_two_lines.view.*
import java.io.Serializable

class FragmentProfile : BaseFragment() {
    var data : ProfileDataModel? = null

    companion object{
        val DATA = "data"
        fun getInstance(uiStep : UIStep, data : ProfileDataModel) : BaseFragment{
            var fragment = FragmentProfile()
            var arg = Bundle()
            arg.putSerializable(DATA, data)

            fragment.arguments = arg
            fragment.uiStep = uiStep
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view =  inflater.inflate(R.layout.fragment_profile, container, false)
        data = arguments?.getSerializable(DATA) as ProfileDataModel?

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextsAndDataToUI()
    }

    override fun bind(data: Any) {

    }

    private fun setTextsAndDataToUI(){
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        setBankAccountView(inflater)
        setCreditCardView(inflater)

    }

    private fun setBankAccountView(inflater : LayoutInflater){
        fun setTopView(){
            var bankAccountsTopView = inflater.inflate(R.layout.item_two_lines, null)
            bankAccountsTopView.titleItemSecond.text = data?.bank.toString()
            banksAccounts.setTopView(bankAccountsTopView)
        }

        fun setBottomView(){
            var bankAccountsBottomView = inflater.inflate(R.layout.bank_account_card, null)
            bankAccountsBottomView.backAccountCardDetails.text = data?.bank.toString()
            banksAccounts.setBottomView(bankAccountsBottomView)
        }

        setTopView()
        setBottomView()

    }

    private fun setCreditCardView(inflater : LayoutInflater){
        fun setTopView(){
            var credit = inflater.inflate(R.layout.item_two_lines, null)
            credit.titleItem.text = "כרטיס אשראי"
            credit.titleItemSecond.text = "${data?.fourLastNumberCreditCard} **** **** ****"
            creditCard.setTopView(credit)
        }

        fun setBottomView(){
            var creditBottomView = inflater.inflate(R.layout.credit_card, null)
            creditBottomView.creditCardDetails.text = "${data?.fourLastNumberCreditCard}  ****  ****  ****"
            creditCard.setBottomView(creditBottomView)
        }

        setTopView()
        setBottomView()
    }

    data class ProfileDataModel(
            var countStars : Int,
            var businessName : String,
            var bank: Bank,
            var fourLastNumberCreditCard : String
    ) : Serializable
}