package nz.ac.canterbury.team1000.gardenersgrove.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class LocationEntityTest {

    @Test
    public void locationIsComplete_CallDisplayAddressMethod_ReturnCompleteLocation() {
        Location location = new Location("1 Street Name", "Suburb", "City", "1000", "Country", "Street Name");
        Assertions.assertEquals(location.displayAddress(), "1 Street Name, Suburb, City, 1000, Country");
    }

    @Test
    public void locationNoAddress_CallDisplayAddressMethod_ReturnLocationWithNoAddress() {
      Location location = new Location("", "Suburb", "City", "1000", "Country", "Street Name");
      Assertions.assertEquals(location.displayAddress(), "Suburb, City, 1000, Country");
    }

  @Test
  public void locationNoSuburb_CallDisplayAddressMethod_ReturnLocationWithNoSuburb() {
    Location location = new Location("1 Street Name", "", "City", "1000", "Country", "Street Name");
    Assertions.assertEquals(location.displayAddress(), "1 Street Name, City, 1000, Country");
  }

  @Test
  public void locationNoCiyu_CallDisplayAddressMethod_ReturnLocationWithNoCity() {
    Location location = new Location("1 Street Name", "Suburb", "", "1000", "Country", "Street Name");
    Assertions.assertEquals(location.displayAddress(), "1 Street Name, Suburb, 1000, Country");
  }

  @Test
  public void locationNoPostcode_CallDisplayAddressMethod_ReturnLocationWithNoPostcode() {
    Location location = new Location("1 Street Name", "", "City", "", "Country", "Street Name");
    Assertions.assertEquals(location.displayAddress(), "1 Street Name, City, Country");
  }

  @Test
  public void locationNoCountry_CallDisplayAddressMethod_ReturnLocationWithNoCountry() {
    Location location = new Location("1 Street Name", "", "City", "1000", "", "Street Name");
    Assertions.assertEquals(location.displayAddress(), "1 Street Name, City, 1000");
  }
}