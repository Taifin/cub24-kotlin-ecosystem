package cub.taifin.data

// TODO: tmp mock
object DataController {
    fun getBooks(query: String = "*", startIndex: Int = 0, maxResults: Int = 100): List<Pair<String, String>> {
        return (1..100).map { "Book$it" to "Author of Book$it" }
    }
}