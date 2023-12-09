package com.eduardo.mscartoes.infra.mqueue;

import com.eduardo.mscartoes.domain.Cartao;
import com.eduardo.mscartoes.domain.ClienteCartao;
import com.eduardo.mscartoes.domain.DadosSolicitacaoEmissaoCartao;
import com.eduardo.mscartoes.infra.CartaoRepository;
import com.eduardo.mscartoes.infra.ClienteCartaoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmissaoCartaoSubscriber {

    private final CartaoRepository cartaoRepository;
    private final ClienteCartaoRepository clienteCartaoRepository;
    @RabbitListener(queues = "${mq.queues.emissao-cartoes}")
    public void receberSolicitacaoEmissao(@Payload String payload){
       try{
        var mapper = new ObjectMapper();

        DadosSolicitacaoEmissaoCartao dados = mapper.readValue(payload, DadosSolicitacaoEmissaoCartao.class);
        Cartao cartao = cartaoRepository.findById(dados.getIdCartao()).orElseThrow();

        ClienteCartao clienteCartao = new ClienteCartao();
        clienteCartao.setCartao(cartao);
        clienteCartao.setCpf(dados.getCpf());
        clienteCartao.setLimite(dados.getLimiteLiberado());

        clienteCartaoRepository.save(clienteCartao);

       }catch (Exception e){
           e.printStackTrace();
       }
    }
}