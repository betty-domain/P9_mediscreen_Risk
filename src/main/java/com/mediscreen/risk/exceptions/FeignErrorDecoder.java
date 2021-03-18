package com.mediscreen.risk.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

public class FeignErrorDecoder implements ErrorDecoder {

    private static final Logger logger = LogManager.getLogger(FeignErrorDecoder.class);

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String str, Response response) {

        Optional<ExceptionMessage> originalException = getOriginalException(response);

        switch (response.status()) {
            case 400: {
                return new BadRequestException(originalException.map(ExceptionMessage::getMessage).orElse("Bad Request"));
            }
            case 404: {
                return new ObjectNotFoundException(originalException.map(ExceptionMessage::getMessage).orElse("Object not Found"));
            }
            default:
                return defaultErrorDecoder.decode(str, response);
        }
    }

    /**
     * Get original Exception from FeignException
     *
     * @param response response containing feignException
     * @return Exception message if exist, null otherwise
     */
    private Optional<ExceptionMessage> getOriginalException(Response response) {
        Optional<ExceptionMessage> exceptionMessage = Optional.empty();
        try {
            String body = new BufferedReader(response.body().asReader(StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            ObjectMapper mapper = new ObjectMapper();
            exceptionMessage = Optional.of(mapper.readValue(body, ExceptionMessage.class));

        } catch (IOException ioException) {
            logger.error("Error during FeignException Decoder : " + ioException.getMessage());
        }
        return exceptionMessage;
    }
}
