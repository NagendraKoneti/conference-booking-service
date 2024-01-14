package com.conference.util;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * This properties class will be loaded application yml file properties
 */
@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {

	private List<MaintenancePeriodConfig> maintenancePeriods;
}
