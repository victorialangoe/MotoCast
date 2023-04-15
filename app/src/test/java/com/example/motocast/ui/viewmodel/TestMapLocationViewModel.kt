package com.example.motocast.ui.viewmodel

import android.Manifest
import android.content.pm.PackageManager
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import android.os.Process

class TestMapLocationViewModel {

    @Mock
    private lateinit var mapLocationViewModel: MapLocationViewModel

    @Mock
    private lateinit var activity: AppCompatActivity

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mapLocationViewModel = MapLocationViewModel(activity)
    }

}