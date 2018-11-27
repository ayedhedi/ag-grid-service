package com.example.demoaggrid;

import org.jfairy.Fairy;
import org.jfairy.producer.person.Person;

import static org.jfairy.producer.person.PersonProperties.male;
import static org.jfairy.producer.person.PersonProperties.minAge;

public class Test {
    public static void main(String[] args) {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();

        System.out.println(person.getCompany().name());
        System.out.println(person.email());
        System.out.println(person.telephoneNumber());

    }
}
