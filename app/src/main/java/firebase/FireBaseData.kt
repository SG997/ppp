package firebase

import adapters.AdapterBusinessSelectProducts
import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import bo.BankAccount
import bo.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import data.Cache
import datamodel.ImageAndPath


object FireBaseData {
    val USER_PATHES = "users"
    val USER_IMAGES = "users/"

    var notify : DataStoring? = null


    var userRef = FirebaseDatabase.getInstance().getReference(USER_PATHES)
    val storeImagesUser: StorageReference = FirebaseStorage.getInstance().reference

    fun validateEmailToUserKey(mail : String) = mail.replace(".", "")

    private fun storeImage(path : String, uir: Uri) = storeImagesUser.child(path).putFile(uir)

    fun updateStars(context : Context, mail : String, starsNumber : Int){
        userRef.child(validateEmailToUserKey(mail)).child("stars").setValue(starsNumber).addOnSuccessListener {
            Toast.makeText(context, "Starsupdate success", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Starsupdate Failed", Toast.LENGTH_LONG).show()
        }
    }

    private fun storeUser(context : Context, user : User, images : ImageAndPath){

        var key = user.email.replace(".", "")

        storeImage(FireBaseImage.getBusinessBannerPath(user.email), images.banner)
        storeImage(FireBaseImage.getBusinessIconPath(user.email), images.icon)

        for (i in 0 until images.products.size){
            storeImage(FireBaseImage.getBusinessProductPathViaMail(user.email, i), images.products[i])
        }
        //var s = FirebaseStorage.getInstance().getReference(USER_PATHES).child(key).

        userRef.child(key).setValue(user).addOnCompleteListener {
            Toast.makeText(context, "is success", Toast.LENGTH_LONG).show()
            notify?.onAction(Action.USER_STORE, true)
        }

        userRef.child(key).setValue(user).addOnFailureListener {
            notify?.onAction(Action.USER_STORE, false)
        }
    }

    fun uploadImage(storageRef: StorageReference, uri: Uri){
        storageRef.putFile(uri)
    }

    fun createUser(context : Context, email: String, password : String, user : User, images : ImageAndPath){
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            var isSuccess = false
            if (it.isSuccessful){
                isSuccess = true
                storeUser(context, user, images)
                Toast.makeText(context, "success", Toast.LENGTH_LONG).show()

            } else{
                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
            }
            notify?.onAction(Action.USER_CRETE, isSuccess)
        }.addOnFailureListener {
            Toast.makeText(context, "User creation was failed", Toast.LENGTH_LONG).show()
        }
    }

    fun observeToUserDataChange(observer : DataStoring){
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }


            override fun onDataChange(snapshot: DataSnapshot) {
                Cache.users = parseUsers(snapshot)
                Cache.currentUser = findUserByMail(Cache.users, FirebaseAuth.getInstance().currentUser?.email!!)
                observer.onAction(Action.DATA_CHANGE)
            }

        })
    }

    fun parseUsers(snapshot: DataSnapshot) : ArrayList<User>{
        var users  = ArrayList<User>()
        (snapshot.value as HashMap<String, User>?)?.keys?.forEach {
            users.size

            ((snapshot.value as HashMap<String, Object>).get(it) as HashMap<String, Object>)?.let{ hash ->

                var password = hash.get("password").toString()
                var name = hash.get("name").toString()
                var email = hash.get("email").toString()
                var explain = hash.get("explain").toString()
                var stars = hash.get("stars") as Long?

                var bankNumber = (hash.get("bankAccount") as HashMap<String, Long>).get("bankNumber")
                var branchNumber = (hash.get("bankAccount") as HashMap<String, Long>).get("branchNumber")
                var accountNumber = (hash.get("bankAccount") as HashMap<String, Long>).get("accountNumber")


                var products = ArrayList<AdapterBusinessSelectProducts.BusinessProduct>()



                ((hash["products"]) as ArrayList<HashMap<String, Object>>).forEach {
                    products.add(AdapterBusinessSelectProducts.BusinessProduct(it["imageUrl"] as String, it["productName"] as String, it["detailes"] as String, it["amount"] as String))
                }




                var bank = BankAccount(bankNumber!!.toInt(), branchNumber!!.toInt(), accountNumber!!.toInt())

                users.add(User(name, password, email, bank, products, explain, stars?.toInt() ?: -1))
            }
        }
        return users
    }

    fun findUserByMail(users : ArrayList<User>, mail : String) : User?{
        users.forEach {
            if (it.email == mail){
                return it
            }
        }
        return null
    }

    fun parseData(snapshot: DataSnapshot){
        //(snapshot.value as HashMap<String, User>).keys.forEach { users.add(snapshot.value[it]) }
    }

    interface DataStoring{
        fun onAction(actionCode : Action, isSuccess : Boolean = true)
    }

    enum class Action{
        USER_STORE,
        USER_CRETE,
        DATA_CHANGE
    }
}