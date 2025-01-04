package org.example.projetjava;

public class ZoneRessource {
    private String type; // "Eau" ou "Nourriture"
    private int x; // Position X
    private int y; // Position Y
    private double quantite; // Quantité disponible

    public ZoneRessource(String type, int x, int y, double quantite) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.quantite = quantite;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double consommer(double quantiteDemandee) {
        double consomme = Math.min(quantite, quantiteDemandee);
        quantite -= consomme;
        return consomme;
    }

    public boolean estEpuisee() {
        return quantite <= 0;
    }

    @Override
    public String toString() {
        return String.format("org.example.projetjava.ZoneRessource [Type: %s, Position: (%d, %d), Quantité: %.2f]", type, x, y, quantite);
    }
}
