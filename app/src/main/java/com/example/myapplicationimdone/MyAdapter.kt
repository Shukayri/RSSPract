package com.example.myapplicationimdone

//import kotlinx.android.synthetic.main.edit_row.view.*
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.edit_row.view.*

class MyAdapter(private var questions: List<Question>):
    RecyclerView.Adapter<MyAdapter.StudentViewHolder>() {
    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var cont: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        cont=parent.context;
        return StudentViewHolder(
            LayoutInflater.from(cont).inflate(
                R.layout.edit_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val message = questions[position]

        holder.itemView.apply {
            questionsRV.text = message.title
        }
        holder.itemView.setOnClickListener{
            //questionsRV.text = "${message.title} + \n + ${message.summary}"
            val dialogBuilder = AlertDialog.Builder(cont)
            dialogBuilder.setMessage("${message.summary}")
                .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })

            val alert = dialogBuilder.create()
            alert.setTitle("${message.title}")
            alert.show()
            //message.title + "\n" + message.summary + "\n"+ message.link + "\n"+ message.publishedDate

        }
    }

    override fun getItemCount()  = questions.size

    fun update(questions: MutableList<Question>){
        this.questions = questions

        notifyDataSetChanged()
    }

}
