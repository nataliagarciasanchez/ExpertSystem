package engine;

import java.util.*;

import domain.Domain;

/**
 * Central class that keeps track of per-domain scores, entry eligibility, clinical and immunologic evidence,
 * as well as differential diagnoses and treatment suggestions.
 */
public class Scorecard {
    private boolean entryEligible = false;
    private final Map<Domain, Integer> points = new EnumMap<>(Domain.class);
    private final Map<Domain, String> criterios = new EnumMap<>(Domain.class);
    private boolean anyClinical = false;
    private final LinkedHashSet<String> diferenciales = new LinkedHashSet<>();
    private final List<String> tratamientos = new ArrayList<>();


    public void setEntryEligible(){
        this.entryEligible = true;
    }

    public boolean isEntryEligible(){
        return this.entryEligible;
    }
    /**
     * Updates a domain score if the new score is higher than the existing one
     *
     * @param domain type of domain
     * @param pts assigned points
     * @param clinical whether the domain is clinical (vs immunologic)
     * @param criterio human-readable description of the criterion
     */
    public void addIfHigher(Domain domain, int pts, boolean clinical,  String criterio){
        int current = points.getOrDefault(domain, 0);
        if(pts > current){
            points.put(domain, pts);
            criterios.put(domain, criterio + " → " + pts + " puntos");
        }
        if(clinical && pts >0){
            anyClinical = true;
        }
    }

    public Collection<String> getCriteriosCumplidos() {
        return criterios.values();
    }

    public int total(){
        return points.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Map<Domain, Integer> getPointsMap() {
        return points;
    }

    public void addTreatment(String tratamiento) {
        tratamientos.add(tratamiento);
    }

    public List<String> getTratamientos() {
        return tratamientos;
    }

    public int getPointsForDomain(Domain domain) {
        return points.getOrDefault(domain, 0);
    }
    public void addDifferential(String txt) {
        if (txt != null && !txt.isBlank()) diferenciales.add(txt.trim());
    }

    public java.util.List<String> getDiferenciales() {
        return new java.util.ArrayList<>(diferenciales);
    }


    @Override
    public String toString() {
        return "engine.Scorecard{" +
                "entryEligible=" + entryEligible +
                ", points=" + points +
                ", total=" + total() +
                ", anyClinical=" + anyClinical +
                ", tratamientos=" + tratamientos +
                '}';
    }
}
