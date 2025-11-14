package br.com.neves.api_boleto.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import br.com.neves.api_boleto.dto.BoletoDTO;
import br.com.neves.api_boleto.dto.BoletoRequestDTO;
import br.com.neves.api_boleto.entity.BoletoEntity;
import br.com.neves.api_boleto.entity.enums.SituacaoBoleto;
import br.com.neves.api_boleto.exception.ApplicationException;
import br.com.neves.api_boleto.messaging.BoletoProducer;
import br.com.neves.api_boleto.repository.BoletoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoletoService {

    private final BoletoRepository boletoRepository;

    private final BoletoProducer boletoProducer;

    public BoletoService(BoletoRepository boletoRepository, BoletoProducer boletoProducer) {
        this.boletoRepository = boletoRepository;
        this.boletoProducer = boletoProducer;
    }

    public List<BoletoDTO> findAll() {
        var all = boletoRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .map(BoletoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<BoletoDTO> findById(Long id) {
        return boletoRepository.findById(id).map(BoletoDTO::fromEntity);
    }

    public BoletoDTO save(BoletoRequestDTO boletoDTO) {

        var boleto = boletoRepository.findByCodigoBarras(boletoDTO.getCodigoBarras());
        if (boleto.isPresent()) {
            throw new ApplicationException("Boleto ja cadastrado");
        }

        BoletoEntity toSave = BoletoEntity.builder()
                .codigoBarras(boletoDTO.getCodigoBarras())
                .situacaoBoleto(SituacaoBoleto.INICIALIZADO)
                .dataCriacao(LocalDateTime.now())
                .dataAtualizacao(LocalDateTime.now())
                .build();

        BoletoEntity saved = boletoRepository.save(toSave);

        boletoProducer.enviarMensagem(BoletoDTO.toAvro(saved)).join();

        return BoletoDTO.fromEntity(saved);
    }

    public void delete(Long id) {
        boletoRepository.deleteById(id);
    }

}
