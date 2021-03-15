package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.item_message_bullets.view.*
import kotlinx.android.synthetic.main.message_row_item.view.*

class AdapterMessages(val data : ArrayList<RecyclerMessages>) : RecyclerView.Adapter<AdapterMessages.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        var layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.message_row_item, parent, false)

        return VH(listItem, layoutInflater, parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class VH (view : View, layoutInflater : LayoutInflater, val parent: ViewGroup) : RecyclerView.ViewHolder(view) {
        val layoutInflater = LayoutInflater.from(view.context)

        fun bind(data : RecyclerMessages) {
            itemView.messagesLayout.removeAllViews()

            data.messages.forEach {
                val view: View = layoutInflater.inflate(R.layout.item_message_bullets, parent, false)
                view.messagesBullet.text = it
                itemView.messagesLayout.addView(view)
            }


        }

    }

    data class RecyclerMessages(var title : String,
                                var messages : ArrayList<String>)
}