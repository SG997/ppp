package firebase

import adapters.AdapterBusinessSelectProducts
import android.content.Context
import android.net.Uri
import android.widget.Toast
import bo.BankAccount
import bo.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import data.Cache
import java.lang.Exception


object FireBaseData {
    val USER_PATHES = "users"
    val USER_IMAGES = "users/"
    val BANNER = "/bannerUrl"

    var notify : DataStoring? = null


    var userRef = FirebaseDatabase.getInstance().getReference(USER_PATHES)
    val storeImagesUser: StorageReference = FirebaseStorage.getInstance().reference

    fun validateEmailToUserKey(mail : String) = mail.replace(".", "")

    private fun storeImage(path : String, uir: Uri, callback : ImageStoring? = null){
        storeImagesUser.child(path).putFile(uir).addOnSuccessListener { it ->
            it.storage.downloadUrl.addOnSuccessListener { uri ->
                callback?.onImageStore(Action.STORE_IMAGE, uri.toString(), true)
            }
        }.addOnFailureListener {
            callback?.onImageStore(Action.STORE_IMAGE, "", false)
        }

    }
    fun getUserDBViaMail(mail : String) : DatabaseReference = userRef.child(validateEmailToUserKey(mail))

    fun storeBannerUrl(mail : String, url : String) = getUserDBViaMail(mail).child(BANNER).setValue(url)

    fun storeBannerViaMail(mail : String, uri : Uri){
        storeImage(FireBaseImage.getBusinessBannerPath(mail), uri, object : ImageStoring{
            override fun onImageStore(actionCode: Action, url: String, isSuccess: Boolean) {
                if (isSuccess) {
                    storeBannerUrl(mail, url)
                }
            }

        })

    }

    fun deleteProductToUserViaMail(mail : String, position : String){
        userRef.child(validateEmailToUserKey(mail)).child("/products/$position").removeValue()
        FirebaseStorage.getInstance().getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/upstore-33769.appspot.com/o/users%2Fabc%40gmailcom%2Fproduct4?alt=media&token=62b2a8a4-c286-4bec-8711-ef5bfd16b1be")
    }

    fun storeProductToUserViaMail(mail : String, product : AdapterBusinessSelectProducts.BusinessProduct, position : String){
        storeImage(FireBaseImage.getBusinessProductPathViaMail(mail, position), product.uri!!, object : ImageStoring{
            override fun onImageStore(actionCode: Action, url: String, isSuccess: Boolean) {
                if (isSuccess){
                    // Now we have the image to be store in we need to store the product
                        var a = HashMap<String, Any>()
                    a.put(position.toString(), product)
                    userRef.child(validateEmailToUserKey(mail)).child("/products/$position/").let {
                        it.child("productName").setValue(product.productName)
                        it.child("detailes").setValue(product.detailes)
                        it.child("amount").setValue(product.amount)
                        it.child("imageUrl").setValue(url)
                    }
                }
            }

        })
    }

    fun updateStars(context : Context, mail : String, starsNumber : Int, listener : DataStoring){
        userRef.child(validateEmailToUserKey(mail)).child("stars").setValue(starsNumber).addOnSuccessListener {
            Toast.makeText(context, "Starsupdate success", Toast.LENGTH_LONG).show()
            listener.onAction(Action.STARS_BALANCE_UPDATE)
        }.addOnFailureListener {
            Toast.makeText(context, "Starsupdate Failed", Toast.LENGTH_LONG).show()
        }
    }

    fun storeUserData(context : Context, user : User){

        var key = user.email.replace(".", "")

/*        storeImage(FireBaseImage.getBusinessBannerPath(user.email), images.banner)
        storeImage(FireBaseImage.getBusinessIconPath(user.email), images.icon)

        for (i in 0 until images.products.size){
            storeImage(FireBaseImage.getBusinessProductPathViaMail(user.email, i), images.products[i], object : ImageStoring{
                override fun onImageStore(actionCode: Action, url: String, isSuccess: Boolean) {
                    if (actionCode == Action.STORE_IMAGE && isSuccess){
                        user.products[i].imageUrl = url
                    }
                }

            })
        }*/
        //var s = FirebaseStorage.getInstance().getReference(USER_PATHES).child(key).

        //storeUserObject(key, user, context)

        userRef.child(key).setValue(user).addOnSuccessListener {
            Toast.makeText(context, "is success", Toast.LENGTH_LONG).show()
            notify?.onAction(Action.USER_STORE, true)
        }.addOnFailureListener {
            notify?.onAction(Action.USER_STORE, false)
        }
    }

    fun storeUserObject(key : String, user : String, context : Context) = userRef.child(key).setValue(user).addOnCompleteListener {

    }

    fun uploadImage(storageRef: StorageReference, uri: Uri){
        storageRef.putFile(uri)
    }

    fun createUser(context : Context, email: String, password : String, user : User){
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            var isSuccess = false
            if (it.isSuccessful){
                isSuccess = true
                storeUserData(context, user)
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
                var bannerUrl = hash.get("bannerUrl") as String?

                var bankNumber = (hash.get("bankAccount") as HashMap<String, Long>).get("bankNumber")
                var branchNumber = (hash.get("bankAccount") as HashMap<String, Long>).get("branchNumber")
                var accountNumber = (hash.get("bankAccount") as HashMap<String, Long>).get("accountNumber")


                var products = ArrayList<AdapterBusinessSelectProducts.BusinessProduct>()
                var hashProducts = HashMap<String, AdapterBusinessSelectProducts.BusinessProduct>()

                var a = 8

                fun getProductFromHash(hashMap: HashMap<String, Any>) : AdapterBusinessSelectProducts.BusinessProduct{
                    var prod = AdapterBusinessSelectProducts.BusinessProduct(hashMap["imageUrl"] as String, hashMap["productName"] as String, hashMap["detailes"] as String, hashMap["amount"] as String)
                    return prod
                }

                try {
                    ((hash["products"]) as ArrayList<HashMap<String, Object>>).let {products ->
                        for (i in 0 until products.size){
                            if (products.get(i) != null){
                                products[i].let {
                                    var prod = AdapterBusinessSelectProducts.BusinessProduct(it["imageUrl"] as String, it["productName"] as String, it["detailes"] as String, it["amount"] as String)
                                    hashProducts.put(i.toString(), prod)
                                }
                            }

                        }
                    }
                } catch (e : Exception){

                    //(((hash["products"]) as Map<*,*>).toList()[0].second as HashMap<*,*>)
                    //(hash["products"] as Map<*,*>).
                    try {
                        hash["products"]?.let {
                            (it as HashMap<String, Any>).forEach {
                                var produ = getProductFromHash(it.value as HashMap<String, Any>)
                                hashProducts[it.key] = produ
                            }
                        }
                    } catch (e: Exception){
                         FireBaseData.userRef.get()
                    }
                }




                var bank = BankAccount(bankNumber!!.toInt(), branchNumber!!.toInt(), accountNumber!!.toInt())

                users.add(User(name, password, email, bank, hashProducts, explain, stars?.toInt() ?: -1, bannerUrl))
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

    interface ImageStoring{
        fun onImageStore(actionCode : Action, url : String, isSuccess : Boolean = true)
    }

    enum class Action{
        USER_STORE,
        USER_CRETE,
        DATA_CHANGE,
        STORE_IMAGE,
        STARS_BALANCE_UPDATE
    }
}