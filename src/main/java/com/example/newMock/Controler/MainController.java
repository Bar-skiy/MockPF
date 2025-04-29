package com.example.newMock.Controler; // Пакет, в котором находится контроллер

import com.example.newMock.Model.RequestDTO; // Импорт запроса DTO
import com.example.newMock.Model.ResponseDTO; // Импорт ответа DTO
import com.fasterxml.jackson.databind.ObjectMapper; // Для конвертации объектов в JSON
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal; // Для работы с деньгами и числами высокой точности
import java.math.RoundingMode;

@RestController // Говорит Spring, что это REST-контроллер (обрабатывает HTTP-запросы)
public class MainController {

    private final Logger log = LoggerFactory.getLogger(MainController.class); // Логгер для вывода сообщений

    ObjectMapper mapper = new ObjectMapper(); // Объект для преобразования объектов в JSON и обратно

    @PostMapping(value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE, // Контроллер возвращает JSON
            consumes = MediaType.APPLICATION_JSON_VALUE) // Контроллер принимает JSON
    public Object postBalances(@RequestBody RequestDTO requestDTO) { // Метод обрабатывает POST-запрос с телом типа RequestDTO
        try {
            String clientId = requestDTO.getClientId(); // Извлекаем clientId из запроса
            char firstDigit = clientId.charAt(0); // Берем первую цифру clientId

            BigDecimal maxLimit; // Переменная для хранения лимита
            String currency;     // Переменная для хранения валюты

            // Определяем maxLimit в зависимости от первой цифры clientId
            if (firstDigit == '8') {
                maxLimit = new BigDecimal(2000);
                 currency = "US";

            } else if (firstDigit == '9') {
                maxLimit = new BigDecimal(1000);
                currency = "EUR";
            } else {
                maxLimit = new BigDecimal(10000);
                currency = "RUB";
            }
            BigDecimal randomBalance = getRandomBalance(new BigDecimal(0), maxLimit);

            ResponseDTO responseDTO = new ResponseDTO(); // Создаем объект ответа

            // Заполняем поля ответа данными
            responseDTO.setRqUID(requestDTO.getRqUID());
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(currency);
            responseDTO.setBalance(randomBalance);
            responseDTO.setMaxLimit(maxLimit);

            // Логируем запрос и ответ в формате JSON
            log.info("\n\n**************** RequestDTO ****************\n{}",
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.info("\n\n**************** ResponseDTO ****************\n{}",
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));


            return responseDTO; // Возвращаем ответ клиенту

        } catch (Exception e) { // Если в процессе что-то пошло не так
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Возвращаем 400 и сообщение об ошибке
        }
    }
    private BigDecimal getRandomBalance(BigDecimal min, BigDecimal max) {
        BigDecimal randomBigDecimal = min.add(
                BigDecimal.valueOf(Math.random()).multiply(max.subtract(min))
        );
        return randomBigDecimal.setScale(2, RoundingMode.HALF_UP);
    }



}