package com.websocket.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BasicController {

  private final GrettingService service;

  @PostMapping("/app/values")
  @MessageMapping("/values")
  @SendTo("/topic/greetings")
  Greeting newEmployee(@RequestBody String value) {
    return service.toValue(value);
  }

}