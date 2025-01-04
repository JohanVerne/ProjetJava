package org.example.projetjava;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Vegetal {
    protected String type;
    protected double energie;
    protected double croissance;
    protected int x; // Position X
    protected int y; // Position Y
    protected boolean mature;
    protected Color color;

    public Vegetal(String type, double energieInitiale, int x, int y) {
        this.type = type;
        this.energie = energieInitiale;
        this.croissance = 0; // Croissance initiale
        this.x = x;
        this.y = y;
        this.mature = false;
    }

    public Vegetal(String type, double energieInitiale, int x, int y, Color color){
        this(type, energieInitiale,  x, y);
        this.color = color;

    }


    // Méthode pour faire croître la plante
    public void croitre() {
        croissance += 10; // Augmenter de 10 unités par jour (ajustable)
        if (croissance >= 100) {
            croissance = 100;
            mature = true;
        }
        System.out.println(type + " à (" + x + ", " + y + ") a une croissance de " + croissance + "%.");
    }

    // Vérifie si la plante est mature
    public boolean estMature() {
        return mature;
    }

    // Méthode pour être consommée par un herbivore
    public double etreConsomme(double quantiteDemandee) {
        double consomme = Math.min(energie, quantiteDemandee);
        energie -= consomme;
        System.out.println(type + " à (" + x + ", " + y + ") a été consommé pour " + consomme + " énergie.");
        return consomme;
    }

    // Méthode pour se reproduire (génère de nouvelles plantes)
    public List<Vegetal> seReproduire(int portee) {
        List<Vegetal> nouvellesPlantes = new ArrayList<>();
        if (mature) {
            int nombreNouvelles = (int) (Math.random() * 2) + 1; // 1 à 3 nouvelles plantes
            for (int i = 0; i < nombreNouvelles; i++) {
                int nouvelX = Math.max(0, x + (int) (Math.random() * portee * 2) - portee);
                int nouvelY = Math.max(0, y + (int) (Math.random() * portee * 2) - portee);
                Vegetal nouvellePlante = new Vegetal(this.type, this.energie / 2, nouvelX, nouvelY);
                nouvellesPlantes.add(nouvellePlante);
                System.out.println("Une nouvelle " + type + " a poussé à (" + nouvelX + ", " + nouvelY + ").");
            }
        }
        return nouvellesPlantes;
    }

    @Override
    public String toString() {
        return String.format("Vegetal [Type: %s, Energie: %.2f, Croissance: %.2f%%, Position: (%d, %d), Mature: %b]",
                type, energie, croissance, x, y, mature);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}