package com.digiturtle.files;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.digiturtle.files.FileChunkReqWriteHandler.FileOutput;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;

public class FileClient {
	
	private Bootstrap bootstrap;
	
	private EventLoopGroup group;
	
	private ChannelFuture futureChannel;
	
	private String hostName;
	
	private int port;
	
	private boolean connected = false;

	private ArrayList<byte[]> data = new ArrayList<>();
	
	private boolean reading = false;
	
	public FileClient(String hostName, int port) {
		this.hostName = hostName;
		this.port = port;
	}
	
	public void connect() {
		bootstrap = new Bootstrap();
		group = new NioEventLoopGroup();
		bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new ChunkedWriteHandler());
				p.addLast(new FileChunkReqWriteHandler(new FileOutput() {

					@Override
					public void startFile() {
						data.clear();
					}

					@Override
					public void write(byte[] bytes) {
						data.add(bytes);
					}

					@Override
					public void endFile() {
						reading = false;
					}
					
				}));
			}
		});
		try {
			futureChannel = bootstrap.connect(hostName, port).sync();
			connected = true;
		} catch (InterruptedException e) {
			
		}
	}
	
	public void downloadFile(String guid, OutputStream output) throws IOException {
		reading = true;
		futureChannel.channel().write(guid);
		while (reading) {
			// Wait
		}
		for (int i = 0; i < data.size(); i++) {
			output.write(data.get(i));
			output.flush();
		}
		data.clear();
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void close() {
		futureChannel.channel().closeFuture();
		group.shutdownGracefully();
	}
	
	public void send(final ChunkedFile file) {
		futureChannel.channel().writeAndFlush(file);
	}

}
