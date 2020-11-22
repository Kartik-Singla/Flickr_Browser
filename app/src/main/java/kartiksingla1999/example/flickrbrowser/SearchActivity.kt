package kartiksingla1999.example.flickrbrowser

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {
    private val TAG = "SearchActivity"
    private var searchView : SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        activateToolbar(true)// we passed true because we want the home button to appear in search activity
        Log.d(TAG,"Oncreates ends of SearchActivity")


    }

    override fun onCreateOptionsMenu(menu: Menu ): Boolean {
        Log.d(TAG,".onCreateOptionsMenu: starts")
        menuInflater.inflate(R.menu.menu_search,menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager // search manager provides access to system search services and the way to get the search manager instance is to call getSystemServices
        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView // refer to video
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        searchView?.setSearchableInfo(searchableInfo)
//        Log.d(TAG,".onCreateOptionsMenu: $componentName")
//        Log.d(TAG,".onCreateOptionsMenu: hint is $")
//        Log.d(TAG,".onCreateOptionsMenu: starts")
        searchView?.isIconified = false
        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{ // when we submit the query the activity which has intent filter is launched and the activity which has intent filter is SearchActivity which is this activity so when we click search view, setOnQueryTextListener is launched
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG,"onQueryTextListener launched")
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext) //Now here we want to pass the application context rather than using this, because the date is going to
                //be retrieved by a different activity to the one that saved it. // Search activity will save the data and MainActivity will retrieve the data
                sharedPref.edit().putString(FLICKR_QUERY, query).apply() // Now we have to call the edit function on the next line to put the shared preferences into a writable state, and then we use the, we call the putString function to store the search query string.
                searchView?.clearFocus()
                finish() // it returns to the class which called this class (invokes onResume of MainActivity)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {// we dont need this function but since it is a part of interface so we have to implement this function so we are simply returning false
                return false
            }
        })
        searchView?.setOnCloseListener {
            finish()
            false
        }

        return true
    }

}
