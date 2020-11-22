package kartiksingla1999.example.flickrbrowser

import android.util.Log
import java.io.BufferedInputStream
import java.io.IOException
import java.io.ObjectStreamException
import java.io.Serializable

// This class will only contain the photos

// Serialization is convertin the object to bits and bytes
class Photo(var title:String, var author:String, var authorid:String,var link:String, var tags:String, var image:String)
    :Serializable{ // there are the properties which we will get from json data
    companion object{
        private const val serialVersionUID = 1L // it is used to make sure that the data it is retrieving is same as the data which is stored
    }

//        override fun toString(): String {
//        return "Photo(title='$title', author='$author', authorid='$authorid',link = '$link' tags='$tags', image='$image')"
//    }


    @Throws(IOException::class)
    private fun writeObject(out: java.io.ObjectOutputStream){ // we are making an output stream of data
        Log.d("Photo", "writeObject called")
        out.writeUTF(title)
        out.writeUTF(author)
        out.writeUTF(authorid)
        out.writeUTF(link)
        out.writeUTF(tags)
        out.writeUTF(image)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(inStream: java.io.ObjectInputStream){ // we are assigning the values from stream to our properties
        Log.d("Photo", "readObject called")
        title = inStream.readUTF()
        author = inStream.readUTF()
        authorid = inStream.readUTF()
        link = inStream.readUTF()
        tags = inStream.readUTF()
        image = inStream.readUTF()

    }



}