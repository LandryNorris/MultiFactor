package io.github.landrynorris.multifactor.mobileapp.test

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString

class MockClipboardManager: ClipboardManager {
    private var contents: AnnotatedString? = null

    override fun setText(annotatedString: AnnotatedString) {
        contents = annotatedString
    }

    override fun getText(): AnnotatedString? {
        return contents
    }
}
