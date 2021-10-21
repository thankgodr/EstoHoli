package com.richard.estoholi

import com.google.gson.Gson
import com.richard.estoholi.models.HolidayResponse
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
        val resuole = "{\n" +
                "  \"error\": false,\n" +
                "  \"holidays\": {\n" +
                "    \"2019-02-02\": [\n" +
                "      {\n" +
                "        \"name\": \"Küünlapäev ehk pudrupäev\",\n" +
                "        \"type\": \"folk\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"2019-02-09\": [\n" +
                "      {\n" +
                "        \"name\": \"Luuvalupäev\",\n" +
                "        \"type\": \"folk\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"2019-02-22\": [\n" +
                "      {\n" +
                "        \"name\": \"Talvine peetripäev\",\n" +
                "        \"type\": \"folk\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"2019-02-24\": [\n" +
                "      {\n" +
                "        \"name\": \"Iseseisvuspäev\",\n" +
                "        \"type\": \"public\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"Talvine madisepäev\",\n" +
                "        \"type\": \"folk\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}"

        val te = Gson().fromJson(resuole, HolidayResponse::class.java)

        assert( te.holidays!!.get("2019-02-09")?.get(0)!!.name.equals("Luuvalupäev"))
    }
}