package org.software.code.service.strategy;


public interface RiskCalculationStrategy {
    String calculateRiskLevel(int population, int confirmedCases);
}