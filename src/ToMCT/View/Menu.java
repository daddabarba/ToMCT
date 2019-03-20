package ToMCT.View;

import ToMCT.Model.ColoredTrails.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

    private class IntVerifier extends InputVerifier{

        public boolean verify(JComponent input) {
            if (input instanceof JFormattedTextField) {
                JFormattedTextField ftf = (JFormattedTextField)input;
                JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
                if (formatter != null) {
                    String text = ftf.getText();
                    try {
                        Integer.parseInt(text);
                        return true;
                    } catch (Exception pe) {
                        return false;
                    }
                }
            }
            return true;
        }
        public boolean shouldYieldFocus(JComponent input) {
            return verify(input);
        }

    }

    private class DoubleVerifier extends InputVerifier{

        public boolean verify(JComponent input) {
            if (input instanceof JFormattedTextField) {
                JFormattedTextField ftf = (JFormattedTextField)input;
                JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
                if (formatter != null) {
                    String text = ftf.getText();
                    try {
                        Double.parseDouble(text);
                        return true;
                    } catch (Exception pe) {
                        return false;
                    }
                }
            }
            return true;
        }
        public boolean shouldYieldFocus(JComponent input) {
            return verify(input);
        }

    }

    private class SubmitAction extends AbstractAction{

        private JTextField lvlPlayer1, lvlPlayer2, handSize, mazeSize, ls1, ls2;
        private JComboBox<Boolean> GUI;

        public SubmitAction(String name, JTextField lvlPlayer1, JTextField lvlPlayer2, JTextField ls1, JTextField ls2, JTextField handSize, JTextField mazeSize, JComboBox<Boolean> GUI){

            super(name);

            this.lvlPlayer1 = lvlPlayer1;
            this.lvlPlayer2 = lvlPlayer2;
            this.ls1 = ls1;
            this.ls2 = ls2;
            this.handSize = handSize;
            this.mazeSize = mazeSize;
            this.GUI = GUI;
        }

        @Override
        public void actionPerformed(ActionEvent e){

            Integer[] orders = {Integer.parseInt(this.lvlPlayer1.getText()), Integer.parseInt(this.lvlPlayer2.getText())};
            Double[] learningSpeeds = {Double.parseDouble(this.ls1.getText()), Double.parseDouble(this.ls2.getText())};
            Game game = new Game(orders, learningSpeeds, Integer.parseInt(handSize.getText()), Integer.parseInt(mazeSize.getText()));

            game.startGame();

            if(GUI.getItemAt(GUI.getSelectedIndex()))
                new GameFrame("Colored Trails", game);
        }
    }

    public Menu(String name){
        super(name);

        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        JLabel title = new JLabel("Insert Parameters");
        JLabel section1 = new JLabel("Insert ToM order of each player");

        JTextField lvlPlayer1 = new JFormattedTextField();
        JTextField lvlPlayer2 = new JFormattedTextField();

        lvlPlayer1.setInputVerifier(new IntVerifier());
        lvlPlayer2.setInputVerifier(new IntVerifier());

        lvlPlayer1.setColumns(10);
        lvlPlayer2.setColumns(10);

        lvlPlayer1.setText("1");
        lvlPlayer2.setText("1");

        JLabel section11 = new JLabel("Insert learning speeds of each player");

        JTextField ls1 = new JFormattedTextField();
        JTextField ls2 = new JFormattedTextField();

        ls1.setInputVerifier(new DoubleVerifier());
        ls2.setInputVerifier(new DoubleVerifier());

        ls1.setColumns(10);
        ls2.setColumns(10);

        ls1.setText("0.9");
        ls2.setText("0.9");

        JLabel section2 = new JLabel("Insert hand size");
        JLabel section3 = new JLabel("Insert maze size");

        JTextField handSize = new JFormattedTextField();
        handSize.setInputVerifier(new IntVerifier());
        handSize.setColumns(10);
        handSize.setText("4");

        JTextField mazeSize = new JFormattedTextField();
        mazeSize.setInputVerifier(new IntVerifier());
        mazeSize.setColumns(10);
        mazeSize.setText("5");

        JLabel section4 = new JLabel("GUI");
        Boolean[] vals = {true, false};
        JComboBox<Boolean> selectGUI = new JComboBox<>(vals);

        JButton submit = new JButton("Submit");
        submit.setAction(new SubmitAction("Submit", lvlPlayer1, lvlPlayer2, ls1, ls2, handSize, mazeSize, selectGUI));

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(title, c);

        c.gridx = 0;
        c.gridy = 1;
        pane.add(section1, c);

        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 1;
        pane.add(lvlPlayer1, c);

        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 1;
        pane.add(lvlPlayer2, c);

        c.gridx = 0;
        c.gridy = 2;
        pane.add(section11, c);

        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 2;
        pane.add(ls1, c);

        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 2;
        pane.add(ls2, c);

        c.gridx = 0;
        c.gridy = 3;
        pane.add(section2, c);

        c.gridx = 1;
        c.gridy = 3;
        pane.add(handSize, c);

        c.gridx = 0;
        c.gridy = 4;
        pane.add(section3, c);

        c.gridx = 1;
        c.gridy = 4;
        pane.add(mazeSize, c);

        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 5;
        pane.add(section4, c);

        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 5;
        pane.add(selectGUI, c);

        c.gridx = 0;
        c.gridy = 6;
        pane.add(submit, c);

        add(pane);

        //set preferences
        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo (null);
        setVisible(true);

        //close program when no frames are opened
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
