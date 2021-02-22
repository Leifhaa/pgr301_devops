package com.example.pgr301_devops.metrics

import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class TaskDistributionSummary {
    @Bean
    fun StateDistributionSummary(registry: MeterRegistry): DistributionSummary {
        return DistributionSummary
                .builder("tasks.billing")
                .description("Measures of how much our company charged for running a task for a customer")
                .baseUnit("usd")
                .register(registry)
    }
}
