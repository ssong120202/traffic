package com.example.myapplication

import android.app.Application
import android.content.Context
import com.example.myapplication.model.TrafficInfo
import com.example.myapplication.service.TrafficRepository
import com.example.myapplication.ui.TrafficViewModel
import io.mockk.impl.annotations.RelaxedMockK

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(Enclosed::class)
class TrafficViewModelTest {
    abstract class Base {
        lateinit var trafficViewModel: TrafficViewModel

        @Mock
        lateinit var application: Application

        @Mock
        lateinit var context: Context

        @RelaxedMockK
        private lateinit var trafficRepository: TrafficRepository

        @Before
        fun setUp() {
            MockitoAnnotations.initMocks(this)
            `when`(application.applicationContext).thenReturn(context)
            trafficRepository = TrafficRepository.getInstance(application)
            trafficViewModel = TrafficViewModel(trafficRepository)
        }
    }

    @RunWith(Parameterized::class)
    class ShouldShowTrackerHelperMethodTest(
            private var currentList: List<TrafficInfo>,
            private var expectList: List<TrafficInfo>) : Base() {
        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<Any>> {
                return listOf(
                        arrayOf(listOf(""),
                                listOf("")),
                        arrayOf(listOf(TrafficInfo(quadrant = "NE"),
                                TrafficInfo(quadrant = "SW"),
                                TrafficInfo(quadrant = "SE"),
                                TrafficInfo(quadrant = "NW")),
                                listOf(TrafficInfo(quadrant = "NE"),
                                        TrafficInfo(quadrant = "NW"),
                                        TrafficInfo(quadrant = "SE"),
                                        TrafficInfo(quadrant = "SW")
                                )
                        )
                )
            }
        }

        @Test
        fun shouldShowMilesTracker() {
            Assert.assertEquals(expectList, trafficViewModel.getSortedList(currentList))
        }
    }
}
