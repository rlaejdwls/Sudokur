package kr.co.treegames.core.manage

import android.content.Context
import android.os.Build
import kr.co.treegames.core.manage.Logger.printStackTrace
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Created by Hwang on 2018-09-04.
 *
 * Description :
 */
//class ExceptionHandler(val context: Context): Thread.UncaughtExceptionHandler {
//    private val LINE_SEPARATOR = "\n"
//
//    init {
//        Thread.setDefaultUncaughtExceptionHandler(this)
//    }
//    override fun uncaughtException(t: Thread?, e: Throwable?) {
//        val stackTrace = StringWriter()
//        e?.printStackTrace(PrintWriter(stackTrace))
//        val errorContent = StringBuilder()
//        errorContent.append("************ CAUSE OF ERROR ************")
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append(stackTrace.toString())
//        errorContent.append("************ CAUSE OF ERROR ************")
//        errorContent.append(LINE_SEPARATOR)
//        val causeOfError = StringBuilder()
//        causeOfError.append(stackTrace.toString())
//
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append("************ DEVICE INFORMATION ***********")
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append("Brand: ")
//        errorContent.append(Build.BRAND)
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append("Device: ")
//        errorContent.append(Build.DEVICE)
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append("Model: ")
//        errorContent.append(Build.MODEL)
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append("Id: ")
//        errorContent.append(Build.ID)
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append("Product: ")
//        errorContent.append(Build.PRODUCT)
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append("************ DEVICE INFORMATION ***********")
//        errorContent.append(LINE_SEPARATOR)
//        val deviceInformation = StringBuilder()
//        errorContent.append("Brand: ")
//        deviceInformation.append(Build.BRAND)
//        deviceInformation.append(LINE_SEPARATOR)
//        deviceInformation.append("Device: ")
//        deviceInformation.append(Build.DEVICE)
//        deviceInformation.append(LINE_SEPARATOR)
//        deviceInformation.append("Model: ")
//        deviceInformation.append(Build.MODEL)
//        deviceInformation.append(LINE_SEPARATOR)
//        deviceInformation.append("Id: ")
//        deviceInformation.append(Build.ID)
//        deviceInformation.append(LINE_SEPARATOR)
//        deviceInformation.append("Product: ")
//        deviceInformation.append(Build.PRODUCT)
//
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append("************ FIRMWARE OF ANDROID ************")
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append("SDK: ")
//        errorContent.append(Build.VERSION.SDK_INT)
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append("Release: ")
//        errorContent.append(Build.VERSION.RELEASE)
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append("Incremental: ")
//        errorContent.append(Build.VERSION.INCREMENTAL)
//        errorContent.append(LINE_SEPARATOR)
//        errorContent.append("************ FIRMWARE OF ANDROID ************")
//        errorContent.append(LINE_SEPARATOR)
//        val firmwareOfAndroid = StringBuilder()
//        firmwareOfAndroid.append("SDK: ")
//        firmwareOfAndroid.append(Build.VERSION.SDK_INT)
//        firmwareOfAndroid.append(LINE_SEPARATOR)
//        firmwareOfAndroid.append("Release: ")
//        firmwareOfAndroid.append(Build.VERSION.RELEASE)
//        firmwareOfAndroid.append(LINE_SEPARATOR)
//        firmwareOfAndroid.append("Incremental: ")
//        firmwareOfAndroid.append(Build.VERSION.INCREMENTAL)
//
//        Logger.printStackTrace(errorContent.toString(), e)
////        notifyDialog(errorContent, causeOfError, deviceInformation, firmwareOfAndroid)
//    }
//}