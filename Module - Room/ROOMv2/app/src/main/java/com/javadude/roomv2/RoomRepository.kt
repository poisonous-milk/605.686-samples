package com.javadude.roomv2

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread

class RoomRepository(application: Application) {
    private val db = Room.databaseBuilder(application,
            TodoDatabase::class.java, "TODO").build()

    @WorkerThread
    fun save(projectEntity: ProjectEntity) =
        db.getProjectDao().insert(projectEntity)

//    fun save(project: Project) {
//        val projectEntity = ProjectEntity()
//        projectEntity.id = project.id
//        projectEntity.name = project.name
//        db.getProjectDao().insert(projectEntity)
//    }

    @WorkerThread
    fun save(projectEntity: ProjectEntity, todoItemEntity: TodoItemEntity) {
        todoItemEntity.projectId = projectEntity.id
        db.getTodoDao().insert(todoItemEntity)
    }

    @UiThread
    fun getTodoItems(projectEntity: ProjectEntity) : LiveData<List<TodoItemEntity>> =
        db.getTodoDao().getByProjectId(projectEntity.id)

    @UiThread
    fun getProjects() : LiveData<List<ProjectEntity>> =
        db.getProjectDao().getAll()

}