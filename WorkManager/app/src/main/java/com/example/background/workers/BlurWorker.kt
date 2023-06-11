package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI

private const val TAG = "BlurWorker"

class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        // context needed for bitmap manipulations
        val appContext : Context = applicationContext
        // get the Uri we passed in from the Data object
        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        // display status notification:
        makeStatusNotification("blurring image", appContext)
        // slow down the work, in order to observe when the different Workers start
        sleep()

        try {
            if(TextUtils.isEmpty(resourceUri)){
                Log.e(TAG, "Invalid input URI")
                throw IllegalArgumentException("Invalid input URI")
            }
            val picture = BitmapFactory.decodeStream(appContext.contentResolver.openInputStream(Uri.parse(resourceUri)))

            // blurred image/result:
            val blurred = blurBitmap(picture, appContext)

            // save the returned URI to a local variable (write bitmap to a temporary file)
            val blurredUri = writeBitmapToFile(appContext, blurred)

            val outputData = workDataOf(KEY_IMAGE_URI to blurredUri.toString())
            return Result.success(outputData)

        } catch (e: Throwable) {
            Log.e(TAG, "Error applying blur")
            e.printStackTrace()
            return Result.failure()
        }
    }
}