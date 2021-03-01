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

class AdapterBusinessSelectProducts(private val dataList : ArrayList<BusinessProduct>) : RecyclerView.Adapter<AdapterBusinessSelectProducts.VH>() {

    var listener : Clicks? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.item_business_product, parent, false)

        return VH(listItem)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        //TODO connectiong
        holder.bind(dataList[position], listener)
    }

    class VH(view : View) : RecyclerView.ViewHolder(view){
        fun bind(data : BusinessProduct, listener : Clicks?){
            itemView.setOnClickListener {
                listener?.onClick(data)
            }
            Picasso.with(itemView.context).load(data.uri).into(itemView.businessProductImage)
            itemView.productName.text = data.productName
            itemView.productExplain.text = data.detailes
            itemView.productAmount.text = data.amount

        }
    }
    data class BusinessProduct(
            var imageUrl : String,
            var productName : String,
            var detailes : String,
            var amount : String,
            var uri : Uri? = null
    )
    interface Clicks{
        fun onClick(data : BusinessProduct)
    }
}