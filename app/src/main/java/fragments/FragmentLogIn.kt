package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.BaseFragment
import com.example.myapplication.R
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
            var main = logInEmail.text.toString()
            var password = logInPassword.text.toString()

            // Handle logIn handle:
            /**
             * If user has user well done go to all businesses else pop up
             *
             * Go to sign in
             */
        }

        goToSignIn.setOnClickListener {
            notifyFinish(LogInData(true))
        }
    }

    data class LogInData(var goToSighIn : Boolean = false,
                         var forgatePassword : Boolean = false)
}