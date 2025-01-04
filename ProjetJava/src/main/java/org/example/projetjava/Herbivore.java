package org.example.projetjava;

import java.util.List;

public class Herbivore extends Animal {

    public Herbivore(String nom, double energieInitiale, int maxAge, double rapidite, double champDeVision, double rationEau, double rationNourriture) {
        super(nom, energieInitiale, maxAge, rapidite, champDeVision, rationEau, rationNourriture);
    }

    public Herbivore(String nom, double energieInitiale, int maxAge, double rapidite, double champDeVision,
                     double rationEau, double rationNourriture, javafx.scene.paint.Color color) {
        super(nom, energieInitiale, maxAge, rapidite, champDeVision, rationEau, rationNourriture, color);
    }


    @Override
    public void seDeplacer() {
        System.out.println(nom + " cherche de la nourriture ou de l'eau.");
        super.seDeplacer();
    }

    public boolean seNourrirDansZone(List<ZoneRessource> ressources) {
        for (ZoneRessource zone : ressources) {
            double distance = calculerDistance(zone.getX(), zone.getY());
            if (distance <= champDeVision && !zone.estEpuisee()) {
                if (zone.getType().equals("Eau")) {
                    double partEau = zone.consommer(rationEau);
                    this.energie += partEau;
                    System.out.println(nom + " consomme " + partEau + " de " + zone.getType() + " dans la zone " + zone);
                    return true;
                } else if (zone.getType().equals("Nourriture")) {
                    double partNourriture = zone.consommer(rationNourriture);
                    this.energie += partNourriture;
                    System.out.println(nom + " consomme " + partNourriture + " de " + zone.getType() + " dans la zone " + zone);
                    return true;
                }
            }
        }
        return false; // Pas de ressource disponible à proximité
    }

    public void reagirAuPredateur(List<Animal> animaux) {
        for (Animal autre : animaux) {
            if (autre instanceof Predateur && autre.estVivant()) {
                double distance = calculerDistance(autre.getX(), autre.getY());
                if (champDeVision >= distance) {
                    double probDetection = 1 - distance / champDeVision;
                    System.out.println(probDetection);
                    if (Math.random() < probDetection) { // Détection aléatoire basée sur la distance
                        System.out.println(nom + " détecte un prédateur (" + autre.nom + ") et tente de fuir !");
                        super.seDeplacer(); // Déplacement dans une direction aléatoire
                        return;
                    }
                }
            }
        }
        System.out.println(nom + " ne détecte aucun prédateur à proximité.");
    }

    public void formerGroupe(List<Animal> animaux) {
        double sommeX = this.x;
        double sommeY = this.y;
        int nombreProches = 1;

        for (Animal autre : animaux) {
            if (autre instanceof Herbivore && autre != this && calculerDistance(autre.getX(), autre.getY()) <= champDeVision) {
                sommeX += autre.getX();
                sommeY += autre.getY();
                nombreProches++;
            }
        }

        if (nombreProches > 1) {
            int nouvelleX = (int) (sommeX / nombreProches);
            int nouvelleY = (int) (sommeY / nombreProches);
            System.out.println(nom + " se rapproche du groupe. Nouvelle position : (" + nouvelleX + ", " + nouvelleY + ")");
            this.x = nouvelleX;
            this.y = nouvelleY;
        }
    }

    @Override
    public void interagirAvecEnvironnement() {
        System.out.println(nom + " surveille les alentours.");
    }


}
