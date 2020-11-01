package com.zedevstuds.jsonfakepics.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.Toast

// Устанаавливает видимость элементов состояния загрузки
fun setProgressViews(status: LoadingStatus, progressBar: View, textView: View) {
    when(status) {
        LoadingStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
            textView.visibility = View.GONE
        }
        LoadingStatus.DONE -> {
            progressBar.visibility = View.GONE
            textView.visibility = View.GONE
        }
        LoadingStatus.ERROR -> {
            progressBar.visibility = View.GONE
            textView.visibility = View.VISIBLE
        }
    }
}

// Проверяет, доступно ли сетевое подключение
fun isNetworkAvailable(context: Context): Boolean {
    val conManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = conManager.activeNetwork ?: return false
        val actNetwork = conManager.getNetworkCapabilities(network) ?: return false
        return when {
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> return false
        }
    } else {
        val networkInfo = conManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected

    }
}

// Показывает Toast
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}