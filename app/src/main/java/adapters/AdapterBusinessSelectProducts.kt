package adapters

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_business_product.view.*

class AdapterBusinessSelectProducts(val dataList : ArrayList<Pair<String, BusinessProduct>>, val isEditable : Boolean = false) : RecyclerView.Adapter<AdapterBusinessSelectProducts.VH>() {

    var listener : Clicks? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        var vh : VH = when (viewType){
            else -> {
                var view = getViewFromRes(parent, R.layout.item_business_product)
                view.editIcon.visibility = if (isEditable) View.VISIBLE else View.GONE
                view.deleteSwitch.visibility = if (isEditable) View.VISIBLE else View.GONE
                VHProduct(view)
            }
        }

        return vh
    }

    fun getViewFromRes(viewGroup: ViewGroup, layoutResRef : Int, layoutInflater : LayoutInflater = LayoutInflater.from(viewGroup.context)) : View = layoutInflater.inflate(layoutResRef, viewGroup, false)

    override fun getItemCount(): Int = dataList.size
    override fun getItemViewType(position: Int): Int = when (position){
        else -> Type.PRODUCT.value
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        //TODO connectiong
        holder.bind(dataList[position], position, listener)
    }

    fun addProduct(product : Pair<String, BusinessProduct>){
        dataList.add(product)
        notifyDataSetChanged()
    }

    fun setProductByPosition(product : BusinessProduct, position : Int){
        dataList[position] = Pair(dataList[position].first, product)
        notifyDataSetChanged()
    }

    open class VH(view : View) : RecyclerView.ViewHolder(view){
        open fun bind(data : Pair<String, BusinessProduct>, position: Int, listener : Clicks?){}
    }

    class VHProduct(view : View) : VH(view){
        override fun bind(wrapData : Pair<String, BusinessProduct>, position: Int, listener : Clicks?){
            var data = wrapData.second
            itemView.setOnClickListener {
                listener?.onClick(wrapData, position)
            }
            Picasso.with(itemView.context).load(data.imageUrl).into(itemView.businessProductImage)
            itemView.productName.text = data.productName
            itemView.productExplain.text = data.detailes
            itemView.productAmount.text = data.amount
            itemView.deleteSwitch.setOnClickListener {
                listener?.onDelete(wrapData.first)
            }

        }
    }

    data class BusinessProduct(
            var imageUrl : String,
            var productName : String,
            var detailes : String,
            var amount : String,
            var uri : Uri? = null,
            var key : String  = ""
    )
    interface Clicks{
        fun onClick(data : Pair<String, BusinessProduct>, position: Int)
        fun onDelete(key: String)
    }

    enum class Type(val value: Int) {
        ADD_PRODUCT(1),
        PRODUCT(2),
    }
}