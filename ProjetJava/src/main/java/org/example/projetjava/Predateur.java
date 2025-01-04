package org.example.projetjava;

import java.util.List;

public class Predateur extends Animal {
    protected double coutChasse;
    protected double gainChasse;

    public Predateur(String nom, double energieInitiale, int maxAge, double rapidite, double champDeVision, double rationEau, double rationNourriture, double coutChasse, double gainChasse) {
        super(nom, energieInitiale, maxAge, rapidite, champDeVision, rationEau, rationNourriture);
        this.coutChasse = coutChasse;
        this.gainChasse = gainChasse;
    }

    public Predateur(String nom, double energieInitiale, int maxAge, double rapidite, double champDeVision,
                     double rationEau, double rationNourriture, double coutChasse, double gainChasse,
                     javafx.scene.paint.Color color) {
        super(nom, energieInitiale, maxAge, rapidite, champDeVision, rationEau, rationNourriture, color);
        this.coutChasse = coutChasse;
        this.gainChasse = gainChasse;
    }


    @Override
    public void seDeplacer() {
        System.out.println(nom + " cherche des proies.");
        super.seDeplacer();
    }

    public boolean mangerProie(List<Animal> animaux) {
        for (Animal autre : animaux) {
            if (autre instanceof Herbivore && autre.estVivant()) {
                double distance = calculerDistance(autre.getX(), autre.getY());
                if (champDeVision >= distance) {
                    double probReussite = 1 - distance / champDeVision;
                    System.out.println(probReussite);
                    if (Math.random() < probReussite) { // Succès aléatoire basé sur la distance
                        autre.energie = 0; // La proie est tuée
                        this.energie -= coutChasse;
                        this.energie += gainChasse;
                        System.out.println(nom + " attrape et mange " + autre.nom + ". Énergie gagnée !");
                        return true;
                    } else {
                        this.energie -= coutChasse;
                        System.out.println(nom + " tente d'attraper " + autre.nom + " mais échoue.");
                    }
                }
            }
        }
        return false; // Aucune proie attrapée
    }


    @Override
    public void interagirAvecEnvironnement() {
        System.out.println(nom + " surveille les alentours pour trouver des proies.");
    }
}

