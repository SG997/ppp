package bo

import adapters.AdapterBusinessSelectProducts
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class User(var name : String,
                var password: String,
                var email: String,
                var bankAccount: BankAccount,
                var products : HashMap<String, AdapterBusinessSelectProducts.BusinessProduct>? = null,
                var explain : String,
                var stars : Int = 0,
                var bannerUrl : String? = null,
                var iconUrl : String? = null)