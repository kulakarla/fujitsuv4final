package com.example.fujitsuv4;

import com.example.fujitsuv4.models.Weather;
import com.example.fujitsuv4.repositories.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class WeatherFetch {

    @Autowired
    private WeatherRepository repository;

    Logger log = LoggerFactory.getLogger(WeatherFetch.class);

    /**
     * A method for automatically fetching the weather data for necessary stations, using CronJob-s
     * Fetches every HH:15:00
     */
    @Scheduled(cron = "0 15 * * * *")
    public void fetchWeatherData(){

        List<String> validCities = Arrays.asList("Tallinn-Harku", "Pärnu", "Tartu-Tõravere");

        LocalDateTime observationTime = LocalDateTime.now();
        try {

            //Fetching the XML
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php").openStream());
            doc.getDocumentElement().normalize();
            log.info("Fetching weather data...");

            NodeList stationList = doc.getElementsByTagName("station");


            //Parsing the XML and only importing weather data for stations "Tartu-Tõravere", "Tallinn-Harku", "Pärnu"
            for (int i = 0; i < stationList.getLength(); i++) {

                Node station = stationList.item(i);

                if(station.getNodeType() == Node.ELEMENT_NODE){

                    Element stationElement = (Element) station;

                    if(validCities.contains(stationElement.getElementsByTagName("name").item(0).getTextContent())){
                        String stationName = stationElement.getElementsByTagName("name").item(0).getTextContent();
                        int stationWMO = Integer.parseInt(stationElement.getElementsByTagName("wmocode").item(0).getTextContent());
                        double airTemp = Double.parseDouble(stationElement.getElementsByTagName("airtemperature").item(0).getTextContent());
                        double windSpeed = Double.parseDouble(stationElement.getElementsByTagName("windspeed").item(0).getTextContent());
                        String phenomenon = stationElement.getElementsByTagName("phenomenon").item(0).getTextContent();

                        log.info("Record fetched for station: " + stationName);



                        //INSERT the weather data into the database
                        repository.save(new Weather(0, stationName, stationWMO, airTemp, windSpeed, phenomenon, observationTime));

                        log.info(stationName + "saved to the database");

                    }


                }
            }
        }catch(Exception e){
            log.debug("ERROR IN FETCHING WEATHER DATA");
        }
    }

    @Scheduled(cron = "30 15 * * * *")
    public void fetchDBJob(){
        List<Weather> weather = repository.findAll();
        System.out.println("Fetch service called in " + LocalDateTime.now().minusSeconds(30));
        log.info("Records successfully fetched.");
    }


}
