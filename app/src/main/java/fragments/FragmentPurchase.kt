package fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import base.BaseFragment
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_purchase.*
import kotlinx.android.synthetic.main.item_star_deal.view.*


class FragmentPurchase : BaseFragment(){
    var dealLst = ArrayList<StarsData>()
    var isInit = false

    val STARS_NUMBER_MARK = "###"
    val PRICE_MARK = "%%%"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_purchase, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()


        if (!isInit){
            isInit = true
        }
        //initPrices()
    }

    override fun bind(data: Any) {

    }

    fun initData(){
        var lastStar = 5
        var price = 50

        dealLst.add(StarsData(lastStar, price))
        for (i in 1..3){
            dealLst.add(StarsData(dealLst[i-1].starsNumber * 5, (dealLst[i-1].price * 5 * 0.95f).toInt()))
        }

        dealLst.forEach { initDealsViews(it) }
    }

    fun initDealsViews(dealData : StarsData) {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflater.inflate(R.layout.item_star_deal, null)

        view.starsDealText.text = view.starsDealText.text
                .replace(STARS_NUMBER_MARK.toRegex(), dealData.starsNumber.toString())
                .replace(PRICE_MARK.toRegex(), dealData.price.toString())

        view.tag = dealData
        view.setOnClickListener {
            Toast.makeText(context, "stars: ${(it.tag as StarsData).starsNumber},   price: ${(it.tag as StarsData).price}", Toast.LENGTH_LONG)
                    .show()
        }
        starsDealMainLayout.addView(view)

    }

    data class StarsData(var starsNumber : Int,
                         var price : Int)

}