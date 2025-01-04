package org.example.projetjava;

import javafx.scene.paint.Color;

import java.util.List;

public class Humain extends Predateur {
    protected int intelligence;
    protected double cooperation;
    protected double aggression;

    public Humain(String nom, String sexe, String espece, double energieInitiale, int niveau, int maxAge,
                  double rapidite, double champDeVision, double coutChasse, double gainChasse, int intelligence,
                  double cooperation, double aggression, javafx.scene.paint.Color color) {
        super(nom, sexe, espece, energieInitiale, niveau, maxAge, rapidite, champDeVision, coutChasse, gainChasse, color );
        this.intelligence = intelligence;
        this.cooperation = cooperation;
        this.aggression = aggression;
    }

    public Humain(String nom, String sexe, String espece, double energieInitiale, int niveau, int maxAge,
                  double rapidite, double champDeVision, double coutChasse, double gainChasse, int intelligence,
                  double cooperation, double aggression) {
        super(nom, sexe, espece, energieInitiale, niveau, maxAge, rapidite, champDeVision, coutChasse, gainChasse);
        this.intelligence = intelligence;
        this.cooperation = cooperation;
        this.aggression = aggression;
    }


    @Override
    protected Animal creerBebe(Animal autre) {
        if (autre instanceof Humain) {
            Humain autreHumain = (Humain) autre;

            // Attributs hérités et combinés pour le bébé
            String nomBebe = "Enfant de " + this.nom + " et " + autreHumain.nom;
            double energieBebe = (this.energie + autreHumain.energie) / 2;
            double rapiditeBebe = (this.rapidite + autreHumain.rapidite) / 2;
            double champDeVisionBebe = (this.champDeVision + autreHumain.champDeVision) / 2;
            double coutChasseBebe = (this.coutChasse + autreHumain.coutChasse) / 2;
            double gainChasseBebe = (this.gainChasse + autreHumain.gainChasse) / 2;
            int intelligenceBebe = (this.intelligence + autreHumain.intelligence) / 2;
            double cooperationBebe = (this.cooperation + autreHumain.cooperation) / 2;
            double aggressionBebe = (this.aggression + autreHumain.aggression) / 2;
            Color color = this.getColor(); //Au pife

            System.out.println(this.nom + " se reproduit avec " + autreHumain.nom + ". Un bébé humain est né !");
            Humain bebe = new Humain(nomBebe, sexealeatoire(), this.espece, energieBebe, this.niveau,  this.maxAge, rapiditeBebe, champDeVisionBebe, coutChasseBebe, gainChasseBebe, intelligenceBebe, cooperationBebe, aggressionBebe, color);
            // Enregistrer les parents du bébé
            bebe.parents.add(this);
            bebe.parents.add(autre);
            return bebe;
        }
        return null;
    }

    // Coopérer avec d'autres humains
    public void cooperer(List<Animal> animaux) {
        for (Animal autre : animaux) {
            if (autre instanceof Humain && autre != this
                    && calculerDistance(autre.getX(), autre.getY()) <= champDeVision) {
                Humain autreHumain = (Humain) autre;
                if (this.cooperation > 0.5) { // Si propension à coopérer est élevée
                    double energiePartagee = this.energie * 0.1; // Partage 10% d'énergie
                    autreHumain.energie += energiePartagee;
                    this.energie -= energiePartagee;
                    System.out.println(this.nom + " coopère avec " + autreHumain.nom + " en partageant "
                            + energiePartagee + " énergie.");
                }
            }
        }
    }

    // Réagir face à une menace ou un conflit
    public void agirFaceAuConflit(List<Animal> animaux) {
        int alliesProches = 0;

        // Calcul du nombre d'humains proches
        for (Animal autre : animaux) {
            if (autre instanceof Humain && autre != this) {
                double distance = calculerDistance(autre.getX(), autre.getY());
                if (distance <= champDeVision) {
                    alliesProches++;
                }
            }
        }

        for (Animal autre : animaux) {
            if (autre instanceof Predateur && !(autre instanceof Humain)) {
                double distance = calculerDistance(autre.getX(), autre.getY());
                if (distance <= champDeVision) {
                    if (Math.random() < this.aggression) { // Probabilité d'agir basée sur l'agression
                        if (alliesProches >= 2) {
                            System.out.println(this.nom + " et les autres humains du groupe tuent et mangent "
                                    + autre.nom);
                            autre.energie = 0; // Réduction de l'énergie de l'ennemi
                            this.energie -= coutChasse;
                            this.energie += gainChasse;  // Gain d'énergie liée au conflit
                        } else {
                            System.out.println(this.nom + " brandit une arme et intimide " + autre.nom);
                            autre.energie -= 30; // Réduction de l'énergie de l'ennemi
                            autre.seDeplacer();
                            this.energie -= coutChasse/2;  // Perte d'énergie liée au conflit
                        }
                    }
                }
            }
        }
    }

    public boolean chasserHerbivore(List<Animal> animaux) {
        for (Animal autre : animaux) {
            if (autre.estVivant() && !autre.espece.equals(this.espece)) {
                double distance = calculerDistance(autre.getX(), autre.getY());
                if (champDeVision >= distance) {
                    double probReussite = 1 - distance / champDeVision;
                    if (Math.random() < probReussite) { // Succès aléatoire basé sur la distance
                        if (autre instanceof Herbivore) {
                            autre.energie = 0; // La proie est tuée
                            this.energie -= coutChasse;
                            this.energie += gainChasse;
                            System.out.println(nom + " attrape et mange " + autre.nom + ". Énergie gagnée !");
                            return true;
                        }
                    } else {
                        this.energie -= coutChasse;
                        System.out.println(nom + " tente de chasser " + autre.nom + " mais échoue.");
                    }
                }
            }
        }
        return false;
    }


    @Override
    public void interagirAvecEnvironnement() {
        System.out.println(nom + " observe et interagit avec son environnement.");
        // Interaction classique comme trouver des ressources ou éviter les dangers
    }

    @Override
    public void seDeplacer() {
        System.out.println(nom + " explore le territoire.");
        super.seDeplacer();
    }

    @Override
    public String toString() {
        return String.format("Humain [Nom: %s, Energie: %.2f, Sexe : %s, Intelligence: %d, " +
                        "Cooperation: %.2f, Aggression: %.2f]",
                nom, energie, sexe, intelligence, cooperation, aggression);
    }
}