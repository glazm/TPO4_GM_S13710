package zad1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class PublisherGui extends JFrame{
    public Publisher publisher;
    public PublisherGui(Publisher publisher){
        this.publisher = publisher;
        new JFrame("Publisher");


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JTextField topic = new JTextField();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridy = 0;

        mainPanel.add(topic, constraints);

        JButton addTopicButton = new JButton("Dodaj temat");
        constraints.gridy = 1;

        mainPanel.add(addTopicButton,constraints);

        addTopicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String arg = topic.getText();
                    publisher.addTopic(arg);

                } catch (IOException ex) {
                    ex.printStackTrace();
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
