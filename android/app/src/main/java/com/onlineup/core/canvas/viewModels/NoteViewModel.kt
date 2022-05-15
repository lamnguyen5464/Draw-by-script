package com.onlineup.core.canvas.viewModels

import android.app.Activity
import androidx.appcompat.widget.AppCompatButton
import com.onlineup.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow

class NoteViewModel(
    private val activity: Activity
) {
    private val triggerOnClear = MutableStateFlow(Unit)
    private val executingScope = CoroutineScope(Dispatchers.Main + Job())


    fun onClear() {
        activity.findViewById<AppCompatButton>(R.id.clear).setOnClickListener {
        }
    }
}