package kartiksingla1999.example.flickrbrowser

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.Intents.Insert.ACTION
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_photo_details.*
import kotlinx.android.synthetic.main.browse.*
import kotlinx.android.synthetic.main.content_photo_details.*

class PhotoDetails() : BaseActivity() {

    private val TAG = "PhotoDetails"

    private lateinit var ges:GestureDetectorCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)
        activateToolbar(true)

        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo

//        photo_title.text = photo.title
        // resources is the property of the activity and lets us access the app's resources
        photo_title.text = resources.getString(R.string.photo_title_text,photo.title) //extracting from strings.xml // photo.title replaces the placeholers
        photo_tags.text = photo.tags
        photo_author.text = photo.author
        Picasso.with(this) // we can have only one picasso object in our app // picassso needs a context as an arguement to with we could have passed context to the constructor of FlickrRecyclerViewAdapter but this just a different way to do it
            // all views exist in a context and all the views have context property to get the context
            .load(photo.link)// 'load' loads the image from URL and it is stored in image field of Photo class
            .error(R.drawable.placeholder) // if there's an error than placeholder image will be used
            .placeholder(R.drawable.placeholder) // when image is being downloaded this placeholder will take its place
            .into(photo_image)


        c_view.setOnTouchListener(object:View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                Log.d(TAG,"clicked")
                if(event.action == MotionEvent.ACTION_UP)
                    finish() // when we lift up the finger it goes to parent screen
                return true
            }
        })
    }





}
