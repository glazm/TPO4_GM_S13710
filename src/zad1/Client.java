package zad1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class Client {
    public static void main(String[] args) throws IOException, ParseException {
        String topics = "";
        SocketChannel socketChannel = null;
        String server = "localhost";
        int serverPort = 50000;
        JSONParser jsonParser = new JSONParser();

        try{
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(server, serverPort));

            while(!socketChannel.finishConnect()){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Połączono");

        Charset charset = Charset.forName("UTF-8");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        CharBuffer charBuffer = null;

        socketChannel.write(charset.encode("{\"msg\":\"getTopics\"}\n"));

        int i =0;

        while(true){
            byteBuffer.clear();
            int readBytes = socketChannel.read(byteBuffer);

            if(readBytes == 0){continue;}
            else if(readBytes == -1){break;}
            else {
                byteBuffer.flip();

                charBuffer = charset.decode(byteBuffer);
                String serverResponse = charBuffer.toString();

                JSONObject jsonObject = (JSONObject) jsonParser.parse(serverResponse);
                if(jsonObject.containsKey("topics")){
                    JSONArray topicsArray =(JSONArray) jsonObject.get("topics");
                    Iterator<String> iter = topicsArray.iterator();
                    int count = 0;
                    int size = topicsArray.size();
                    while(iter.hasNext()){
                        count++;
                        if(size!=count){
                            topics = topics.concat(iter.next()+", ");
                        }else {
                            topics = topics.concat(iter.next());
                        }
//                        System.out.println(iter.next());
                    }
                    if(!topics.isEmpty()) {
                        System.out.println("Lista dostępnych tematów: " + topics);
                    }else{
                        System.out.println("Brak tematów");
                    }
                }

                System.out.println("Server: "+serverResponse);
                charBuffer.clear();

            }
            if(i ==0) {
                System.out.println("stało się");
                i++;
                socketChannel.write(charset.encode("{\"msg\":\"hi2\"}\n"));
            }

        }

    }

}
