package com.rockwellcollins.atc.agree.analysis.ast;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

import com.rockwellcollins.atc.agree.agree.TimeInterval;

import jkind.lustre.Expr;

public class AgreeTimesPattern extends AgreePattern{
    
    public final BigInteger causeTimes;
    public final BigInteger effectTimes;

    public AgreeTimesPattern(String string, EObject reference, Expr cause, Expr effect,
            TimeInterval causeInterval, TimeInterval effectInterval, TriggerType causeType,
            TriggerType effectType, BigInteger causeTimes, BigInteger effectTimes) {
        super(string, reference, cause, effect, causeInterval, effectInterval, causeType, effectType);
        this.causeTimes = causeTimes;
        this.effectTimes = effectTimes;
    }

}