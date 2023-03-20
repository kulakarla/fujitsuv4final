package com.example.fujitsuv4.repositories;

import com.example.fujitsuv4.models.RegBaseFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegBaseFeeRepository extends JpaRepository<RegBaseFee, Integer> {

    /**
     * A QUERY for fetching the regional base fees for different types of vehicles for a desired town
     * @param townName the town which we want the regional base fees of
     * @return The regional base fee-s of town "townName"
     */
    @Query(value = "SELECT * FROM Reg_Base_Fee WHERE station_name = :CityName", nativeQuery = true)
    RegBaseFee regionalBaseFeeBasedOnCityAndVehicle(@Param("CityName") String townName);
}
