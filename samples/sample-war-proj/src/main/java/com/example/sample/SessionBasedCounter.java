package com.example.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SessionBasedCounter {
    @Autowired
    private BuildProperties buildProperties;
    
    @RequestMapping("/session")
    public String currentCount(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(true);
        return "Counter : " + getCurrentCountFromSession(httpSession) + ", " +  
                "Build: " + buildProperties.getVersion() + ", " +
                "Machine: " + getMachine();
    }

    private String getMachine() {
        try {
            InetAddress localMachine = InetAddress.getLocalHost();
            return localMachine.getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    private String getCurrentCountFromSession(HttpSession httpSession) {
        AtomicLong counter = (AtomicLong)httpSession.getAttribute("counter");
        if (counter == null) {
            counter = new AtomicLong();
            httpSession.setAttribute("counter", counter);
        }
        return counter.incrementAndGet() + "";
    }
}
