package data

import bo.User
import com.google.firebase.auth.FirebaseUser

object Cache{
    var users = ArrayList<User>()
    var choosedBusiness : Int = -1
    var currenProduct : Int = -1
    var currentUser : User? = null
}