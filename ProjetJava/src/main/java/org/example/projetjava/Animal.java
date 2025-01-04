package org.example.projetjava;

import javafx.scene.paint.Color;

import java.util.Random;

public abstract class Animal {
    protected String nom;
    protected double energie;
    protected int age;
    protected int maxAge;
    protected double rapidite;
    protected double champDeVision;
    protected double rationEau;
    protected double rationNourriture;
    protected int x; // Position sur l'axe X
    protected int y; // Position sur l'axe Y
    protected Color color; // Color for GUI

    public Animal(String nom, double energie, int maxAge, double rapidite, double champDeVision, double rationEau, double rationNourriture, Color color) {
        this.nom = nom;
        this.energie = energie;
        this.age = 0;
        this.maxAge = maxAge;
        this.rapidite = rapidite;
        this.champDeVision = champDeVision;
        this.rationEau = rationEau;
        this.rationNourriture = rationNourriture;
        this.x = 0; // Position initiale par défaut
        this.y = 0; // Position initiale par défaut
        this.color = color != null ? color : Color.WHITE; // Use provided color or default to white
    }

    // Constructor without color (defaults to white)
    public Animal(String nom, double energie, int maxAge, double rapidite, double champDeVision,
                  double rationEau, double rationNourriture) {
        this(nom, energie, maxAge, rapidite, champDeVision, rationEau, rationNourriture, Color.WHITE);
    }

    public boolean estVivant() {
        return energie > 0 && age < maxAge;
    }

    public void vieillir() {
        this.age++;
        if (this.age >= maxAge) {
            this.energie = 0; // Mort naturelle
        }
    }

    public void seDeplacer() {
        Random rand = new Random();
        this.x += rand.nextInt((int) rapidite * 2 + 1) - rapidite; // Déplacement aléatoire
        this.y += rand.nextInt((int) rapidite * 2 + 1) - rapidite;

        // Limiter la position aux bornes de la carte (0, tailleCarte)
        this.x = Math.max(0, Math.min(this.x, Ecosysteme.tailleCarte - 1)); // Taille max : tailleCarte - 1
        this.y = Math.max(0, Math.min(this.y, Ecosysteme.tailleCarte - 1));

        // Perte d'énergie proportionnelle à la rapidité
        double coutEnergieDeplacement = rapidite / 2; // Par exemple, 10% de la rapidité
        this.energie -= coutEnergieDeplacement;

        // Vérification que l'énergie ne tombe pas en dessous de 0
        if (this.energie < 0) {
            this.energie = 0;
        }

        System.out.println(nom + " s'est déplacé. Nouvelle position : (" + x + ", " + y + "), énergie restante : " + energie);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double calculerDistance(int cibleX, int cibleY) {
        return Math.sqrt(Math.pow(this.x - cibleX, 2) + Math.pow(this.y - cibleY, 2));
    }


    public abstract void interagirAvecEnvironnement();

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
