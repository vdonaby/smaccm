package com.rockwellcollins.atc.resolute.analysis.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EObject;
import org.osate.aadl2.AbstractNamedValue;
import org.osate.aadl2.BasicPropertyAssociation;
import org.osate.aadl2.BooleanLiteral;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.ComponentClassifier;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.DataAccess;
import org.osate.aadl2.DataPort;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.Feature;
import org.osate.aadl2.IntegerLiteral;
import org.osate.aadl2.ListValue;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.Property;
import org.osate.aadl2.PropertyConstant;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.RealLiteral;
import org.osate.aadl2.RecordValue;
import org.osate.aadl2.StringLiteral;
import org.osate.aadl2.Subcomponent;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.ConnectionReference;
import org.osate.aadl2.instance.EndToEndFlowInstance;
import org.osate.aadl2.instance.FeatureCategory;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.FlowSpecificationInstance;
import org.osate.aadl2.instance.InstanceReferenceValue;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.properties.PropertyDoesNotApplyToHolderException;
import org.osate.aadl2.properties.PropertyNotPresentException;
import org.osate.aadl2.util.OsateDebug;
import org.osate.xtext.aadl2.errormodel.errorModel.ErrorBehaviorTransition;
import org.osate.xtext.aadl2.errormodel.errorModel.ErrorPropagation;
import org.osate.xtext.aadl2.errormodel.errorModel.ErrorTypes;
import org.osate.xtext.aadl2.errormodel.errorModel.TypeToken;
import org.osate.xtext.aadl2.errormodel.util.EMV2Util;
import org.osate.xtext.aadl2.properties.util.GetProperties;
import org.osate.xtext.aadl2.properties.util.PropertyUtils;

import com.rockwellcollins.atc.resolute.analysis.values.BoolValue;
import com.rockwellcollins.atc.resolute.analysis.values.IntValue;
import com.rockwellcollins.atc.resolute.analysis.values.NamedElementValue;
import com.rockwellcollins.atc.resolute.analysis.values.RangeValue;
import com.rockwellcollins.atc.resolute.analysis.values.RealValue;
import com.rockwellcollins.atc.resolute.analysis.values.ResoluteRecordValue;
import com.rockwellcollins.atc.resolute.analysis.values.ResoluteValue;
import com.rockwellcollins.atc.resolute.analysis.values.SetValue;
import com.rockwellcollins.atc.resolute.analysis.values.StringValue;
import com.rockwellcollins.atc.resolute.resolute.BuiltInFnCallExpr;

public class ResoluteBuiltInFnCallEvaluator {
	private static final BoolValue TRUE = new BoolValue(true);
	private static final BoolValue FALSE = new BoolValue(false);

	private final EvaluationContext context;

	public ResoluteBuiltInFnCallEvaluator(EvaluationContext context) {
		this.context = context;
	}

