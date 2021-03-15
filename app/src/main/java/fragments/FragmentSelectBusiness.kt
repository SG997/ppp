package fragments

import adapters.AdapterBusinessPresent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import base.BaseFragment
import base.UIStep
import bo.Messages
import bo.User
import com.example.myapplication.R
import com.google.firebase.storage.FirebaseStorage
import data.Cache
import firebase.FireBaseData
import firebase.FireBaseImage
import firebase.FireBaseMessagesManager
import kotlinx.android.synthetic.main.fragment_select_business.*
import kotlinx.android.synthetic.main.fragment_select_business.view.*


class FragmentSelectBusiness: BaseFragment() {

    companion object{
        fun getInstance(uiStep : UIStep) : BaseFragment{
            var fragment = FragmentSelectBusiness()
            fragment.uiStep = uiStep
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_business, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.goToProfile.setOnClickListener {
            notifyFinish(SELECT_BUSINESS.PROFILE)
        }
    }

    fun setUpListener(){
        messageView.setOnClickListener {
            notifyFinish(SELECT_BUSINESS.MESSAGE)
        }
    }

    private fun createFakeData() : ArrayList<AdapterBusinessPresent.BusinessPresentationData>{
        var lst = ArrayList<AdapterBusinessPresent.BusinessPresentationData>()

        for (i in  0..5){
            lst.add(AdapterBusinessPresent.BusinessPresentationData("נטי", "שדגשדגשדג", "ש", "ד"))
        }
        return lst
    }

    private fun createDataForRecyclerView(data : ArrayList<User>) : ArrayList<AdapterBusinessPresent.BusinessPresentationData>{
        var lst = ArrayList<AdapterBusinessPresent.BusinessPresentationData>()

        for (user in data){
            lst.add(AdapterBusinessPresent.BusinessPresentationData(user.name, user.explain, user.bannerUrl, user.iconUrl))
        }
        return lst
    }

    override fun bind(data: Any) {
        setUpListener()
    }

    fun asd() = object : FireBaseData.DataStoring{
        override fun onAction(actionCode: FireBaseData.Action, isSuccess: Boolean) {
            try {
                when (actionCode){
                    FireBaseData.Action.DATA_CHANGE -> {
                        if (isAdded() && isVisible() && getUserVisibleHint()){
                            loadImages()
                        }
                    }

                    FireBaseData.Action.MESSAGE_LOADED -> {
                        messageView.setNumber(Messages.getMyMessage(Cache.currentUser!!.email, Cache.messages).size)
                    }
                }
            } catch (e: Exception){

            }
        }
    }

    override fun onStart() {
        super.onStart()
        FireBaseData.observeToUserDataChange(asd())

        FireBaseMessagesManager.observToMessage(asd())
    }

    fun configRecycler(){
        recyclerViewBusiness.layoutManager = LinearLayoutManager(context)
        recyclerViewBusiness.setHasFixedSize(true)

        // Change data to be independent
        var adapter = AdapterBusinessPresent(createDataForRecyclerView(Cache.users))
        adapter.listener = object : AdapterBusinessPresent.Clicks {
            override fun onClick(position : Int) {
                print("asdfghjkl")
                Cache.choosedBusiness = position
                notifyFinish(SELECT_BUSINESS.BUSINESS)
            }
        }
        recyclerViewBusiness.adapter = adapter
    }

    fun loadImages(){
        configRecycler()
    }

    enum class SELECT_BUSINESS{
        PROFILE,
        BUSINESS,
        MESSAGE
    }
}