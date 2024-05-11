package zad1;

import org.json.simple.JSONArray;
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
    private static Map<String,List<SelectionKey>> clientsKeys = new HashMap<String,List<SelectionKey>>();
    private static List<String> topics = new ArrayList<>();
    private static List<SelectionKey> allClientKeys = new ArrayList<>();
    private static Map<String,String> topicsNews = new HashMap<>();
    public static void main(String[] args) throws IOException {
        new Server();
    }
    Server() throws IOException{
        String host = "localhost";
        int port = 50000;
//        JSONParser jsonParser = new JSONParser();

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
//                    if(clientsKeys.containsValue(selectionKey)){
                    /////////////////////////////COS DZIALA
//                    if(!clientsKeys.isEmpty()) {
//                        clientsKeys.forEach(
//                                (key, value) -> {
//                                    System.out.println("Prawda: " +value+" lub "+selectionKey);
//                                }
//                        );
//                    }
////                    }
//                    selectionKey.interestOps(SelectionKey.OP_READ);
                    /////////////////////////////////COS DZIALA
//                    publishNews(selectionKey);
//                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
//
//                    if(clients.containsKey("testy")) {
//                        topicsNews.forEach(
//                                (key, value) -> {
////                                System.out.println(key+" news: "+value);
//
//
//                                    try {
//                                        JSONArray  prepareNews = (JSONArray) jsonParser.parse(clients.get(key));
//                                        Iterator<String> iter = prepareNews.iterator();
//                                        while (iter.hasNext()) {
//                                            String testTmp = iter.next();
//                                            System.out.println(testTmp + " =? " + selectionKey.toString());
//                                            if (testTmp.equals(selectionKey.toString())) {
//                                                System.out.println("Prawda");
//                                                //                            socketChannel.write(charset.encode("{\"" + topic + "\":\"" + news + "\"}"));
//                                            }
//                                        }
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }

//                   System.out.println(prepareNews.toString());
//                    socketChannel.write(charset.encode("{\"" + topic + "\":\"" + news + "\"}"));
//                                }
//                        );
//                    }
//                            }


