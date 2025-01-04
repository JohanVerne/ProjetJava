package org.example.projetjava;

public class Saison {
    private String nom;
    private double modificateurNourriture; // Modificateur pour la régénération des ressources
    private double modificateurEau;

    public Saison(String nom, double modificateurNourriture, double modificateurEau) {
        this.nom = nom;
        this.modificateurNourriture = modificateurNourriture;
        this.modificateurEau = modificateurEau;
    }

    public double ajusterNourriture(double quantite) {
        return quantite * modificateurNourriture;
    }

    public double ajusterEau(double quantite) {
        return quantite * modificateurEau;
    }

    @Override
    public String toString() {
        return String.format("org.example.projetjava.Saison [Nom: %s, Modificateurs - Nourriture: %.2f, Eau: %.2f]", nom, modificateurNourriture, modificateurEau);
    }
}
