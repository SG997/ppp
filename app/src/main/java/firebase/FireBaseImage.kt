package firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

object FireBaseImage {
    val USER_IMAGES = "users/"
    val BANNER_PATH = "/banner"
    val ICON_PATH = "/icon"
    val PRODUCT_PATH = "/product"

    private fun getUserImagePathViaMail(mail : String) = USER_IMAGES + validateMail(mail).replace(".", "")

    fun getBusinessBannerPath(mail : String) = getUserImagePathViaMail(mail) + BANNER_PATH

    fun getBusinessIconPath(mail : String) = getUserImagePathViaMail(mail) + ICON_PATH

    fun getBusinessProductPathViaMail(mail : String, number : Int) = getUserImagePathViaMail(mail) + PRODUCT_PATH + number
    fun getBusinessProductPathViaMail(mail : String, number : String) = getUserImagePathViaMail(mail) + PRODUCT_PATH + number

    private fun validateMail(mail : String) = mail.replace(".", "")

    fun getBannerImageUri(mail: String, onImageLoaded: onImageLoaded){
        FirebaseStorage.getInstance().reference.child(getBusinessBannerPath(mail)).downloadUrl.addOnSuccessListener {
            onImageLoaded.onImageLoaded(it)
        }
    }

    fun getBusinessProducts(mail : String, numberOfProducts : Int, onImageLoaded: onImageLoaded){
        for (i in 0 until numberOfProducts){
            FirebaseStorage.getInstance().reference.child(getBusinessProductPathViaMail(mail, i)).downloadUrl.addOnSuccessListener {
                onImageLoaded.onImageLoaded(it, i)
            }
        }
    }

    interface onImageLoaded{
        fun onImageLoaded(uri : Uri, num : Int = -1)
    }
}