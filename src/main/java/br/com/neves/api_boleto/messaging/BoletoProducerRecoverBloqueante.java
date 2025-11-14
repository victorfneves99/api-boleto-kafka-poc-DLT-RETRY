// package br.com.neves.api_boleto.messaging;

// import java.util.concurrent.CompletableFuture;

// import org.apache.kafka.clients.producer.RecordMetadata;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.support.SendResult;
// import org.springframework.retry.annotation.Backoff;
// import org.springframework.retry.annotation.Recover;
// import org.springframework.retry.annotation.Retryable;
// import org.springframework.stereotype.Component;

// import org.springframework.kafka.KafkaException;

// import br.com.neves.avro.Boleto;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Component
// @RequiredArgsConstructor
// @Slf4j
// public class BoletoProducer {

//     @Value("${kafka.topic.main}")
//     private String topic;

//     @Value("${kafka.topic.dlt}")
//     private String dltTopic;

//     private final KafkaTemplate<String, Boleto> kafkaTemplate;

//     /**
//      * Tenta enviar 3 vezes, com 2s de intervalo entre cada tentativa.
//      * Se todas falharem, chama o método @Recover (envia para DLT).
//      * 
//      * @throws Exception
//      */

//     /**
//      * Retry automático: 3 tentativas com backoff exponencial (2s, 4s, 8s)
//      */
//     @Retryable(value = KafkaException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
//     public void enviarMensagem(Boleto boleto) {
//         log.info("Enviando boleto [{}] para tópico [{}]", boleto, topic);

//         // ⚠️ Forçando um erro proposital (simulação)
//         if (String.valueOf(boleto.getCodigoBarras()).equals("1234")) {
//             throw new KafkaException("Erro simulado para teste de retry e DLT");
//         }

//         CompletableFuture<SendResult<String, Boleto>> future = kafkaTemplate.send(topic, boleto);

//         future.whenComplete((result, ex) -> {
//             if (ex != null) {
//                 log.error("Falha ao enviar boleto [{}]: {}", boleto, ex.getMessage());
//                 throw new KafkaException("Erro ao enviar para Kafka", ex);
//             } else {
//                 RecordMetadata metadata = result.getRecordMetadata();
//                 log.info(
//                         "Boleto [{}] enviado com sucesso para [{}] - partição {}, offset {}",
//                         boleto, metadata.topic(), metadata.partition(), metadata.offset());
//             }
//         });
//     }

//     /**
//      * Chamado quando todas as tentativas de retry falham.
//      */
//     @Recover
//     public void recover(Exception e, Boleto boletoDTO) {
//         log.warn("Enviando boleto [{}] para DLT após falha: {}", boletoDTO, e.getMessage());
//         kafkaTemplate.send(dltTopic, boletoDTO);
//     }
// }
