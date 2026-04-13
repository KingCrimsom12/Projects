import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Main {
    public static void main(String[] args)
    {
        JFileChooser fc = new JFileChooser();
        int seleccion = fc.showOpenDialog(fc);
        if (seleccion == JFileChooser.APPROVE_OPTION){
            File fichero = fc.getSelectedFile();
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fichero);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("book");

                for (int i = 0; i < nList.getLength(); i++)
                {
                    Node node = nList.item(i);

                    if(node.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element element = (Element) node;

                        if(element.hasChildNodes()) {
                            NodeList nl = node.getChildNodes();
                            for(int j=0; j<nl.getLength(); j++) {
                                Node nd = nl.item(j);
                                nd.normalize();
                                System.out.println(nd.getTextContent());
                            }
                        }
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}