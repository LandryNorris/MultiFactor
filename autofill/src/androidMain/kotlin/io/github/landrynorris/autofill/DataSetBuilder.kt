package io.github.landrynorris.autofill

import android.service.autofill.Dataset

class DataSetBuilder {
    fun build(): Dataset {
        val dataset = Dataset.Builder()

        return dataset.build()
    }
}