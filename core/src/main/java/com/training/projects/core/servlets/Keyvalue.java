package com.training.projects.core.servlets;

/**
 * this class used as POJO class.
 */
public class Keyvalue {
    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public double getCases() {
        return cases;
    }

    public void setCases(final double cases) {
        this.cases = cases;
    }

    private String state;
    private double cases;

    /**
     * this method used for object creation.
     *
     * @param state
     * @param cases
     */
    Keyvalue(final String state, final double cases) {
        this.state = state;
        this.cases = cases;
    }
}
