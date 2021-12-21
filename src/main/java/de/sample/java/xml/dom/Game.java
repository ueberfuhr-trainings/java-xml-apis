package de.sample.java.xml.dom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    private String title;
    private String[] genres;
    private int rating;
    private Duration timePlayed;
    private double price;

}
