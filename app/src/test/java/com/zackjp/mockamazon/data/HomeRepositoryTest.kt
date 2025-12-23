package com.zackjp.mockamazon.data

import com.zackjp.mockamazon.shared.model.ItemSection
import com.zackjp.mockamazon.shared.model.TopHomeGroup
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeRepositoryTest {

    private val homeFakeApiDataSource = mockk<HomeFakeApiDataSource>()

    private lateinit var repository: HomeRepository

    @BeforeEach
    fun setUp() {
        repository = HomeRepository(
            homeFakeApiDataSource = homeFakeApiDataSource,
        )
    }

    @Test
    fun getTopHomeGroups_HavingResultsFromApi_ReturnsTopHomeGroupsAsync() = runTest {
        val topHomeGroup1 = mockk<TopHomeGroup>()
        val topHomeGroup2 = mockk<TopHomeGroup>()
        coEvery { homeFakeApiDataSource.fetchTopHomeGroups() } returns listOf(
            topHomeGroup1,
            topHomeGroup2,
        )

        repository.getTopHomeGroups() shouldBe listOf(topHomeGroup1, topHomeGroup2)
    }

    @Test
    fun getHomeSections_HavingResultsFromApi_ReturnsHomeSectionsAsync() = runTest {
        val homeSection1 = mockk<ItemSection>()
        val homeSection2 = mockk<ItemSection>()
        coEvery { homeFakeApiDataSource.fetchHomeSections() } returns listOf(
            homeSection1,
            homeSection2,
        )

        repository.getHomeSections() shouldBe listOf(homeSection1, homeSection2)
    }

}