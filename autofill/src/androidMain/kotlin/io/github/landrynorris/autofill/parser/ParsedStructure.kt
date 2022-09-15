package io.github.landrynorris.autofill.parser

import android.view.autofill.AutofillId

data class ParsedStructure(val passwordNode: AutofillId?, val usernameNode: AutofillId?)
