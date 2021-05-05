package org.lamisplus.modules.bootstrap.configurer;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"org.lamisplus.modules.bootstrap.repository"})
public class DomainConfiguration {
}
