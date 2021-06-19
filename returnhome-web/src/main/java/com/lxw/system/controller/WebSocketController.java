package com.lxw.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping("/websocket")
public class WebSocketController {

    @Autowired
    private WebSocket webSocket;

    @PostMapping("/sendAllWebSocket")
    public String test(){
        webSocket.sendAllMessage("群发消息");
        return "success";
    }

    @PostMapping("/sendOneWebSocket")
    public String sendOneWebSocket(@RequestParam("id") Integer id) {
        webSocket.sendOneMessage("lsnweb", id+"");
        return "success";
    }



}

