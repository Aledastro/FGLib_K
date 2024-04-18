package com.uzery.fglib.core.program

import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable

class FGClipboard(private val clipboard: Clipboard) {
    var string: String?
        get() = getContent(DataFlavor.stringFlavor) as String?
        set(value) = setContent(StringSelection(value))

    private fun getContent(flavor: DataFlavor): Any?{
        val content = clipboard.getContents(null)?: return null

        if (content.isDataFlavorSupported(flavor)) {
            return content.getTransferData(flavor)
        }

        return null
    }

    private fun setContent(content: Transferable) {
        clipboard.setContents(content, null)
    }
}
