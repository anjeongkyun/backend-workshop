package org.backend.workshop.concurrency.control.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = ["org.backend.workshop.concurrency.control"])
@EntityScan("org.backend.workshop.concurrency.control")
class DataSourceConfig
