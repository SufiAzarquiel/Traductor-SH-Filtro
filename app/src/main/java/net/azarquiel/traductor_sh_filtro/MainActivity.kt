package net.azarquiel.traductor_sh_filtro

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import net.azarquiel.traductor_sh_filtro.adapter.WordAdapter
import net.azarquiel.traductor_sh_filtro.databinding.ActivityMainBinding
import net.azarquiel.traductor_sh_filtro.model.Word
import net.azarquiel.traductor_sh_filtro.util.Util
import java.util.Locale

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, WordAdapter.TextToSpeechListener {

    private lateinit var searchView: SearchView
    private lateinit var binding: ActivityMainBinding
    private lateinit var tts: TextToSpeech
    private lateinit var adapter:WordAdapter
    private lateinit var words: java.util.ArrayList<Word>
    private lateinit var englishSH: SharedPreferences
    private lateinit var spanishSH: SharedPreferences
    private lateinit var searchLanguage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        searchLanguage = "sp"
        createTTS()
        loadResources()

        getAllWords()

        initRv()
        adapter.setWords(words)
    }

    private fun createTTS() {
        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.getDefault()
            }
        }
    }

    override fun onTextToSpeechRequested(text: String, language: String) {
        if(language == "sp"){
            ttsReadText(text, "es")
        }
        else if (language == "en"){
            ttsReadText(text, "en")
        }
    }

    private fun ttsReadText(text: String, language: String) {
        tts.language = Locale(language)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun loadResources() {
        // XML files
        Util.inyecta(this, "espanol.xml")
        Util.inyecta(this, "ingles.xml")

        // Shared Preferences
        spanishSH = getSharedPreferences("espanol", Context.MODE_PRIVATE)
        englishSH = getSharedPreferences("ingles", Context.MODE_PRIVATE)
    }

    private fun getAllWords() {
        words = ArrayList()
        spanishSH.all.forEach {
            val enWord = englishSH.getString(it.key, null)
            val word = Word(it.key, it.value.toString(), enWord.toString())
            words.add(word)
        }
    }

    private fun initRv() {
        val rvWords = binding.contentMain.rvWords
        adapter = WordAdapter(this, R.layout.row_word, this)
        rvWords.adapter = adapter
        rvWords.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        // Filter
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search word..."
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        return when (item.itemId) {
            R.id.action_flag -> {

                if(searchLanguage == "en"){
                    item.setIcon(R.drawable.flage)
                    searchLanguage = "sp"
                } else {
                    item.setIcon(R.drawable.flagi)
                    searchLanguage = "en"
                }
                Log.d("sufiDev", "Lang: $searchLanguage")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Filter
    override fun onQueryTextChange(query: String): Boolean {
        val original = ArrayList<Word>(words)
        if (searchLanguage == "en")
            adapter.setWords(original.filter { word -> word.enWord.contains(query,true) })
        else if (searchLanguage == "sp")
            adapter.setWords(original.filter { word -> word.spWord.contains(query,true) })
        return false
    }

    override fun onQueryTextSubmit(text: String): Boolean {
        return false
    }
}