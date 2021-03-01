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
import bo.User
import com.example.myapplication.R
import com.google.firebase.storage.FirebaseStorage
import data.Cache
import firebase.FireBaseData
import firebase.FireBaseImage
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
            lst.add(AdapterBusinessPresent.BusinessPresentationData(user.name, user.explain, "asd", "asd"))
        }
        return lst
    }

    override fun bind(data: Any) {

    }

    override fun onStart() {
        super.onStart()
        FireBaseData.observeToUserDataChange(object : FireBaseData.DataStoring{
            override fun onAction(actionCode: FireBaseData.Action, isSuccess: Boolean) {
                loadImages()
            }
        })
    }

    fun configRecycler(imagesBanner : Array<Uri?>, imagesIcon : Array<Uri?>){
        recyclerViewBusiness.layoutManager = LinearLayoutManager(context)
        recyclerViewBusiness.setHasFixedSize(true)

        // Change data to be independent
        var adapter = AdapterBusinessPresent(createDataForRecyclerView(Cache.users), imagesBanner, imagesIcon)
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
        var imagesBanner = Array<Uri?>(Cache.users.size) {null}
        var imagesIcon = Array<Uri?>(Cache.users.size) {null}


        var storageRef = FirebaseStorage.getInstance().reference

        for (i in 0 until Cache.users.size){

            storageRef.child(FireBaseImage.getBusinessBannerPath(Cache.users[i].email)).downloadUrl.addOnSuccessListener { uri ->
                imagesBanner[i] = uri
            }
            storageRef.child(FireBaseImage.getBusinessIconPath(Cache.users[i].email)).downloadUrl.addOnSuccessListener {uri ->
                imagesIcon[i] = uri
            }
        }

        Handler().postDelayed({
            configRecycler(imagesBanner, imagesIcon)
        }, 1100)

    }

    enum class SELECT_BUSINESS{
        PROFILE,
        BUSINESS
    }
}