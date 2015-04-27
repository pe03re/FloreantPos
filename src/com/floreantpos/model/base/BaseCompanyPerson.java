package com.floreantpos.model.base;

import com.floreantpos.model.Company;
import com.floreantpos.model.CompanyPerson;
import com.floreantpos.model.Person;

public class BaseCompanyPerson implements java.io.Serializable {

	public static String PROP_ID = "id";
	public static String PROP_COMPANY = "company";
	public static String PROP_PERSON = "person";

	protected Integer id;
	protected Company company;
	protected Person person;
	private int hashCode = Integer.MIN_VALUE;

	public BaseCompanyPerson() {
	}

	public BaseCompanyPerson(Company company, Person person) {
		this.company = company;
		this.person = person;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof CompanyPerson))
			return false;
		else {
			CompanyPerson compPers = (CompanyPerson) obj;
			return (this.getCompany().equals(compPers.getCompany()) && this.getPerson().equals(compPers.getPerson()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo(Object obj) {
		if (obj.hashCode() > hashCode())
			return 1;
		else if (obj.hashCode() < hashCode())
			return -1;
		else
			return 0;
	}

	public String toString() {
		return super.toString();
	}

}
