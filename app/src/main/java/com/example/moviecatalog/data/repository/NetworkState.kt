package com.example.moviecatalog.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {
    companion object {
        /* We use companion objects when we want something to be static.
         * Now we can use these variables without instancing the class. */
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "Running")
        val ERROR: NetworkState = NetworkState(Status.FAILED, "Something went wrong")
        val EOL: NetworkState = NetworkState(Status.FAILED, "End of list")
    }
}