	public ResoluteValue evaluate(BuiltInFnCallExpr fnCallExpr, List<ResoluteValue> args) {
		switch (fnCallExpr.getFn()) {
		/*
		 * Primary type: aadl
		 */
		case "has_property": {
			NamedElement element = args.get(0).getNamedElement();
			Property prop = (Property) args.get(1).getNamedElement();
			return bool(getPropertyExpression(element, prop) != null);
		}

		case "property": {
			NamedElement element = args.get(0).getNamedElement();
			Property prop = (Property) args.get(1).getNamedElement();

			PropertyExpression expr = getPropertyExpression(element, prop);
			if (expr == null) {
				if (args.size() > 2) {
					return args.get(2);

				}
				throw new ResoluteFailException("Property " + prop.getName() + " not defined on " + element.getName(),
						fnCallExpr);
			}

			return exprToValue(expr);
		}

		case "property_member": {
			ResoluteRecordValue record = (ResoluteRecordValue) args.get(0);
			String fieldName = args.get(1).getString().toLowerCase();

			ResoluteValue fieldValue = record.getField(fieldName);
			if (fieldValue == null) {
				throw new ResoluteFailException("Record Field " + fieldName + " not found", fnCallExpr);
			}
			return fieldValue;
		}

		case "has_parent": {
			NamedElement element = args.get(0).getNamedElement();
			EObject parent = element.eContainer();
			return bool(parent instanceof NamedElement);
		}

		case "parent": {
			NamedElement element = args.get(0).getNamedElement();
			EObject parent = element.eContainer();
			if (parent instanceof NamedElement) {
				return new NamedElementValue((NamedElement) parent);
			} else {
				throw new ResoluteFailException("Unable to get parent of " + args.get(0), fnCallExpr);
			}
		}

		case "name": {
			return new StringValue(args.get(0).getNamedElement().getName());
		}

		case "type": {
			NamedElement element = args.get(0).getNamedElement();
			NamedElement type = builtinType(element);
			if (type == null) {
				throw new IllegalArgumentException("Unable to get type of: " + element);
			}
			return new NamedElementValue(type);
		}

		case "has_type": {
			NamedElement element = args.get(0).getNamedElement();
			NamedElement type = builtinType(element);
			return bool(type != null);
		}

		case "is_bound_to": {
			NamedElement ne = args.get(0).getNamedElement();
			NamedElement resource = args.get(1).getNamedElement();
			if ((ne instanceof ComponentInstance) && (resource instanceof ComponentInstance)) {
				ComponentInstance componentInstance = (ComponentInstance) ne;
				ComponentInstance resourceInstance = (ComponentInstance) resource;

				/**
				 * Check the processor binding
				 */

				for (ComponentInstance binding : GetProperties.getActualProcessorBinding(componentInstance)) {
					if (binding == resourceInstance) {
						return bool(true);
					}
				}

				/**
				 * Check the memory binding
				 */
				for (ComponentInstance binding : GetProperties.getActualMemoryBinding(componentInstance)) {
					if (binding == resourceInstance) {
						return bool(true);
					}
				}

			}

			if ((ne instanceof ConnectionInstance) && (resource instanceof ComponentInstance)) {
				ConnectionInstance ci = (ConnectionInstance) ne;
				ComponentInstance resourceInstance = (ComponentInstance) resource;

				for (ComponentInstance binding : GetProperties.getActualConnectionBinding(ci)) {
					if (binding == resourceInstance) {
						return bool(true);
					}
				}
			}
			return bool(false);

		}

		case "is_of_type": {
			NamedElement element = args.get(0).getNamedElement();
			NamedElement type = args.get(1).getNamedElement();
			if (element instanceof ComponentInstance) {
				ComponentInstance ci;
				ComponentType ct;
				ci = (ComponentInstance) element;

				if ((ci == null) || (ci.getSubcomponent() == null)) {
					return bool(false);
				}

				ct = ci.getSubcomponent().getComponentType();
				// cl = (Classifier) type;
				// return bool ((ct == cl ) || (ct.isDescendentOf(cl)));

				while (ct != null) {
					if (ct == type) {
						return bool(true);
					}
					ct = ct.getExtended();
				}

			}
			return bool(false);
		}

		case "has_member": {
			boolean hasMember;
			String memberName;
			NamedElement element;

			hasMember = false;
			element = args.get(0).getNamedElement();
			memberName = args.get(1).getString();

			if (element instanceof ComponentInstance) {
				element = ((ComponentInstance) element).getComponentClassifier();
			}

			if (element instanceof ComponentClassifier) {
				ComponentClassifier cc = (ComponentClassifier) element;
				for (Feature f : cc.getAllFeatures()) {
					if (f.getName().equalsIgnoreCase(memberName)) {
						hasMember = true;
					}
				}
			}

			if (element instanceof ComponentImplementation) {
				ComponentImplementation ci = (ComponentImplementation) element;
				for (Subcomponent s : ci.getAllSubcomponents()) {
					if (s.getName().equalsIgnoreCase(memberName)) {
						hasMember = true;
					}
				}
			}

			return bool(hasMember);
		}

		case "features": {
			NamedElement e = args.get(0).getNamedElement();
			if (e instanceof ComponentInstance) {
				ComponentInstance ci = (ComponentInstance) e;
				return createSetValue(ci.getFeatureInstances());
			} else if (e instanceof FeatureInstance) {
				FeatureInstance fi = (FeatureInstance) e;
				return createSetValue(fi.getFeatureInstances());
			} else {
				throw new ResoluteFailException("features not defined on object of type: " + args.get(0).getType(),
						fnCallExpr);
			}
		}

		case "connections": {
			NamedElement e = args.get(0).getNamedElement();
			if (e instanceof FeatureInstance) {
				FeatureInstance feat = (FeatureInstance) e;
				return new SetValue(context.getConnectionsForFeature(feat));
			} else if (e instanceof ComponentInstance) {
				ComponentInstance ci = (ComponentInstance) e;
				List<ResoluteValue> result = new ArrayList<>();
				// Include connections for all features on the component
				for (FeatureInstance feat : ci.getFeatureInstances()) {
					result.addAll(context.getConnectionsForFeature(feat));
				}
				// Include connections originating or terminating with the
				// component
				result.addAll(createSetValue(ci.getSrcConnectionInstances()).getSet());
				result.addAll(createSetValue(ci.getDstConnectionInstances()).getSet());
				return new SetValue(result);
			} else {
				throw new ResoluteFailException("connections not defined on object of type: " + args.get(0).getType(),
						fnCallExpr);
			}
		}

		/*
		 * Primary type: component
		 */
		case "subcomponents": {
			ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();
			SetValue sv = createSetValue(ci.getComponentInstances());
			return sv;
		}

		case "is_in_array": {
			boolean result = false;

			if (args.get(0).getNamedElement() instanceof ComponentInstance) {
				ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();

				if ((ci.getIndices() != null) && (ci.getIndices().size() > 0) && (ci.getIndices().get(0) > 0)) {
					result = true;
				}
			}
			if (args.get(0).getNamedElement() instanceof FeatureInstance) {
				FeatureInstance fi = (FeatureInstance) args.get(0).getNamedElement();
				if (fi.getIndex() > 0) {
					result = true;
				}
			}
			return new BoolValue(result);
		}

		case "has_prototypes": {
			boolean result = false;

			if (args.get(0).getNamedElement() instanceof ComponentInstance) {
				ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();

				if ((ci.getComponentClassifier().getAllPrototypes() != null)
						&& (ci.getComponentClassifier().getAllPrototypes().size() > 0)) {
					result = true;
				}

			}
			return new BoolValue(result);
		}

		case "has_modes": {
			boolean result = false;

			if (args.get(0).getNamedElement() instanceof ComponentInstance) {
				ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();
				if ((ci.getModeInstances() != null) && (ci.getModeInstances().size() > 0)) {
					result = true;
				}
			}
			return new BoolValue(result);
		}

		/*
		 * Primary type: connection
		 */
		case "source": {
			ConnectionInstance conn = (ConnectionInstance) args.get(0).getNamedElement();
			return new NamedElementValue(conn.getSource());
		}

		case "destination": {
			ConnectionInstance conn = (ConnectionInstance) args.get(0).getNamedElement();
			return new NamedElementValue(conn.getDestination());
		}

		/*
		 * Primary type: feature
		 */
		case "direction": {
			FeatureInstance feat = (FeatureInstance) args.get(0).getNamedElement();
			return new StringValue(feat.getDirection().toString());
		}

		case "is_processor": {
			ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();
			return new BoolValue(ci.getCategory() == ComponentCategory.PROCESSOR);
		}

		case "is_virtual_processor": {
			ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();
			return new BoolValue(ci.getCategory() == ComponentCategory.VIRTUAL_PROCESSOR);
		}

		case "is_system": {
			ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();
			return new BoolValue(ci.getCategory() == ComponentCategory.SYSTEM);
		}

		case "is_bus": {
			ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();
			return new BoolValue(ci.getCategory() == ComponentCategory.BUS);
		}

		case "is_virtual_bus": {
			ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();
			return new BoolValue(ci.getCategory() == ComponentCategory.VIRTUAL_BUS);
		}

		case "is_device": {
			ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();
			return new BoolValue(ci.getCategory() == ComponentCategory.DEVICE);
		}

		case "is_memory": {
			ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();
			return new BoolValue(ci.getCategory() == ComponentCategory.MEMORY);
		}

		case "is_thread": {
			ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();
			return new BoolValue(ci.getCategory() == ComponentCategory.THREAD);
		}

		case "is_process": {
			ComponentInstance ci = (ComponentInstance) args.get(0).getNamedElement();
			return new BoolValue(ci.getCategory() == ComponentCategory.PROCESS);
		}

		case "is_event_port": {
			NamedElement feat = args.get(0).getNamedElement();
			if (feat instanceof FeatureInstance) {

				FeatureInstance fi = (FeatureInstance) feat;

				if (fi.getCategory() == org.osate.aadl2.instance.FeatureCategory.EVENT_PORT) {
					return new BoolValue(true);
				}
				if (fi.getCategory() == org.osate.aadl2.instance.FeatureCategory.EVENT_DATA_PORT) {
					return new BoolValue(true);
				}
			}
			return new BoolValue(false);
		}

		case "is_abstract_feature": {
			boolean result = false;
			NamedElement feat = args.get(0).getNamedElement();
			if (feat instanceof FeatureInstance) {
				FeatureInstance fi = (FeatureInstance) feat;
				if (fi.getCategory() == FeatureCategory.ABSTRACT_FEATURE) {
					result = true;
				}
			}
			return new BoolValue(result);
		}

		case "is_port": {
			boolean ret;
			ret = false;
			NamedElement feat = args.get(0).getNamedElement();
			if (feat instanceof FeatureInstance) {
				FeatureInstance fi = (FeatureInstance) feat;
				if (fi.getCategory() == org.osate.aadl2.instance.FeatureCategory.DATA_PORT) {
					ret = true;
				}
				if (fi.getCategory() == org.osate.aadl2.instance.FeatureCategory.EVENT_DATA_PORT) {
					ret = true;
				}
				if (fi.getCategory() == org.osate.aadl2.instance.FeatureCategory.EVENT_PORT) {
					ret = true;
				}

			}
			return new BoolValue(ret);

		}

		case "is_data_port": {
			NamedElement feat = args.get(0).getNamedElement();
			if (feat instanceof FeatureInstance) {

				FeatureInstance fi = (FeatureInstance) feat;

				if (fi.getCategory() == org.osate.aadl2.instance.FeatureCategory.DATA_PORT) {
					return new BoolValue(true);
				}
				if (fi.getCategory() == org.osate.aadl2.instance.FeatureCategory.EVENT_DATA_PORT) {
					return new BoolValue(true);
				}
			}
			return new BoolValue(false);
		}
		/*
		 * Primary type: range
		 */
		case "lower_bound": {
			RangeValue rv = (RangeValue) args.get(0);
			return rv.getMin();
		}

		case "upper_bound": {
			RangeValue rv = (RangeValue) args.get(0);
			return rv.getMax();
		}

		/*
		 * Primary type: set
		 */
		case "member": {
			return bool(args.get(1).getSet().contains(args.get(0)));
		}

		case "size":
		case "length": {
			List<ResoluteValue> set = args.get(0).getSet();
			int setsize = set.size();
			return new IntValue(setsize);
		}

		case "sum": {
			List<ResoluteValue> set = args.get(0).getSet();
			if (set.isEmpty()) {
				return new IntValue(0);
			}

			ResoluteValue first = set.iterator().next();
			if (first.isInt()) {
				long sum = 0;
				for (ResoluteValue item : set) {
					sum += item.getInt();
				}
				return new IntValue(sum);
			} else {
				double sum = 0;
				for (ResoluteValue item : set) {
					sum += item.getReal();
				}
				return new RealValue(sum);
			}
		}

		case "union": {
			List<ResoluteValue> set = new ArrayList<>();
			set.addAll(args.get(0).getSet());
			set.addAll(args.get(1).getSet());
			return new SetValue(set);
		}

		case "intersect": {
			List<ResoluteValue> set = new ArrayList<>();
			for (ResoluteValue val1 : args.get(0).getSet()) {
				for (ResoluteValue val2 : args.get(1).getSet()) {
					if (val1.equals(val2)) {
						set.add(val1);
					}
				}
			}
			return new SetValue(set);
		}

		/*
		 * Other
		 */
		case "analysis": {
			String analysisName = args.get(0).getString();
			List<ResoluteValue> analysisArgs = args.subList(1, args.size());

			ResoluteValue value = EvaluateExternalAnalysis.evaluate(analysisName, context, analysisArgs);

			if (value == null) {
				throw new ResoluteFailException("External analysis '" + analysisName + "' failed", fnCallExpr);
			} else {
				return value;
			}
		}

		case "is_data_access": {
			NamedElement feat = args.get(0).getNamedElement();
			if (feat instanceof ConnectionInstance) {
				ComponentInstance accessedComponent = null;
				ConnectionInstance ci = (ConnectionInstance) feat;

//				OsateDebug.osateDebug("source=" + ci.getSource());
//				OsateDebug.osateDebug("destination=" + ci.getDestination());
				if (ci.getSource() instanceof ComponentInstance) {
					accessedComponent = (ComponentInstance) ci.getSource();
				}

				if (ci.getDestination() instanceof ComponentInstance) {
					accessedComponent = (ComponentInstance) ci.getDestination();
				}

				return new BoolValue((ci.getKind() == org.osate.aadl2.instance.ConnectionKind.ACCESS_CONNECTION)
						&& (accessedComponent.getCategory() == ComponentCategory.DATA));

			}

			return new BoolValue(feat instanceof DataAccess);
		}

		case "instance": {
			NamedElement decl = args.get(0).getNamedElement();
			SystemInstance top = context.getThisInstance().getSystemInstance();
			ComponentInstance result = null;
			for (ComponentInstance ci : top.getAllComponentInstances()) {
				if (isInstanceOf(ci, decl)) {
					if (result == null) {
						result = ci;
					} else {
						throw new ResoluteFailException("Found multiple instances of declarative element", fnCallExpr);
					}
				}
			}
			if (result != null) {
				return new NamedElementValue(result);
			} else {
				throw new ResoluteFailException("Failed to find instance of declarative element", fnCallExpr);
			}
		}

		case "debug": {
			int i = 0;
			String s = "";
			for (ResoluteValue arg : args) {
				if (i > 0) {
					s += ",";
				}
				s += "#" + i + ": " + arg.toString();
				i++;
			}
			OsateDebug.osateDebug(s);

			return TRUE;
		}

		case "instances": {
			NamedElement decl = args.get(0).getNamedElement();
			SystemInstance top = context.getThisInstance().getSystemInstance();
			List<NamedElementValue> result = new ArrayList<>();
			for (ComponentInstance ci : top.getAllComponentInstances()) {
				if (isInstanceOf(ci, decl)) {
					result.add(new NamedElementValue(ci));
				}
			}
			return new SetValue(result);
		}

		/*
		 * Error Annex
		 */
		case "error_state_reachable": {
			ComponentInstance comp = (ComponentInstance) args.get(0).getNamedElement();
			String stateName = args.get(1).getString();

			for (ErrorBehaviorTransition ebt : EMV2Util.getAllErrorBehaviorTransitions(comp)) {
				if (ebt.getTarget().getName().equalsIgnoreCase(stateName)) {
					return TRUE;
				}
			}

			return FALSE;
		}

		case "propagate_error": {
			ComponentInstance comp = (ComponentInstance) args.get(0).getNamedElement();
			String errorName = args.get(1).getString();

			for (ErrorPropagation ep : EMV2Util.getAllOutgoingErrorPropagations(comp.getComponentClassifier())) {
				for (TypeToken tt : ep.getTypeSet().getTypeTokens()) {
					for (ErrorTypes et : tt.getType()) {
						if (et.getName().equalsIgnoreCase(errorName)) {
							return TRUE;
						}
					}
				}
			}

			return FALSE;
		}

		case "flow_source": {
			FlowSpecificationInstance flowSpec = (FlowSpecificationInstance) args.get(0).getNamedElement();
			return new NamedElementValue(flowSpec.getSource());
		}

		case "flow_destination": {
			FlowSpecificationInstance flowSpec = (FlowSpecificationInstance) args.get(0).getNamedElement();
			return new NamedElementValue(flowSpec.getDestination());
		}

		case "flow_elements": {
			EndToEndFlowInstance etef = (EndToEndFlowInstance) args.get(0).getNamedElement();
			return createSetValue(etef.getFlowElements());
		}

		case "flow_specifications": {
			ComponentInstance comp = (ComponentInstance) args.get(0).getNamedElement();
			return createSetValue(comp.getFlowSpecifications());
		}

		case "end_to_end_flows": {
			ComponentInstance comp = (ComponentInstance) args.get(0).getNamedElement();
			return createSetValue(comp.getEndToEndFlows());
		}

		default:
			throw new IllegalArgumentException("Unknown function: " + fnCallExpr.getFn());
		}
	}

