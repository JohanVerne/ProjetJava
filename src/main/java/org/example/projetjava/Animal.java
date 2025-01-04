package org.example.projetjava;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Animal {
    protected String nom;
    protected String sexe;
    protected String espece;
    protected double energie;
    protected int niveau; // Niveau hiérarchique du prédateur (1 = faible, 3 = fort)
    protected int age;
    protected int maxAge;
    protected double rapidite;
    protected double champDeVision;
    protected int x; // Position sur l'axe X
    protected int y; // Position sur l'axe Y
    protected Color color; // Color for GUI

    // Nouvel attribut pour enregistrer les parents
    protected List<Animal> parents;

    public Animal(String nom, String sexe, String espece, double energie, int niveau, int maxAge,
                  double rapidite, double champDeVision, Color color) {
        this.nom = nom;
        this.sexe = sexe;
        this.espece = espece;
        this.energie = energie;
        this.niveau = niveau;
        this.age = 0;
        this.maxAge = maxAge;
        this.rapidite = rapidite;
        this.champDeVision = champDeVision;
        this.x = 0; // Position initiale par défaut
        this.y = 0; // Position initiale par défaut
        this.parents = new ArrayList<>();
        this.color = color != null ? color : Color.WHITE; // Use provided color or default to white
    }

    // Constructor without color (defaults to white)
    public Animal(String nom, String sexe, String espece, double energie, int niveau, int maxAge,
                  double rapidite, double champDeVision) {
        this(nom, sexe, espece, energie, niveau, maxAge,
        rapidite, champDeVision, Color.WHITE);
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

    public int getNiveau() {
        return niveau;
    }

    public void seDeplacer() {
        Random rand = new Random();
        this.x += rand.nextInt((int) rapidite * 2 + 1) - rapidite; // Déplacement aléatoire
        this.y += rand.nextInt((int) rapidite * 2 + 1) - rapidite;

        // Limiter la position aux bornes de la carte (0, tailleCarte)
        this.x = Math.max(0, Math.min(this.x, Ecosysteme.tailleCarte - 1)); // Taille max : tailleCarte - 1
        this.y = Math.max(0, Math.min(this.y, Ecosysteme.tailleCarte - 1));

        // Perte d'énergie proportionnelle à la rapidité
        double coutEnergieDeplacement = rapidite/2; // Par exemple, 10% de la rapidité
        this.energie -= coutEnergieDeplacement;

        // Vérification que l'énergie ne tombe pas en dessous de 0
        if (this.energie < 0) {
            this.energie = 0;
        }

        System.out.println(nom + " s'est déplacé. Nouvelle position : (" + x + ", " + y + "), " +
                "énergie restante : " + energie);
    }

    public void formerGroupe(List<Animal> animaux) {
        double sommeX = this.x;
        double sommeY = this.y;
        int nombreProches = 1;

        for (Animal autre : animaux) {
            if (this.espece.equals(autre.espece)
                    && autre != this && calculerDistance(autre.getX(), autre.getY()) <= champDeVision) {
                sommeX += autre.getX();
                sommeY += autre.getY();
                nombreProches++;
            }
        }

        if (nombreProches > 1) {
            int nouvelleX = (int) (sommeX / nombreProches);
            int nouvelleY = (int) (sommeY / nombreProches);
            System.out.println(nom + " se rapproche du groupe. " +
                    "Nouvelle position : (" + nouvelleX + ", " + nouvelleY + ")");
            this.x = nouvelleX;
            this.y = nouvelleY;
        }
    }

    public Animal reproduire(List<Animal> animaux) {
        // Conditions générales pour permettre la reproduction
        if (this.energie < 50) {
            return null;
        }
        for (Animal autre : animaux) {
            if (this.espece.equals(autre.espece) && autre != this && autre.energie >= 50
                    && !this.sexe.equals(autre.sexe) && !this.estParentDe(autre)) {
                double distance = calculerDistance(autre.getX(), autre.getY());
                if (distance <= champDeVision) {
                    this.energie -= 30;
                    autre.energie -= 30;
                    // Appel de la méthode spécifique à l'espèce pour créer un bébé
                    return creerBebe(autre);
                }
            }
        }
        return null; // Pas de partenaire compatible
    }

    // Méthode pour vérifier si un animal est un parent
    public boolean estParentDe(Animal autre) {
        return this.parents.contains(autre) || autre.parents.contains(this);
    }

    // Méthode abstraite pour créer un bébé (spécifique dans chaque classe)
    protected abstract Animal creerBebe(Animal autre);

    public void fuir(List<Animal> animaux) {
        for (Animal autre : animaux) {
            if (autre.estVivant() && autre != this && autre.getNiveau() > this.niveau) {
                double distance = calculerDistance(autre.getX(), autre.getY());
                if (champDeVision >= distance) {
                    double probDetection = 1 - distance/champDeVision;
                    if (Math.random() < probDetection) { // Détection aléatoire basée sur la distance
                        System.out.println(nom + " détecte un prédateur (" + autre.nom + ") et tente de fuir !");
                        seDeplacer(); // Déplacement dans une direction aléatoire
                        return;
                    }
                }
            }
        }
        System.out.println(nom + " ne détecte aucun prédateur à proximité.");
    }

    public static String sexealeatoire() {
        if (Math.random() < 0.5) {
            return "mâle";
        }
        return "femelle";
    }

    public String getSpecies() {
        return this.espece; // Assuming a field "species" exists
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

    public String getNom() {
        return nom.replaceAll("\\d+$", ""); // Removes trailing digits
    }
}
