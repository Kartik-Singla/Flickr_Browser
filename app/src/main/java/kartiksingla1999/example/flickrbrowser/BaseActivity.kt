package kartiksingla1999.example.flickrbrowser

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

//
//import androidx.appcompat.app.AppCompatActivity
internal const val FLICKR_QUERY = "FLICKR_QUERY"
internal const val PHOTO_TRANSFER = "PHOTO_TRANSFER"
open class BaseActivity : AppCompatActivity() {
    private val TAG = "BaseActivity"
    internal fun activateToolbar(enablehome:Boolean){ // all this function does is inflate the toolbar from activity_main.xml
        Log.d(TAG,".activateToolbar")
        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enablehome) // it is used to setup the home button according to the paremeter passed in enablehome variable
    }

}
