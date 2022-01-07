team package org.eclipse.objectteams.example.fbapplication.FlightBonus;


/**
 * In flight booking our subscribers are passengers.
 */ 
public class Subscriber playedBy Passenger 
	// adapt only registered passengers (guard predicate):
	base when (FlightBonus.this.hasRole(base)) 
{
	// Callin method binding:
	buy     <- replace book;

	// Callout method binding
	String getName() -> String getName();
}
