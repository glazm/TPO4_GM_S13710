package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Publisher {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = null;
        String server = "localhost";
        int serverPort = 50000;

        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(server, serverPort));

            while (!socketChannel.finishConnect()) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Połączono");

        Charset charset = Charset.forName("UTF-8");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        CharBuffer charBuffer = null;

//        socketChannel.write(charset.encode("Cześć jestem Adminem\n"));
//        addTopic("Sport", socketChannel, charset);
//        Polityka Sport Plotki Muzyka
//        removeTopic("Muzyka", socketChannel, charset);
        topicNews("Muzyka","Koncert zepsołu w Warszawie", socketChannel, charset);
    }

    public static void addTopic(String topic, SocketChannel socketChannel, Charset charset) throws IOException {
        String topicJson = "{\"addTopic\":\"" + topic + "\"}\n";
        socketChannel.write(charset.encode(topicJson));
    }

    public static void removeTopic(String topic, SocketChannel socketChannel, Charset charset) throws IOException {
        String topicJson = "{\"removeTopic\":\"" + topic + "\"}\n";
        socketChannel.write(charset.encode(topicJson));
    }

    public static void topicNews(String topic, String news, SocketChannel socketChannel, Charset charset) throws IOException {
        String newsJson = "{\"topicNews\":\"" + topic + "\",\"news\":\""+news+"\"}\n";
        socketChannel.write(charset.encode(newsJson));
    }
}
