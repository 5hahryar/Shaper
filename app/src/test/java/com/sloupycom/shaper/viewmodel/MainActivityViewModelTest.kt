package com.sloupycom.shaper.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sloupycom.shaper.data.repository.TaskRepository
import com.sloupycom.shaper.data.source.FakeTaskRepository
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MainActivityViewModelTest {

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private lateinit var mRepository: TaskRepository
    private lateinit var mViewModel: MainActivityViewModel

    @Before
    fun setUp() {
        mRepository = FakeTaskRepository()
        mViewModel = MainActivityViewModel(mRepository)
    }

    /**
     *  Add new task event is called
     */
    @Test
    fun addNewTask_eventIsCalled() {

    }

    /**
     *  Open settings event is called
     */
}