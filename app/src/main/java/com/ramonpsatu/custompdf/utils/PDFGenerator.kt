package com.ramonpsatu.custompdf.utils

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build.VERSION
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ramonpsatu.custompdf.view.fragment.CustomDialogFragment
import com.ramonpsatu.custompdf.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Calendar
import java.util.TimeZone


/**
 * Custom PDF.
 *
 * @property pageHeight represents the height of the page.
 * @property pageWidth represents the width of the page.
 * @property gridPageNumber represents the number of pages in the PDF.
 * @property pdfDocument starts an instance of the PDFDocument
 * class responsible for giving us access to the page creation methods.
 * @property pageInfo creates an instance of the class that assigns configuration values to the page.
 * @property paint Create a new paint with default settings.Used to draw texts and the icon.
 * @property line Create a new paint with default settings. Used to draw the lines.
 * @property scope defines a scope for new coroutines.
 * @author Ramon Satu
 */
class PDFGenerator {

    private val pageHeight = 842
    private val pageWidth = 595
    private var gridPageNumber = 0
    private var pdfDocument: PdfDocument? = PdfDocument()
    private var paint: Paint? = Paint()
    private var line: Paint? = Paint()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var pageInfo: PdfDocument.PageInfo

    init {
        paint?.flags = Paint.ANTI_ALIAS_FLAG
        line?.flags = Paint.ANTI_ALIAS_FLAG
        line?.strokeWidth = 2f
    }

    /**
     * @param size represents the number of rows in the grid.
     */
    fun printOutBlankSubjectGrid(size: String, context: Context, activity: AppCompatActivity) {
        scope.launch {
            if (size.isNotEmpty() && size != "0" && size != "00") {
                generatePDFWeekSubjects(context, activity, size.toInt(), paint!!,line!!)

            } else {

                toastMessageShort(
                    context,
                    context.getString(R.string.text_warning_field_cannot_null)
                )

            }
        }

    }

    /**
     * Contains the logical structure for creating PDF pages.
     * @param subjectListSize represents the number of rows in the grid.
     */
    private suspend fun generatePDFWeekSubjects(
        context: Context,
        activity: AppCompatActivity,
        subjectListSize: Int, paint: Paint,line:Paint
    ) {

        val bitmapLogo = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.ic_small_size_logo_app_40x40
        )
        val bitmapLogoScaled = Bitmap.createScaledBitmap(bitmapLogo, 64, 64, true)


        var indexOnThePage = 1
        var indexMin = 0
        var indexMax = 5

        var subjectListSizeAid = subjectListSize
        val tosPhrase = context.getString(R.string.text_tos_phrase)
        val subjectsWord = context.getString(R.string.text_subjects_word)
        val amountOfPhrase = context.getString(R.string.text_amount_of_subjects)
        val dateFormat = DateFormat.getLongDateFormat(context)
        val date = dateFormat.format(Calendar.getInstance(TimeZone.getDefault()).time)

        gridPageNumber = (subjectListSize / 6) + 1

        //create page information
        pageInfo = PdfDocument.PageInfo.Builder(pageHeight, pageWidth, gridPageNumber).create()



