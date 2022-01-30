package com.service.microjc.Activity.App.Utils;

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File


/**
 * apk下载工具类
 */
class DownloadUtils(private val context: Context) {
    //下载器
    private var downloadManager: DownloadManager? = null

    //下载的ID
    private var downloadId: Long = 0
    private var filePath: String? = null
    private lateinit var mFileName: String
    private var isDownloading = false



    //下载apk
    fun downloadAPK(url: String, fileName: String,title: String): Boolean {
        if (isDownloading) {
            return false
        }
        mFileName = fileName
        //创建下载任务
        val request = DownloadManager.Request(Uri.parse(url))
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false)
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setTitle(title)
        request.setDescription("新版本下载中...")
        request.setVisibleInDownloadsUi(true)

        //设置下载的路径
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
        if (file.exists()) {
            file.delete()
        }
        request.setDestinationUri(Uri.fromFile(file))
        filePath = file.absolutePath
        //获取DownloadManager
        if (downloadManager == null) downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        if (downloadManager != null) {
            downloadId = downloadManager!!.enqueue(request)
        }
        isDownloading = true

        //注册广播接收者，监听下载状态
        context.registerReceiver(receiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        return true
    }

    //广播监听下载的各个状态
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            checkStatus(context)
        }
    }

    //检查下载状态
    private fun checkStatus(context: Context) {
        val query = DownloadManager.Query()
        //通过下载的id查找
        query.setFilterById(downloadId)
        val cursor = downloadManager!!.query(query)
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            when (status) {
                DownloadManager.STATUS_PAUSED -> {
                }
                DownloadManager.STATUS_PENDING -> {
                }
                DownloadManager.STATUS_RUNNING -> {
                }
                DownloadManager.STATUS_SUCCESSFUL -> {
                    Toast.makeText(this.context, "下载完成，可在通知栏中点击安装", Toast.LENGTH_SHORT).show()
                    //下载完成安装APK
                    installAPK()
                    cursor.close()
                    context.unregisterReceiver(receiver)
                    isDownloading = false
                }
                DownloadManager.STATUS_FAILED -> {
                    Toast.makeText(this.context, "下载失败", Toast.LENGTH_SHORT).show()
                    cursor.close()
                    context.unregisterReceiver(receiver)
                    isDownloading = false
                }
            }
        }
    }

    private fun installAPK() {
        val intent = Intent(Intent.ACTION_VIEW)
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //Android 7.0以上要使用FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val file = File(filePath)
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            val apkUri = FileProvider.getUriForFile(context, "com.service.microjc.fileprovider", file)
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(File(Environment.DIRECTORY_DOWNLOADS, mFileName)), "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }

    private fun getAppName(context: Context): String {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}
