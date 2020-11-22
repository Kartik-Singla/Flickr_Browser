package kartiksingla1999.example.flickrbrowser
// now this class only does downloading part
//any class can call this class for downloading purpose only
// whatever we want want to do with downloaded class we define it in the class which called this class (GetRawData)
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus{
    OK, IDLE, NOT_INITIALIZED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}
class GetRawData(private val listener:OnDownloadComplete) : AsyncTask<String,Void,String>(){ // Because MainActivity implements the OnDownloadComplete interface, it can be used anywhere, where an OnDownloadComplete object can be used.
    private val TAG = "GetRawData"
    private var downloadstatus = DownloadStatus.IDLE // initialized status to IDLE
    interface OnDownloadComplete{ // when onpostexecute calls onDownloadComplete it does not guarantee that mainactivity has OndownloadComplete is present in the MainActivity so we are implementing an interface so that it becomes compulsory to have that function in our class
        fun onDownloadComplete(data:String, status:DownloadStatus)
    }
//    private var listener:MainActivity? = null
//    fun setDownloadCompleteListener(callbackobject:MainActivity){
//        listener = callbackobject
//    }
    override fun doInBackground(vararg params: String?): String {
        if(params[0]==null) // if we write (params == null) it will aways be false
        {
            downloadstatus = DownloadStatus.NOT_INITIALIZED
            return "No URL specified"
        }
        try{
            downloadstatus=DownloadStatus.OK
            return URL(params[0]).readText()
        }
        catch(e:Exception){
            val errormessage = when(e){
                is MalformedURLException -> {
                    downloadstatus = DownloadStatus.NOT_INITIALIZED
                    "Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadstatus = DownloadStatus.FAILED_OR_EMPTY
                    "IO exception ${e.message}"
                }
                is SecurityException -> {
                    downloadstatus = DownloadStatus.PERMISSIONS_ERROR
                    "Security Error ${e.message}"
                }
                else ->{
                    downloadstatus = DownloadStatus.ERROR
                    "Unknown error ${e.message}"
                }

            }
            return errormessage
        }
    }
// in the top 10 downloader app we have binded the other actions such as parsing in onPostExecute function that is we have tightly coupled it but now we want to keep this class for downloading purpose only thats why we are using the concept of callback function
    override fun onPostExecute(result: String) {
        // super.onPostExecute(result) // it is empty thats why commenting it
        Log.d(TAG, "Onpostexecute called parameter is ${result}")
    listener.onDownloadComplete(result,DownloadStatus.OK)
    }
}