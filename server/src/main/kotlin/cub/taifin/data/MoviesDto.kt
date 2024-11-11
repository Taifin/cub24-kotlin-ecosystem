package cub.taifin.data

import kotlinx.serialization.Serializable

@Serializable
data class MoviesDto(val page: Int, val results: List<MovieInfo>)

@Serializable
data class MovieInfo(val title: String,
                     // This api just does not return any info about any people involved in the creation of the movie.
                     // I really believe it is not a big deal to have just the description here instead of
                     // some list of some people
                     val overview: String)