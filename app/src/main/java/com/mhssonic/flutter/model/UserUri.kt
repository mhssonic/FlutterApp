package com.mhssonic.flutter.model

import android.net.Uri
import java.io.Serializable

class UserUri: Serializable {
    lateinit var avatarUri: Uri
    lateinit var headerUri: Uri
}