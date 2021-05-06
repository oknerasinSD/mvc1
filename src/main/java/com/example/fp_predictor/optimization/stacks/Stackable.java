package com.example.fp_predictor.optimization.stacks;

import com.example.fp_predictor.analysis.prediction.PlayerForecast;

/**
 * Интерфейс, который должны реализовывать классы, описывающие стеки
 */
public interface Stackable {

    public double getPrice();
    public double getExpectedPoints();
    public PlayerForecast[] getPlayers();
}