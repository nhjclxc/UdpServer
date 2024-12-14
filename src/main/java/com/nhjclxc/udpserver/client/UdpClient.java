package com.nhjclxc.udpserver.client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

/**
 * 实现 UDP 客户端
 */
@Component
public class UdpClient {

    @Value("${tcp.client.host}")
    private String host;
    @Value("${tcp.client.port}")
    private Integer port;

    private ChannelFuture future;

    public UdpClient() { }

    /**
     * 初始化 UdpClient
     */
//    @PostConstruct
    public void initUdpServr() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            // 定义客户端
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        protected void initChannel(DatagramChannel ch) throws Exception {
                            ch.pipeline().addLast(new UdpClientHandler());
                        }
                    });
            // 连接
            future = bootstrap.connect(new InetSocketAddress(host, port)).sync();

            System.out.println("UDP Client started, flag = " + future.isSuccess());

            // 等待关闭
            future.channel().closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void send(String msg, Consumer<Boolean> callback) throws InterruptedException {
        if (null == msg || "".equals(msg)) {
            callback.accept(false);
            return;
        }

        // 构建数据包
        DatagramPacket packet = new DatagramPacket(
                Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8),
                new InetSocketAddress(host, port)
        );

        // 发送消息给服务器
        future.channel().writeAndFlush(packet).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("Message sent successfully.");
            } else {
                System.err.println("Message sending failed: " + future.cause().getMessage());
            }
            callback.accept(future.isSuccess()); // 异步返回结果
        }).sync();
    }


//    public static void main(String[] args) throws InterruptedException {
//        System.setProperty("io.netty.noUnsafe", "true");
//
//        // 禁用 JUL 日志输出
//        Logger.getLogger("io.netty").setLevel(Level.OFF);
//        new UdpClient("127.0.0.1", 8888).start();
//    }

}
