package com.nguyen.jpmorgan

import android.app.Application

class MyApplication: Application() {
    val repository = Repository()
}