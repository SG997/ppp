package fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import base.BaseFragment
import base.UIStep
import bo.BankAccount
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_insert_detailes.*


class FragmentInsertDetailes : BaseFragment(), FragmentSignIn.PopulateData {
    companion object{
        val DATA = "data"

        fun getInstance(uiStep : UIStep) : BaseFragment{
            var fragment = FragmentInsertDetailes()
            fragment.uiStep = uiStep
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_insert_detailes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListener()
    }

    override fun bind(data: Any) {

    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 100)
    }


    fun importImage(view : ImageButton){
        openGallery()
        (activity as MainActivity).imageLoaded = object : MainActivity.OnImageLoaded{
            override fun onImageLoaded(uir: Uri) {
                view.setImageURI(uir)
                view.tag = uir
            }
        }
    }

    fun collectBankAccountData() : BankAccount = BankAccount(bankNumber.text.toString().toInt(), branchNumber.text.toString().toInt(), accountNumber.text.toString().toInt())

    fun setUpListener(){
        banner.setOnClickListener {
            importImage(banner)
        }

        bannerLogo.setOnClickListener {
            importImage(bannerLogo)
        }
    }

    override fun setLocalData(user: FragmentSignIn.CompleteRegistrationData): Boolean {
        user.name = businessName.text.toString()
        user.email = businessMail.text.toString()
        user.password = password.text.toString()
        user.explain = businessExplain.text.toString()

        user.bankAccount = collectBankAccountData()

        user.banner = banner.tag as Uri
        user.bannerLogo = bannerLogo.tag as Uri

        return true
    }
}