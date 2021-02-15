package com.os.operando.advertisingid

import com.google.android.gms.ads.identifier.AdvertisingIdClient
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.PluginRegistry.Registrar
import kotlin.concurrent.thread

class AdvertisingIdPlugin(private val registrar: Registrar) : MethodCallHandler {

    val mResult: Result

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "advertising_id")
            channel.setMethodCallHandler(AdvertisingIdPlugin(registrar))
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        mResult = result
        when (call.method) {
            "getAdvertisingId" -> thread {
                try {
                    val id = AdvertisingIdClient.getAdvertisingIdInfo(registrar.context()).id
                    registrar.activity().runOnUiThread {
                        mResult?.success(id)
                        mResult = null
                    }
                } catch (e: Exception) {
                    registrar.activity().runOnUiThread {
                        mResult?.error(e.javaClass.canonicalName, e.localizedMessage, null)
                        mResult = null
                    }
                }
            }
            "isLimitAdTrackingEnabled" -> thread {
                try {
                    val isLimitAdTrackingEnabled = AdvertisingIdClient.getAdvertisingIdInfo(registrar.context()).isLimitAdTrackingEnabled
                    registrar.activity().runOnUiThread {
                        mResult?.success(isLimitAdTrackingEnabled)
                        mResult = null
                    }
                } catch (e: Exception) {
                    registrar.activity().runOnUiThread {
                        mResult?.error(e.javaClass.canonicalName, e.localizedMessage, null)
                        mResult = null
                    }
                }
            }
            else -> {
                mResult?.notImplemented()
                mResult = null
            }
        }
    }
}
