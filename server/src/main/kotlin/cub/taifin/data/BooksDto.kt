package cub.taifin.data

import kotlinx.serialization.Serializable

@Suppress("PropertyName")
@Serializable
data class DocsDto(val start: Int, val num_found: Int, val docs: List<BookInfo>)

@Suppress("PropertyName")
@Serializable
data class BookInfo(val title: String, val author_name: List<String>)
