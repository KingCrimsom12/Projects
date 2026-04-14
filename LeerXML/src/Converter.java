import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import java.io.File;

public class Cositas {
    public static void main(String[] args) {
        try {
            // Crear el documento XML
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            // Definir el elemento raíz del documento
            Element eRaiz = doc.createElement("concesionario");
            doc.appendChild(eRaiz);

            // Definir el nodo coche
            Element eCoche = doc.createElement("coche");
            eRaiz.appendChild(eCoche);

            // Atributo para el nodo coche
            Attr attr = doc.createAttribute("id");
            attr.setValue("1");
            eCoche.setAttributeNode(attr);

            // Definir los elementos dentro del coche
            Element eMarca = doc.createElement("marca");
            eMarca.appendChild(doc.createTextNode("Renault"));
            eCoche.appendChild(eMarca);

            Element eModelo = doc.createElement("modelo");
            eModelo.appendChild(doc.createTextNode("Megane"));
            eCoche.appendChild(eModelo);

            Element eCilindrada = doc.createElement("cilindrada");
            eCilindrada.appendChild(doc.createTextNode("1.5"));
            eCoche.appendChild(eCilindrada);

            // Crear el transformador para convertir el documento a archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            // Usar JFileChooser para que el usuario seleccione el lugar donde guardar
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar archivo XML");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos XML", "xml"));

            // Mostrar el cuadro de diálogo para guardar el archivo
            int result = fileChooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                // Obtener el archivo seleccionado
                File selectedFile = fileChooser.getSelectedFile();

                // Si el archivo no tiene la extensión .xml, agregarla
                if (!selectedFile.getName().endsWith(".xml")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".xml");
                }

                // Crear el StreamResult para guardar el archivo
                StreamResult resultStream = new StreamResult(selectedFile);

                // Realizar la transformación y guardar el archivo
                transformer.transform(source, resultStream);
                System.out.println("Archivo guardado en: " + selectedFile.getAbsolutePath());
            } else {
                System.out.println("Operación cancelada.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
