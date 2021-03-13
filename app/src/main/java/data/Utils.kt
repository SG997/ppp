package data

object Utils {
    private val charPool : List<Char> = ('0'..'4') + ('5'..'9')

    fun generateKey(hash: ArrayList<String>) : String{
        var key = getRandomString()
        while (hash.contains(key)){
            key = getRandomString()
        }
        return key
    }

    fun getRandomString() : String = (1..3).map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("");

}