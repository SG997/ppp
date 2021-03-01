package datamodel

import android.net.Uri

data class ImageAndPath(var banner : Uri,
                        var icon : Uri,
                        var products : ArrayList<Uri> = ArrayList()){
}