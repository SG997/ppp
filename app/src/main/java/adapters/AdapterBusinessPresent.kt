package adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bo.User
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import firebase.FireBaseData
import kotlinx.android.synthetic.main.item_business.view.*

class AdapterBusinessPresent(val data : ArrayList<BusinessPresentationData>) : RecyclerView.Adapter<AdapterBusinessPresent.BusinessVH>() {
    var listener : Clicks? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.item_business, parent, false)

        return BusinessVH(listItem)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BusinessVH, position: Int) {
        holder.bind(data[position], listener, position)
    }

    class BusinessVH(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(itemData : BusinessPresentationData, listene : Clicks?, position : Int){
            itemView.setOnClickListener {
                listene?.onClick(position)
            }

            itemView.businessNameBanner.text = itemData.businessName
            itemView.bannerExplane.text = itemData.description


            Picasso.with(itemView.context).load(itemData.bannerUrl).into(itemView.businessViewBanner)
            Picasso.with(itemView.context).load(itemData.iconUrl).into(itemView.circleImage)
        }

    }

    interface Clicks{
        fun onClick(position : Int)
    }

    data class BusinessPresentationData(var businessName : String,
                                        var description : String,
                                        var bannerUrl : String?,
                                        var iconUrl : String?)
}