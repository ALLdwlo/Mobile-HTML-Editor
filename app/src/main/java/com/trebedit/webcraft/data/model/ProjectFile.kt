package com.trebedit.webcraft.data.model

data class ProjectFile(
    val id: String,
    val filename: String,
    val content: String,
    val language: String,
    val lastModified: Long,
    val projectId: String
)
