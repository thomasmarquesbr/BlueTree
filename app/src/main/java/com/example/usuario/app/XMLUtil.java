package com.example.usuario.app;

import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Marina on 15/09/2015.
 *
 * Classe responsavel por tarefas relacionadas a manipulacao de arquivos XML
 */
public class XMLUtil {

    public static ApplicationXML parseFileMetadataSync(){

        Log.d("Info Debug","parseFileMetadata");

        ApplicationXML app;

        try{

            String dirFile = "/storage/sdcard0/BlueTree/metadata.xml";
           // String dirFile = Environment.getExternalStorageDirectory()+"/BlueTree/metadata.xml";
            File fileSync = new File(dirFile);

            if(fileSync.exists()){
                Log.d("Info Debug", "Arquivo de sincronizacao encontrado");

                FileInputStream is = new FileInputStream(fileSync);

                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();

                XMLReader xr = sp.getXMLReader();

                XMLHandler myXMLHandler = new XMLHandler();
                xr.setContentHandler(myXMLHandler);
                InputSource inStream = new InputSource(is);
                xr.parse(inStream);

                is.close();

                app = myXMLHandler.getApplicationInfo();

                return app;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    public static ApplicationXML parseFileReleased(){

        Log.d("Info Debug","parseFileReleased");

        ApplicationXML app;

        try{

            String dirFile = "/storage/sdcard0/BlueTree/releasedActivities.xml";
            //String dirFile = Environment.getExternalStorageDirectory()+"/BlueTree/releasedAcitivities.xml";
            File file = new File(dirFile);

            if(file.exists()){
                Log.d("Info Debug", "Arquivo de atividades liberadas encontrado");

                FileInputStream is = new FileInputStream(file);

                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();

                XMLReader xr = sp.getXMLReader();

                XMLReleasedHandler myXMLHandler = new XMLReleasedHandler();
                xr.setContentHandler(myXMLHandler);
                InputSource inStream = new InputSource(is);
                xr.parse(inStream);

                is.close();

                app = myXMLHandler.getApplicationInfo();

                return app;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Metodo que cria um arquivo xml com informacoes sobre as activities liberadas
     */
    public static void createFile() throws ParserConfigurationException {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // cria o elemento raiz
        Element rootElement = document.createElement("released");
        document.appendChild(rootElement);

        try {

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Properties outFormat = new Properties();
            outFormat.setProperty(OutputKeys.INDENT, "yes");
            outFormat.setProperty(OutputKeys.METHOD, "xml");
            outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            outFormat.setProperty(OutputKeys.VERSION, "1.0");
            outFormat.setProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperties(outFormat);
            DOMSource domSource = new DOMSource(document.getDocumentElement());
            OutputStream output = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(output);
            transformer.transform(domSource, result);

            String xmlString = output.toString();
            saveFile(xmlString);

        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }


    }

    public static void saveFile(String result){

        String dirName ,fileName;
        dirName = "/storage/sdcard0/BlueTree/";
       // dirName = Environment.getExternalStorageDirectory()+"/BlueTree";
        fileName = "releasedActivities.xml";
        Writer writer;

        File outDir = new File(dirName);
        if (!outDir.isDirectory()) {
            outDir.mkdir();
            Log.d("Info Debug", "Criar diretorio da aplicacao");
        }
        try {

            File outputFile = new File(outDir, fileName);
            writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            Log.d("Info Debug","Erro ao salvar arquivo!");
            e.printStackTrace();
        }
    }

    public static void insertActivityReleased(String nodeType, int id) {

        String dirName ,fileName;
        dirName = "/storage/sdcard0/BlueTree/";
        //dirName = Environment.getExternalStorageDirectory()+"/BlueTree/";
        fileName = "releasedActivities.xml";

        File outDir = new File(dirName);
        if (!outDir.isDirectory()) {
            outDir.mkdir();
            Log.d("Info Debug", "Criar diretorio da aplicacao");
        }

        String pathFile = dirName+fileName;
        File fileXML = new File(pathFile);
        if(!fileXML.exists()){
            Log.d("Info Debug","Criar aquivo com activities liberadas");
            try {
                createFile();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }

        Log.d("Info Debug", "Adicionar no' " + nodeType + " Id: " + id);

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setIgnoringComments(true);
        domFactory.setValidating(false);



        try {

            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(new FileInputStream(new File(pathFile)));

            Element released = doc.getDocumentElement();
            Element elementActivity = doc.createElement("activity");
            released.appendChild(elementActivity);
            elementActivity.setAttribute("type", nodeType);
            elementActivity.setAttribute("id", String.valueOf(id));

            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            Writer out = new StringWriter();
            tf.transform(new DOMSource(doc), new StreamResult(out));

            saveFile(out.toString());

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }


    }

}
