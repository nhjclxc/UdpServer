package com.nhjclxc.udpserver.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 实现 UDP 服务器
 */
@Component
public class UdpServer {

    @Value("${tcp.server.port}")
    private Integer port;

    private ChannelFuture future;

    public UdpServer() { }

    /**
     * 初始化 UdpServer
     */
    @PostConstruct
    public void initUdpServr() throws InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        protected void initChannel(DatagramChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder(), new StringEncoder(), new UdpServerHandler());
                        }
                    });
            future = bootstrap.bind(port).sync();
            System.out.println("UDP Server started on port: " + port);
            future.channel().closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }

//        // 开启子线程加载初始化数据，加快项目启动
//        new Thread(() -> {
//        }).start();

    }



//    public static void main(String[] args) throws InterruptedException {
//        System.setProperty("io.netty.noUnsafe", "true");
//
//
//        // 禁用 JUL 日志输出
//        Logger.getLogger("io.netty").setLevel(Level.OFF);
//        // 禁用所有 Netty 日志
//        new UdpServer(8888).start();
//    }

}
