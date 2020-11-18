package com.example.pgr301_devops.metrics

import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class TaskDistributionSummary {
    @Bean
    fun StateDistributionSummary(registry: MeterRegistry): DistributionSummary {
        /*
        * Useful distribution summary for finding if we're getting more tasks than we can handle.
        * A dominance of mean value > 50 means there can be too many tasks opened compared to completed
        *
        * */
        return DistributionSummary
                .builder("tasks.open_complete_ratio")
                .description("A distributions of tasks being opened/completed. If 0 is inserted, a new task is opened and if 100 is inserted, a task is completed")
                .baseUnit("percentage")
                .scale(100.0)
                .register(registry)
    }
}
