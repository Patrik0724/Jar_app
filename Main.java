import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class Main{
    static private JFrame jf = new JFrame("Jar_app");
    static private JButton but1 = new JButton("New Jar");
    static private JButton but2 = new JButton("Load Jar");
    static private JButton but3 = new JButton("Save");
    static private JButton but4 = new JButton("Add paper");
    static private JButton but5 = new JButton("Read papers");
    static private JLabel background;
    static private Jar jar = new Jar();
    public static void main(String[] args){
        setup1();
    }

    //this is the default setup, how the program will look like
    public static void setup1(){
        ImageIcon img = new ImageIcon(jar.getPath());
        background = new JLabel("", img, JLabel.CENTER){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                jar.draw(g, jf);
            }
        };
        LayoutManager mgr = new GridLayout(2, 1);
        background.setLayout(mgr);

        but1.addActionListener(new NewJarButtonListener());
        but2.addActionListener(new LoadJarButtonListener());
        background.add(but1);
        background.add(but2);
        background.setBorder(new EmptyBorder(new Insets(400, 800, 400, 800)));

        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jf.add(background, BorderLayout.CENTER);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    public static class NewJarButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            background.remove(but1);
            background.remove(but2);
            background.revalidate();
            jf.remove(background);
            setup2();
        }
    }

    public static class LoadJarButtonListener implements ActionListener {
        static private JFrame frame = new JFrame();

        @Override
        public void actionPerformed(ActionEvent e) {
            String[] files = new File(System.getProperty("user.dir") + "\\jars").list();

            if(files.length == 0)
                return;

            int i = files.length;
            int j = i / (int) Math.sqrt(i);

            LayoutManager mgr = new GridLayout(j, j, 10, 10);

            JPanel jp = new JPanel();
            jp.setLayout(mgr);
            jp.setBorder(new EmptyBorder(new Insets(50, 50, 50, 50)));

            for (String file : files) {
                JButton jb = new JButton(file);
                jb.addActionListener(new ButtonListener());
                jb.setPreferredSize(new Dimension(100, 30));
                jp.add(jb);
            }

            frame.add(jp, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        }

        public static class ButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                jar.readXML(System.getProperty("user.dir") + "\\jars\\" + ((JButton) e.getSource()).getText());

                SaveButtonListener.setFileName(((JButton) e.getSource()).getText());

                background.remove(but1);
                background.remove(but2);
                background.revalidate();
                jf.remove(background);

                setup2();
            }
        }
    }

    public static class SaveButtonListener implements ActionListener {
        private static JButton jb = new JButton("Ok");
        private static JTextField jtf = new JTextField(20);
        private static JFrame frame = new JFrame("File name");

        public static void setFileName(String s){
            jtf.setText(s);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            jb.addActionListener(new OkButtonListener());

            JPanel jp = new JPanel();
            LayoutManager layout = new GridLayout(2, 1);
            jp.setLayout(layout);
            jp.add(jtf);
            jp.add(jb);

            frame.add(jp, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        public static class OkButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                jar.writeXML(System.getProperty("user.dir") + "\\jars\\" + jtf.getText());
                jtf.setText("");
            }
        }
    }

    public static class AddPaperButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Paper p = new Paper();
        }
    }

    public static class ReadPapersButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            jar.shuffle();
            jar.pop(0);
        }
    }

    //this is the setup to a new Jar
    public static void setup2(){
        JPanel jp = new JPanel();
        LayoutManager mgr = new GridLayout(3, 1, 0, 20);
        jp.setLayout(mgr);
        but3.setPreferredSize(new Dimension(200, 200));
        jp.add(but3);
        jp.add(but4);
        jp.add(but5);
        jp.setBorder(new EmptyBorder(new Insets(350, 400, 350, 0)));

        but3.addActionListener(new SaveButtonListener());
        but4.addActionListener(new AddPaperButtonListener());
        but5.addActionListener(new ReadPapersButtonListener());

        background.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));

        jf.add(background, BorderLayout.EAST);
        jf.add(jp, BorderLayout.WEST);
        jf.revalidate();
        jf.repaint();
    }

    //this method is called in Paper class when a paper is new paper is added, this will refresh the look of JFrame
    public static void paperAdded(Paper p){
        jar.addPaper(p);
        jf.repaint();
    }

    //this method is called in Paper class when the user wants to see the next paper in the jar
    public static void nextPaper(Paper p){
        jar.pop(jar.getIndex(p) + 1);
    }

    //this method is called in Paper class when the user wants to see the previous paper in the jar
    public static void previousPaper(Paper p){
        jar.pop(jar.getIndex(p) - 1);
    }
}
