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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Client {
    public List<String> myTopicsGui = new ArrayList<>();
    public static List<String> myTopics = new ArrayList<>();
    public static List<String> topicsList = new ArrayList<>();
    private static SocketChannel socketChannel = null;
    private static Charset charset = Charset.forName("UTF-8");
    public static ClientGui gui;
    public static boolean flag = true;
    public Client(){
        gui=new ClientGui(this);
    }
    public static void main(String[] args) throws IOException, ParseException {
        new Client();
//        String topics = "";
        //When Gui is ready add subscribed topics list, changes with responses from server
//        SocketChannel socketChannel = null;
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

        while(flag){
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
//                    List<String> tmp = new ArrayList<>();
//                    for (String s:topicsList){
//                        tmp.add(s);
//                    }
                    topicsList.clear();
                    JSONArray topicsArray =(JSONArray) jsonObject.get("topics");
                    Iterator<String> iter = topicsArray.iterator();
//                    int count = 0;
//                    int size = topicsArray.size();
                    while(iter.hasNext()){
                        String topic = iter.next();
                        addTopic(topic);
//                        count++;
//                        if(size!=count){
//                            topics = topics.concat(topic+", ");
//                        }else {
//                            topics = topics.concat(topic);
//                        }
//                        System.out.println(iter.next());
                    }
//                    if(!topics.isEmpty()) {
                        gui.updateTopics();
//                        System.out.println("Lista dostępnych tematów: " + topics);
//                    }else{
//                        System.out.println("Brak tematów");
//                    }
                }
                for(String subject: myTopics){
                    if(jsonObject.containsKey(subject)){
                        String subjectNews =(String) jsonObject.get(subject);
                        gui.newsText.setText("["+subject+" news ]: "+subjectNews);
                    }
                }
//                if(jsonObject.containsKey("sub")){
//
//                }

                System.out.println("Server: "+serverResponse);
                charBuffer.clear();

            }
            if(i ==0) {
                System.out.println("stało się");
                i++;
//                socketChannel.write(charset.encode("{\"msg\":\"hi2\"}\n"));
//                subscribeToTopic("Sport");
//                subscribeToTopic("Sport",socketChannel, charset);
//                unsubscribeToTopic("Sport",socketChannel, charset);
            }

        }

    }
    public static void addTopic(String topic) throws IOException {

        topicsList.add(topic);
            String topicJson = "{\"addTopic\":\"" + topic + "\"}\n";
//            System.out.println(topicJson);
//            socketChannel.write(charset.encode(topicJson));

    }
    public void subscribeToTopic(String topic) throws IOException {
        if(!myTopics.contains(topic)) {
            String topicJson = "{\"subscribe\":\"" + topic + "\"}\n";
            socketChannel.write(charset.encode(topicJson));
            myTopics.add(topic);
            myTopicsGui.add(topic);
        }
        System.out.println("Moje tematy: "+myTopics);
    }
    public void unsubscribeToTopic(String topic) throws IOException {
        if(myTopics.contains(topic)){
            String topicJson = "{\"unsubscribe\":\"" + topic + "\"}\n";
            socketChannel.write(charset.encode(topicJson));
            myTopics.remove(topic);
            myTopicsGui.remove(topic);
        }
        System.out.println("Moje tematy: "+myTopics);
    }
    public void removeFromServer() throws IOException {
        String json = "{\"bye\":\"true\"}\n";
        socketChannel.write(charset.encode(json));

    }
    public void closingClient() throws IOException {
        flag=false;
            socketChannel.close();
            socketChannel.socket().close();

    }

}
