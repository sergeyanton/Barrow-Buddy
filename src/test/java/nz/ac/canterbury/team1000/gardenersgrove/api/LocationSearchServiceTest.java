package nz.ac.canterbury.team1000.gardenersgrove.api;

import java.util.ArrayList;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class LocationSearchServiceTest {

  private LocationSearchService locationSearchService;

  @Mock
  private RestTemplate restTemplate;

  @BeforeEach
  void setUp() {
    restTemplate = Mockito.mock(RestTemplate.class);
    locationSearchService = new LocationSearchService(restTemplate);
  }

  @Test
  public void LocationNeverRequested_RequestAsked_ReturnsNonEmptyArrayList() {
    // Mock the returned json response when a restTemplate.getForObject() is called
    String jsonResponse = "[{\"place_id\":\"322674240814\",\"osm_id\":\"12690272\",\"osm_type\":\"way\","
        + "\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"-43.519362\",\"lon\":"
        + "\"172.578903\",\"boundingbox\":[\"-43.5196131\",\"-43.5191968\",\"172.5788343\",\"172.5790066\"]"
        + ",\"class\":\"highway\",\"type\":\"secondary\",\"display_name\":\"Ilam Road, Ilam, Christchurch, "
        + "Christchurch City, Canterbury, 8041, New Zealand\",\"display_place\":\"Ilam Road\",\"display_address\""
        + ":\"Ilam, Christchurch, Christchurch City, Canterbury, 8041, New Zealand\",\"address\":{\"name\":"
        + "\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"county\":\"Christchurch City\",\"state\""
        + ":\"Canterbury\",\"postcode\":\"8041\",\"country\":\"New Zealand\",\"country_code\":\"nz\"}}]";
    Mockito.when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonResponse);

    List<Location> locationList = locationSearchService.searchLocations("Ilam Road",
        new String[] {"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"},  "address");
    Assertions.assertFalse(locationList.isEmpty());
  }

  @Test
  public void LocationRequestedTwice_AnotherRequestAsked_ReturnsEmptyArrayList() {
    // Mock the returned json response when a restTemplate.getForObject() is called
    String jsonResponse = "[{\"place_id\":\"322674240814\",\"osm_id\":\"12690272\",\"osm_type\":\"way\","
        + "\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"-43.519362\",\"lon\":"
        + "\"172.578903\",\"boundingbox\":[\"-43.5196131\",\"-43.5191968\",\"172.5788343\",\"172.5790066\"]"
        + ",\"class\":\"highway\",\"type\":\"secondary\",\"display_name\":\"Ilam Road, Ilam, Christchurch, "
        + "Christchurch City, Canterbury, 8041, New Zealand\",\"display_place\":\"Ilam Road\",\"display_address\""
        + ":\"Ilam, Christchurch, Christchurch City, Canterbury, 8041, New Zealand\",\"address\":{\"name\":"
        + "\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"county\":\"Christchurch City\",\"state\""
        + ":\"Canterbury\",\"postcode\":\"8041\",\"country\":\"New Zealand\",\"country_code\":\"nz\"}}]";
    Mockito.when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonResponse);

    locationSearchService.searchLocations("Ilam Road", new String[]{"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"}, "address");
    locationSearchService.searchLocations("Ilam Road", new String[]{"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"}, "address");
    List<Location> locationList = locationSearchService.searchLocations("Ilam Road",
        new String[] {"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"}, "address");

    Assertions.assertTrue(locationList.isEmpty());
  }

  @Test
  public void LocationRequested_NoMatches_ReturnsEmptyArrayList() {
    // Mock the returned json response when a restTemplate.getForObject() is called
    String jsonResponse = "[]";
    Mockito.when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonResponse);

    List<Location> locationList = locationSearchService.searchLocations("Ilam Road",
        new String[] {"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"}, "address");
    Assertions.assertTrue(locationList.isEmpty());
  }

  @Test
  public void LocationRequested_HasOneMatch_ReturnsArrayListOfSizeOne() {
    // Mock the returned json response when a restTemplate.getForObject() is called
    String jsonResponse = "[{\"place_id\":\"322674240814\",\"osm_id\":\"12690272\",\"osm_type\":\"way\","
        + "\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"-43.519362\",\"lon\":"
        + "\"172.578903\",\"boundingbox\":[\"-43.5196131\",\"-43.5191968\",\"172.5788343\",\"172.5790066\"]"
        + ",\"class\":\"highway\",\"type\":\"secondary\",\"display_name\":\"Ilam Road, Ilam, Christchurch, "
        + "Christchurch City, Canterbury, 8041, New Zealand\",\"display_place\":\"Ilam Road\",\"display_address\""
        + ":\"Ilam, Christchurch, Christchurch City, Canterbury, 8041, New Zealand\",\"address\":{\"name\":"
        + "\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"county\":\"Christchurch City\",\"state\""
        + ":\"Canterbury\",\"postcode\":\"8041\",\"country\":\"New Zealand\",\"country_code\":\"nz\"}}]";
    Mockito.when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonResponse);
    List<Location> locationList = locationSearchService.searchLocations("Ilam Road",
        new String[] {"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"}, "address");

    Assertions.assertEquals(1, locationList.size());
  }

  @Test
  public void LocationRequested_HasTwoMatches_ReturnsArrayListOfSizeTwo() {
    // Mock the returned json response when a restTemplate.getForObject() is called
    StringBuilder jsonResponseBuilder = new StringBuilder("[");
    int repeatCount = 2;

    for (int i = 0; i < repeatCount; i++) {
      jsonResponseBuilder.append("{\"place_id\":\"322674240814\",\"osm_id\":\"12690272\",\"osm_type\":\"way\",")
          .append("\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"-43.519362\",\"lon\":")
          .append("\"172.578903\",\"boundingbox\":[\"-43.5196131\",\"-43.5191968\",\"172.5788343\",\"172.5790066\"]")
          .append(",\"class\":\"highway\",\"type\":\"secondary\",\"display_name\":\"Ilam Road, Ilam, Christchurch, ")
          .append("Christchurch City, Canterbury, 8041, New Zealand\",\"display_place\":\"Ilam Road\",\"display_address\"")
          .append(":\"Ilam, Christchurch, Christchurch City, Canterbury, 8041, New Zealand\",\"address\":{\"name\":")
          .append("\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"county\":\"Christchurch City\",\"state\"")
          .append(":\"Canterbury\",\"postcode\":\"8041\",\"country\":\"New Zealand\",\"country_code\":\"nz\"}}");

      if (i < repeatCount - 1) {
        jsonResponseBuilder.append(",");
      }
    }

    jsonResponseBuilder.append("]");
    String jsonResponse = jsonResponseBuilder.toString();

    Mockito.when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonResponse);

    List<Location> locationList = locationSearchService.searchLocations("Ilam Road",
        new String[] {"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"}, "address");
    Assertions.assertEquals(2, locationList.size());
  }

  @Test
  public void LocationRequested_HasThreeMatches_ReturnsArrayListOfSizeThree() {
    // Mock the returned json response when a restTemplate.getForObject() is called
    StringBuilder jsonResponseBuilder = new StringBuilder("[");
    int repeatCount = 3;

    for (int i = 0; i < repeatCount; i++) {
      jsonResponseBuilder.append("{\"place_id\":\"322674240814\",\"osm_id\":\"12690272\",\"osm_type\":\"way\",")
          .append("\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"-43.519362\",\"lon\":")
          .append("\"172.578903\",\"boundingbox\":[\"-43.5196131\",\"-43.5191968\",\"172.5788343\",\"172.5790066\"]")
          .append(",\"class\":\"highway\",\"type\":\"secondary\",\"display_name\":\"Ilam Road, Ilam, Christchurch, ")
          .append("Christchurch City, Canterbury, 8041, New Zealand\",\"display_place\":\"Ilam Road\",\"display_address\"")
          .append(":\"Ilam, Christchurch, Christchurch City, Canterbury, 8041, New Zealand\",\"address\":{\"name\":")
          .append("\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"county\":\"Christchurch City\",\"state\"")
          .append(":\"Canterbury\",\"postcode\":\"8041\",\"country\":\"New Zealand\",\"country_code\":\"nz\"}}");

      if (i < repeatCount - 1) {
        jsonResponseBuilder.append(",");
      }
    }

    jsonResponseBuilder.append("]");
    String jsonResponse = jsonResponseBuilder.toString();

    Mockito.when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonResponse);

    List<Location> locationList = locationSearchService.searchLocations("Ilam Road",
        new String[] {"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"}, "address");
    Assertions.assertEquals(3, locationList.size());
  }

  @Test
  public void LocationRequested_HasFourMatches_ReturnsArrayListOfSizeFour() {
    // Mock the returned json response when a restTemplate.getForObject() is called
    StringBuilder jsonResponseBuilder = new StringBuilder("[");
    int repeatCount = 4;

    for (int i = 0; i < repeatCount; i++) {
      jsonResponseBuilder.append("{\"place_id\":\"322674240814\",\"osm_id\":\"12690272\",\"osm_type\":\"way\",")
          .append("\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"-43.519362\",\"lon\":")
          .append("\"172.578903\",\"boundingbox\":[\"-43.5196131\",\"-43.5191968\",\"172.5788343\",\"172.5790066\"]")
          .append(",\"class\":\"highway\",\"type\":\"secondary\",\"display_name\":\"Ilam Road, Ilam, Christchurch, ")
          .append("Christchurch City, Canterbury, 8041, New Zealand\",\"display_place\":\"Ilam Road\",\"display_address\"")
          .append(":\"Ilam, Christchurch, Christchurch City, Canterbury, 8041, New Zealand\",\"address\":{\"name\":")
          .append("\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"county\":\"Christchurch City\",\"state\"")
          .append(":\"Canterbury\",\"postcode\":\"8041\",\"country\":\"New Zealand\",\"country_code\":\"nz\"}}");

      if (i < repeatCount - 1) {
        jsonResponseBuilder.append(",");
      }
    }

    jsonResponseBuilder.append("]");
    String jsonResponse = jsonResponseBuilder.toString();

    Mockito.when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonResponse);

    List<Location> locationList = locationSearchService.searchLocations("Ilam Road",
        new String[] {"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"}, "address");
    Assertions.assertEquals(4, locationList.size());
  }

  @Test
  public void LocationRequested_HasFiveMatches_ReturnsArrayListOfSizeFive() {
    // Mock the returned json response when a restTemplate.getForObject() is called
    StringBuilder jsonResponseBuilder = new StringBuilder("[");
    int repeatCount = 5;

    for (int i = 0; i < repeatCount; i++) {
      jsonResponseBuilder.append("{\"place_id\":\"322674240814\",\"osm_id\":\"12690272\",\"osm_type\":\"way\",")
          .append("\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"-43.519362\",\"lon\":")
          .append("\"172.578903\",\"boundingbox\":[\"-43.5196131\",\"-43.5191968\",\"172.5788343\",\"172.5790066\"]")
          .append(",\"class\":\"highway\",\"type\":\"secondary\",\"display_name\":\"Ilam Road, Ilam, Christchurch, ")
          .append("Christchurch City, Canterbury, 8041, New Zealand\",\"display_place\":\"Ilam Road\",\"display_address\"")
          .append(":\"Ilam, Christchurch, Christchurch City, Canterbury, 8041, New Zealand\",\"address\":{\"name\":")
          .append("\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"county\":\"Christchurch City\",\"state\"")
          .append(":\"Canterbury\",\"postcode\":\"8041\",\"country\":\"New Zealand\",\"country_code\":\"nz\"}}");

      if (i < repeatCount - 1) {
        jsonResponseBuilder.append(",");
      }
    }

    jsonResponseBuilder.append("]");
    String jsonResponse = jsonResponseBuilder.toString();

    Mockito.when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonResponse);

    List<Location> locationList = locationSearchService.searchLocations("Ilam Road",
        new String[] {"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"}, "address");
    Assertions.assertEquals(5, locationList.size());
  }

  @Test
  public void LocationRequested_HasOneMatchButDifferentField_ReturnsEmptyArrayList() {
    // Mock the returned json response when a restTemplate.getForObject() is called
    String jsonResponse = "[{\"place_id\":\"322674240814\",\"osm_id\":\"12690272\",\"osm_type\":\"way\","
        + "\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"-43.519362\",\"lon\":"
        + "\"172.578903\",\"boundingbox\":[\"-43.5196131\",\"-43.5191968\",\"172.5788343\",\"172.5790066\"]"
        + ",\"class\":\"highway\",\"type\":\"secondary\",\"display_name\":\"Ilam Road, Ilam, Christchurch, "
        + "Christchurch City, Canterbury, 8041, New Zealand\",\"display_place\":\"Ilam Road\",\"display_address\""
        + ":\"Ilam, Christchurch, Christchurch City, Canterbury, 8041, New Zealand\",\"address\":{\"name\":"
        + "\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"county\":\"Christchurch City\",\"state\""
        + ":\"Canterbury\",\"postcode\":\"8041\",\"country\":\"New Zealand\",\"country_code\":\"nz\"}}]";
    Mockito.when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonResponse);
    List<Location> locationList = locationSearchService.searchLocations("Ilam Road",
        new String[] {"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"}, "city");

    Assertions.assertTrue(locationList.isEmpty());
  }

  @Test
  public void LocationRequested_HasOneMatchWithNoCity_ReturnsEmptyArrayList() {
    // Mock the returned json response when a restTemplate.getForObject() is called
    String jsonResponse = "[{\"place_id\":\"322674240814\",\"osm_id\":\"12690272\",\"osm_type\":\"way\","
        + "\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"-43.519362\",\"lon\":"
        + "\"172.578903\",\"boundingbox\":[\"-43.5196131\",\"-43.5191968\",\"172.5788343\",\"172.5790066\"]"
        + ",\"class\":\"highway\",\"type\":\"secondary\",\"display_name\":\"Ilam Road, Ilam, Christchurch, "
        + "Christchurch City, Canterbury, 8041, New Zealand\",\"display_place\":\"Ilam Road\",\"display_address\""
        + ":\"Ilam, Christchurch, Christchurch City, Canterbury, 8041, New Zealand\",\"address\":{\"name\":"
        + "\"Ilam Road\",\"suburb\":\"Ilam\",\"county\":\"Christchurch City\",\"state\""
        + ":\"Canterbury\",\"postcode\":\"8041\",\"country\":\"New Zealand\",\"country_code\":\"nz\"}}]";
    Mockito.when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonResponse);
    List<Location> locationList = locationSearchService.searchLocations("Ilam Road",
        new String[] {"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"}, "address");

    Assertions.assertTrue(locationList.isEmpty());
  }

  @Test
  public void LocationRequested_HasOneMatchWithNoCountry_ReturnsEmptyArrayList() {
    // Mock the returned json response when a restTemplate.getForObject() is called
    String jsonResponse = "[{\"place_id\":\"322674240814\",\"osm_id\":\"12690272\",\"osm_type\":\"way\","
        + "\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"-43.519362\",\"lon\":"
        + "\"172.578903\",\"boundingbox\":[\"-43.5196131\",\"-43.5191968\",\"172.5788343\",\"172.5790066\"]"
        + ",\"class\":\"highway\",\"type\":\"secondary\",\"display_name\":\"Ilam Road, Ilam, Christchurch, "
        + "Christchurch City, Canterbury, 8041, New Zealand\",\"display_place\":\"Ilam Road\",\"display_address\""
        + ":\"Ilam, Christchurch, Christchurch City, Canterbury, 8041, New Zealand\",\"address\":{\"name\":"
        + "\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"county\":\"Christchurch City\",\"state\""
        + ":\"Canterbury\",\"postcode\":\"8041\",\"country_code\":\"nz\"}}]";
    Mockito.when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonResponse);
    List<Location> locationList = locationSearchService.searchLocations("Ilam Road",
        new String[] {"Ilam Road", "Ilam", "Christchurch", "8041"}, "address");

    Assertions.assertTrue(locationList.isEmpty());
  }
  @Test
  public void InputInCountryField_HasOneMatch_ReturnsArrayListOfSizeOne() {
    // Mock the returned json response when a restTemplate.getForObject() is called
    String jsonResponse = "[{\"place_id\":\"323950852310\",\"osm_id\":\"556706\",\"osm_type\":"
        + "\"relation\",\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"-41.5000831\""
        + ",\"lon\":\"172.8344077\",\"boundingbox\":[\"-52.8213687\",\"-29.0303303\",\"-179.059153\","
        + "\"179.3643594\"],\"class\":\"place\",\"type\":\"country\",\"display_name\":\"New Zealand, "
        + "New Zealand\",\"display_place\":\"New Zealand\",\"display_address\":\"nz\",\"address\":"
        + "{\"name\":\"New Zealand\",\"country\":\"New Zealand\",\"country_code\":\"nz\"}}]";
    Mockito.when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonResponse);
    List<Location> locationList = locationSearchService.searchLocations("New Zealand",
        new String[] {"Ilam Road", "Ilam", "Christchurch", "8041", "New Zealand"}, "country");

    Assertions.assertEquals(1, locationList.size());
  }
}

