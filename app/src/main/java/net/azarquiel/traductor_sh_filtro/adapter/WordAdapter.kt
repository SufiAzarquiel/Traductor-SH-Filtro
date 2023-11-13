package net.azarquiel.traductor_sh_filtro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.azarquiel.traductor_sh_filtro.R
import net.azarquiel.traductor_sh_filtro.model.Word

class WordAdapter(val context: Context,
                    val layout: Int, private val listener: TextToSpeechListener
) : RecyclerView.Adapter<WordAdapter.ViewHolder>() {

    private var dataList: List<Word> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal fun setWords(words: List<Word>) {
        this.dataList = words
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context, private val listener: TextToSpeechListener ) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Word){
            var tvSpanish = itemView.findViewById(R.id.tvSpanish) as TextView
            var tvEnglish = itemView.findViewById(R.id.tvEnglish) as TextView
            var ivSpeakerSpanish = itemView.findViewById(R.id.ivSpeakerSpanish) as ImageView
            var ivSpeakerEnglish = itemView.findViewById(R.id.ivSpeakerEnglish) as ImageView

            tvSpanish.text = dataItem.spWord
            tvEnglish.text = dataItem.enWord

            ivSpeakerSpanish.setOnClickListener {
                onTextViewClick(dataItem, "sp")
            }

            ivSpeakerEnglish.setOnClickListener {
                onTextViewClick(dataItem, "en")
            }

            itemView.tag = dataItem
        }

        private fun onTextViewClick(dataItem: Word, language: String) {
            val textToRead = if (language == "sp") dataItem.spWord else dataItem.enWord
            listener.onTextToSpeechRequested(textToRead, language)
        }
    }

    interface TextToSpeechListener {
        fun onTextToSpeechRequested(text: String, language: String)
    }
}