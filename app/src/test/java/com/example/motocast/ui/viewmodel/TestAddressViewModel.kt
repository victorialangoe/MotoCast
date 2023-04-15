package com.example.motocast.ui.viewmodel

import android.location.Location
import com.example.motocast.data.datasource.AddressDataSource
import com.example.motocast.ui.viewmodel.address.Address
import com.example.motocast.ui.viewmodel.address.AddressDataViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class TestAddressViewModel {

    @Mock
    private lateinit var addressDataSource: AddressDataSource
    private lateinit var addressDataViewModel: AddressDataViewModel
    private val mainThreadSurrogate = newSingleThreadContext("AddressViewModelTest")



    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        addressDataViewModel = AddressDataViewModel().apply {
            this.addressDataSource = addressDataSource
        }
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun testAddFormerAddress(){
        val addressDataViewModel = AddressDataViewModel()
        val address = Address(
            addressText = "123 Example Street",
            municipality = "Some City",
            latitude = 10.0,
            longitude = 20.0,
            distanceFromUser = 1000
        )

        addressDataViewModel.addFormerAddress(address)

        val formerAddresses = addressDataViewModel.uiState.value.formerAddresses
        assertEquals(1, formerAddresses.size)
        assertTrue(formerAddresses.contains(address))
    }

    @Test
    fun testClearQuery() = runTest{
        launch(Dispatchers.Main){


            val addressDataViewModel = AddressDataViewModel()
            val address = Address(
                addressText = "123 Example Street",
                municipality = "Some City",
                latitude = 10.0,
                longitude = 20.0,
                distanceFromUser = 1000
            )


            // Call fetchAddressData() to add addresses to the list
            addressDataViewModel.fetchAddressData(
                query = "123 Example Street",
                getAirDistanceFromLocation = { location: Location -> 1000 },

                )



            assertTrue(addressDataViewModel.getQuery().isNotEmpty())
            addressDataViewModel.clearQuery()

            assertTrue(addressDataViewModel.getQuery().isEmpty())
        }
    }



}