package com.example.motocast.data.datasource

import com.example.motocast.data.api.metalerts.MetAlertsHelper
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.util.data.DataHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MetAlertsDataSource : DataHelper() {
    private val metRetrofitService = MetAlertsHelper().createMetAlertsAPI()
    suspend fun getMetAlertsData(): MetAlertsDataModel? {
        return withContext(Dispatchers.IO) {
            fetchData(
                apiCall = { metRetrofitService?.getMetAlertsData()?.execute() },
                onSuccess = { metAlertsData: MetAlertsDataModel -> metAlertsData },
                onError = { Throwable("Error: ${it.message}") }
            )
        }
    }
}
