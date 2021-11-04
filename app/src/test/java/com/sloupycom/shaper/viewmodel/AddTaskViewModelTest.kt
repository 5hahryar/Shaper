package com.sloupycom.shaper.viewmodel

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.sloupycom.shaper.R
import androidx.test.runner.AndroidJUnit4
import com.sloupycom.shaper.core.util.Event
import com.sloupycom.shaper.data.repository.TaskRepository
import com.sloupycom.shaper.data.source.FakeTaskRepository
import com.sloupycom.shaper.getOrAwaitValue
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddTaskViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private lateinit var mRepository: TaskRepository
    private lateinit var mViewModel: AddTaskViewModel

    @Before
    fun setUp() {
        mRepository = FakeTaskRepository()
        mViewModel = AddTaskViewModel(InstrumentationRegistry.getInstrumentation().context, mRepository)
    }

    /**
     *  New task is added successfully
     */
    @Test
    fun onAddTask_eventIsFiredSuccessfully() {
        mViewModel.textTitle = "Test Task"

        mViewModel.onAddTask()

        assertEquals(Event(Unit), mViewModel.newTaskAddedEvent.getOrAwaitValue())
    }

    /**
     *  New task is not added because the title in empty and error is set
     */
    @Test
    fun onAddTask_failsReturnsError() {

        mViewModel.onAddTask()

        assertEquals(R.string.empty_title_error.toString(), mViewModel.textError.get())
    }
}