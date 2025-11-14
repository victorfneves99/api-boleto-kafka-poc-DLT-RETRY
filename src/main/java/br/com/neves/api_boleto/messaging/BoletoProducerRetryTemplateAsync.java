// package br.com.neves.api_boleto.messaging;

// import java.util.concurrent.CompletableFuture;

// import org.apache.kafka.clients.producer.RecordMetadata;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.kafka.KafkaException;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.support.SendResult;
// import org.springframework.retry.support.RetryTemplate;
// import org.springframework.stereotype.Component;

// import br.com.neves.avro.Boleto;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Component
// @Slf4j
// @RequiredArgsConstructor
// public class BoletoProducerRetryTemplateAsync {

//     private final KafkaTemplate<String, Boleto> kafkaTemplate;
//     private final RetryTemplate retryTemplate;

//     @Value("${kafka.topic.main}")
//     private String topic;

//     @Value("${kafka.topic.dlt}")
//     private String dltTopic;

//     public void enviarMensagem(Boleto boleto) {
//         CompletableFuture.runAsync(() -> {
//             try {
//                 retryTemplate.execute(context -> {
//                     log.info("📤 Tentando enviar boleto [{}], tentativa {}", boleto, context.getRetryCount() + 1);

//                     // ⚠️ Simula erro (para testar retry e DLT)
//                     if (String.valueOf(boleto.getCodigoBarras()).equals("1234")) {
//                         throw new KafkaException("Erro simulado para retry e DLT");
//                     }

//                     CompletableFuture<SendResult<String, Boleto>> future = kafkaTemplate.send(topic, boleto);

//                     // Quando concluir o envio
//                     future.whenComplete((result, ex) -> {
//                         if (ex != null) {
//                             throw new KafkaException("Erro ao enviar boleto: " + ex.getMessage(), ex);
//                         }
//                         log.info("✅ Boleto enviado com sucesso! offset={}", result.getRecordMetadata().offset());
//                     });

//                     return null;
//                 });
//             } catch (Exception ex) {
//                 log.error("❌ Falha final no envio do boleto [{}], enviando para DLT...", boleto, ex);
//                 enviarParaDLT(boleto);
//             }
//         });
//     }

//     private void enviarParaDLT(Boleto boleto) {
//         kafkaTemplate.send(dltTopic, boleto).whenComplete((result, ex) -> {
//             if (ex != null) {
//                 log.error("🔥 Falha ao enviar boleto para DLT: {}", ex.getMessage());
//             } else {
//                 log.info("☠️ Mensagem enviada para DLT com sucesso: {}", boleto);
//             }
//         });
//     }
// }
