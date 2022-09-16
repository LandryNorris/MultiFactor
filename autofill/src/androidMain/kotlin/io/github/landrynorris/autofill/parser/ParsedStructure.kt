package io.github.landrynorris.autofill.parser

import android.view.autofill.AutofillId

data class ParsedStructure(val pkg: String? = null, val passwordNode: AutofillId? = null,
                           val usernameNode: AutofillId? = null,
                           val focused: AutofillId? = null)
