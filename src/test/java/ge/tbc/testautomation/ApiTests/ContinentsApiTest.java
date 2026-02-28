package ge.tbc.testautomation.ApiTests;

import ge.tbc.testautomation.data.Constants;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertTrue;

public class ContinentsApiTest {

    @BeforeClass
    public void setupAllureFilter() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = Constants.CONTINENT_URL;
    }

    
    @Test
    public void continentsInfoTest() {
        Response response = given().when().get().then().extract().response();
        XmlPath xmlPath = response.xmlPath();
        List<String> sNames = xmlPath.getList("ArrayOftContinent.tContinent.sName");
        List<String> sCodes = xmlPath.getList("ArrayOftContinent.tContinent.sCode");


        assertThat(sNames.size(), equalTo(6));


        sNames.forEach(sName -> assertThat(sName, not(emptyString())));


        String lastContinentName = xmlPath.getString("ArrayOftContinent.tContinent[-1].sName");
        assertThat(lastContinentName, not(emptyString()));


        Map<String, String> continentsMap = Map.of(
                "AF", "Africa",
                "AN", "Antarctica",
                "AS", "Asia",
                "EU", "Europe",
                "OC", "Ocenania",
                "AM", "The Americas"
        );

        for (int i = 0; i < sCodes.size(); i++) {
            assertThat(continentsMap.get(sCodes.get(i)), equalTo(sNames.get(i)));
        }

        List<String> expectedNamesList = List.of("Africa", "Antarctica", "Asia", "Europe", "Ocenania", "The Americas");
        List<String> expectedCodesList = List.of("AF", "AN", "AS", "EU", "OC", "AM");
        assertTrue(sCodes.containsAll(expectedCodesList));
        assertTrue(sNames.containsAll(expectedNamesList));


        sNames.forEach(sName -> assertThat(sName, matchesPattern("^[^0-9]*$")));


        Set<String> uniqueSNames = new HashSet<>(sNames);
        assertThat(uniqueSNames.size(), equalTo(sNames.size()));


        String oceaniaName = xmlPath.getString("**.find { it.sName.text().startsWith('O') }.sName.text()");
        assertThat(oceaniaName, equalTo("Ocenania"));


        List<String> filteredList = sNames.stream()
                .filter(name -> name.startsWith("A") && name.endsWith("ca"))
                .toList();
        assertThat(filteredList, contains("Africa", "Antarctica"));


        sCodes.forEach(sCode -> assertThat(sCode, matchesPattern("^[A-Z]{2}$")));
    }
}
