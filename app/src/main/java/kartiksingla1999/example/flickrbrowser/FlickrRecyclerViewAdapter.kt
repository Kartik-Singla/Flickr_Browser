package kartiksingla1999.example.flickrbrowser

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso  // Image downloading, transformation, and caching manager.


// the adapter takes the data from the data source and packages the data in ViewHolder object
//the ViewHolders are sent to RecyclerView whenever it requests more data
class FlickrImageViewHolder(view: View):RecyclerView.ViewHolder(view){ // if we use list view we have no compulsion to use viewholder but in case of recyclerview adapter we have compulsion to have a viewholder
    var thumbnail:ImageView = view.findViewById(R.id.thumbnail)
    var title:TextView = view.findViewById(R.id.title)
}



/*
 data and view to display to display are providede by recycler adapter
 the layout is performed by layout manager
 the view lives in view holder
 everything has its own responsibilities so recycler view is more flexible than listview

 */



class FlickrRecyclerViewAdapter(private var photoList:List<Photo>,private val context:Context):RecyclerView.Adapter<FlickrImageViewHolder>() { // mainactivity is going to give us the list of photos
    private val TAG="FlickRecyclerViewAdapt" // we can use only 23 characters max for naming a tag
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder { // we will inflate browse.xml layout here
        // called by layout manager when it needs a new view
        Log.d(TAG, ".OnCreateViewHolder new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse,parent,false) // false means not to attach the inflated view to its parent
        return FlickrImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d(TAG,".getItemCount called")
        return if(photoList.isNotEmpty()) photoList.size else 0
    }
    fun loadNewData(newPhoto:List<Photo>)
    {
        photoList = newPhoto
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int): Photo? {
        return if (photoList.isNotEmpty()) photoList[position] else null
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) { // it is called when new data is to be stored to display it
        // called by the layout manager when it wants new data in existing view
        val photoitem = photoList[position] // retreiving the photo object which is to be displayed next
        Log.d(TAG,"onBindViewHoler called ${photoitem.title} --> $position")
        Picasso.with(holder.thumbnail.context) // we can have only one picasso object in our app // picassso needs a context as an arguement to with we could have passed context to the constructor of FlickrRecyclerViewAdapter but this just a different way to do it
                // all views exist in a context and all the views have context property to get the context
            .load(photoitem.image)// 'load' loads the image from URL and it is stored in image field of Photo class
            .error(R.drawable.placeholder) // if there's an error than placeholder image will be used
            .placeholder(R.drawable.placeholder) // when image is being downloaded this placeholder will take its place
            .into(holder.thumbnail)

        holder.title.text = photoitem.title

    }
}