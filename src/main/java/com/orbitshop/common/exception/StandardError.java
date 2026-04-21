package com.orbitshop.common.exception;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

/**
 * Represents an RFC 7807 compliant error response.
 */
public record StandardError(
        //URI que identifica o tipo do erro
        URI type,
        //Resumo curto do erro
        String title,
        //Código HTTP.
        Integer status,
        //Mensagem detalhada para o cliente.
        String detail,
        //URI da requisição que gerou o erro.
        URI instance,
        //Momento do erro (opcional, mas útil).
        Instant timestamp,
        //Permite incluir extensões como errors de validação ou correlationId.
        Map<String, Object> additionalProperties
) { }