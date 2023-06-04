package com.nguyen.jpmorgan

import android.app.Application
import com.nguyen.jpmorgan.model.Repository

class MyApplication: Application() {
    val repository = Repository()
}