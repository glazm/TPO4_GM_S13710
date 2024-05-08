package zad1;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

public class Server {
    private static Map<String,String> clients = new HashMap<String,String>();
    private static List<String> topics = new ArrayList<>();
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

                    readRequest(socketChannel, selectionKey);

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

    public void readRequest(SocketChannel socketChannel, SelectionKey selectionKey) throws IOException {
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

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(request);
            if(jsonObject.containsKey("addTopic")){
                String topic =(String) jsonObject.get("addTopic");
//                System.out.println(topic);
                topics.add(topic);
                socketChannel.close();
                socketChannel.socket().close();
            }
            else if (jsonObject.containsKey("removeTopic")){
                String topic =(String) jsonObject.get("removeTopic");
//                System.out.println(topic);
                topics.remove(topic);
                socketChannel.close();
                socketChannel.socket().close();
            } else if(jsonObject.containsKey("topicNews")){
                String news =(String) jsonObject.get("news");
                String topic = (String) jsonObject.get("topicNews");
                System.out.println("Wiadomości dotyczące "+topic+": "+ news);
                socketChannel.close();
                socketChannel.socket().close();
            }
            for(String topic : topics){
                System.out.println(topic);
            }

            System.out.println(selectionKey);

            if(jsonObject.containsKey("msg")) { ///also use for (un)subscribe respone
                if(jsonObject.get("msg").equals("getTopics")) {
                    String topicsJson = "{\"topics\":[";
                    int i = 0;
                    int size = topics.size();
                    for(String topic:topics){
                        i++;
                        if(size!=i) {
                            topicsJson = topicsJson.concat("\"" + topic + "\",");
                        }else{
                            topicsJson = topicsJson.concat("\"" + topic + "\"");
                        }
                    }
                    topicsJson = topicsJson.concat("]}");
                    System.out.println(topicsJson);
                    socketChannel.write(charset.encode(topicsJson));
                }
                socketChannel.write(charset.encode(CharBuffer.wrap(stringBuffer)));
            }
            if(jsonObject.containsKey("subscribe")){
                String subTopic = jsonObject.get("subscribe").toString();
                String objects ="";
                if(clients.containsKey(subTopic)) {
                    objects = clients.get(subTopic);
                    objects = objects.replaceAll("]","");
                    objects = objects.concat(",\""+selectionKey.toString()+"\"]");
                    clients.put(subTopic, objects);
                }else{
                    clients.put(subTopic,"[\""+selectionKey.toString()+"\"]");
                }

                clients.forEach(
                        (key, value) -> {
                            System.out.println(key+" clients: "+value);
                        }
            );
                socketChannel.write(charset.encode("{\"sub\":\""+subTopic+"\"}"));
            }
            if(jsonObject.containsKey("unsubscribe")){
                String unsubTopic = jsonObject.get("unsubscribe").toString();
                String objects ="";
                if(clients.containsKey(unsubTopic)) {
                    objects = clients.get(unsubTopic);
                    objects = objects.replaceAll("\""+selectionKey+"\"","");//in future also replace "," with "" if it's not last element
                    clients.put(unsubTopic, objects);
                }else{
                    clients.put(unsubTopic,"[\""+selectionKey.toString()+"\"],");
                    objects = clients.get(unsubTopic);
                    objects = objects.concat("[\"TEST\"]");
                    clients.put(unsubTopic,objects);
                    clients.forEach(
                            (key, value) -> {
                                System.out.println(key+" clients: "+value);
                            }
                    );

                    objects = clients.get(unsubTopic);
                    objects = objects.replaceAll("\""+selectionKey+"\"","");
                    clients.put(unsubTopic, objects);
                    System.out.println("Nie ma takiego tematu");
                }

                clients.forEach(
                        (key, value) -> {
                            System.out.println(key+" clients: "+value);
                        }
                );
                socketChannel.write(charset.encode("{\"unsub\":\""+unsubTopic+"\"}"));
            }

        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}
