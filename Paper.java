import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class Paper extends JFrame{
    static private HashMap<String, Color> KNOWNCOLORS = new HashMap<>();

    static {
        KNOWNCOLORS.put("black", Color.BLACK);
        KNOWNCOLORS.put("gray", Color.GRAY);
        KNOWNCOLORS.put("white", Color.WHITE);
        KNOWNCOLORS.put("red", Color.RED);
        KNOWNCOLORS.put("blue", Color.BLUE);
        KNOWNCOLORS.put("green", Color.GREEN);
        KNOWNCOLORS.put("pink", Color.PINK);
        KNOWNCOLORS.put("yellow", Color.YELLOW);
    }
    private String background;
    private String font;
    private int[] pos = new int[3];

    private JTextArea ta = new JTextArea("Write here your achievement");
    private JTextField tf = new JTextField();
    private JPanel jp1 = new JPanel();
    private JPanel jp2 = new JPanel();
    private JPanel jp3 = new JPanel();
    private JPanel jp4 = new JPanel();
    private JButton jb1 = new JButton("Ok");
    private JButton jb2 = new JButton("Cancel");
    private JButton jb3 = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\images\\right.png"));
    private JButton jb4 = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\images\\left.png"));

    public Paper(){
        determineColors();
        setup();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Paper(String bg, String fg, int[] p, String c){
        ta.setText(c);
        pos = p;
        background = bg;
        font = fg;
        setup();
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }

    public int[] getPos(){
        return pos;
    }

    public String getPaperBackground(){
        return background;
    }

    public String getPaperFont() {
        return font;
    }

    public String getText(){
        return ta.getText();
    }

    //this is the default setup to a paper in write mode
    public void setup(){
        Border border = BorderFactory.createLineBorder(KNOWNCOLORS.get(background), 100);

        ta.setPreferredSize(new Dimension(800, 400));
        ta.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        ta.setBackground(KNOWNCOLORS.get(background));
        ta.setForeground(KNOWNCOLORS.get(font));
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.addMouseListener(new TextAreaListener());
        ta.setBorder(border);

        jb1.setPreferredSize(new Dimension(150, 30));
        jb1.addActionListener(new OkButtonListener());
        jb2.setPreferredSize(new Dimension(150, 30));
        jb2.addActionListener(new CancelButtonListener());

        jp1.add(ta);
        jp1.setBackground(KNOWNCOLORS.get(background));
        jp2.add(jb1);
        jp2.add(jb2);
        jp2.setBackground(KNOWNCOLORS.get(background));

        add(jp1, BorderLayout.CENTER);
        add(jp2, BorderLayout.SOUTH);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(KNOWNCOLORS.get(font), 2));
    }

    //this is how the paper will look like when it is in read mode
    public void setup2(int i, int n){
        Border border = BorderFactory.createLineBorder(KNOWNCOLORS.get(background), 40);

        tf.setText(i + "/" + n);
        tf.setBackground(KNOWNCOLORS.get(background));
        tf.setForeground(KNOWNCOLORS.get(font));
        tf.setEditable(false);

        ta.setPreferredSize(new Dimension(680, 410));
        ta.setBorder(border);

        jb3.setPreferredSize(new Dimension(50, 50));
        jb3.addActionListener(new RightButtonListener());
        jb4.setPreferredSize(new Dimension(50, 50));
        jb4.addActionListener(new LeftButtonListener());

        jp2.remove(jb1);
        jp2.remove(jb2);
        jp2.add(jb4);
        jp2.setBorder(new EmptyBorder(new Insets(170, 0, 180, 0)));
        jp2.setBackground(KNOWNCOLORS.get(background));
        jp2.revalidate();

        jp3.add(jb3);
        jp3.setBorder(new EmptyBorder(new Insets(170, 0, 180, 0)));
        jp3.setBackground(KNOWNCOLORS.get(background));

        jp4.add(tf);
        jp4.setBackground(KNOWNCOLORS.get(background));

        add(jp1, BorderLayout.CENTER);
        add(jp2, BorderLayout.WEST);
        add(jp3, BorderLayout.EAST);
        add(jp4, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public class TextAreaListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            ta.getCaret().setVisible(true);
            ta.setCaretColor(KNOWNCOLORS.get(font));
            ta.setText("");
            ta.setEditable(true);
            ta.removeMouseListener(this);
        }
    }

    public class OkButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            if(ta.getText().equals("") || ta.getText().equals("Write here your achievement"))
                return;
            ta.setEditable(false);
            addPaper();
        }
    }

    public class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    public class RightButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            getNext();
        }
    }

    public class LeftButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            getPrevious();
        }
    }

    //this method initializes the font color and the background color of the paper
    public void determineColors(){
        Random r = new Random();
        int i = r.nextInt(8);
        switch (i){
            default:
                background = "white";
                font = "black";
                break;
            case 1:
                background = "yellow";
                font = "blue";
                break;
            case 2:
                background = "red";
                font = "pink";
                break;
            case 3:
                background ="gray";
                font = "green";
                break;
            case 4:
                background = "green";
                font = "gray";
                break;
            case 5:
                background = "blue";
                font = "yellow";
                break;
            case 6:
                background = "pink";
                font = "red";
                break;
            case 7:
                background = "black";
                font = "white";
                break;
        }
    }

    //this method is called in order to add paper to a jar
    public void addPaper(){
        Main.paperAdded(this);
    }

    //this method is called in order to reach the next paper from the jar
    public void getNext(){
        Main.nextPaper(this);
    }

    //this method is called in order to reach the previous paper from the jar
    public void getPrevious(){
        Main.previousPaper(this);
    }

    public BufferedImage getImg(){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(System.getProperty("user.dir") + "\\images\\" + background + "_paper_transparent.png"));
        } catch (IOException e){}
        return img;
    }
}
