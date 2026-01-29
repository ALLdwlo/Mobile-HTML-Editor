package com.trebedit.webcraft.data.repository

import com.trebedit.webcraft.data.model.ProjectFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FileRepository {
    
    fun getFile(fileId: String): Flow<ProjectFile?> = flow {
        emit(null)
    }
    
    suspend fun saveFile(file: ProjectFile): Result<Unit> {
        return Result.success(Unit)
    }
    
    fun getProjectFileCount(projectId: String): Flow<Int> = flow {
        emit(0)
    }
}
