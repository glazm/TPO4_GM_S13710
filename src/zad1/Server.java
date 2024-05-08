package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args) throws IOException {
        new Server();
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

                    readRequest(socketChannel);

                    continue;
                }
                if(selectionKey.isWritable()){
                    //SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    continue;
                }

            }
        }
    }

    Charset charset = Charset.forName("UTF-8");
    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private StringBuffer stringBuffer = new StringBuffer();

    public void readRequest(SocketChannel socketChannel) throws IOException {
        if(!socketChannel.isOpen()){return;}

        stringBuffer.setLength(0);
        byteBuffer.clear();

        try{
            readLoop:
            while (true){
                int size = socketChannel.read(byteBuffer);

                if(size > 0){
                    byteBuffer.flip();
                    CharBuffer charBuffer = charset.decode(byteBuffer);

                    while (charBuffer.hasRemaining()){
                        char character = charBuffer.get();
                        if(character == '\r' || character == '\n'){break readLoop;}
                        else {
                            stringBuffer.append(character);
                        }
                    }
                }

            }

            String request = stringBuffer.toString();
            System.out.println(request);


//            socketChannel.write(charset.encode(CharBuffer.wrap(stringBuffer)));
            socketChannel.close();
            socketChannel.socket().close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
