package com.nhjclxc.udpserver;

//import com.nhjclxc.udpserver.client.UdpClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tcp")
public class UdpTestController {

//    @Autowired
//    private UdpClient udpClient;

    @GetMapping("/client/send")
    public String clientSend(String msg) throws InterruptedException {
//        udpClient.send(msg, success -> callback(msg, success));
        return "clientSend ok." + msg;
    }

    public void callback(String msg, boolean success) {
        if (success) {
            System.out.println("Callback: Message sent successfully.");
        } else {
            System.err.println("Callback: Failed to send message.");
        }
    }

}
