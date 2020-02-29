package xyz.majexh.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import xyz.majexh.workflow.exceptions.ExceptionEnum;

import java.util.HashMap;

public class ResEntity {

    @Data
    public static class Entity<T> {
        private HttpStatus status;
        private String message;
        private T data;
    }

    private static<T> HashMap<String, Object> createPayload(int status, String message, T data) {
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("status", status);
        resMap.put("message", message);
        resMap.put("data", data);
        return resMap;
    }

    public static<T> ResponseEntity<HashMap<String, Object>> okDefault(T data) {
        HashMap<String, Object> resMap = createPayload(200, "success", data);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    public static<T> ResponseEntity<HashMap<String, Object>> error(HttpStatus status, String message, T data) {
        HashMap<String, Object> resMap = createPayload(status.value(), message, data);
        return new ResponseEntity<>(resMap, status);
    }

    public static<T> ResponseEntity<HashMap<String, Object>> error(ExceptionEnum exceptionEnum) {
        HashMap<String, Object> resMap = createPayload(exceptionEnum.getStatus().value(), exceptionEnum.getMessage(), null);
        return new ResponseEntity<>(resMap, exceptionEnum.getStatus());
    }
}
