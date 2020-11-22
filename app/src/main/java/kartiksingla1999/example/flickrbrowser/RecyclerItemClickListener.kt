package kartiksingla1999.example.flickrbrowser

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
//we use SimpleOnItemTouchListener instead of OnItemTouchListener because we dont want to implement all members of OnItemTouchListener
class RecyclerItemClickListener(context:Context, recyclerView:RecyclerView, private val listener:OnRecyclerClickListener) : RecyclerView.SimpleOnItemTouchListener(){
    private val TAG = "RecyclerItemClickListen"
    interface OnRecyclerClickListener {
        fun onItemClick(view: View, position: Int) // for normal tap
        fun  onItemLongClick(view: View, position: Int) // for long tap
    }
    private val gestureDetector = GestureDetectorCompat(context, object:GestureDetector.SimpleOnGestureListener(){ // using anonymous inner class (anonymous inner classes are not only for implementing an interface it can also be used for classes too)
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            Log.d(TAG, ".onSingleTapUp starts")
            val childView = recyclerView.findChildViewUnder(e.x,e.y) //it sees which view was under the x,y coordinates of the tap and returns it
            if (childView != null) {
                listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView)) // calling the callback function
            }
            return true
        }

        override fun onLongPress(e: MotionEvent){
            Log.d(TAG, ".onLongPress starts")
            val childView = recyclerView.findChildViewUnder(e.x,e.y)
            if (childView != null) {
                listener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))
            }
            super.onLongPress(e)
        }

    })
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean { // this function will be called whenever any sort of touch happened whether it is a touch, swipe etc
        Log.d(TAG,"onInterceptTouchEvent called $e")
        //below is kind of gateway to other gestures
        val result = gestureDetector.onTouchEvent(e) // if it handles the event it should return true or else return false so something else can handle it
        Log.d(TAG,"onInterceptTouchEvent returning $result")
        return super.onInterceptTouchEvent(rv, e)
    }

}