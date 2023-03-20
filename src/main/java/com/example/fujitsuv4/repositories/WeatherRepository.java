package com.example.fujitsuv4.repositories;

import com.example.fujitsuv4.models.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Integer> {

    /**
     * An SQL-Query in order to fetch the latest weather data for the desired town
     * @param townName the town which we need to find the latest weather data for
     * @return the row with the latest weather data
     */
    @Query(value = "SELECT * FROM Weather WHERE LOWER(STATION_NAME) LIKE CONCAT('%',:CityName,'%') ORDER BY OBS_TIME DESC LIMIT 1", nativeQuery = true)
    Weather findLatestWeatherDataForCity(@Param("CityName") String townName);


}
