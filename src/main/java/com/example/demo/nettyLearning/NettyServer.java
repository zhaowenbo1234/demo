package com.example.demo.nettyLearning;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    private final int PORT = 10000;

    public static void main(String[] args) throws Exception {
        new NettyServer().start();

    }
    public void start() throws Exception {

        NioEventLoopGroup group = new NioEventLoopGroup();//3.创建 EventLoopGroup
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)//4.创建 ServerBootstrap
                    .channel(NioServerSocketChannel.class)//5.指定使用 NIO 的传输 Channel
                    .localAddress(PORT)//6.设置 socket 地址使用所选的端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {//7.添加 EchoServerHandler 到 Channel 的 ChannelPipeline

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    System.out.println("连接成功");
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    String str = msg.toString();

                                    System.out.println(str);
                                }


                            });
                        }
                    });

            ChannelFuture f = b.bind().sync();//8.绑定的服务器;sync 等待服务器关闭
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();//9.关闭 channel 和 块，直到它被关闭

        } finally {
            group.shutdownGracefully().sync();//10.关机的 EventLoopGroup，释放所有资源。
        }
    }


}
