package com.zackjp.mockamazon.shared.ui.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.withLink
import com.zackjp.mockamazon.shared.theme.LinkBlue


fun AnnotatedString.Builder.appendLink(text: String, onClick: () -> Unit = {}) {
    val linkAnnotation = LinkAnnotation.Clickable(
        linkInteractionListener = { linkAnnotation -> onClick() },
        styles = TextLinkStyles(style = SpanStyle(color = LinkBlue)),
        tag = "clickable",
    )
    withLink(linkAnnotation) {
        append(text)
    }
}