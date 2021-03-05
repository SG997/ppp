package fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.BaseFragment
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import dialogs.CustomDialog
import kotlinx.android.synthetic.main.fragment_log_in.*

class FragmentLogIn : BaseFragment() {

    companion object{
        private var frag : FragmentLogIn? = null
        fun newInstance() : FragmentLogIn{
            if (frag == null){
                frag = FragmentLogIn()
            }
            return frag!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLogic()
    }

    override fun bind(data: Any) {

    }

    fun setUpLogic() {
        entryBtn.setOnClickListener {
            logIn()
        }

        goToSignIn.setOnClickListener {
            notifyFinish(LogInData(true))
        }
    }

    private fun logIn(){
        var mail = logInEmail.text.toString()
        var password = logInPassword.text.toString()

        val user = FirebaseAuth.getInstance()

        user.signInWithEmailAndPassword(mail, password).addOnSuccessListener {
            notifyFinish(LogInData())
        }.addOnFailureListener {
            context?.let {
                CustomDialog.getAlertDialog(it, "משהו השתבש...", "אחד מהפרטים שהזנת לא תואמים את המידע השמור אצלינו במערכת אנה נסה שוב...", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                }).show()
            }
        }
    }

    data class LogInData(var goToSighIn : Boolean = false,
                         var forgatePassword : Boolean = false)
}