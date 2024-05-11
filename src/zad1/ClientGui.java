package zad1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ClientGui extends JFrame {
    public Client client;
    JComboBox<String> topicL = new JComboBox();
    JTextArea newsText = new JTextArea(10,50);
    JTextArea subsText = new JTextArea(10,50);

    public ClientGui(Client client){
        this.client = client;
        new  JFrame("Client");
        this.setTitle("Client");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

//        JLabel subscribedLabel = new JLabel("Subskrybcje: ");
//        info.setBorder(BorderFactory.createTitledBorder("Info"));
        JLabel subscribedLabel = new JLabel("Subskrybcje: ");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridy = 0;
        mainPanel.add(subscribedLabel,constraints);



        subsText.setLineWrap(true);
        subsText.setWrapStyleWord(true);
        subsText.setEditable(false);
        constraints.gridy = 1;
        mainPanel.add(new JScrollPane(subsText),constraints);


        JButton subscribeButton = new JButton("Subskrybuj");
        constraints.gridy = 2;
//        constraints.gridx = 0;
        mainPanel.add(subscribeButton,constraints);

        JButton unsubscribeButton = new JButton("UnSubskrybuj");
        constraints.gridy = 3;
//        constraints.gridx = 1;
        mainPanel.add(unsubscribeButton,constraints);

//        JComboBox<String> topicList = new JComboBox();
        constraints.gridy = 4;
        mainPanel.add(topicL,constraints);

//        JTextArea newsText = new JTextArea(10,10);
        newsText.setLineWrap(true);
        newsText.setWrapStyleWord(true);
        newsText.setEditable(false);
        constraints.gridy = 5;
        mainPanel.add(new JScrollPane(newsText),constraints);


        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Sub");
                if(topicL.getSelectedItem()!=null){
                    try {
                        client.subscribeToTopic(topicL.getSelectedItem().toString());
                        String args = "";
                        for(String s: client.myTopicsGui){
                            args+=" \""+s+"\" ";
                        }
                        String result = args;
                        subsText.setText(result);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        unsubscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("unsub");
                if(topicL.getSelectedItem()!=null){
                    try {
                        client.unsubscribeToTopic(topicL.getSelectedItem().toString());
                        String args = "";
                        for(String s: client.myTopicsGui){
                            args+=" "+s+" ";
                        }
                        String result = args;
                        subsText.setText(result);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        this.add(mainPanel);
        this.setResizable(false);
        this.pack();
        this.setSize(700,500);
        this.setVisible(true);
    }
    public void updateTopics(){
        topicL.setModel(new DefaultComboBoxModel(
                client.topicsList.toArray()
//                publisher.getTopics().toArray()
        ));
        String args = "";
        for(String s: client.myTopicsGui){
            args+=" "+s+" ";
        }
        String result = args;
        subsText.setText(result);
    }
    public void publishNews(String subject, String subjectNews){
        newsText.setText("["+subject+" news ]: "+subjectNews);
    }
    @Override
    protected void processWindowEvent(WindowEvent e){
        super.processWindowEvent(e);
        if(e.getID() == WindowEvent.WINDOW_CLOSING){
//wywołanie metody client.removeFromServer()

            try {
                client.removeFromServer();
                client.closingClient();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        }
    };
}
