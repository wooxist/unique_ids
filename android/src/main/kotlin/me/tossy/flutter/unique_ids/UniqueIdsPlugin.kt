package me.tossy.flutter.unique_ids

import android.content.Context
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.io.IOException
import java.util.*
import android.os.*

class UniqueIdsPlugin(private val registrar: Registrar) : MethodCallHandler {

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "unique_ids")
            channel.setMethodCallHandler(UniqueIdsPlugin(registrar))
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when {
            call.method == "adId" -> {
                val backgroundThread = object : Thread("adId") {
                    override fun run() {
                        var id: String = "";

                        try {
                            id = getAdId()
                        } catch (e: IOException) {
                            // Unrecoverable error connecting to Google Play services (e.g.,
                            // the old version of the service doesn't support getting AdvertisingId).
                        } catch (e: GooglePlayServicesNotAvailableException) {
                            // Google Play services is not available entirely.
                        }
                        success(result, id)
                    }
                }
                backgroundThread.start()
            }

            call.method == "uuid" -> result.success(UUID.randomUUID().toString())
            else -> result.notImplemented()
        }
    }

    fun getAdId(): String {
        val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(registrar.context())
        val isLimitAdTrackingEnabled = adInfo.isLimitAdTrackingEnabled
        // if (isLimitAdTrackingEnabled) {
        return adInfo.id
        // }
        return ""
    }

    fun success(result: Result, id: String) {
        Handler(Looper.getMainLooper()).post {
            result.success(id)
        }
    }
}
