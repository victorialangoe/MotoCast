package com.example.motocast

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun getKeyFromLocal(){
        print(BuildConfig.TEST_KEYSTORE_PASSWORD)
        assertEquals("123456", BuildConfig.TEST_KEYSTORE_PASSWORD)
    }
}