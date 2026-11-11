package com.bawerk.kycflowlocal

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.view.ViewGroup
import android.widget.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var list: LinearLayout
    private val folder by lazy { File(filesDir, "documents").apply { mkdirs() } }
    private var pendingName = "document"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(28, 40, 28, 20); setBackgroundColor(Color.rgb(245,247,250)) }
        fun title(text: String, size: Float) = TextView(this).apply { this.text=text; textSize=size; setTextColor(Color.rgb(25,35,55)); setPadding(0,10,0,12) }
        root.addView(title("KYCFlow Local", 26f))
        root.addView(title("Gerenciador de documentos • sem login • armazenamento local", 14f))
        fun action(text: String, name: String, mime: String) {
            root.addView(Button(this).apply { this.text=text; setOnClickListener { pendingName=name; startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply { type=mime; addCategory(Intent.CATEGORY_OPENABLE) }, 100) } })
        }
        action("Importar imagem de documento", "document", "image/*")
        action("Importar foto facial", "face", "image/*")
        action("Importar vídeo", "video", "video/*")
        root.addView(title("Arquivos salvos neste aparelho", 19f))
        val scroll = ScrollView(this)
        list = LinearLayout(this).apply { orientation=LinearLayout.VERTICAL }
        scroll.addView(list); root.addView(scroll, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        setContentView(root); refresh()
    }
    private fun refresh() {
        list.removeAllViews()
        val files=folder.listFiles()?.sortedByDescending { it.lastModified() }.orEmpty()
        if(files.isEmpty()) list.addView(TextView(this).apply { text="Nenhum arquivo importado."; textSize=16f })
        files.forEach { f ->
            val row=LinearLayout(this).apply { orientation=LinearLayout.HORIZONTAL }
            row.addView(TextView(this).apply { text=f.name; textSize=13f; setPadding(0,18,8,18) }, LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1f))
            row.addView(Button(this).apply { text="Excluir"; setOnClickListener { f.delete(); refresh() } })
            list.addView(row)
        }
    }
    @Deprecated("Activity result API")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode,resultCode,data)
        if(requestCode!=100 || resultCode!=RESULT_OK) return
        val uri=data?.data ?: return
        try {
            val stamp=SimpleDateFormat("yyyyMMdd_HHmmss_SSS",Locale.US).format(Date())
            val out=File(folder,"${pendingName}_${stamp}.bin")
            contentResolver.openInputStream(uri)?.use { input -> out.outputStream().use { input.copyTo(it) } }
            Toast.makeText(this,"Arquivo salvo localmente",Toast.LENGTH_SHORT).show(); refresh()
        } catch(e:Exception) { Toast.makeText(this,"Falha ao importar arquivo",Toast.LENGTH_LONG).show() }
    }
}
