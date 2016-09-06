/** NetId: rs2352, hy483. Time spent: 2 hours, 5 minutes.
An instance maintains info about the PhD of a person. */

/** Successfully generated Javadoc for this project,
 *  and checked the Javadoc output. */

package a1PhD;

public class PhD {
	
	private String name; // PhD's name. At least 1 character.
	
	private char gender; // PhD's gender. Should be either 'M' for male or 'F' for female. 
	
	private int PhDMonth; // month PhD was awarded. In range 1..12, with 1 meaning Jan, etc.
		
	private int PhDYear; // year PhD was awarded.
	
	private PhD firstAdvisor; // the PhD's first advisor -- null if unknown.
	
	private PhD secondAdvisor; // the PhD's second advisor -- null if unknown 
							   // or the first advisor is unknown.
	
	private int adviseeNum;	// number of the PhD's advisees. Under control of the program.

	/** Constructor: an instance for a person with name n, gender g, PhD month m,
     *  and PhD year y. Its advisors are unknown, and it has no advisees.
     *  Precondition: n has at least 1 char. m is in 1..12. 
     *  			  g is 'F' for female or 'M' for male.
	 */
	public PhD(String n, char g, int m, int y){

		assert n != null && n.length() > 0;
		assert g == 'M' || g == 'F';
		assert m > 0 && m <= 12;
		assert y > 0;
		
		this.name = n;
		this.gender = g;
		this.PhDMonth = m;
		this.PhDYear = y;
		this.firstAdvisor = null;
		this.secondAdvisor = null;
		this.adviseeNum = 0;
		
	}
	
	
	 /** Constructor: a PhD with name n, gender g, PhD month m, PhD year y,
	  *  first advisor adv, and no second advisor.
	  *	 Precondition: n has at least 1 char, g is 'F' for female or 'M' for male,
	  *  m is in 1..12, and adv is not null.
	  */
	 public PhD(String n, char g, int m, int y, PhD adv){
		
		this(n, g, m, y);
		
		assert adv != null;
		
		this.firstAdvisor = adv;
		adv.increaseAdvisee();
		
	}

	/** Constructor: a PhD with name n, gender g, PhD month m, PhD year y,
	 *  first advisor adv1, and second advisor adv2.
	 *  Precondition: n has at least 1 char. g is 'F' for female or 'M' for male.
	 *  m is in 1..12. adv1 and adv2 are not null. adv1 and adv2 are different.
	 */  
	public PhD(String n, char g, int m, int y, PhD adv1, PhD adv2){
		
		this(n, g, m, y, adv1);
		
		assert adv2 != null && adv1 != adv2;
		
		this.secondAdvisor = adv2;
		adv2.increaseAdvisee();
		
	}

	/** Return the name of this PhD. */
	public String getName() {
		return name;
	}
	
	/** Set the name of the PhD to parameter name.
	 *  Precondition: name is not null, and name is at least 1 char.
	 */
	public void setName(String name) {
		assert name != null && name.length() > 0;
		this.name = name;
	}

	/** Return the value of the sentence "This person is a female." */
	public boolean isFemale() {
		return this.gender == 'F';
	}
	
	/** Set the gender of the PhD to parameter gender.
	 *  Precondition: gender is 'F' for female or 'M' for male.
	 */
	public void setGender(char gender) {
		assert gender == 'F' || gender == 'M';	
		this.gender = gender;
	}

	/** Return the month this person got their PhD. */
	public int getMonth() {
		return PhDMonth;
	}
	
	/** Set the month PhD was awarded to phDMonth.
	 *  Precondition: pHDMonth is in 1..12.
	 */
	public void setMonth(int phDMonth) {
		assert phDMonth > 0 && phDMonth <= 12;
		PhDMonth = phDMonth;
	}
	
	/** Return the year this person got their PhD. */
	public int getYear() {
		return PhDYear;
	}
	
	/** Set the year PhD was awarded to phDYear.
	 * Precondition: phDYear is greater than 0.
	 */
	public void setYear(int phDYear) {
		assert phDYear >= 0;
		PhDYear = phDYear;
	}
	
	/** Return the first advisor of this PhD (null if unknown). */
	public PhD advisor1() {
		return firstAdvisor;
	}

	/** Add p as the first advisor of this person.
 	 *	Precondition: the first advisor is unknown and p is not null.
	 */
	public void addAdvisor1(PhD firstAdvisor) {
		assert firstAdvisor != null && this.firstAdvisor == null;
		this.firstAdvisor = firstAdvisor;
		firstAdvisor.increaseAdvisee();
	}

	/** Return the second advisor of this PhD (null if unknown or non-existent). */
	public PhD advisor2() {
		return secondAdvisor;
	}
	
	/** Add p as the second advisor of this person.
	 *  Precondition: The first advisor (of this person) is known, the second advisor
	 *  is unknown, p is not null, and p is different from the first advisor.
	 */
	public void addAdvisor2(PhD secondAdvisor) {
		assert secondAdvisor != null && this.firstAdvisor != null
				&& this.secondAdvisor == null && this.firstAdvisor != this.secondAdvisor;
		this.secondAdvisor = secondAdvisor;
		secondAdvisor.increaseAdvisee();
	}

	/** Return the number of PhD advisees of this person. */	
	public int numberOfAdvisEES() {
		return adviseeNum;
	}
	
	/** Increase the number of PhD advisees of this person by 1. */
	public void increaseAdvisee(){
		++this.adviseeNum;
	}
	
	/** Return value of "p is not null and this person got their
	 *  PhD after p did."
	 */  
	public boolean gotAfter(PhD p){
		
		return p != null && (p.getYear() <= this.PhDYear
				|| (p.getYear() == this.PhDYear && p.getMonth() < this.PhDMonth));	
	}
	
	/** Return value of "this person and p are intellectual siblings."
	 *  Precondition: p is not null.
	 */
	public boolean arePhDSiblings(PhD p){
		assert p != null;
		
		return p != this && this.firstAdvisor != null && p.advisor1() != null
				&& (this.firstAdvisor == p.advisor1() 
				    || (this.secondAdvisor != null && this.secondAdvisor == p.advisor1())
				    || (p.advisor2() != null && this.firstAdvisor == p.advisor2())
				    || (this.secondAdvisor != null && p.advisor2() != null 
				        && (this.firstAdvisor == p.advisor2() || this.secondAdvisor == p.advisor2())));
	}
}
