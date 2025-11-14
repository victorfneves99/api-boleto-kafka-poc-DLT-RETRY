package br.com.neves.api_boleto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate template = new RetryTemplate();

        // Política de backoff (exponencial)
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000);   // 1s
        backOffPolicy.setMultiplier(2.0);         // duplica a cada retry
        backOffPolicy.setMaxInterval(8000);       // limite 8s
        template.setBackOffPolicy(backOffPolicy);

        // Política de tentativas
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3); // tenta 3 vezes
        template.setRetryPolicy(retryPolicy);

        return template;
    }
}
