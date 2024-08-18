package org.software.code.service.strategy.strategies;

import org.software.code.service.strategy.RiskCalculationStrategy;
import org.springframework.stereotype.Component;

@Component
public class RiskCalculationStrategy201912 implements RiskCalculationStrategy {
    @Override
    public String calculateRiskLevel(int population, int confirmedCases) {
        double infectionRate = (double) confirmedCases / population;
        if (infectionRate <= 0.1) {
            return "green";
        } else if (infectionRate <= 0.3) {
            return "yellow";
        } else {
            return "red";
        }
    }
}