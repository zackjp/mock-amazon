package com.zackjp.mockamazon.shared



inline fun <reified T> Any.runIf(block: T.() -> Unit) {
    if (this is T) {
        block()
    }
}
