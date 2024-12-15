package com.example.try2

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.content.Context
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TimePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type


data class Report(
    val type: String,
    val kind: String,
    val startDate: String,
    val endDate: String,
    val fullTime: String,
    val MH: String,
    val UHL: String,
    val UHR: String,
    val EHL: String,
    val EHR: String,
    val model: String,
    val index_SDO: String,
    val executor: String,
    val post: String,
    val comment: String,
    val description: String,
    val installed: String,
    val additional: String,
    val recommendations: String,
    val requirement_MPZ: String
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val timePicker = findViewById<TimePicker>(R.id.timePicker1)
        // Устанавливаем 24-часовой формат
        timePicker.setIs24HourView(true)
        val timePicker2 = findViewById<TimePicker>(R.id.timePicker2)
        timePicker2.setIs24HourView(true)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun makeReport(view: View) {
        var datePicker = findViewById<DatePicker>(R.id.datePicker1)
        var day = datePicker.dayOfMonth
        var month = datePicker.month + 1
        var year = datePicker.year
        var selectedDateStart = "$day-$month-$year"
        datePicker = findViewById<DatePicker>(R.id.datePicker2)
        day = datePicker.dayOfMonth
        month = datePicker.month + 1
        year = datePicker.year
        var selectedDateEnd = "$day-$month-$year"

        var timePicker = findViewById<TimePicker>(R.id.timePicker1)
        var hour = timePicker.hour
        var minute = timePicker.minute
        var selectedTime = "$hour:$minute"
        selectedDateStart += " " + selectedTime

        timePicker = findViewById<TimePicker>(R.id.timePicker2)
        hour = timePicker.hour
        minute = timePicker.minute
        selectedTime = "$hour:$minute"
        selectedDateEnd += " " + selectedTime
        val fullTime = findViewById<EditText>(R.id.editTextTime).text.toString()
        val type = findViewById<Spinner>(R.id.mySpinner).selectedItem.toString()
        val kind = findViewById<Spinner>(R.id.mySpinner1).selectedItem.toString()
        val MH = findViewById<EditText>(R.id.MCH).text.toString()
        val UHL = findViewById<EditText>(R.id.UHL).text.toString()
        val UHR = findViewById<EditText>(R.id.UHR).text.toString()
        val EHL = findViewById<EditText>(R.id.EHL).text.toString()
        val EHR = findViewById<EditText>(R.id.EHR).text.toString()
        val model = findViewById<EditText>(R.id.MODEL).text.toString()
        val index_sdo = findViewById<EditText>(R.id.INDEX_SDO).text.toString()
        val post = findViewById<EditText>(R.id.POST).text.toString()
        val executor = findViewById<EditText>(R.id.EXECUTOR).text.toString()
        val comment = findViewById<EditText>(R.id.TextEdit1).text.toString()
        val description = findViewById<EditText>(R.id.TextEdit2).text.toString()
        val installed = findViewById<EditText>(R.id.TextEdit3).text.toString()
        val additional = findViewById<EditText>(R.id.TextEdit4).text.toString()
        val recommendations = findViewById<EditText>(R.id.TextEdit5).text.toString()
        val requirement_MPZ = findViewById<EditText>(R.id.TextEdit6).text.toString()
        val report = Report(
            type = type,
            kind = kind,
            startDate = selectedDateStart,
            endDate = selectedDateEnd,
            fullTime = fullTime,
            MH = MH,
            UHL = UHL,
            UHR = UHR,
            EHL = EHL,
            EHR = EHR,
            model = model,
            index_SDO = index_sdo,
            executor = executor,
            post = post,
            comment = comment,
            description = description,
            installed = installed,
            additional = additional,
            recommendations = recommendations,
            requirement_MPZ = requirement_MPZ
        )

        val fileName = "reports.json"
        val gson = Gson()
        val file = File("/data/data/com.example.try2/files" + fileName)
        println("PIZDA" + file.length())
        val reports: MutableList<Report> = try {

            if (file.exists() && file.length() > 0) {
                // Читаем JSON и парсим в список
                val jsonString = file.readText()
                val listType: Type = object : TypeToken<MutableList<Report>>() {}.type
                gson.fromJson<MutableList<Report>>(jsonString, listType)
            } else {
                // Файл не существует или пустой, создаём пустой список
                println("Файл не существует или пустой. Создаём пустой массив.")
                mutableListOf()
            }
        } catch (e: Exception) {
            println("Ошибка при чтении файла: ${e.message}")
            mutableListOf() // Возвращаем пустой список в случае ошибки
        }
        println("PIZDA" + reports)

        reports.add(report)

        val jsonString = gson.toJson(reports)

        // Имя файла


        // Сохраняем JSON во внутреннее хранилище
        openFileOutput(fileName, MODE_PRIVATE).use { outputStream ->
            outputStream.write(jsonString.toByteArray())
        }

        println("Файл $fileName успешно создан и записан во внутреннее хранилище.")


    }

    fun sendReport(view: View) {
        val filePath = "reports.json"
        val file = File(filePath)
        if (!file.exists()) {
            println("Файла не существует")
            return
        }
        val gson = Gson()
        val jsonString = File(filePath).readText() // Читаем файл
        if (jsonString.isBlank()) {
            println("Файл пустой")
            return
        }
        val listType: Type = object : TypeToken<List<Report>>() {}.type
        val reports: List<Report> = Gson().fromJson(jsonString, listType)
        reports.forEach { println(it) }


    }
}