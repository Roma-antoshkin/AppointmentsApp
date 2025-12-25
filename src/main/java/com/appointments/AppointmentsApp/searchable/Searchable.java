package com.appointments.AppointmentsApp.searchable;

import java.util.HashSet;
import java.util.Set;

class TrigramSearch {

    public static Set<String> trigrams(String text) {
        Set<String> result = new HashSet<>();
        if (text == null) return result;

        text = text.toLowerCase();

        for (int i = 0; i <= text.length() - 3; i++) {
            result.add(text.substring(i, i + 3));
        }
        return result;
    }

    public static double containmentScore(String small, String big) {
        Set<String> smallTrigrams = trigrams(small);
        Set<String> bigTrigrams = trigrams(big);

        if (smallTrigrams.isEmpty()) return 1.0;

        int matches = 0;
        for (String tri : smallTrigrams) {
            if (bigTrigrams.contains(tri)) {
                matches++;
            }
        }

        return (double) matches / smallTrigrams.size();
    }
}

public interface Searchable {
    default boolean matches(String keyword, double identity) {

        return TrigramSearch.containmentScore(keyword.toLowerCase().replaceAll("\\s+", " ")
                , this.toString().toLowerCase().replaceAll("\\s+", " ")) >= identity;
    }
    default boolean matches(String keyword) {
        return matches(keyword, 0.6);
    }
}
