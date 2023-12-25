package com.project.forestresourcesmanageapplication.responses;

import java.util.HashMap;

import org.json.JSONObject;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccessLogResponse {
    private int id;
    private String username;
    private String httpMethod;
    private String requestUrl;
    private int statusCode;
    private HashMap<String,Object> requestBody;
    private HashMap<String,Object> responseBody;
}
