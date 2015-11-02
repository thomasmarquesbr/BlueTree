package com.example.usuario.app;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by usuario on 24/09/2015.
 */
public class XMLReleasedHandler extends DefaultHandler {

    boolean currentElement = false;
    String currentValue = "";
    ActivityXML activityInfo;
    ApplicationXML appInfo;


    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;

        if(qName.equals("released")) {
            appInfo = new ApplicationXML();
        }else if(qName.equals("activity")){
            //String type, int id
            String type = attributes.getValue("type");
            String id =  attributes.getValue("id");
            activityInfo = new ActivityXML(type,Integer.parseInt(id));
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{

        currentElement = false;

        if(qName.equalsIgnoreCase("activity")){
            appInfo.addActivity(activityInfo);
        }

        currentValue = "";

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException{

        if(currentElement){
            currentValue = currentValue + new String(ch,start,length);
        }
    }

    public ApplicationXML getApplicationInfo(){
        return appInfo;
    }

}
