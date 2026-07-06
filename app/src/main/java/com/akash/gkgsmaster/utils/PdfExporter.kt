package com.akash.gkgsmaster.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.akash.gkgsmaster.data.model.NoteEntity
import java.io.File
import java.io.FileOutputStream

object PdfExporter {

    fun exportNoteToPdf(context: Context, note: NoteEntity): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
        val page = pdfDocument.startPage(pageInfo)

        val canvas: Canvas = page.canvas
        val paint = Paint()

        // Title
        paint.color = Color.BLACK
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText(note.title, 50f, 50f, paint)

        // Content
        paint.isFakeBoldText = false
        paint.textSize = 14f
        var y = 100f
        val lines = note.content.split("\n")
        for (line in lines) {
            canvas.drawText(line, 50f, y, paint)
            y += 20f
            if (y > 800) break // Simple pagination limit for one page
        }

        pdfDocument.finishPage(page)

        val file = File(context.getExternalFilesDir(null), "${note.title.replace(" ", "_")}.pdf")
        return try {
            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()
            file
        } catch (e: Exception) {
            pdfDocument.close()
            null
        }
    }
}
