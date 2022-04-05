package com.hjl.commonlib.utils

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TaskDispatcher  {

    private val executors: ExecutorService = Executors.newCachedThreadPool()
    private val mTaskList = arrayListOf<Task>()


    fun addTask(task : Task) : TaskDispatcher {
        mTaskList.add(task)
        return this
    }

    fun executeTask(){

        for (task in mTaskList){
            executors.execute(task)
        }

    }


    abstract class Task() : Runnable {

        private var onFinishTaskListener : OnFinishTaskListener? = null

        abstract fun execute() : Any?

        override fun run() {
            var result = execute()
            onFinishTaskListener?.onFinishTask(result)
        }
    }

    interface OnFinishTaskListener{
        fun onFinishTask(data : Any?)
    }


}