package com.example.fakeamazon.shared



inline fun <reified T> Any.runIf(block: T.() -> Unit) {
    if (this is T) {
        block()
    }
}