	private static ResoluteValue bool(boolean bool) {
		return bool ? TRUE : FALSE;
	}

	private static SetValue createSetValue(Iterable<? extends NamedElement> iterable) {
		List<ResoluteValue> result = new ArrayList<ResoluteValue>();
		for (NamedElement ne : iterable) {
			result.add(new NamedElementValue(ne));
		}
		return new SetValue(result);
	}

	private static boolean isInstanceOf(ComponentInstance instance, NamedElement declarative) {
		ComponentClassifier cc = instance.getComponentClassifier();
		if (cc.equals(declarative)) {
			return true;
		}

		if (cc instanceof ComponentImplementation) {
			ComponentImplementation ci = (ComponentImplementation) cc;
			return (ci.getType().equals(declarative));
		}

		return false;
	}

	private static ResoluteValue exprToValue(PropertyExpression expr) {
		if (expr instanceof StringLiteral) {
			StringLiteral value = (StringLiteral) expr;
			return new StringValue(value.getValue());
		} else if (expr instanceof NamedValue) {
			NamedValue namedVal = (NamedValue) expr;
			if (namedVal.getNamedValue() instanceof PropertyConstant) {
				PropertyConstant pc = (PropertyConstant) namedVal.getNamedValue();
				return exprToValue(pc.getConstantValue());
			}
			AbstractNamedValue absVal = namedVal.getNamedValue();
			if (absVal instanceof EnumerationLiteral) {
				EnumerationLiteral enVal = (EnumerationLiteral) absVal;
				return new StringValue(enVal.getName());
			} else if (absVal instanceof PropertyConstant) {
				PropertyConstant pc = (PropertyConstant) absVal;
				return exprToValue(pc.getConstantValue());
			} else {
				throw new IllegalArgumentException("Unhandled NamedValue type: " + expr.getClass().getName());
			}
		} else if (expr instanceof BooleanLiteral) {
			BooleanLiteral value = (BooleanLiteral) expr;
			return bool(value.getValue());
		} else if (expr instanceof IntegerLiteral) {
			IntegerLiteral value = (IntegerLiteral) expr;
			return new IntValue((long) value.getScaledValue());
		} else if (expr instanceof RealLiteral) {
			RealLiteral value = (RealLiteral) expr;
			return new RealValue(value.getValue());
		} else if (expr instanceof org.osate.aadl2.RangeValue) {
			org.osate.aadl2.RangeValue value = (org.osate.aadl2.RangeValue) expr;
			return new RangeValue(exprToValue(value.getMinimum()), exprToValue(value.getMaximum()));
		} else if (expr instanceof InstanceReferenceValue) {
			InstanceReferenceValue value = (InstanceReferenceValue) expr;
			return new NamedElementValue(value.getReferencedInstanceObject());
		} else if (expr instanceof ListValue) {
			ListValue value = (ListValue) expr;
			List<ResoluteValue> result = new ArrayList<>();
			for (PropertyExpression element : value.getOwnedListElements()) {
				result.add(exprToValue(element));
			}
			return new SetValue(result);
		} else if (expr instanceof RecordValue) {
			Stream<BasicPropertyAssociation> fieldsStream = ((RecordValue) expr).getOwnedFieldValues().stream();
			Map<String, ResoluteValue> fieldsMap = fieldsStream.collect(Collectors.toMap(field -> {
				return field.getProperty().getName().toLowerCase();
			}, field -> {
				return exprToValue(field.getOwnedValue());
			}));
			return new ResoluteRecordValue(fieldsMap);
		} else {
			throw new IllegalArgumentException("Unknown property expression type: " + expr.getClass().getName());
		}
	}

