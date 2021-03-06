package com.adroitandroid.near.discovery

import android.content.Context
import android.os.Looper
import com.adroitandroid.near.discovery.server.UdpServerService
import com.adroitandroid.near.model.Host

interface NearDiscovery {
    fun makeDiscoverable(hostName: String, mustMatch: String = "")
    fun makeNonDiscoverable()
    fun startDiscovery()
    fun stopDiscovery()
    val allAvailablePeers: Set<Host>
    val isDiscoverable: Boolean
    val isDiscovering: Boolean

    class Builder {
        var mDiscoverableTimeout: Long = 60000
        var mDiscoveryTimeout: Long = 60000
        var mDiscoverablePingInterval: Long = 5000
        lateinit var mListener: Listener
        lateinit var mLooper: Looper
        lateinit var mContext: Context
        var mPort: Int = UdpServerService.DISCOVERY_PORT
        var mRegex: Regex = Regex("^$")

        fun setDiscoverableTimeoutMillis(discoverableTimeout: Long): Builder {
            mDiscoverableTimeout = discoverableTimeout
            return this
        }

        fun setDiscoveryTimeoutMillis(discoveryTimeout: Long): Builder {
            mDiscoveryTimeout = discoveryTimeout
            return this
        }

        fun setDiscoverablePingIntervalMillis(discoverablePingInterval: Long): Builder {
            mDiscoverablePingInterval = discoverablePingInterval
            return this
        }

        fun setDiscoveryListener(listener: Listener, looper: Looper): Builder {
            mListener = listener
            mLooper = looper
            return this
        }

        fun setContext(context: Context): Builder {
            mContext = context
            return this
        }

        fun setPort(port: Int): Builder {
            mPort = port
            return this
        }

        fun setFilter(regex: Regex): Builder {
            mRegex = regex
            return this
        }

        fun build(): NearDiscovery {
            return NearDiscoveryImpl(mDiscoverableTimeout, mDiscoveryTimeout,
                    mDiscoverablePingInterval, mListener, mLooper, mContext, mPort, mRegex)
        }
    }

    interface Listener {
        fun onPeersUpdate(host: Set<Host>)
        fun onDiscoveryTimeout()
        fun onDiscoveryFailure(e: Throwable)
        fun onDiscoverableTimeout()
    }
}