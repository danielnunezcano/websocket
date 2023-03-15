package com.websocket.websocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class GreetingController {

  private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

  private final SimpMessagingTemplate messagingTemplate;

  public final Map<String, HelloMessage> hashMap = new HashMap<>();

  @MessageMapping("/hello")
    @SendTo("/topic/greetings")
  public Greeting greeting(HelloMessage message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
    String sessionId = headerAccessor.getSessionId();
    hashMap.put(sessionId,message);
    return new Greeting(hashMap.values().stream().collect(Collectors.toList()));
  }

  @MessageMapping("/init")
  @SendTo("/topic/greetings")
  public Greeting init() throws Exception {
    return returnHashMap();
  }

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectEvent event) {
    logger.info("Received a new web socket connection.");
  }

  @EventListener
  @SendTo("/topic/greetings")
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    String sessionId = event.getSessionId();
    hashMap.remove(sessionId);
    logger.info("Session disconnected: {}", sessionId);
    messagingTemplate.convertAndSend("/topic/greetings", returnHashMap());
  }

  private Greeting returnHashMap(){
    return new Greeting(hashMap.values().stream().collect(Collectors.toList()));
  }

}