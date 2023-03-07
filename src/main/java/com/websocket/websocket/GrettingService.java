package com.websocket.websocket;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service
public class GrettingService {

    public Greeting toValue(final String value){
        return new Greeting(value,55);
    }
}
