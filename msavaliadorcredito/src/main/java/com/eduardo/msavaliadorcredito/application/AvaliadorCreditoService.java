package com.eduardo.msavaliadorcredito.application;

import com.eduardo.msavaliadorcredito.domain.model.CartaoCliente;
import com.eduardo.msavaliadorcredito.domain.model.DadosCliente;
import com.eduardo.msavaliadorcredito.domain.model.SituacaoCliente;
import com.eduardo.msavaliadorcredito.infra.clients.CartoesResourceClient;
import com.eduardo.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf){
        //obterDadosCliente -MSCLIENTES
        //obter cartoes do cliente -MSCARTOES

        ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
        ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesClient.getCartoesByCliente(cpf);

        return SituacaoCliente.builder()
                .cliente(dadosClienteResponse.getBody())
                .cartoes(cartoesResponse.getBody())
                .build();
    }
}