	private static NamedElement builtinType(NamedElement ne) {
		if (ne instanceof ConnectionInstance) {
			ConnectionInstance ci = (ConnectionInstance) ne;
			if (ci.getSource() instanceof FeatureInstance) {
				FeatureInstance src = (FeatureInstance) ci.getSource();
				return (NamedElement) src.getFeature().getFeatureClassifier();
			} else if (ci.getDestination() instanceof FeatureInstance) {
				FeatureInstance src = (FeatureInstance) ci.getDestination();
				return (NamedElement) src.getFeature().getFeatureClassifier();
			}
		} else if (ne instanceof DataPort) {
			DataPort dp = (DataPort) ne;
			return dp.getDataFeatureClassifier();
		} else if (ne instanceof FeatureInstance) {
			FeatureInstance fi = (FeatureInstance) ne;
			if (fi.getFeature() instanceof DataPort) {
				DataPort dp = (DataPort) fi.getFeature();
				return dp.getDataFeatureClassifier();
			}
			return (NamedElement) fi.getFeature().getFeatureClassifier();
		} else if (ne instanceof ComponentInstance) {
			ComponentInstance ci = (ComponentInstance) ne;
			return ci.getComponentClassifier();
		}

		return null;
	}

	private static PropertyExpression getPropertyExpression(NamedElement comp, Property prop) {
		PropertyExpression result;

		if (comp instanceof ConnectionInstance) {
			PropertyExpression expr;
			ConnectionInstance conn = (ConnectionInstance) comp;

			for (ConnectionReference ref : conn.getConnectionReferences()) {
				expr = getPropertyExpression(ref, prop);
				if (expr != null) {
					result = expr;
				}
			}
		}

		try {
			comp.getPropertyValue(prop); // this just checks to see if the
											// property is associated
			result = PropertyUtils.getSimplePropertyValue(comp, prop);
		} catch (PropertyDoesNotApplyToHolderException propException) {
			return null;
		} catch (PropertyNotPresentException propNotPresentException) {
			return null;
		}
		return result;
	}
}
