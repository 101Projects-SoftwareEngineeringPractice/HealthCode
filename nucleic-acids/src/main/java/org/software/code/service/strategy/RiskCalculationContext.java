package org.software.code.service.strategy;

import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RiskCalculationContext {

    private final Map<String, RiskCalculationStrategy> strategies;

    @Autowired
    public RiskCalculationContext(List<RiskCalculationStrategy> strategyList) {
        strategies = strategyList.stream().collect(Collectors.toMap(strategy -> strategy.getClass().getSimpleName(), strategy -> strategy));
        System.out.println(strategies);
    }

    public String calculateRiskLevel(int population, int confirmedCases) {
        RiskCalculationStrategy strategy = getLatestStrategy();
        if (strategy == null) {
            throw new BusinessException(ExceptionEnum.RISK_CALCULATION_NOT_FIND);
        }
        return strategy.calculateRiskLevel(population, confirmedCases);
    }

    private RiskCalculationStrategy getLatestStrategy() {
        String latestStrategyName = "RiskCalculationStrategy202006"; // 设置最新策略的选择
        return strategies.get(latestStrategyName);
    }
}