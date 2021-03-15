package fragments

import adapters.AdapterMessages
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import base.BaseFragment
import bo.Messages
import com.example.myapplication.R
import data.Cache
import firebase.FireBaseData
import kotlinx.android.synthetic.main.fragment_messages.*

class FragmentMessages : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }



    override fun bind(data: Any) {
        setUp()
    }

    fun setUp(){
        messages.layoutManager = LinearLayoutManager(context)
        messages.adapter = AdapterMessages(createCustomData())
    }

    fun createCustomData() : ArrayList<AdapterMessages.RecyclerMessages>{
        var list = ArrayList<AdapterMessages.RecyclerMessages>()
        Messages.getMyMessage(FireBaseData.validateEmailToUserKey(Cache.currentUser!!.email), Cache.messages).forEach {
            list.add(AdapterMessages.RecyclerMessages(it.title, it.rows))
        }
        return list
    }

    fun createFakeData() : ArrayList<AdapterMessages.RecyclerMessages> {
        var list = ArrayList<AdapterMessages.RecyclerMessages>()

        var texts = ArrayList<String>()
        texts.add("שורה ראשונה")
        texts.add("שורה שנייה")
        texts.add("שורה שלישית")
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))
        list.add(AdapterMessages.RecyclerMessages("כותרת ראשונה", texts))

        return list
    }
}