/*
 * Tencent is pleased to support the open source community by making Spring Cloud Tencent available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.cloud.polaris.ratelimit.config;

import com.tencent.cloud.common.constant.ContextConstant;
import com.tencent.cloud.polaris.context.ConditionalOnPolarisEnabled;
import com.tencent.cloud.polaris.context.PolarisConfigModifier;
import com.tencent.polaris.factory.config.ConfigurationImpl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Autoconfiguration of rate limit at bootstrap phase.
 *
 * @author Haotian Zhang
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnPolarisEnabled
@ConditionalOnProperty(name = "spring.cloud.polaris.ratelimit.enabled", matchIfMissing = true)
public class PolarisRateLimitBootstrapConfiguration {

	@Bean
	public PolarisRateLimitProperties polarisRateLimitProperties() {
		return new PolarisRateLimitProperties();
	}

	@Bean
	public RateLimitConfigModifier rateLimitConfigModifier(PolarisRateLimitProperties polarisRateLimitProperties) {
		return new RateLimitConfigModifier(polarisRateLimitProperties);
	}

	/**
	 * Config modifier for rate limit.
	 *
	 * @author Haotian Zhang
	 */
	public static class RateLimitConfigModifier implements PolarisConfigModifier {

		private PolarisRateLimitProperties polarisRateLimitProperties;

		public RateLimitConfigModifier(PolarisRateLimitProperties polarisRateLimitProperties) {
			this.polarisRateLimitProperties = polarisRateLimitProperties;
		}

		@Override
		public void modify(ConfigurationImpl configuration) {
			// Update MaxQueuingTime.
			configuration.getProvider().getRateLimit()
					.setMaxQueuingTime(polarisRateLimitProperties.getMaxQueuingTime());
		}

		@Override
		public int getOrder() {
			return ContextConstant.ModifierOrder.CIRCUIT_BREAKER_ORDER;
		}

	}
}