//                    if(!socketChannel.isOpen()){return;}
//                    socketChannel.write(charset.encode("{\""+key+"\":\""+subTopic+"\"}"));
//                    topicsNews.forEach(
//                            (key, value) -> {
//                                System.out.println(key+" news: "+value);
//                            }
//                    );

                    continue;

                }

            }
        }
    }

    Charset charset = Charset.forName("UTF-8");
    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private StringBuffer stringBuffer = new StringBuffer();

    public void publishNews(SelectionKey kk)throws IOException{
//        SocketChannel cli = (SocketChannel) kk.channel();
//        ByteBuffer buf = (ByteBuffer) kk.attachment();
//
//        System.out.println(String.format("Wrinting into the channel %s", cli));
//        buf.flip();//prepare the buffer
//        buf.rewind();
//        topicsNews.forEach(
//                (key, value) -> {
//                    System.out.println(key+" news: "+value);
//                    try {
//                        cli.write(charset.encode(value));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//        );
//
//
//        if (buf.hasRemaining()) {
//            //If there is more content remaining, compact the buffer
//            buf.compact();
//        } else {
//            buf.clear();
//            kk.interestOps(SelectionKey.OP_READ);
//        }
    }
    public void updateTopic(){

    }

    public void readRequest(SocketChannel socketChannel, SelectionKey selectionKey) throws IOException {
        if (!socketChannel.isOpen()) {
            return;
        }
        System.out.println("To jest nowe czytanie: " + selectionKey);
//        boolean flag = true;
//        if (flag){
        /////////////////////////////////Póżniej
//            if (!allClientKeys.isEmpty()) {
//                for (SelectionKey kkk : allClientKeys) {
//                    System.out.println(kkk);
//                    if (!kkk.equals(selectionKey)) {
//                        if (!clientsKeys.isEmpty()) {
//                            for (String key : clientsKeys.keySet()) {
//                                List<SelectionKey> selectionKeys = clientsKeys.get(key);
//                                for (SelectionKey selKey : selectionKeys) {
//                                    if (!allClientKeys.contains(selKey)) {
//                                        System.out.println(allClientKeys);
//                                        List<SelectionKey> tmpList = selectionKeys;
//                                        System.out.println("Stara lista: " + key + " - " + tmpList);
//                                        tmpList.remove(selKey);
//                                        System.out.println("Nowa lista: " + key + " - " + tmpList);
//                                        clientsKeys.put(key, tmpList);
//                                        selKey.selector().close();
////                                        flag = false;
//                                    }
//
//                                }
//                            }
//                        }
//                    }
//
//
//                }
//            }
        /////////////////////////////////Póżniej
//        }
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

                /////////////////////////////////
                //Dodawać do nowej listy przy każdym nowym łączeniu selectionKey clientów
                String topicsJson = "{\"topics\":[";
                int i = 0;
                int size = topics.size();
                for(String topicPublished:topics){
                    i++;
                    if(size!=i) {
                        topicsJson = topicsJson.concat("\"" + topicPublished + "\",");
                    }else{
                        topicsJson = topicsJson.concat("\"" + topicPublished + "\"");
                    }
                }
                topicsJson = topicsJson.concat("]}");
                System.out.println(topicsJson);
                if(!allClientKeys.isEmpty()) {
                    for (SelectionKey sk : allClientKeys) {
                        SocketChannel sc = (SocketChannel) sk.channel();
                        sc.write(charset.encode(topicsJson));
                    }
                }
                ///////////////////////////////////
//                socketChannel.close();
//                socketChannel.socket().close();
            }
            else if (jsonObject.containsKey("removeTopic")){
                String topic =(String) jsonObject.get("removeTopic");
//                System.out.println(topic);
                topics.remove(topic);
                clientsKeys.remove(topic);
                System.out.println("Usunięto temat: "+topic);
                /////////////////////////////////
                //Dodawać do nowej listy przy każdym nowym łączeniu selectionKey clientów
                String topicsJson = "{\"topics\":[";
                int i = 0;
                int size = topics.size();
                for(String topicPublished:topics){
                    i++;
                    if(size!=i) {
                        topicsJson = topicsJson.concat("\"" + topicPublished + "\",");
                    }else{
                        topicsJson = topicsJson.concat("\"" + topicPublished + "\"");
                    }
                }
                topicsJson = topicsJson.concat("]}");
                System.out.println(topicsJson);
                if(!allClientKeys.isEmpty()) {
                    for (SelectionKey sk : allClientKeys) {
                        SocketChannel sc = (SocketChannel) sk.channel();
                        sc.write(charset.encode(topicsJson));
                    }
                }
                ///////////////////////////////////
//                socketChannel.close();
//                socketChannel.socket().close();
            }
            else if(jsonObject.containsKey("removeAllTopics")){
                if(jsonObject.get("removeAllTopics").equals("true")) {
                    topics.clear();
                    System.out.println("Tematy: " + topics);
                    socketChannel.close();
                    socketChannel.socket().close();
                }
            }
            else if(jsonObject.containsKey("bye")){
                if(jsonObject.get("bye").equals("true")) {
                    SelectionKey tempSelKey=null;
                    allClientKeys.remove(selectionKey);
                    /////////////////////////////////Póżniej
                    for(String key : clientsKeys.keySet()) {
//                        List<SelectionKey> selectionKeys = clientsKeys.get(key);
//                        for (SelectionKey selKey : selectionKeys) {
//                            if(selectionKey.equals(selKey)){
//                                tempSelKey = selKey;
////                                selectionKeys.remove(selectionKey);//trzeba usunąć klucz
//                                System.out.println("Zegnam: "+selectionKey);
//                            }
//
//                        }
                        String obj ="";
                        List<SelectionKey> list = new ArrayList<>();
//                        if(clientsKeys.containsKey(key)) {
//                    clientsKeys.forEach(
//                            (key, value) -> {
//                                System.out.println(key+" clients: "+value);
//                            }
//                    );
                            list =clientsKeys.get(key);
                            list.remove(selectionKey);
                            clientsKeys.put(key, list);
//                    System.out.println("Jestem");
//                        }
                    }

                    /////////////////////////////////Póżniej

                    socketChannel.close();
                    socketChannel.socket().close();
                    System.out.println("Żyję: "+ clientsKeys.keySet()+" "+allClientKeys);
                }
            }
            else if(jsonObject.containsKey("topicNews")){
                String news =(String) jsonObject.get("news");
                String topic = (String) jsonObject.get("topicNews");
                System.out.println("Wiadomości dotyczące "+topic+": "+ news);
//                socketChannel.close();
//                socketChannel.socket().close();


//                topicsNews.put("\""+topic+"\"","\""+news+"\"");
//
//                topicsNews.forEach(
//                        (key, value) -> {
//                            System.out.println(key+" news: "+value);
//                        }
//                );
                if(clientsKeys.containsKey(topic)) {
                    List<SelectionKey> selectionKeys = clientsKeys.get(topic);
                    for(SelectionKey selKey:selectionKeys){
                        if(selKey.selector().isOpen()) {
                            SocketChannel spreadNews = (SocketChannel) selKey.channel();
                            spreadNews.write(charset.encode("{\"" + topic + "\":\"" + news + "\"}"));
                        }
                    }
//                   JSONArray prepareNews = (JSONArray) jsonParser.parse(clients.get(topic));
//                   Iterator<String> iter = prepareNews.iterator();
//                    while(iter.hasNext()){
//                        String testTmp = iter.next();
//                        System.out.println(testTmp);
//                        SelectionKey kk =
//                        SocketChannel spreadNews =
//                        socketChannel.write(charset.encode("{\"" + topic + "\":\"" + news + "\"}"));
//                        if(testTmp.equals(selectionKey.toString())) {
//                            System.out.println("Prawda");
////                            socketChannel.write(charset.encode("{\"" + topic + "\":\"" + news + "\"}"));
//                        }
//                    }
//                   System.out.println(prepareNews.toString());
//                    socketChannel.write(charset.encode("{\"" + topic + "\":\"" + news + "\"}"));
                }
            }
            for(String topic : topics){
                System.out.println(topic);
            }

            System.out.println(selectionKey);

            if(jsonObject.containsKey("msg")) { ///also use for (un)subscribe respone
                if(jsonObject.get("msg").equals("getTopics")) {
                    allClientKeys.add(selectionKey);
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
                    objects = objects.concat(",\""+selectionKey+"\"]");
                    clients.put(subTopic, objects);
                }else{
                    clients.put(subTopic,"[\""+selectionKey+"\"]");
                }

                clients.forEach(
                        (key, value) -> {
                            System.out.println(key+" clients: "+value);
                        }
            );
                /////////////////////////Testy
                String obj ="";
                List<SelectionKey> list = new ArrayList<>();
                if(clientsKeys.containsKey(subTopic)) {
//                    clientsKeys.forEach(
//                            (key, value) -> {
//                                System.out.println(key+" clients: "+value);
//                            }
//                    );
                    list =clientsKeys.get(subTopic);
                    list.add(selectionKey);
                    clientsKeys.put(subTopic, list);
                    System.out.println("Jestem");
                }else{
                    list.add(selectionKey);
                    clientsKeys.put(subTopic,list);
                }

                clientsKeys.forEach(
                        (key, value) -> {
//                            System.out.println(key+" clients: "+value);
//                            JSONArray msg = (JSONArray) jsonObject.get(key);
//                            Iterator<String> iterator = msg.iterator();
//                            while (iterator.hasNext()) {
                                for(SelectionKey sk : value){
                                    System.out.println(key+" clients SelectionKey: " +sk);
                                }
//                            }

                        }
                );
                /////////////////////////Testy

                socketChannel.write(charset.encode("{\"sub\":\""+subTopic+"\"}"));
            }
            if(jsonObject.containsKey("unsubscribe")){
                String unsubTopic = jsonObject.get("unsubscribe").toString();
                String objects ="";
//                if(clients.containsKey(unsubTopic)) {
//                    objects = clients.get(unsubTopic);
//                    objects = objects.replaceAll("\""+selectionKey+"\"","");//in future also replace "," with "" if it's not last element
//                    clients.put(unsubTopic, objects);
//                }else{
//                    clients.put(unsubTopic,"[\""+selectionKey.toString()+"\"],");
//                    objects = clients.get(unsubTopic);
//                    objects = objects.concat("[\"TEST\"]");
//                    clients.put(unsubTopic,objects);
//                    clients.forEach(
//                            (key, value) -> {
//                                System.out.println(key+" clients: "+value);
//                            }
//                    );
//
//                    objects = clients.get(unsubTopic);
//                    objects = objects.replaceAll("\""+selectionKey+"\"","");
//                    clients.put(unsubTopic, objects);
//                    System.out.println("Nie ma takiego tematu");
//                }
//
//                clients.forEach(
//                        (key, value) -> {
//                            System.out.println(key+" clients: "+value);
//                        }
//                );

                /////////////////////////Testy
                String obj ="";
                List<SelectionKey> list = new ArrayList<>();
                if(clientsKeys.containsKey(unsubTopic)) {
//                    clientsKeys.forEach(
//                            (key, value) -> {
//                                System.out.println(key+" clients: "+value);
//                            }
//                    );
                    list =clientsKeys.get(unsubTopic);
                    list.remove(selectionKey);
                    clientsKeys.put(unsubTopic, list);
//                    System.out.println("Jestem");
                }

                clientsKeys.forEach(
                        (key, value) -> {
//                            System.out.println(key+" clients: "+value);
//                            JSONArray msg = (JSONArray) jsonObject.get(key);
//                            Iterator<String> iterator = msg.iterator();
//                            while (iterator.hasNext()) {
                            for(SelectionKey sk : value){
                                System.out.println(key+" clients SelectionKey: " +sk);
                            }
//                            }

                        }
                );
                /////////////////////////Testy
                socketChannel.write(charset.encode("{\"unsub\":\""+unsubTopic+"\"}"));
            }

        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }
//        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

}
