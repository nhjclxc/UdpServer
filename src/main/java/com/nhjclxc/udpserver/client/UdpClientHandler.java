package com.nhjclxc.udpserver.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        // 接收服务器响应
        String response = packet.content().toString(CharsetUtil.UTF_8);
        System.out.println("Client received: " + response);


        // todo 进行业务处理


        long timeMillis = System.currentTimeMillis();
        if (timeMillis % 2 == 0) {
            // 回应服务器
            String responseTo = "响应服务器：" + timeMillis;
            ByteBuf byteBuf = Unpooled.copiedBuffer(responseTo, CharsetUtil.UTF_8);
            DatagramPacket responsePacket = new DatagramPacket(byteBuf, packet.sender());
            ctx.writeAndFlush(responsePacket);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
