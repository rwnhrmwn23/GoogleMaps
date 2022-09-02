package com.onedev.googlemaps.activity

import android.os.Bundle
import com.onedev.googlemaps.databinding.ActivityUserBinding
import com.onedev.googlemaps.utils.BaseActivityBinding

class UserActivity : BaseActivityBinding<ActivityUserBinding>() {
    override fun inflateBinding(): ActivityUserBinding {
        return ActivityUserBinding.inflate(layoutInflater)
    }

    override fun onCreateBinding(savedInstanceState: Bundle?) {

    }

}