package com.example.motocast.data.datasource

import com.example.motocast.data.api.metalerts.MetAlertsHelper
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.util.data.DataHelper

class MetAlertsDataSource: DataHelper() {
    private val metRetrofitService = MetAlertsHelper().createMetAlertsAPI()
    fun getMetAlertsData(
        onSuccess: (MetAlertsDataModel) -> Unit,
        onError: (String) -> Unit
    ) {
        if (metRetrofitService != null) {
            fetchData(
                apiCall = { metRetrofitService.getMetAlertsData().execute() },
                onSuccess = { metAlertsData: MetAlertsDataModel -> onSuccess(metAlertsData) },
                onError = { errorMessage: String -> onError(errorMessage) }
            )
        } else {
            onError("Error: metRetrofitService is null")
        }
    }

}