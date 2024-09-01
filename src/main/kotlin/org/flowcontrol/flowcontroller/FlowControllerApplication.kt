package org.flowcontrol.flowcontroller

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlowControllerApplication

fun main(args: Array<String>) {
    runApplication<FlowControllerApplication>(*args)
}
