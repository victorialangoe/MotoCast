package com.example.motocast.data.remote.mock

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MockRemoteDataSourceTest {

    private val mockRemoteDataSource = MockRemoteDataSource()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetAddressesShouldNotReturnNull() = runTest {
        val result = mockRemoteDataSource.getAddresses("This road does not exist")
        assertNotNull(result)
    }


}