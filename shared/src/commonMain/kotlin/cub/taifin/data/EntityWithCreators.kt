package cub.taifin.data

sealed interface DrawableEntity

class EntityWithCreators(val title: String, val creators: List<String>) : DrawableEntity