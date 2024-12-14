package com.nhjclxc.udpserver.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 * 处理消息的 Handler
 */
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
        String message = packet.content().toString(CharsetUtil.UTF_8);
        System.out.println("Received: " + message);

        // 回应客户端
        String response = "回应客户端消息 [" + message + "]";
        ByteBuf byteBuf = Unpooled.copiedBuffer(response, CharsetUtil.UTF_8);
        DatagramPacket responsePacket = new DatagramPacket(byteBuf, packet.sender());
        ctx.writeAndFlush(responsePacket);

//        ChannelHandlerPool.saveChannelByToken("", ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
