package org.example.projetjava;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ecosysteme {
    private List<Animal> animaux;
    private List<ZoneRessource> zonesRessources;
    private Saison saisonActuelle;
    public static int tailleCarte; // Taille de la carte (statique pour être utilisée globalement)

    public Ecosysteme(int tailleCarte) {
        this.animaux = new ArrayList<>();
        this.zonesRessources = new ArrayList<>();
        this.tailleCarte = tailleCarte;
        this.saisonActuelle = new Saison("Été", 1.2, 1.1); // org.example.projetjava.Saison initiale
    }

    public void ajouterAnimal(Animal animal) {
        animaux.add(animal);
    }

    public void ajouterZoneRessource(ZoneRessource zone) {
        zonesRessources.add(zone);
    }

    public void changerSaison(Saison nouvelleSaison) {
        saisonActuelle = nouvelleSaison;
        System.out.println("Changement de saison : " + saisonActuelle);
    }

    public void simulerJournee() {
        System.out.println("\n--- Début de la journée ---");
        System.out.println("org.example.projetjava.Saison actuelle : " + saisonActuelle);

        // Mise à jour des animaux
        for (Animal animal : new ArrayList<>(animaux)) { // Copie pour éviter les erreurs de modification
            if (animal.estVivant()) {
                // Interaction avec l'environnement
                animal.interagirAvecEnvironnement();

                if (animal instanceof Herbivore) {
                    Herbivore herbivore = (Herbivore) animal;

                    // Former un groupe avec les autres herbivores
                    herbivore.formerGroupe(animaux);

                    // Réagir si un prédateur est détecté
                    herbivore.reagirAuPredateur(animaux);

                    // Se nourrir dans les zones de ressources
                    boolean nourri = herbivore.seNourrirDansZone(zonesRessources);
                    if (!nourri) {
                        System.out.println(herbivore.nom + " n'a pas trouvé assez de nourriture ou d'eau.");
                    }
                }

                if (animal instanceof Predateur) {
                    Predateur predateur = (Predateur) animal;

                    // Traquer et manger des proies
                    boolean aMange = predateur.mangerProie(animaux);
                    if (!aMange) {
                        System.out.println(predateur.nom + " n'a pas trouvé de proies.");
                    }
                }

                // Déplacement et vieillissement
                animal.seDeplacer();
                animal.vieillir();

                // Affichage de l'état de l'animal
                System.out.println(animal);
            } else {
                System.out.println(animal.nom + " est mort.");
                animaux.remove(animal); // Retirer les animaux morts
            }
        }

        System.out.println("--- Fin de la journée ---\n");
    }


    private void regenererRessources() { //pas utilisée pour le moment, sert à rajouter de la nourriture ou de l'eau aux ressources naturelles
        for (ZoneRessource zone : zonesRessources) {
            if (zone.getType().equals("Nourriture")) {
                double regeneNourriture = saisonActuelle.ajusterNourriture(20);
                zone.consommer(-regeneNourriture); // Ajout de ressource (quantité négative pour "ajouter")
            } else if (zone.getType().equals("Eau")) {
                double regeneEau = saisonActuelle.ajusterEau(15);
                zone.consommer(-regeneEau); // Ajout de ressource
            }
        }
    }

    public static void main(String[] args) {
        // Création de l'écosystème
        Ecosysteme ecosysteme = new Ecosysteme(100);

        // Ajout de zones de ressources
        ecosysteme.ajouterZoneRessource(new ZoneRessource("Nourriture", 50, 50, 100));
        ecosysteme.ajouterZoneRessource(new ZoneRessource("Eau", 20, 30, 80));

        // Ajout d'animaux
        Random rand = new Random();
        Herbivore lapin1 = new Herbivore("Lapin 1", 50.0, 5, 10.0, 40, 5.0, 10);
        lapin1.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(lapin1);

        Herbivore lapin2 = new Herbivore("Lapin 2", 50.0, 5, 10.0, 40, 5.0, 10);
        lapin2.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(lapin2);

        Herbivore lapin3 = new Herbivore("Lapin 3", 50.0, 5, 10.0, 40, 5.0, 10);
        lapin3.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(lapin3);

        Herbivore lapin4 = new Herbivore("Lapin 4", 50.0, 5, 10.0, 40, 5.0, 10);
        lapin4.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(lapin4);


        Predateur lion = new Predateur("Lion", 100.0, 10, 20.0, 70, 0.0, 0, 15, 70);
        lion.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(lion);

        // Simulation de plusieurs journées
        for (int jour = 1; jour <= 5; jour++) {
            System.out.println("\n--- JOUR " + jour + " ---");
            ecosysteme.simulerJournee();

            // Changer la saison après 3 jours
            if (jour == 3) {
                ecosysteme.changerSaison(new Saison("Hiver", 0.8, 0.7));
            }
        }
    }

    public List<Animal> getAnimaux() {
        return animaux;
    }

    public List<ZoneRessource> getZonesRessources() {
        return zonesRessources;
    }
}
