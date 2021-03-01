package firebase

import com.google.firebase.database.FirebaseDatabase

object FireBaseMoney {
    fun chargeClientByMail(mail : String, price : Int){
        FirebaseDatabase.getInstance().getReference("users").child(mail)
    }
}