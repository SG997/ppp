package bo

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Outer {
    var id : String? = null
    var inner : Inner? = null

    constructor(id : String, inner : Inner){
        this.inner = inner
        this.id = id
    }

    constructor()
}