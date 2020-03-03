/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010  Technical University Berlin, Germany, and others.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.samples.ordersystem.data;

/**
 * @author Dehla
 *
 */
public class Address {

	@SuppressWarnings("unused") private int id;

	private String street;
	private String postalcode;
	private String city;
	private String country;
	
	public Address(String street, String postalcode, String city, String country) {
		this.street = street;
		this.postalcode = postalcode;
		this.city = city;
		this.country = country;
	}

	Address() {}
	
	
	/**
	 * @return Returns the city.
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city The city to set.
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return Returns the country.
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country The country to set.
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return Returns the postalcode.
	 */
	public String getPostalcode() {
		return postalcode;
	}
	/**
	 * @param postalcode The postalcode to set.
	 */
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	/**
	 * @return Returns the street.
	 */
	public String getStreet() {
		return street;
	}
	/**
	 * @param street The street to set.
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String toString() {
		return street + ", " + postalcode + " " + city + ", " + country; 
	}
}
