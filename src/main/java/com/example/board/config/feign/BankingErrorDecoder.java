package com.example.board.config.feign;

import com.example.board.rest.dto.feign.ErrorDto;
import com.example.board.rest.errorController.exception.BoardAppExternalMicroserviceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class BankingErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        ErrorDto error;
        try (Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {
            error = objectMapper.readValue(reader, ErrorDto.class);
        } catch (IOException e) {
            return new IOException(e);
        }

        return new BoardAppExternalMicroserviceException(error.getMessage());
    }
}