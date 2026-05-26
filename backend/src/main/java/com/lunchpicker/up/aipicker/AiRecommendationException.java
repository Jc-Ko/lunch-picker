package com.lunchpicker.up.aipicker;

public class AiRecommendationException extends RuntimeException {

    public AiRecommendationException(String message) {
        super(message);
    }

    public AiRecommendationException(String message, Throwable cause) {
        super(message, cause);
    }
}
