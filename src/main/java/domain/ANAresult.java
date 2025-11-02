package domain;

public class ANAresult {
    private final boolean positive;
    private final int denominator;
    /**
     * @param positive ANA result is positive or not
     * @param denominator the dilution level (ex. 80, 160, 320)
     */
    public ANAresult(boolean positive, int denominator){
        this.positive = positive;
        this.denominator = denominator;
    }

    public boolean isPositive(){
        return this.positive;
    }

    public int getDenominator(){
        return this.denominator;
    }

    @Override
    public String toString() {
        return "ANAResult{" +
                "positive=" + positive +
                ", titer=1:" + denominator +
                '}';
    }

}
