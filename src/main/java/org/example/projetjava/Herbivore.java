package org.example.projetjava;

import java.util.List;

public class Herbivore extends Animal {

    protected double rationEau;
    protected double rationNourriture;

    public Herbivore(String nom, String sexe, String espece, double energieInitiale, int niveau, int maxAge, double rapidite, double champDeVision, double rationEau, double rationNourriture, javafx.scene.paint.Color color) {
        super(nom, sexe, espece, energieInitiale, niveau, maxAge, rapidite, champDeVision, color);
        this.rationEau = rationEau;
        this.rationNourriture = rationNourriture;
    }


    public Herbivore(String nom, String sexe, String espece, double energieInitiale, int niveau, int maxAge,
                     double rapidite, double champDeVision, double rationEau, double rationNourriture) {
        super(nom, sexe, espece, energieInitiale, niveau, maxAge, rapidite, champDeVision);
        this.rationEau = rationEau;
        this.rationNourriture = rationNourriture;
    }

    @Override
    protected Animal creerBebe(Animal autre) {
        if (autre instanceof Herbivore) {
            String nomBebe = "Bébé de " + this.nom + " et " + autre.nom;
            double energieBebe = (this.energie + autre.energie) / 2;
            double rapiditeBebe = (this.rapidite + autre.rapidite) / 2;
            double champDeVisionBebe = (this.champDeVision + autre.champDeVision) / 2;
            double rationEauBebe = (this.rationEau + ((Herbivore) autre).rationEau) / 2;
            double rationNourritureBebe = (this.rationNourriture + ((Herbivore) autre).rationNourriture) / 2;


            System.out.println(this.nom + " se reproduit avec " + autre.nom + ". Un bébé herbivore est né !");
            Herbivore bebe = new Herbivore(nomBebe, sexealeatoire(), this.espece, energieBebe, 0, this.maxAge,
                    rapiditeBebe, champDeVisionBebe, rationEauBebe, rationNourritureBebe, this.color);
            // Enregistrer les parents du bébé
            bebe.parents.add(this);
            bebe.parents.add(autre);
            return bebe;
        }
        return null;
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
                    System.out.println(nom + " consomme " + partEau + " de " + zone.getType()
                            + " dans la zone " + zone);
                    return true;
                } else if (zone.getType().equals("Nourriture")) {
                    double partNourriture = zone.consommer(rationNourriture);
                    this.energie += partNourriture;
                    System.out.println(nom + " consomme " + partNourriture + " de " + zone.getType()
                            + " dans la zone " + zone);
                    return true;
                }
            }
        }
        return false; // Pas de ressource disponible à proximité
    }


    @Override
    public void interagirAvecEnvironnement() {
        System.out.println(nom + " surveille les alentours.");
    }

    @Override
    public String toString() {
        return String.format("Herbivore [Nom: %s, Energie: %.2f, Sexe: %s]",
                nom, energie, sexe);
    }

}
