package uz.mymax.savvyenglish.utils

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Manually mark this event as handled
     *
     * Returns the original state
     */
    fun handleIfNotHandled() : Boolean {
        val originalState = hasBeenHandled
        hasBeenHandled = true

        return originalState
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

}