package kartiksingla1999.example.flickrbrowser


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception

class MainActivity : BaseActivity(), GetRawData.OnDownloadComplete,
    GetFlickrJsonData.OnDataAvaliable,
    RecyclerItemClickListener.OnRecyclerClickListener{
    private val TAG = "MainActivity"
    private val flickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList(),this)
    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(TAG, "OnCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activateToolbar(false)
        // in case of recycler view we have seperate layout manager
        // 'recycler_view' is the ID of the recycler view layout
        recycler_view.layoutManager = LinearLayoutManager(this) // Adapters are only responsible for creating and managing views for items (called ViewHolder), these classes do not decide how these views are arranged when displaying them. Instead, they rely on a separate class called LayoutManager.
        // we are initializing a layout manager for recycler view as in case of recycler view adapters we have to seperately link layout manager
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this,recycler_view,this)) // responsible for touch events
        recycler_view.adapter = flickrRecyclerViewAdapter

        /*
        ***************************************
        we can see in url below that tags is the first parameter after '?' we want to provide the functionality for the user to search for differenct tags

         */

        val url = createUri("https://www.flickr.com/services/feeds/photos_public.gne", "cars","en-us",true)
        val getrawdata = GetRawData(this) // constructor called
        getrawdata.execute(url)
       // we have moved this code to onResume because after onResume is called after onCreate
        //also when we search for keyword, after submitting the query onResume function of MainActivity is called it is redundant to write the code here



//        // we are just doing the same thing as clicking a button
//        //setDownloadCompleteListener is same as setOnClickListener and OnDownloadComplete is same as OnClick
//        //getrawdata.setDownloadCompleteListener(this) // we are just registering our activity as a listener here (same way we do it for pressing a button)
//        // this refers to current instance of a class
//        //private val this = MainActivity() (Kotlin is automatically doing just like this (not doing in actual))
//        getrawdata.execute("https://www.flickr.com/services/feeds/photos_public.gne?tags=android&format=json&nojsoncallback=1")
        Log.d(TAG, "Oncreate ends")
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_LONG).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Toast.makeText(this, "Long tap at position $position", Toast.LENGTH_LONG).show()
        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        if(photo!=null){
            val intent = Intent(this, PhotoDetails()::class.java)
            intent.putExtra(PHOTO_TRANSFER,photo) //putExtra is used to transfer data from one activity to another using key value combination
            startActivity(intent)
        }
    }

    private fun createUri(baseUrl:String, searchcriteria:String, lang:String, matchall:Boolean):String{
        Log.d(TAG, "createUri starts")
        var uri = Uri.parse(baseUrl) // this is the base url (in our case it is "https://www.flickr.com/services/feeds/photos_public.gne"
        var builder = uri.buildUpon() // we are initialising a builder to build the various parameters
        builder = builder.appendQueryParameter("tags",searchcriteria)
        builder = builder.appendQueryParameter("tagmode",if(matchall) "ALL" else "ANY")
        builder = builder.appendQueryParameter("lang",lang)
        builder = builder.appendQueryParameter("format","json")
        builder = builder.appendQueryParameter("nojsoncallback","1")
        uri = builder.build() // we have finally built the whole URL
        return uri.toString()
        // instead of doing what we have done above we could have also performed chaining
    }


        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            // Inflate the menu; this adds items to the action bar if it is present.
            Log.d(TAG, "OnCreateOptionsMenu called")
            menuInflater.inflate(R.menu.menu_main, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            Log.d(TAG, "OnOptionsItemSelected called")
            return when (item.itemId) {
                R.id.action_search -> {
                    startActivity(Intent(this,SearchActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }


    override fun onDownloadComplete(data: String, status: DownloadStatus) { // it is a callback function
        if(status==DownloadStatus.OK)
        {
            Log.d(TAG,"OnDownloadComplete called : Data is $data")
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        }
        else
        {
            Log.d(TAG,"OnDownloadComplete called : Error is $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, "onDataAvailabe called, data is $data")
        flickrRecyclerViewAdapter.loadNewData(data)

    }

    override fun onError(exception: Exception) { // if any exception would have been caught by GetFlickrJsonData class then onDataAvailabe would not be called instead OnError will be called
        Log.d(TAG, "onError called, exception is $exception")
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult = sharedPref.getString(FLICKR_QUERY,"")

        if (queryResult != null) {
            if(queryResult.isNotEmpty()){
                val url = createUri("https://www.flickr.com/services/feeds/photos_public.gne", queryResult,"en-us",true)
                val getrawdata = GetRawData(this) // constructor called
                getrawdata.execute(url)

            }
        }
    }
}