        for (indexPageNumber in 0 until gridPageNumber step 1) {

            //set page information
            val myPage: PdfDocument.Page = pdfDocument!!.startPage(pageInfo)
            // creating a variable for canvas
            val canvas = myPage.canvas


            //--------page-01-Header------------
            if (indexPageNumber == 0) {

                canvas.drawBitmap(bitmapLogoScaled, 25f, 40f, paint)

                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textSize = 18F
                paint.color =
                    ContextCompat.getColor(context, R.color.black)
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText(tosPhrase, 104f, 60f, paint)

                paint.textSize = 14F
                canvas.drawText(date, 104f, 85f, paint)
                paint.textAlign = Paint.Align.RIGHT

                canvas.drawText(amountOfPhrase, 783f, 145f, paint)

                paint.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                paint.textSize = 24F
                paint.color =
                    ContextCompat.getColor(context, R.color.black)
                paint.textAlign = Paint.Align.CENTER
                canvas.drawText(subjectsWord, 421f, 120f, paint)

            }

            //-------------Table----------------
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.textAlign = Paint.Align.CENTER
            paint.color =
                ContextCompat.getColor(context, R.color.black)

            var constY = 0
            var constYTitle = 0
            for (index in 0 until subjectListSize step 1) {

                if (index == 0) {
                    constY = 0
                    constYTitle = 0

                } else {
                    constY += 80
                    constYTitle += 80

                }


                //First page
                if ((indexPageNumber == 0) && (index < 5)) {
                    subjectListSizeAid -= 1


                    //Draws the lines from left to right of the table
                    canvas.drawLine(91f, 260f + constY, 817f, 260f + constY, line)
                    //Draws the lines from top to bottom of the table
                    canvas.drawLine(26f, 180f + constY, 26f, 260f + constY, line)
                    canvas.drawLine(91f, 180f + constY, 91f, 260f + constY, line)
                    canvas.drawLine(196f, 180f + constY, 196f, 260f + constY, line)
                    canvas.drawLine(303f, 180f + constY, 303f, 260f + constY, line)
                    canvas.drawLine(410f, 180f + constY, 410f, 260f + constY, line)
                    canvas.drawLine(516f, 180f + constY, 516f, 260f + constY, line)
                    canvas.drawLine(623f, 180f + constY, 623f, 260f + constY, line)
                    canvas.drawLine(719f, 180f + constY, 719f, 260f + constY, line)
                    canvas.drawLine(816f, 180f + constY, 816f, 260f + constY, line)


                    //Draw table header
                    if (index == 0) {

                        paint.textSize = 16F
                        canvas.drawText(
                            context.getString(R.string.text_period_word),
                            58f,
                            175f,
                            paint
                        )
                        canvas.drawText(
                            context.getString(R.string.text_day_monday_form),
                            142f,
                            175f,
                            paint
                        )
                        canvas.drawText(
                            context.getString(R.string.text_day_tuesday_form),
                            249f,
                            175f,
                            paint
                        )
                        canvas.drawText(
                            context.getString(R.string.text_day_wednesday_form),
                            355f,
                            175f,
                            paint
                        )
                        canvas.drawText(
                            context.getString(R.string.text_day_thursday_form),
                            463f,
                            175f,
                            paint
                        )
                        canvas.drawText(
                            context.getString(R.string.text_day_friday_form),
                            569f,
                            175f,
                            paint
                        )
                        canvas.drawText(
                            context.getString(R.string.text_day_saturday_form),
                            671f,
                            175f,
                            paint
                        )
                        canvas.drawText(
                            context.getString(R.string.text_day_sunday_form),
                            768f,
                            175f,
                            paint
                        )
                        canvas.drawLine(25f, 150f, 817f, 150f, line)
                        canvas.drawLine(25f, 180f, 817f, 180f, line)

                        //Draws the lines from top to bottom of the table
                        canvas.drawLine(26f, 150f, 26f, 180f, line)
                        canvas.drawLine(91f, 150f, 91f, 180f, line)
                        canvas.drawLine(196f, 150f, 196f, 180f, line)
                        canvas.drawLine(303f, 150f, 303f, 180f, line)
                        canvas.drawLine(410f, 150f, 410f, 180f, line)
                        canvas.drawLine(516f, 150f, 516f, 180f, line)
                        canvas.drawLine(623f, 150f, 623f, 180f, line)
                        canvas.drawLine(719f, 150f, 719f, 180f, line)
                        canvas.drawLine(816f, 150f, 816f, 180f, line)

                        //Draw page number
                        paint.textSize = 12F
                        canvas.drawText((indexPageNumber + 1).toString(), 802f, 38f, paint)
                    }

                    if (subjectListSizeAid == 0 || (subjectListSizeAid > 0 && index == 4)) {
                        canvas.drawLine(25f, 260f + constY, 91f, 260f + constY, line)
                    }


                    if (index == 4) {
                        indexMin = index + 1
                        indexMax += index + 1
                    }

                }
                //Other pages, 2...3..100...
                if ((indexPageNumber == indexOnThePage) && (index in indexMin..indexMax)) {

                    subjectListSizeAid -= 1

                    if (index == indexMin) {
                        constY = 0

                        // The First top row of cells
                        canvas.drawLine(25f, 60f + constY, 817f, 60f + constY, line)

                        //Draw page number
                        paint.textSize = 12F
                        canvas.drawText((indexPageNumber + 1).toString(), 802f, 38f, paint)

                    }

                    // Draws the lines from left to right of the table
                    canvas.drawLine(91f, 140f + constY, 817f, 140f + constY, line)
                    //Draws the lines from top to bottom of the table
                    canvas.drawLine(26f, 60f + constY, 26f, 140f + constY, line)
                    canvas.drawLine(91f, 60f + constY, 91f, 140f + constY, line)
                    canvas.drawLine(196f, 60f + constY, 196f, 140f + constY, line)
                    canvas.drawLine(303f, 60f + constY, 303f, 140f + constY, line)
                    canvas.drawLine(410f, 60f + constY, 410f, 140f + constY, line)
                    canvas.drawLine(516f, 60f + constY, 516f, 140f + constY, line)
                    canvas.drawLine(623f, 60f + constY, 623f, 140f + constY, line)
                    canvas.drawLine(719f, 60f + constY, 719f, 140f + constY, line)
                    canvas.drawLine(816f, 60f + constY, 816f, 140f + constY, line)


                    //Bottom row of period cell when page list size is less than 6
                    if (subjectListSizeAid == 0) {
                        canvas.drawLine(25f, 140f + constY, 91f, 140f + constY, line)
                    }

                    //Bottom row of period cell when page list size equals than 6
                    if (subjectListSizeAid == 0 || (subjectListSizeAid > 0 && index == indexMax)) {
                        canvas.drawLine(25f, 140f + constY, 91f, 140f + constY, line)
                    }

                    if (index == (indexMax)) {
                        indexMin = index + 1
                        indexMax = indexMin + 5
                        indexOnThePage += 1
                    }

                }


            }

            pdfDocument!!.finishPage(myPage)


        }

