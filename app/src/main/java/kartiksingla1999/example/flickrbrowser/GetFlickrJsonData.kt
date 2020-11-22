package kartiksingla1999.example.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class GetFlickrJsonData(private val listener:OnDataAvaliable):AsyncTask<String, Void, ArrayList<Photo>>() {
    private val TAG = "GetFlickrJsonData"
    override fun doInBackground(vararg params: String): ArrayList<Photo> {
        Log.d(TAG,"doInBackground starts")
        val photolist = ArrayList<Photo>()
        try{
            val jsondata=JSONObject(params[0]) // made an object of inbuilt class JSONObject to parse json data
            val itemsarray = jsondata.getJSONArray("items") // items is the array in json formatted code which we downloaded in GetRawData class
            for (i in 0 until itemsarray.length()){
                val jsonphoto = itemsarray.getJSONObject(i) // it is a single row of the items array
                val title = jsonphoto.getString("title")
                val author = jsonphoto.getString("author")
                val author_id = jsonphoto.getString("author_id")
                val tags = jsonphoto.getString("tags")
                val jsonmedia = jsonphoto.getJSONObject("media") // m is inside media (check json code) thats why we are first retreiving media
                val photourl = jsonmedia.getString("m") // it will give the url of photo to show in initial list
                val link = photourl.replaceFirst("_m.jpg", "_b.jpg") // it will provide the url of full size picture'
                //we have converted _m.jpeg to _b.jpeg because _b.jpeg gives the url of larger image
                val photoobject = Photo(title,author,author_id,link,tags,photourl)
                photoobject.title = title
                photoobject.author = author
                photoobject.authorid = author_id
                photoobject.tags = tags
                photoobject.link = link
                photoobject.image = photourl
                photolist.add(photoobject)
                Log.d(TAG, "doInbackground $photoobject")
            }
        }
        catch(e:JSONException){
            e.printStackTrace()
            Log.e(TAG, "doinbackground: Error prodessing json data ${e.message}")
            //if we got an error then also onPostExecute function will be called, we dont want that
            //so we are setting cancel(true) to interrupt the task of parsing
            cancel(true)
            listener.onError(e)// as we said that we will call this function whenever we got exception
        }
        Log.d(TAG,"doinbackground ends")
        return photolist // it is sent to onpostexecute
    }
    /*
    we cant use try catch block for catching the exception that is caused on the other thread (watch Lecture 139)
    thats why we are using the onError method
     */
    interface OnDataAvaliable {
        fun onDataAvailable(data: List<Photo>)
        fun onError(exception :Exception) // if our program had some error while parsing the json data its going to call onError function
    }

    override fun onPostExecute(result: ArrayList<Photo>) {
        Log.d(TAG,"doPostExecute starts")
        super.onPostExecute(result)
        listener.onDataAvailable(result)
        Log.d(TAG,"onpostexecute ends")
    }
}