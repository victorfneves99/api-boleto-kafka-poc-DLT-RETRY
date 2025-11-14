package br.com.neves.api_boleto.messaging;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.neves.avro.Boleto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class BoletoProducer {

    private final KafkaTemplate<String, Boleto> kafkaTemplate;

    @Value("${kafka.topic.main}")
    private String topic;

    @Value("${kafka.topic.dlt}")
    private String dltTopic;

    private final RetryTemplate retryTemplate;

    @Async
    public CompletableFuture<Void> enviarMensagem(Boleto boleto) {
        return CompletableFuture.runAsync(() -> {
            retryTemplate.execute(context -> {
                log.info("📤 Tentando enviar boleto [{}], tentativa {}", boleto, context.getRetryCount() + 1);

                // if ("1234".equals(String.valueOf(boleto.getCodigoBarras()))) {
                //     throw new KafkaException("Erro simulado para retry e DLT");
                // }

                kafkaTemplate.send(topic, boleto)
                        .whenComplete((result, ex) -> {
                            if (ex != null) {
                                throw new KafkaException("Erro ao enviar boleto: " + ex.getMessage(), ex);
                            }
                            log.info("✅ Boleto enviado com sucesso!");
                        });

                return null;
            }, context -> { // <- recover callback
                log.error("❌ Todas as tentativas falharam. Enviando para DLT: {}", boleto);

                ProducerRecord<String, Boleto> record = new ProducerRecord<>(dltTopic, boleto);
                record.headers()
                        .add(new RecordHeader("x-original-topic", topic.getBytes(StandardCharsets.UTF_8)))
                        .add(new RecordHeader("x-error-message", context.getLastThrowable().getMessage()
                                .getBytes(StandardCharsets.UTF_8)))
                        .add(new RecordHeader("x-timestamp",
                                Instant.now().toString().getBytes(StandardCharsets.UTF_8)));

                kafkaTemplate.send(record);
                return null;
            });
        });
    }

}
