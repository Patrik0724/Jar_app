import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Jar {
    private String imgPath;
    private ArrayList<Paper> list = new ArrayList<>();

    public Jar(){
        imgPath = System.getProperty("user.dir") + "\\images\\jar_transparent.png";
    }

    public String getPath(){
        return imgPath;
    }

    //this method pops out the i. paper from the jar
    public void pop(int i){
        try{
            list.get(i).setup2(i + 1, list.size());
        }
        catch (IndexOutOfBoundsException e) {}
    }

    public void addPaper(Paper p){
        setPos(p);
        list.add(p);
    }

    //draws the jar and its content on the JFrame that is got as a parameter
    public void draw(Graphics g, JFrame jf){
        if(list.size() == 0)
            return;
        for(int i = 0; i < 4; ++i) {
            for (Paper p : list) {
                if (p.getPos()[0] == i) {
                    Point point = calcPos(p);
                    g.drawImage(p.getImg(), 300 + point.x, 800 - point.y, jf);
                }
            }
        }
    }

    //sets the position to a paper in the jar
    public void setPos(Paper p){
        Random r = new Random();
        int i = r.nextInt(4);
        int k = r.nextInt(4);
        int[] pos = new int[]{i, k, list.size() / 16};
        for(Paper pap: list){
            if(Arrays.equals(pap.getPos(), pos)){
                pos = new int[]{i, k, list.size() / 16};
            }
        }
        p.setPos(pos);
    }

    //calculates the position to a paper on the screen
    public Point calcPos(Paper p){
        int[] pos = p.getPos();
        Point point = new Point();
        point.y = pos[2] * 100;
        if (pos[0] == 0 || (pos[0] == 1 && pos[1] == 0)) {
            point.x = pos[1] * 150 + 40 + pos[2] * 5;
        }
        else if(pos[0] == 2 || (pos[0] == 1 && pos[1] == 2)){
            point.x = pos[1] * 150 - 40 - pos[2] * 5;
        }
        else
            point.x = pos[1] * 150;
        return point;
    }

    public void shuffle(){
        Collections.shuffle(list);
    }

    public int getIndex(Paper p){
        return list.indexOf(p);
    }

    public void writeXML(String xml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element root = doc.createElement("jar");
            doc.appendChild(root);

            for (Paper p : list) {
                Element paper = doc.createElement("paper");
                root.appendChild(paper);

                Element background = doc.createElement("background");
                background.appendChild(doc.createTextNode(p.getPaperBackground()));
                paper.appendChild(background);

                Element font = doc.createElement("font");
                font.appendChild(doc.createTextNode(p.getPaperFont()));
                paper.appendChild(font);

                Element pos = doc.createElement("pos");
                paper.appendChild(pos);
                Element x = doc.createElement("x");
                x.appendChild(doc.createTextNode(String.valueOf(p.getPos()[0])));
                pos.appendChild(x);
                Element y = doc.createElement("y");
                y.appendChild(doc.createTextNode(String.valueOf(p.getPos()[1])));
                pos.appendChild(y);
                Element z = doc.createElement("z");
                z.appendChild(doc.createTextNode(String.valueOf(p.getPos()[2])));
                pos.appendChild(z);

                Element content = doc.createElement("content");
                content.appendChild(doc.createTextNode(p.getText()));
                paper.appendChild(content);
            }

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                tr.transform(new DOMSource(doc),
                        new StreamResult(new FileOutputStream(xml)));

            } catch (TransformerException | IOException te) {
                System.out.println(te.getMessage());
            }
        } catch (ParserConfigurationException e) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + e);
        }
    }

    public void readXML(String xml){
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler(){
                private String bg;
                private String fg;
                private String content;
                private int[] pos;

                String chars;

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    switch (qName) {
                        default -> {
                        }
                        case "background" -> bg = chars;
                        case "font" -> {
                            fg = chars;
                            pos = new int[3];
                        }
                        case "x" -> pos[0] = Integer.parseInt(chars);
                        case "y" -> pos[1] = Integer.parseInt(chars);
                        case "z" -> pos[2] = Integer.parseInt(chars);
                        case "content" -> content = chars;
                        case "paper" -> {
                            list.add(new Paper(bg, fg, pos, content));
                        }
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    chars = new String(ch, start, length);
                }
            };

            parser.parse(xml, handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}