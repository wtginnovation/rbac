package de.vsfexperts.rbac.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import de.vsfexperts.rbac.spring.configuration.RbacProperties;

/**
 * Auto configuration in order to load properties
 */
@Configuration
@EnableConfigurationProperties(RbacProperties.class)
@ConditionalOnMissingBean(RbacProperties.class)
public class RbacPropertiesAutoConfiguration {

}
