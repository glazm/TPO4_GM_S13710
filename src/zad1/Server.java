package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args){

    }
    Server() throws IOException{
        String host = "localhost";
        int port = 50000;

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(host,port));

        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            selector.select();

            Set keys = selector.selectedKeys();

            Iterator iterator = keys.iterator();

            while (iterator.hasNext()){
                SelectionKey selectionKey = (SelectionKey) iterator.next();

                iterator.remove();

                if(selectionKey.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE);

                    continue;
                }
                if(selectionKey.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    continue;
                }
                if(selectionKey.isWritable()){
                    //SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    continue;
                }

            }
        }
    }
}
