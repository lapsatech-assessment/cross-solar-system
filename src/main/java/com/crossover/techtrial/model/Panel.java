package com.crossover.techtrial.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

/**
 * Panel class hold information related to a Solar panel.
 * 
 * @author Crossover
 *
 */
@Entity
@Table(name = "panel")
public class Panel implements Serializable {

  private static final long serialVersionUID = -8755580144497437564L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @NotNull
  @Size(min = 16, max = 16, message = "Serial number must be 16 characters length")
  @Column(name = "serial", unique = true)
  String serial;

  @NotNull
  @Column(name = "longitude")
  @Digits(integer = 3, fraction = 6, message = "Latitude/longitude must contains 6 decimal places")
  @DecimalMax(value = "180", message = "Invalid longitude range")
  @DecimalMin(value = "-180", message = "Invalid longitude range")
  BigDecimal longitude;

  @NotNull
  @Column(name = "latitude")
  @Digits(integer = 2, fraction = 6, message = "Latitude/longitude must contains 6 decimal places")
  @DecimalMax(value = "90", message = "Invalid latitude range")
  @DecimalMin(value = "-90", message = "Invalid latitude range")
  BigDecimal latitude;

  @Nullable
  @Column(name = "brand")
  String brand;

  public Panel() {
  }

  public Panel(String serial, BigDecimal longitude, BigDecimal latitude, String brand) {
    this.serial = serial;
    this.longitude = longitude;
    this.latitude = latitude;
    this.brand = brand;
  }

  public Panel(Long id, String serial, BigDecimal longitude, BigDecimal latitude, String brand) {
    this(serial, longitude, latitude, brand);
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSerial() {
    return serial;
  }

  public void setSerial(String serial) {
    this.serial = serial;
  }

  public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  /*
   * Id, Serial and Brand are only fields required to uniquely identify a Panel
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((brand == null) ? 0 : brand.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((serial == null) ? 0 : serial.hashCode());
    return result;
  }

  /*
   * Id, Serial and Brand are only fields required to uniquely identify a Panel
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Panel other = (Panel) obj;
    if (brand == null) {
      if (other.brand != null) {
	return false;
      }
    } else if (!brand.equals(other.brand)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
	return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (serial == null) {
      if (other.serial != null) {
	return false;
      }
    } else if (!serial.equals(other.serial)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Panel [id=" + id + ", serial=" + serial + ", longitude=" + longitude + ", latitude="
	+ latitude + ", brand=" + brand + "]";
  }
}
