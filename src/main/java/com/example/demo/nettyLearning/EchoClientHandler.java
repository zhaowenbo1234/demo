package com.example.demo.nettyLearning;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 客户端的工作内容：
 * 连接服务器
 * 发送信息
 * 发送的每个信息，等待和接收从服务器返回的同样的信息
 * 关闭连接
 */
@ChannelHandler.Sharable //1.@Sharable标记这个类的实例可以在 channel 里共享
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",//2.当被通知该 channel 是活动的时候就发送信息
                CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));    //3 记录接收到的消息
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {//4.记录日志错误并关闭 channel
        cause.printStackTrace();
        ctx.close();
    }
}
