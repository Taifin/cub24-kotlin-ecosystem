package cub.taifin.data

import kotlinx.serialization.Serializable

@Serializable
data class ItemizedResponse<T>(val items: List<T>)

@Serializable
data class VolumeDto(val id: String, val volumeInfo: VolumeInfo)

@Serializable
data class VolumeInfo(val title: String, val authors: List<String>)
