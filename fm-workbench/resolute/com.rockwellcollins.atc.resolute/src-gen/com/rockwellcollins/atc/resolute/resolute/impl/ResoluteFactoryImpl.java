/**
 */
package com.rockwellcollins.atc.resolute.resolute.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.rockwellcollins.atc.resolute.resolute.Arg;
import com.rockwellcollins.atc.resolute.resolute.BinaryExpr;
import com.rockwellcollins.atc.resolute.resolute.BoolExpr;
import com.rockwellcollins.atc.resolute.resolute.BuiltInFuncCallExpr;
import com.rockwellcollins.atc.resolute.resolute.BuiltinType;
import com.rockwellcollins.atc.resolute.resolute.ClaimArg;
import com.rockwellcollins.atc.resolute.resolute.ClaimBody;
import com.rockwellcollins.atc.resolute.resolute.ClaimString;
import com.rockwellcollins.atc.resolute.resolute.ConstantDefinition;
import com.rockwellcollins.atc.resolute.resolute.Definition;
import com.rockwellcollins.atc.resolute.resolute.DefinitionBody;
import com.rockwellcollins.atc.resolute.resolute.Expr;
import com.rockwellcollins.atc.resolute.resolute.FailExpr;
import com.rockwellcollins.atc.resolute.resolute.FilterMapExpr;
import com.rockwellcollins.atc.resolute.resolute.FnCallExpr;
import com.rockwellcollins.atc.resolute.resolute.FuncBody;
import com.rockwellcollins.atc.resolute.resolute.FunctionDefinition;
import com.rockwellcollins.atc.resolute.resolute.IdExpr;
import com.rockwellcollins.atc.resolute.resolute.IfThenElseExpr;
import com.rockwellcollins.atc.resolute.resolute.IntExpr;
import com.rockwellcollins.atc.resolute.resolute.ProveStatement;
import com.rockwellcollins.atc.resolute.resolute.QuantifiedExpr;
import com.rockwellcollins.atc.resolute.resolute.RealExpr;
import com.rockwellcollins.atc.resolute.resolute.ResoluteFactory;
import com.rockwellcollins.atc.resolute.resolute.ResoluteLibrary;
import com.rockwellcollins.atc.resolute.resolute.ResolutePackage;
import com.rockwellcollins.atc.resolute.resolute.ResoluteSubclause;
import com.rockwellcollins.atc.resolute.resolute.SetType;
import com.rockwellcollins.atc.resolute.resolute.StringExpr;
import com.rockwellcollins.atc.resolute.resolute.ThisExpr;
import com.rockwellcollins.atc.resolute.resolute.Type;
import com.rockwellcollins.atc.resolute.resolute.UnaryExpr;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class ResoluteFactoryImpl extends EFactoryImpl implements ResoluteFactory {
    /**
     * Creates the default factory implementation. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static ResoluteFactory init() {
        try {
            ResoluteFactory theResoluteFactory = (ResoluteFactory) EPackage.Registry.INSTANCE
                    .getEFactory("http://www.rockwellcollins.com/atc/resolute/Resolute");
            if (theResoluteFactory != null) {
                return theResoluteFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ResoluteFactoryImpl();
    }

    /**
     * Creates an instance of the factory. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public ResoluteFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
        case ResolutePackage.RESOLUTE_LIBRARY:
            return createResoluteLibrary();
        case ResolutePackage.DEFINITION:
            return createDefinition();
        case ResolutePackage.TYPE:
            return createType();
        case ResolutePackage.BUILTIN_TYPE:
            return createBuiltinType();
        case ResolutePackage.ARG:
            return createArg();
        case ResolutePackage.CONSTANT_DEFINITION:
            return createConstantDefinition();
        case ResolutePackage.FUNCTION_DEFINITION:
            return createFunctionDefinition();
        case ResolutePackage.CLAIM_STRING:
            return createClaimString();
        case ResolutePackage.DEFINITION_BODY:
            return createDefinitionBody();
        case ResolutePackage.EXPR:
            return createExpr();
        case ResolutePackage.RESOLUTE_SUBCLAUSE:
            return createResoluteSubclause();
        case ResolutePackage.PROVE_STATEMENT:
            return createProveStatement();
        case ResolutePackage.SET_TYPE:
            return createSetType();
        case ResolutePackage.CLAIM_ARG:
            return createClaimArg();
        case ResolutePackage.FUNC_BODY:
            return createFuncBody();
        case ResolutePackage.CLAIM_BODY:
            return createClaimBody();
        case ResolutePackage.BINARY_EXPR:
            return createBinaryExpr();
        case ResolutePackage.UNARY_EXPR:
            return createUnaryExpr();
        case ResolutePackage.ID_EXPR:
            return createIdExpr();
        case ResolutePackage.THIS_EXPR:
            return createThisExpr();
        case ResolutePackage.FAIL_EXPR:
            return createFailExpr();
        case ResolutePackage.INT_EXPR:
            return createIntExpr();
        case ResolutePackage.REAL_EXPR:
            return createRealExpr();
        case ResolutePackage.BOOL_EXPR:
            return createBoolExpr();
        case ResolutePackage.STRING_EXPR:
            return createStringExpr();
        case ResolutePackage.IF_THEN_ELSE_EXPR:
            return createIfThenElseExpr();
        case ResolutePackage.QUANTIFIED_EXPR:
            return createQuantifiedExpr();
        case ResolutePackage.BUILT_IN_FUNC_CALL_EXPR:
            return createBuiltInFuncCallExpr();
        case ResolutePackage.FN_CALL_EXPR:
            return createFnCallExpr();
        case ResolutePackage.FILTER_MAP_EXPR:
            return createFilterMapExpr();
        default:
            throw new IllegalArgumentException("The class '" + eClass.getName()
                    + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ResoluteLibrary createResoluteLibrary() {
        ResoluteLibraryImpl resoluteLibrary = new ResoluteLibraryImpl();
        return resoluteLibrary;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Definition createDefinition() {
        DefinitionImpl definition = new DefinitionImpl();
        return definition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Type createType() {
        TypeImpl type = new TypeImpl();
        return type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public BuiltinType createBuiltinType() {
        BuiltinTypeImpl builtinType = new BuiltinTypeImpl();
        return builtinType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Arg createArg() {
        ArgImpl arg = new ArgImpl();
        return arg;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ConstantDefinition createConstantDefinition() {
        ConstantDefinitionImpl constantDefinition = new ConstantDefinitionImpl();
        return constantDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public FunctionDefinition createFunctionDefinition() {
        FunctionDefinitionImpl functionDefinition = new FunctionDefinitionImpl();
        return functionDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ClaimString createClaimString() {
        ClaimStringImpl claimString = new ClaimStringImpl();
        return claimString;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public DefinitionBody createDefinitionBody() {
        DefinitionBodyImpl definitionBody = new DefinitionBodyImpl();
        return definitionBody;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Expr createExpr() {
        ExprImpl expr = new ExprImpl();
        return expr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ResoluteSubclause createResoluteSubclause() {
        ResoluteSubclauseImpl resoluteSubclause = new ResoluteSubclauseImpl();
        return resoluteSubclause;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ProveStatement createProveStatement() {
        ProveStatementImpl proveStatement = new ProveStatementImpl();
        return proveStatement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public SetType createSetType() {
        SetTypeImpl setType = new SetTypeImpl();
        return setType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ClaimArg createClaimArg() {
        ClaimArgImpl claimArg = new ClaimArgImpl();
        return claimArg;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public FuncBody createFuncBody() {
        FuncBodyImpl funcBody = new FuncBodyImpl();
        return funcBody;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ClaimBody createClaimBody() {
        ClaimBodyImpl claimBody = new ClaimBodyImpl();
        return claimBody;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public BinaryExpr createBinaryExpr() {
        BinaryExprImpl binaryExpr = new BinaryExprImpl();
        return binaryExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public UnaryExpr createUnaryExpr() {
        UnaryExprImpl unaryExpr = new UnaryExprImpl();
        return unaryExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public IdExpr createIdExpr() {
        IdExprImpl idExpr = new IdExprImpl();
        return idExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ThisExpr createThisExpr() {
        ThisExprImpl thisExpr = new ThisExprImpl();
        return thisExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public FailExpr createFailExpr() {
        FailExprImpl failExpr = new FailExprImpl();
        return failExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public IntExpr createIntExpr() {
        IntExprImpl intExpr = new IntExprImpl();
        return intExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public RealExpr createRealExpr() {
        RealExprImpl realExpr = new RealExprImpl();
        return realExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public BoolExpr createBoolExpr() {
        BoolExprImpl boolExpr = new BoolExprImpl();
        return boolExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public StringExpr createStringExpr() {
        StringExprImpl stringExpr = new StringExprImpl();
        return stringExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public IfThenElseExpr createIfThenElseExpr() {
        IfThenElseExprImpl ifThenElseExpr = new IfThenElseExprImpl();
        return ifThenElseExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public QuantifiedExpr createQuantifiedExpr() {
        QuantifiedExprImpl quantifiedExpr = new QuantifiedExprImpl();
        return quantifiedExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public BuiltInFuncCallExpr createBuiltInFuncCallExpr() {
        BuiltInFuncCallExprImpl builtInFuncCallExpr = new BuiltInFuncCallExprImpl();
        return builtInFuncCallExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public FnCallExpr createFnCallExpr() {
        FnCallExprImpl fnCallExpr = new FnCallExprImpl();
        return fnCallExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public FilterMapExpr createFilterMapExpr() {
        FilterMapExprImpl filterMapExpr = new FilterMapExprImpl();
        return filterMapExpr;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ResolutePackage getResolutePackage() {
        return (ResolutePackage) getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @deprecated
     * @generated
     */
    @Deprecated
    public static ResolutePackage getPackage() {
        return ResolutePackage.eINSTANCE;
    }

} // ResoluteFactoryImpl