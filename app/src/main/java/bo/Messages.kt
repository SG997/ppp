package bo

data class Messages (var rows : ArrayList<String>,
                     var mailId : String,
                     var title : String,
                     var sender : String,
                     var isRead : Boolean = false,
                     var isForEveryBody : Boolean = false){

    companion object{
        fun getMyMessage(mail : String, map : ArrayList<Messages>) : ArrayList<Messages>{
            var unRead = ArrayList<Messages>()

            map.forEach {
                if (it.isForEveryBody || it.equals(mail)){
                    unRead.add(it)
                }
            }

            return unRead
        }
    }
}