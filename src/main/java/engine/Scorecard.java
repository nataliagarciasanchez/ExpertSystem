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
    private final Map<Domain, String> criteria = new EnumMap<>(Domain.class);
    private boolean anyClinical = false;
    private final LinkedHashSet<String> differentials = new LinkedHashSet<>();
    private final List<String> treatments = new ArrayList<>();


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
     * @param criteria human-readable description of the criterion
     */
    public void addIfHigher(Domain domain, int pts, boolean clinical,  String criteria){
        int current = this.points.getOrDefault(domain, 0);
        if(pts > current){
            this.points.put(domain, pts);
            this.criteria.put(domain, criteria + " → " + pts + " points");
        }
        if(clinical && pts >0){
            this.anyClinical = true;
        }
    }

    public Collection<String> getMetCriteria() {
        return this.criteria.values();
    }

    public int total(){
        return this.points.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Map<Domain, Integer> getPointsMap() {
        return this.points;
    }

    public void addTreatment(String treatment) {
        this.treatments.add(treatment);
    }

    public List<String> getTreatments() {
        return this.treatments;
    }

    public int getPointsForDomain(Domain domain) {
        return this.points.getOrDefault(domain, 0);
    }
    public void addDifferential(String txt) {
        if (txt != null && !txt.isBlank()) this.differentials.add(txt.trim());
    }

    public boolean hasDifferentials() {
        return !this.differentials.isEmpty();
    }

    public boolean hasDifferentialName(String name) {
        return differentials.contains(name);
    }



    public java.util.List<String> getDifferentials() {
        return new java.util.ArrayList<>(this.differentials);
    }

    public Set<String> getDifferentialsSet() {
        return this.differentials;
    }


    public void reset() {
        this.points.clear();
        this.criteria.clear();
        this.differentials.clear();
        this.treatments.clear();
        this.entryEligible = false;
        this.anyClinical = false;
    }


    @Override
    public String toString() {
        return "engine.Scorecard{" +
                "entryEligible=" + this.entryEligible +
                ", points=" + this.points +
                ", total=" + total() +
                ", anyClinical=" + this.anyClinical +
                ", treatments=" + this.treatments +
                '}';
    }
}
