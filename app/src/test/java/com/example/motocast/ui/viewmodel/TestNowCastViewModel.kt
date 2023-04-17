package com.example.motocast.ui.viewmodel

import android.location.Location
import android.util.Log
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TestNowCastViewModel {

    @Mock
    private lateinit var nowCastViewModel: NowCastViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        nowCastViewModel = NowCastViewModel()

        // create a mock of the android.util.Log class
        mockStatic(Log::class.java)
    }

    @Test
    fun testStopFetchingNowCastData() {
        // stub the d() method of the Log class
        `when`(Log.d(anyString(), anyString())).thenReturn(0)

        nowCastViewModel.startFetchingNowCastData {
            // Do nothing
            Location("Test")
        }
        assertTrue(nowCastViewModel.getJob() != null)
        nowCastViewModel.stopFetchingNowCastData()
        assertTrue(nowCastViewModel.getJob() == null)
    }
}