package io.github.landrynorris.app

import kotlinx.io.files.Path

data class Directories(
    val downloadsDirectory: Path,
    val tempDirectory: Path,
)
