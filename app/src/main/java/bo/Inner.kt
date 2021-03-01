package bo

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

class Inner{
    var id1 : String? = null
    var id2 : String? = null

    constructor(id1 : String, id2 : String){
        this.id1 = id1
        this.id2 = id2
    }

    constructor()
}