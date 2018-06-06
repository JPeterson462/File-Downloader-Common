package com.digiturtle.files;

import java.io.RandomAccessFile;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;

public class FileServer {
	
	private ServerBootstrap bootstrap;
	
	private EventLoopGroup group, slaveGroup;
	
	private int port;
	
	public FileServer(int port) {
		this.port = port;
	}
	
	public void start() throws InterruptedException {
		group = new NioEventLoopGroup();
		slaveGroup = new NioEventLoopGroup();
		bootstrap = new ServerBootstrap();
		bootstrap.group(group, slaveGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new ChunkedWriteHandler());
				ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {

					@Override
					protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
						RandomAccessFile raf = null;
						long length = -1;
						try {
							raf = new RandomAccessFile(msg, "r");
							length = raf.length();
						} catch (Exception e) {
							ctx.writeAndFlush("ERR: " + e.getClass().getSimpleName() + ": " + e.getMessage() + "\n");
							return;
						} finally {
							if (length < 0 && raf != null) {
								raf.close();
							}
						}
						ctx.write("OK: " + raf.length() + "\n");
						ctx.write(new ChunkedFile(raf));
						ctx.writeAndFlush("\n");
					}
					
				});
			}
		}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.bind(port).sync();
	}
	
	public void shutdown() {
		group.shutdownGracefully();
		slaveGroup.shutdownGracefully();
	}

}