        val pdfFileName = context.getString(R.string.tos_blank_subject_grid)


        val filePDF = File(getPathFile("/TOS-PDFs", context), pdfFileName)

        try {
            withContext(Dispatchers.IO) {
                pdfDocument!!.writeTo(FileOutputStream(filePDF))

            }

            toastMessageShort(
                context,
                context.getString(R.string.text_warning_pdf_generated_successfully)
            )


        } catch (ex: IOException) {

            ex.printStackTrace()

        }

        pdfDocument!!.close()
        pdfDocument = null
        this.paint = null
        this.line = null


        insertFileInMediaStore(filePDF, pdfFileName, context)

        goToFileDirectory(activity)


    }

    /**
     * Returns the path where the file will be created.
     */
    @Suppress("SameParameterValue")
    private fun getPathFile(folderName: String, context: Context): File {

        var path = File(Environment.getExternalStorageDirectory(), folderName)


        if (VERSION.SDK_INT in 26..28) {


            path = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                folderName
            )
            if (!path.exists()) {
                path.mkdirs()
            }

        }

        if (VERSION.SDK_INT >= 29) {

            path = File(context.getExternalFilesDir(folderName)!!.absolutePath)

            if (!path.exists()) {
                path.mkdirs()
            }
        }

        return path
    }

    /**
     * Redirects the user to the Download folder.
     */
    @SuppressLint("QueryPermissionsNeeded")
    private fun goToFileDirectory(activity: AppCompatActivity) {

        val intentPdf = Intent()

        intentPdf.action = DownloadManager.ACTION_VIEW_DOWNLOADS
        val activityExists =
            intentPdf.resolveActivityInfo(
                activity.packageManager,
                0
            ) != null

        if (activityExists) {
            CustomDialogFragment(Intent.createChooser(intentPdf, "Open Folder"), true).show(
                activity.supportFragmentManager,
                CustomDialogFragment.TAG
            )
            scope.cancel()
        } else {
            CustomDialogFragment(Intent.createChooser(intentPdf, "Open Folder"), false).show(
                activity.supportFragmentManager,
                CustomDialogFragment.TAG
            )
            scope.cancel()
        }


    }

    /**
     * Insert the PDF into the MediaStore.
     */
    private suspend fun insertFileInMediaStore(filePDF: File, nameFile: String, context: Context) {

        if (
            VERSION.SDK_INT >= 29
        ) {

            val contentResolver: ContentResolver = context.contentResolver
            val contentValues = ContentValues()
            val mimeType = "application/pdf"


            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, nameFile)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            contentValues.put(MediaStore.MediaColumns.DATA, System.currentTimeMillis() / 1000)
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS
            )

            val uri =
                contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

            if (uri != null) {

                withContext(Dispatchers.IO) {

                    try {
                        val outputStream: OutputStream = contentResolver.openOutputStream(uri)!!
                        val inputStream: InputStream = FileInputStream(filePDF)

                        val buffer = ByteArray(1024)
                        var bytesRead: Int

                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }

                        outputStream.flush()
                        inputStream.close()
                        outputStream.close()

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 0)

                    if (
                        VERSION.SDK_INT >= 30
                    ) {
                        contentResolver.update(uri, contentValues, null, null)
                    }
                }


            }


        }
    }

}