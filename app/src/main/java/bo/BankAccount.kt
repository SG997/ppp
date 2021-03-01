package bo

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class BankAccount(var bankNumber : Int,
                       var branchNumber : Int,
                       var accountNumber : Int) {
}