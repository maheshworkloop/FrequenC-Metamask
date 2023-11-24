package com.dev.frequenc.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.jetbrains.annotations.Nullable
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.nio.ByteBuffer


object ImageUtil {
    @Throws(IllegalArgumentException::class)
    fun convert(base64Str: String): Bitmap {
        val decodedBytes = Base64.decode(
            base64Str.substring(base64Str.indexOf(",") + 1),
            Base64.DEFAULT
        )
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun convert(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        val  byteArray = byteArrayOutputStream .toByteArray()
        val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
        return encoded
    }

    fun convertByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        val  byteArray = byteArrayOutputStream .toByteArray()
//        val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
        return byteArray
    }

    fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        // Calculate the number of bytes needed to store the bitmap
        val size = bitmap.rowBytes * bitmap.height

        // Create a ByteBuffer with the calculated size
        val buffer = ByteBuffer.allocateDirect(size)

        // Copy the bitmap's bytes into the ByteBuffer
        bitmap.copyPixelsToBuffer(buffer)

        // Reset the position of the ByteBuffer to the beginning
        buffer.rewind()

        return buffer
    }


    fun rotateBitmap(src: String, bitmap: Bitmap): Bitmap? {
        try {
            val orientation = getExifOrientation(src)
            if (orientation == 1) {
                return bitmap
            }
            val matrix = Matrix()
            when (orientation) {
                2 -> matrix.setScale(-1f, 1f)
                3 -> matrix.setRotate(180f)
                4 -> {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }
                5 -> {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }
                6 -> matrix.setRotate(90f)
                7 -> {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }
                8 -> matrix.setRotate(-90f)
                else -> return bitmap
            }
            return try {
                val oriented = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )
                bitmap.recycle()
                oriented
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    @Throws(IOException::class)
    private fun getExifOrientation(src: String): Int {
        var orientation = 1
        try {
            /**
             * if your are targeting only api level >= 5
             * ExifInterface exif = new ExifInterface(src);
             * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                val exifClass =
                    Class.forName("android.media.ExifInterface")
                val exifConstructor = exifClass.getConstructor(
                    *arrayOf<Class<*>>(
                        String::class.java
                    )
                )
                val exifInstance =
                    exifConstructor.newInstance(*arrayOf<Any>(src))
                val getAttributeInt = exifClass.getMethod(
                    "getAttributeInt", *arrayOf<Class<*>?>(
                        String::class.java,
                        Int::class.javaPrimitiveType
                    )
                )
                val tagOrientationField =
                    exifClass.getField("TAG_ORIENTATION")
                val tagOrientation = tagOrientationField[null] as String
                orientation = getAttributeInt.invoke(
                    exifInstance,
                    *arrayOf<Any>(tagOrientation, 1)
                ) as Int
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: java.lang.IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        return orientation
    }


    fun loadImage(imageView: ImageView, url: String?) {
        try {
            Glide.with(imageView.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        @Nullable e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false // Return false so the error placeholder will be used
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        imageView.setImageDrawable(resource)
                        return true
                    }
                })
                .into(imageView)
        }
        catch (e: Exception) { e.printStackTrace() }
    }
}