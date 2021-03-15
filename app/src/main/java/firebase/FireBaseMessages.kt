package firebase

import adapters.AdapterBusinessSelectProducts
import android.content.Context
import bo.Messages
import bo.User
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import data.Cache
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object FireBaseMessagesManager {
    private val MESSAGES_REF = "messages"

    private var messageRef = FirebaseDatabase.getInstance().getReference(MESSAGES_REF)
    var count = 0

    private fun sendMessages(number : Int, message : Messages) = messageRef.child(number.toString()).setValue(message)

    fun getTime() : String = Calendar.getInstance().let { "${it.get(Calendar.DAY_OF_MONTH)}/${it.get(Calendar.MONTH) + 1}/${it.get(Calendar.YEAR)}" }

    fun buyProduct(context : Context, sellerMail : String, buyer : User, product : AdapterBusinessSelectProducts.BusinessProduct){

        var texts = ArrayList<String>()
        var text = context.getText(R.string.product_bought_text)
                .replace(Regex("₪₪₪"), product.productName)
                .replace(Regex("###"), buyer.name)
                .replace(Regex("%%%"), getTime())
                .replace(Regex("!!!"), product.amount)
        texts.add(text)

        sendMessages(count, Messages(texts, FireBaseData.validateEmailToUserKey(sellerMail), "רכישת מוצר", FireBaseData.validateEmailToUserKey(buyer.email)))
        count ++

    }


    fun observToMessage(listener : FireBaseData.DataStoring) {
        messageRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }


            override fun onDataChange(snapshot: DataSnapshot) {
                Cache.messages = parseMessage(snapshot)
                listener.onAction(FireBaseData.Action.MESSAGE_LOADED, true)
            }

        })
    }

    fun parseMessage(snapshot: DataSnapshot) : ArrayList<Messages>{
        var mapMasseges : HashMap<String, Messages> = HashMap()
        var mapMassegesArray : ArrayList<Messages> = ArrayList()

        fun getMessage(map : HashMap<String, Any>) : Messages{
            var read = map["read"] as Boolean
            var sender = map["sender"] as String
            var mailIl = map["mailId"] as String
            var rows = map["rows"] as ArrayList<String>
            var title = map["title"] as String
            var forEveryBody = map["forEveryBody"] as Boolean

            return Messages(rows, mailIl, title, sender, read, forEveryBody)
        }


        (snapshot.value as ArrayList<Any>).let {
            it.forEach {value ->
                mapMasseges.put(it.indexOf(value).toString(), getMessage(value as HashMap<String, Any>))
                mapMassegesArray.add(getMessage(value))
            }
        }

        return mapMassegesArray
    }


}