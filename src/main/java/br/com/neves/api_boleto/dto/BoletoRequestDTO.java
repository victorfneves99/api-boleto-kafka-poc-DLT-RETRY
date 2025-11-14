package br.com.neves.api_boleto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoletoRequestDTO {

    @NotNull(message = "O código de barras não pode ser nulo")
    @NotBlank(message = "O código de barras não pode ser vazio")
    private String codigoBarras;
}
