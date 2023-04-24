package com.example.motocast.ui.viewmodel

import androidx.appcompat.app.AppCompatActivity
import com.example.motocast.ui.viewmodel.map.MapViewModel
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TestMapViewModel {

    @Mock
    private lateinit var mapViewModel: MapViewModel

    @Mock
    private lateinit var activity: AppCompatActivity

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mapViewModel = MapViewModel(activity)
    }

}