package com.uzery.fglib.core.program

import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor

class FGClipboard(private val clipboard: Clipboard) {
    val string: String?
        get() = getContent(DataFlavor.stringFlavor) as String?

    private fun getContent(flavor: DataFlavor): Any?{
        val content = clipboard.getContents(null)

        if (content.isDataFlavorSupported(flavor)) {
            return content.getTransferData(flavor)
        }

        return null
    }
}
