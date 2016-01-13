package com.rockwellcollins.atc.agree.analysis.realtime;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

import com.rockwellcollins.atc.agree.agree.AlwaysStatement;
import com.rockwellcollins.atc.agree.agree.ClosedTimeInterval;
import com.rockwellcollins.atc.agree.agree.IntLitExpr;
import com.rockwellcollins.atc.agree.agree.OpenLeftTimeInterval;
import com.rockwellcollins.atc.agree.agree.OpenRightTimeInterval;
import com.rockwellcollins.atc.agree.agree.OpenTimeInterval;
import com.rockwellcollins.atc.agree.agree.PatternStatement;
import com.rockwellcollins.atc.agree.agree.TimeInterval;
import com.rockwellcollins.atc.agree.agree.WhenHoldsStatement;
import com.rockwellcollins.atc.agree.agree.WhenOccursStatment;
import com.rockwellcollins.atc.agree.agree.WheneverBecomesTrueStatement;
import com.rockwellcollins.atc.agree.agree.WheneverHoldsStatement;
import com.rockwellcollins.atc.agree.agree.WheneverImpliesStatement;
import com.rockwellcollins.atc.agree.agree.WheneverOccursStatement;
import com.rockwellcollins.atc.agree.agree.util.AgreeSwitch;
import com.rockwellcollins.atc.agree.analysis.AgreeException;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeASTBuilder;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeStatement;
import com.rockwellcollins.atc.agree.analysis.realtime.AgreePattern.TriggerType;
import com.rockwellcollins.atc.agree.analysis.realtime.AgreePatternInterval.IntervalType;

import jkind.lustre.BinaryExpr;
import jkind.lustre.BinaryOp;
import jkind.lustre.Expr;
import jkind.lustre.IntExpr;
import jkind.lustre.UnaryExpr;
import jkind.lustre.UnaryOp;

public class AgreePatternBuilder extends AgreeSwitch<AgreeStatement> {

    private final String str;
    private final EObject ref;
    private final AgreeASTBuilder builder;

    public AgreePatternBuilder(String str, EObject ref, AgreeASTBuilder builder) {
        this.str = str;
        this.ref = ref;
        this.builder = builder;
    }

    @Override
    public AgreeStatement caseAlwaysStatement(AlwaysStatement object) {
        return new AgreeStatement(str, builder.doSwitch(object.getExpr()), ref);
    }

    @Override
    public AgreeStatement caseWheneverHoldsStatement(WheneverHoldsStatement object) {
        Expr cause = builder.doSwitch(object.getCause());
        Expr effect = builder.doSwitch(object.getEffect());
        
        AgreePatternInterval interval = getIntervalType(object.getInterval());
        
        return new AgreePattern(str, ref, cause, effect, null, interval, TriggerType.EVENT,
                TriggerType.CONDITION);
    }

    @Override
    public AgreeStatement caseWheneverImpliesStatement(WheneverImpliesStatement object) {
        Expr cause = builder.doSwitch(object.getCause());
        Expr lhs = builder.doSwitch(object.getLhs());
        Expr rhs = builder.doSwitch(object.getRhs());
        AgreePatternInterval effectInterval = getIntervalType(object.getInterval());

        Expr effect = new BinaryExpr(lhs, BinaryOp.IMPLIES, rhs);
        return new AgreePattern(str, ref, cause, effect, null, effectInterval, TriggerType.EVENT,
                TriggerType.CONDITION);
    }

    @Override
    public AgreeStatement caseWheneverOccursStatement(WheneverOccursStatement object) {
        Expr cause = builder.doSwitch(object.getCause());
        Expr effect = builder.doSwitch(object.getEffect());
        AgreePatternInterval effectInterval = getIntervalType(object.getInterval());

        return new AgreePattern(str, ref, cause, effect, null, effectInterval, TriggerType.EVENT,
                TriggerType.EVENT);
    }

    @Override
    public AgreeStatement caseWheneverBecomesTrueStatement(WheneverBecomesTrueStatement object) {
        Expr cause = builder.doSwitch(object.getCause());
        Expr effect = builder.doSwitch(object.getEffect());
        AgreePatternInterval effectInterval = getIntervalType(object.getInterval());

        // make the effect rising edge sensitive
//        Expr preEffect = new UnaryExpr(UnaryOp.PRE, effect);
//        Expr notPreEffect = new UnaryExpr(UnaryOp.NOT, preEffect);
//        Expr edgeEffect = new BinaryExpr(notPreEffect, BinaryOp.AND, effect);
//        effect = new BinaryExpr(effect, BinaryOp.ARROW, edgeEffect);
        return new AgreePattern(str, ref, cause, effect, null, effectInterval, TriggerType.EVENT,
                TriggerType.EVENT);
    }

    @Override
    public AgreeStatement caseWhenHoldsStatement(WhenHoldsStatement object) {
        Expr condition = builder.doSwitch(object.getCondition());
        Expr effect = builder.doSwitch(object.getEvent());
        AgreePatternInterval conditionInterval = getIntervalType(object.getConditionInterval());
        AgreePatternInterval effectInterval = getIntervalType(object.getEventInterval());

        return new AgreePattern(str, ref, condition, effect, conditionInterval, effectInterval,
                TriggerType.CONDITION, TriggerType.EVENT);
    }

    @Override
    public AgreeStatement caseWhenOccursStatment(WhenOccursStatment object) {
        Expr condition = builder.doSwitch(object.getCondition());
        Expr effect = builder.doSwitch(object.getEvent());
        Expr timesExpr = builder.doSwitch(object.getTimes());
        if (!(timesExpr instanceof IntExpr)) {
            throw new AgreeException("Expected an integer literal in 'When Occurs' pattern");
        }
        BigInteger times = ((IntExpr) timesExpr).value;
        AgreePatternInterval interval = getIntervalType(object.getInterval());

        return new AgreeTimesPattern(str, ref, condition, effect, interval, null, TriggerType.CONDITION,
                TriggerType.CONDITION, times, null);
    }
    
    private AgreePatternInterval getIntervalType(TimeInterval interval) {
        if(interval == null){
            return null;
        }
        Expr low = builder.doSwitch(interval.getLow());
        Expr high = builder.doSwitch(interval.getHigh());
        IntervalType type;

        if (interval instanceof OpenTimeInterval) {
            type = IntervalType.OPEN;
        } else if (interval instanceof OpenLeftTimeInterval) {
            type = IntervalType.OPEN_LEFT;
        } else if (interval instanceof OpenRightTimeInterval) {
            type = IntervalType.OPEN_RIGHT;
        } else if (interval instanceof ClosedTimeInterval) {
            type = IntervalType.CLOSED;
        } else {
            throw new AgreeException("Unhandled TimeInterval type: " + interval.getClass());
        }
        return new AgreePatternInterval(type, low, high);
    }


}