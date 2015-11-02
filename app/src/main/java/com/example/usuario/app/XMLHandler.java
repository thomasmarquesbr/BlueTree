package com.example.usuario.app;

import android.util.Log;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.xml.sax.Attributes;

/**
 * Created by Marina on 02/09/2015.
 */
public class XMLHandler extends DefaultHandler{

    boolean currentElement = false;
    String currentValue = "";
    ActivityXML activityInfo;
    ApplicationXML appInfo;


    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException{

        currentElement = true;

         if(qName.equals("application")) {
            String start = attributes.getValue("start");
            String end = attributes.getValue("end");
            appInfo = new ApplicationXML(start, end);
        }else if(qName.equals("activity")){
             //String type, int id
            String type = attributes.getValue("type");
            String id =  attributes.getValue("id");
            activityInfo = new ActivityXML(type,Integer.parseInt(id));
//            activityInfo.setType(type);
//            activityInfo.setId(id);
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{

        currentElement = false;

        if(qName.equalsIgnoreCase("begin")){
            String begin = currentValue.trim();
            begin = begin.substring(0,begin.length()-1);
            activityInfo.setBegin(Integer.parseInt(begin));
        }else if(qName.equalsIgnoreCase("duration")){
            String duration = currentValue.trim();
            duration = duration.substring(0,duration.length()-1);
            activityInfo.setDuration(Integer.parseInt(duration));
        }else if(qName.equalsIgnoreCase("activity")){
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
