package com.example.myapplicationimdone


import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_row.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    //lateinit var listView: ListView
    lateinit var rvMain: RecyclerView
    //lateinit var questo: ArrayList<String>
    var questions = mutableListOf<Question>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMain = findViewById(R.id.rvQuestions)
        //rvQuestions = findViewById(R.id.rvQuestions)
        //listView = findViewById<ListView>(R.id.listView)
        //questo = ArrayList()

        FetchQuestions().execute()
    }

    private inner class FetchQuestions : AsyncTask<Void, Void, MutableList<Question>>() {
        val parser = XMLParser()
        override fun doInBackground(vararg params: Void?): MutableList<Question> {
            val url = URL("https://stackoverflow.com/feeds")
            val urlConnection = url.openConnection() as HttpURLConnection
            questions =
                urlConnection.inputStream?.let { parser.parse(it) } as MutableList<Question>
            return questions
        }

        override fun onPostExecute(result: MutableList<Question>?) {
            super.onPostExecute(result)
            //var txt = ""
            // for(quest in questions) {
            //   txt += "${quest.title} - ${quest.summary} - ${quest.link} - ${quest.publishedDate}\n"
            //}
            //questo.add(txt)
            rvMain.adapter = result?.let { MyAdapter(it) }
            rvMain.layoutManager = LinearLayoutManager(this@MainActivity)

            //rvQuestions.adapter?.notifyDataSetChanged()
            //rvQuestions.scrollToPosition(questo.size)
            //val adapter =
            //    ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, questions)

        }

    }

}