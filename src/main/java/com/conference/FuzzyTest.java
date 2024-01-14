package com.conference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class FuzzyTest {

	private static final String EMPTY = "";
	private static final String REGEX = "@";
	public static final String REGEX_SPECIAL_CHARS = "[^a-zA-Z]";

	public static void main(String[] args) {
		
		/**String input = "nagen-dra.koneti_as009b@gmail.com";
		 String output = Arrays.stream(input.split(REGEX)).findFirst().orElse(EMPTY).replaceAll(REGEX_SPECIAL_CHARS, EMPTY);
		System.out.println(output);**/
		Map<String, String> myMap = getCustomersData();

		for(Map.Entry<String, String> entry : myMap.entrySet()) {
			 String output = Arrays.stream(entry.getKey().split(REGEX)).findFirst().orElse(EMPTY).replaceAll(REGEX_SPECIAL_CHARS, EMPTY);
			int matchingRatio = getFuzzyWazzyScore(output,entry.getValue());
			System.out.println(entry.getKey() + ","+ output+"," +entry.getValue()+","+matchingRatio);
		}
	}

	public static int getFuzzyWazzyScore(String emailId, String employeeName) {
		 String email = emailId.replaceAll(REGEX_SPECIAL_CHARS, EMPTY);
		return FuzzySearch.ratio(convertToUpperCase(email), convertToUpperCase(employeeName));
	}

	private static String convertToUpperCase(String input) {
		return Optional.ofNullable(input).map(String::toUpperCase).orElse("Input string is null");
	}

	private static Map<String, String> getCustomersData() {
		Map<String, String> myMap = new HashMap<>();
		myMap.put("Sirbudhram@gmail.co", "Ram RamChand");
		myMap.put("shariq.saleem@almarai.com", "Saleem SaleemUddin");
		myMap.put("janaka.mampe@fourpoints.com", "SampathThenennehelage");
		myMap.put("azrabb81@gmail.com", "Akram MuhammadAkram");
		myMap.put("anand.sekharan@emirateshospital.ae", "Chandra SekharanVallat");
		myMap.put("mohammaddeeshankhan@gmail.com", "Deeshan MohammAyyub");
		myMap.put("awais.naseem@hilton.com", "Naseem Mu AkhtarChohan");
		myMap.put("lord.muzammil@yahoo.com", "Mahboob MehboobSagheer");
		myMap.put("mianaamir71157115@gmail.com", "Javed JavedIqbal");
		myMap.put("samahmerghani3@gmail.com", "Maerghni MohamedAlkedr");
		myMap.put("dcunhaleron28@gmail.com", "FlavinDcunha");
		myMap.put("ROHITH.JAYAKRISHNAN@BILFINGER.COM", "Krishna NarayananNair");
		myMap.put("sakthisee@gmail.com", "SankarSankar");
		myMap.put("nijim@alpagoproperties.com", "Nizam Nizam PutMohamed");
		myMap.put("joycexia186@gmail.com", "Ann RicaldeRojo");
		return myMap;
	}

}