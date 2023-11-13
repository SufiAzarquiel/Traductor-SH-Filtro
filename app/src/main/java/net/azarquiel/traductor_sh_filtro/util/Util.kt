package net.azarquiel.traductor_sh_filtro.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.Xml
import android.widget.Toast
import java.io.*

/**
 * Autor: Paco Pulido
 */

@SuppressLint("StaticFieldLeak")
object Util {
    private lateinit var XMLFile:String
    private lateinit var context: Context

    fun inyecta(context: Context, XMLFile:String) {
        this.context = context
        this.XMLFile = XMLFile
        if (!File("/data/data/${context.packageName}/shared_prefs/$XMLFile").exists()) {
            Toast.makeText(context,"Copying words...", Toast.LENGTH_LONG).show()
            copiarXML()
        }
    }
    private fun copiarXML() {
        creaDirectorio()
        copiar(XMLFile)
    }

    private fun creaDirectorio() {
        val file = File("/data/data/${context.packageName}/shared_prefs")
        file.mkdir()
    }

    private fun copiar(XMLFile: String) {
        val ruta = ("/data/data/${context.packageName}/shared_prefs/$XMLFile")
        var input: InputStream? = null
        var output: OutputStream? = null
        try {
            input = context.assets.open(XMLFile)
            output = FileOutputStream(ruta)
            copyFile(input, output)
            input!!.close()
            output.close()
        } catch (e: IOException) {
            Log.e("Traductor", "Fallo en la copia del archivo desde el asset", e)
        }
    }

    private fun copyFile(input: InputStream?, output: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        read = input!!.read(buffer)
        while (read != -1) {
            output.write(buffer, 0, read)
            read = input!!.read(buffer)
        }
    }

}