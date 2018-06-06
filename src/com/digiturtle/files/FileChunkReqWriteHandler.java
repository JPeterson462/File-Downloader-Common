package com.digiturtle.files;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedFile;

public class FileChunkReqWriteHandler extends SimpleChannelInboundHandler<ChunkedFile> {
	
	public interface FileOutput {
		
		public void startFile();
		
		public void write(byte[] bytes);
		
		public void endFile();
		
	}
	
	private FileOutput output;
	
	public FileChunkReqWriteHandler(FileOutput output) {
		this.output = output;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ChunkedFile file) throws Exception {
		ByteBufAllocator alloc = new PooledByteBufAllocator();
		output.startFile();
		while (!file.isEndOfInput()) {
			ByteBuf buf = file.readChunk(alloc);
			output.write(buf.array());
		}
		output.endFile();
	}

}
