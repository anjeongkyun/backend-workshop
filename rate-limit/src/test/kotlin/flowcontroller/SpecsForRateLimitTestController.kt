package flowcontroller

import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.util.concurrent.Callable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class SpecsForRateLimitTestController {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should limit requests when called in parallel`() {
        // Arrange
        val executorService = Executors.newFixedThreadPool(10)
        val latch = CountDownLatch(1)

        // Act
        val tasks =
            List(10) {
                Callable {
                    latch.await()

                    val result =
                        mockMvc
                            .perform(
                                post("/rate-limit-test")
                                    .header("X-Forwarded-For", "192.168.1.2"),
                            ).andReturn()

                    result.response.status
                }
            }

        latch.countDown()
        val futures = executorService.invokeAll(tasks, 10, java.util.concurrent.TimeUnit.SECONDS)
        executorService.shutdown()

        // Assert
        val responseStatuses = futures.map { it.get() }
        val successCount = responseStatuses.count { it == HttpStatus.OK.value() }
        val tooManyRequestsCount = responseStatuses.count { it == HttpStatus.TOO_MANY_REQUESTS.value() }

        assertEquals(5, successCount)
        assertEquals(5, tooManyRequestsCount)
    }
}
