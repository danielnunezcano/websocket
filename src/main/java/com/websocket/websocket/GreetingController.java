package com.websocket.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GreetingController {

  private final List<String> lista = new ArrayList<>();

  @MessageMapping("/hello")
    @SendTo("/topic/greetings")
  public Greeting greeting(HelloMessage message) throws Exception {
    lista.add(message.getName());
    return new Greeting(message.getName().concat(":connect"));
  }

  @MessageMapping("/bye")
  @SendTo("/topic/greetings")
  public Greeting greeting2(HelloMessage message) throws Exception {
    lista.remove(lista.indexOf(message.getName()));
    return new Greeting(message.getName().concat(":disconnect"));
  }

  @MessageMapping("/init")
  @SendTo("/topic/greetings")
  public Greeting init() throws Exception {
    final String output = lista.stream()
            .collect(Collectors.joining(", "));
    return new Greeting(output);
  }

}