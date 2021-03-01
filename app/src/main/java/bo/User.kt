package bo

import adapters.AdapterBusinessPresent
import adapters.AdapterBusinessSelectProducts
import android.net.Uri
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class User(var name : String,
                var password: String,
                var email: String,
                var bankAccount: BankAccount,
                var products : ArrayList<AdapterBusinessSelectProducts.BusinessProduct>,
                var explain : String,
                var stars : Int = 0)