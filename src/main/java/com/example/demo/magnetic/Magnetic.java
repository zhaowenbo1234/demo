package com.example.demo.magnetic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Magnetic {

    private final static Logger log = LoggerFactory.getLogger(Magnetic.class);
    private final static int PORT = 6666;

    public static void main(String[] args) {
        ServerSocket ss = null;
        Socket s = null;
        BufferedReader in;
        try {

            // 使用指定端口监听指定的端口 并绑定到本地 IP 地址
            ss = new ServerSocket(PORT,PORT,InetAddress.getByName("192.168.1.199"));


        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("server 启动成功！");
        try {
            s = ss.accept();
           // ss.getInetAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("client 连接成功");
        boolean flag = true;
        while (flag) {
            try {
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String msg = in.readLine();
                if (msg != null) {
                    System.out.println(msg);
                    log.info(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}