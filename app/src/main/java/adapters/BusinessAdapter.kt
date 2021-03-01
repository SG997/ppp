package adapters

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.squareup.picasso.Picasso


class BusinessAdapter() : RecyclerView.Adapter<BusinessAdapter.ViewHolderBusiness>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBusiness {

        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.business_item, parent, false)

        return ViewHolderBusiness(listItem)

    }

    //TODO change to datamodel.length
    override fun getItemCount(): Int = 50

    override fun onBindViewHolder(holder: ViewHolderBusiness, position: Int) {
        holder.bind()
    }

    class ViewHolderBusiness(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(){
            itemView.findViewById<TextView>(R.id.businessFirstTitle).text = "Sdf"
            itemView.findViewById<TextView>(R.id.businessSecondTitle).text = "Sdf"
            Picasso.with(itemView.context).load("https://i.imgur.com/DvpvklR.png").into(itemView.findViewById<ImageView>(R.id.businessLogo))
            createExpand(itemView.findViewById<LinearLayout>(R.id.expandable))
        }

        private fun createExpand(layout : LinearLayout){

            //TODO change from data set
            for (i in 0..3){
                val layoutInflater = LayoutInflater.from(itemView.context)
                val view: View = layoutInflater.inflate(R.layout.expandable_item_bullet, layout, false)
                view.findViewById<TextView>(R.id.bulletText).text = "מחירים גמישים"
                layout.addView(view)
            }
            setUpListener(itemView)
        }

        private fun setUpListener(view: View){
            // Create an animation instance


            var startAngle = 0f
            var finishAngle = -90f
            var anim: ObjectAnimator? = null
            anim?.duration = 350



            // Change to expand/collapse
            view.tag = true
            view.setOnClickListener {

                //itemView.findViewById<ImageView>(R.id.chevron).startAnimation(an)


                var expanOrCollapseView = it.findViewById<LinearLayout>(R.id.expandable)
                if (it.tag as Boolean){
                    expanOrCollapseView.visibility = View.VISIBLE
                } else{
                    expanOrCollapseView.visibility = View.GONE
                }
                it.tag = !(it.tag as Boolean)



                anim = ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.chevron), "rotation", startAngle, finishAngle)
                var temp = startAngle
                startAngle = finishAngle
                finishAngle = temp

                anim?.start()
            }
        }
    }
}