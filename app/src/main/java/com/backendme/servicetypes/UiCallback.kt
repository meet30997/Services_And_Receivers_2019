package com.backendme.servicetypes

import android.os.Message

interface UiCallback {

    fun publishToUiThread(message: Message)


}