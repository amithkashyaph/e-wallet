package com.project.ApiGateway.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class APIGatewayController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/user/create")
    public JSONObject createUser(@RequestBody JSONObject jsonObject) {
        return restTemplate.postForEntity("http://localhost:5001/user/create", jsonObject, JSONObject.class).getBody();
    }
}
