package br.com.neves.api_boleto.dto;

import java.time.LocalDateTime;

import br.com.neves.api_boleto.entity.BoletoEntity;
import br.com.neves.api_boleto.entity.enums.SituacaoBoleto;
import br.com.neves.avro.Boleto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoletoDTO {

	private Long id;
	private String codigoBarras;
	private SituacaoBoleto situacaoBoleto;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAtualizacao;

	/**
	 * Returns a BoletoDTO object from a BoletoEntity object.
	 *
	 * If the given BoletoEntity object is null, this method returns null.
	 *
	 * @param entity the BoletoEntity object to convert
	 * @return a BoletoDTO object or null if the given BoletoEntity object is null
	 */
	public static BoletoDTO fromEntity(BoletoEntity entity) {
		if (entity == null)
			return null;
		return BoletoDTO.builder()
				.id(entity.getId())
				.codigoBarras(entity.getCodigoBarras())
				.situacaoBoleto(entity.getSituacaoBoleto())
				.dataCriacao(entity.getDataCriacao())
				.dataAtualizacao(entity.getDataAtualizacao())
				.build();
	}

	/**
	 * Returns a BoletoEntity object from a BoletoDTO object.
	 *
	 * If the given BoletoDTO object is null, this method returns null.
	 *
	 * @return a BoletoEntity object or null if the given BoletoDTO object is null
	 */
	public BoletoEntity toEntity() {
		return BoletoEntity.builder()
				.id(this.id)
				.codigoBarras(this.codigoBarras)
				.situacaoBoleto(this.situacaoBoleto)
				.dataCriacao(this.dataCriacao)
				.dataAtualizacao(this.dataAtualizacao)
				.build();
	}

	/**
	 * Returns a Boleto Avro object from a BoletoEntity object.
	 *
	 * If the given BoletoEntity object is null, this method returns null.
	 *
	 * @param entity the BoletoEntity object to convert
	 * @return a Boleto Avro object or null if the given BoletoEntity object is null
	 */
	public static Boleto toAvro(BoletoEntity entity) {
		if (entity == null)
			return null;
		return Boleto.newBuilder()
				.setCodigoBarras(entity.getCodigoBarras())
				.setSituacaoBoleto(
						entity.getSituacaoBoleto() == null ? null
								: br.com.neves.avro.SituacaoBoleto.valueOf(entity.getSituacaoBoleto().name()))
				.build();
	}

}
