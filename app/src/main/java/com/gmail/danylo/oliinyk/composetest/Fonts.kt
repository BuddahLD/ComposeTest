package com.gmail.danylo.oliinyk.composetest

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

object Fonts {

    val PT_SANS = FontFamily(
        Font(R.font.pt_sans_regular),
        Font(R.font.pt_sans_bold, weight = FontWeight.Bold),
        Font(R.font.pt_sans_italic, style = FontStyle.Italic),
        Font(R.font.pt_sans_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
    )
}
