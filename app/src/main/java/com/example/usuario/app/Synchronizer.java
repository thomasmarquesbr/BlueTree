package com.example.usuario.app;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Thomas on 02/09/15.
 */
public class Synchronizer extends Thread{

    private boolean stopSync;
    private boolean started;
    private boolean finished;
    private ApplicationXML applicationXml;
    private WaitingActivity waitingActivity;

    public Synchronizer(WaitingActivity waitingActivity){

        this.stopSync = false;
        this.started = false;
        this.finished = false;
        this.waitingActivity = waitingActivity;

        /*
        Inserir parte que le do arquivo XML
         */
        applicationXml = XMLUtil.parseFileMetadataSync();

    }


    public void run(){

       while(!stopSync){
            //convert data de applicationXml
            String dateBeginXml = applicationXml.getStart().substring(0,12);
            String dateEndXml = applicationXml.getEnd().substring(0,12);

            Calendar c = Calendar.getInstance();
            String dateToday = getFormatDate(c);

            //ESTÁ SENDO CONSIDERADO QUE O PROGRAMA DURA SOMENTE UM DIA
            if((dateToday.equals(dateBeginXml)) && (dateToday.equals(dateEndXml))) {//dataBegin match

                int xmlBegin = convertFullDateToSec(applicationXml.getStart());
                int xmlEnd = convertFullDateToSec(applicationXml.getEnd());
                int timeNow = convertFullDateToSec(getFullDateFormatNow(c));

                if((timeNow >= xmlBegin)&&(timeNow < xmlEnd)) {//timeBegin match

                    if (timeNow == (xmlBegin + applicationXml.getActivity(0).getBegin())){
                        int lastBegin = 0;
                        int lastDuration = 0;

                        //Inicia a chamada das activities
                        for (int i = 0; i < applicationXml.getListActivity().size(); i++) {
                            //timeNow = convertFullDateToSec(getFullDateFormatNow(c));
                            System.out.println("timeNow: " + timeNow);
                            int a = xmlBegin + applicationXml.getActivity(i).getBegin();
                            System.out.println("xmlBegin+application: " + a);
                            if ((!applicationXml.getActivity(i).getReleased())) {
                                applicationXml.getActivity(i).setReleased(true);//ja visitada

                                //chama activity
                                if (applicationXml.getActivity(i).getType().equals("word")) {//chama activity word
                                    this.waitingActivity.callActivityWord(applicationXml.getActivity(i).getId(), applicationXml.getActivity(i).getDuration());

                                    int newStart = applicationXml.getActivity(i).getBegin() - (lastBegin + lastDuration);
                                    try {
                                        sleep(newStart * 1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    //Aguarda
                                    try {
                                        this.sleep(applicationXml.getActivity(i).getDuration() * 1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    lastBegin = applicationXml.getActivity(i).getBegin();
                                    lastDuration = applicationXml.getActivity(i).getDuration();

                                } else if (applicationXml.getActivity(i).getType().equals("puzzle")) {//chama activity puzzle
                                    this.waitingActivity.callActivityPuzzle(applicationXml.getActivity(i).getDuration());

                                    int newStart = applicationXml.getActivity(i).getBegin() - (lastBegin + lastDuration);
                                    try {
                                        sleep(newStart * 1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    //Aguarda
                                    try {
                                        this.sleep(applicationXml.getActivity(i).getDuration() * 1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    lastBegin = applicationXml.getActivity(i).getBegin();
                                    lastDuration = applicationXml.getActivity(i).getDuration();

                                    stopSync();
                                }

                            }

                            if (i == applicationXml.getListActivity().size() - 1) {
                                stopSync();
                            }
                        }

                        if (timeNow == xmlBegin) {
                            //iniciou o sync na hora correta
                            this.started = true;
                        }
                    }
                }else if(timeNow == xmlEnd){//terminou a aplicação
                    stopSync();
                    this.finished = true;
                }else if(timeNow > xmlEnd){
                    stopSync();
                    Log.d("DEBUUUG","Ja passou do horario");
                }

            }

            try { this.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        }
    }

    private String getFormatDate(Calendar calendar){

        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        String fYear = year.format(calendar.getTime());
        SimpleDateFormat month = new SimpleDateFormat("MM");
        String fMonth = month.format(calendar.getTime());
        SimpleDateFormat day = new SimpleDateFormat("dd");
        String fDay = day.format(calendar.getTime());

        return "P"+fYear+"Y"+fMonth+"M"+fDay+"D";
    }

    private String getFullDateFormatNow(Calendar calendar){

        SimpleDateFormat hour = new SimpleDateFormat("HH");
        String fHour = hour.format(calendar.getTime());
        SimpleDateFormat min = new SimpleDateFormat("mm");
        String fMin = min.format(calendar.getTime());
        SimpleDateFormat sec = new SimpleDateFormat("ss");
        String fSec = sec.format(calendar.getTime());

        return getFormatDate(calendar)+"T"+fHour+"H"+fMin+"M"+fSec+"S";
    }

    private int convertFullDateToSec(String fullDate){
        int hour = Integer.parseInt(fullDate.substring(13, 15));
        int min = Integer.parseInt(fullDate.substring(16, 18));
        int sec = Integer.parseInt(fullDate.substring(19, 21));

        return (hour*3600)+(min*60)+sec;
    }

    public void stopSync(){
        this.stopSync = true;
    }


    /*private void parseXML(){

        try{

            String dirFile = "/storage/sdcard0/PingosApp/metadata.xml";
            File fileSync = new File(dirFile);

            if(fileSync.exists()){
                Log.d("Info Debug","Arquivo de sincronizacao encontrado");

                FileInputStream is = new FileInputStream(fileSync);

                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();

                XMLReader xr = sp.getXMLReader();

                XMLHandler myXMLHandler = new XMLHandler();

                xr.setContentHandler(myXMLHandler);

                InputSource inStream = new InputSource(is);
                xr.parse(inStream);
                is.close();

                applicationXml = myXMLHandler.getApplicationInfo();

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if(applicationXml == null){
            Log.d("DEBUG", "Nenhum arquivo foi encontrado!");
            stopSync();
        }


    }*/


}