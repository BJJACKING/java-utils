package com.znb.java.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @author zhangnaibin@xiaomi.com
 * @time 2016-04-18 上午12:37
 */
public class NIOTest {
    public static void main(String[] args) {

    }

    public void byteRead(String fileName) throws IOException {
        File file = new File(fileName);
        FileInputStream in = new FileInputStream(fileName);
        byte[] buf = new byte[32];
        while (in.read() != -1) {
            in.read(buf);
            // process byte[]
        }
    }

    public void charRead(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader in = new FileReader(fileName);
        int t = in.read();
        while (t != -1) {
            System.out.println((char)t);
            t = in.read();
        }

        BufferedReader reader = new BufferedReader(in);
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    public void nio(String fileName) throws Exception{
        RandomAccessFile file = new RandomAccessFile(fileName, "rw");
        FileChannel channel = file.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(10);
        int bytesRead;
        while ((bytesRead = channel.read(buf)) != -1) {
            System.out.println(bytesRead);
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.println((char)buf.get());
            }
            buf.clear();
        }
    }

    public void select() throws Exception {
        Selector selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        ServerSocket socket = new ServerSocket();
        SelectionKey key = channel.register(selector, SelectionKey.OP_ACCEPT);
    }
}
