package zad1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class PublisherGui extends JFrame{
    public Publisher publisher;
//    public ArrayList<String> topicListValues = new ArrayList<String>();
    public PublisherGui(Publisher publisher){
        this.publisher = publisher;
        new JFrame("Publisher");


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JPanel addTopicPanel = new JPanel();
        addTopicPanel.setLayout(new GridBagLayout());
        GridBagConstraints addConstraints = new GridBagConstraints();

        JTextField topic = new JTextField();
        addConstraints.fill = GridBagConstraints.HORIZONTAL;
        addConstraints.weightx = 0.5;
        addConstraints.weighty = 0.5;
        addConstraints.gridy = 0;

        addTopicPanel.add(topic,addConstraints);

        JButton addTopicButton = new JButton("Dodaj temat");
        addConstraints.gridy = 1;

        addTopicPanel.add(addTopicButton,addConstraints);

        constraints.gridy = 0;

        mainPanel.add(addTopicPanel,constraints);

        JPanel removeTopicPanel = new JPanel();
        removeTopicPanel.setLayout(new GridBagLayout());
        GridBagConstraints removeConstraints = new GridBagConstraints();

        JComboBox<String> topicList = new JComboBox();
        removeConstraints.gridy = 0;
        removeConstraints.gridx = 0;

        removeTopicPanel.add(topicList,removeConstraints);

        JButton removeTopicButton = new JButton("Usuń temat");
        removeConstraints.gridy = 0;
        removeConstraints.gridx = 1;

        removeTopicPanel.add(removeTopicButton,removeConstraints);

        constraints.gridy = 1;

        mainPanel.add(removeTopicPanel,constraints);
//        JTextField topic = new JTextField();
//        constraints.fill = GridBagConstraints.HORIZONTAL;
//        constraints.weightx = 0.5;
//        constraints.weighty = 0.5;
//        constraints.gridy = 0;

//        mainPanel.add(topic, constraints);
//
//        JButton addTopicButton = new JButton("Dodaj temat");
//        constraints.gridy = 1;
//
//        mainPanel.add(addTopicButton,constraints);

        addTopicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!topic.getText().isEmpty()) {
                    try {
                        String arg = topic.getText();
                        if (publisher.addTopic(arg) == true) {
                            topicList.setModel(new DefaultComboBoxModel(
                                    publisher.getTopics().toArray()
                            ));
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        removeTopicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(topicList.getSelectedItem()!=null) {
                    System.out.println(topicList.getSelectedItem().toString());
                    try {
                        publisher.removeTopic(topicList.getSelectedItem().toString());
                        topicList.setModel(new DefaultComboBoxModel(
                                publisher.getTopics().toArray()
                        ));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        this.add(mainPanel);

        this.pack();
        this.setSize(500,300);
        this.setVisible(true);

    }
    @Override
    protected void processWindowEvent(WindowEvent e){
        super.processWindowEvent(e);
        if(e.getID() == WindowEvent.WINDOW_CLOSING){
//            System.out.println("Będzie zamykane");
//            for(String t:publisher.getTopics()){
//                try {
//                    System.out.println("Usuwanie: "+ t);
//                    publisher.removeTopic(t);
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
            try {
                publisher.removeAllTopics();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
            publisher.closingPublisher();
            System.exit(0);
        }
    };
}
