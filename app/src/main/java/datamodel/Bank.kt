package datamodel

data class Bank(
        var bankNumber : Int,
        var branch : Int,
        var accountNumber : Int
){
    override fun toString(): String {
        return "$accountNumber  $branch  $bankNumber"
    }
}