--main options
%options ACTION, AN=JavaAction.java, GP=java, 
%options FILE-PREFIX=java, ESCAPE=$, PREFIX=TokenName, OUTPUT-SIZE=125 ,
%options NOGOTO-DEFAULT, SINGLE-PRODUCTIONS, LALR=1 , TABLE, 

--error recovering options.....
%options ERROR_MAPS 

--grammar understanding options
%options first follow
%options TRACE=FULL ,
%options VERBOSE

%options DEFERRED
%options NAMES=MAX
%options SCOPES

--Usefull macros helping reading/writing semantic actions
$Define 
$putCase 
/.    case $rule_number : if (DEBUG) { System.out.println("$rule_text"); }  //$NON-NLS-1$
		   ./

$break
/. 
			break;
./


$readableName 
/.1#$rule_number#./
$compliance
/.2#$rule_number#./
$recovery
/.2#$rule_number# recovery./
$recovery_template
/.3#$rule_number#./
$no_statements_recovery
/.4#$rule_number# 1./
-- here it starts really ------------------------------------------
$Terminals

	Identifier

	abstract assert boolean break byte case catch char class 
	continue const default do double else enum extends false final finally float
	for goto if implements import instanceof int
	interface long native new null package private
	protected public return short static strictfp super switch
	synchronized this throw throws transient true try void
	volatile while module open requires transitive exports opens to uses provides with

-- {ObjectTeams: keywords
	as base callin playedBy precedence team tsuper when with within

-- The following symbols are recognized as keywords only in specific contexts:
-- After "<-":
		replace after before 
-- After "->":
		get set
    
-- Markus Witte}

	IntegerLiteral
	LongLiteral
	FloatingPointLiteral
	DoubleLiteral
	CharacterLiteral
	StringLiteral
	TextBlock

	PLUS_PLUS
	MINUS_MINUS
	EQUAL_EQUAL
	LESS_EQUAL
	GREATER_EQUAL
	NOT_EQUAL
	LEFT_SHIFT
	RIGHT_SHIFT
	UNSIGNED_RIGHT_SHIFT
	PLUS_EQUAL
	MINUS_EQUAL
	MULTIPLY_EQUAL
	DIVIDE_EQUAL
	AND_EQUAL
	OR_EQUAL
	XOR_EQUAL
	REMAINDER_EQUAL
	LEFT_SHIFT_EQUAL
	RIGHT_SHIFT_EQUAL
	UNSIGNED_RIGHT_SHIFT_EQUAL
	OR_OR
	AND_AND
	PLUS
	MINUS
	NOT
	REMAINDER
	XOR
	AND
	MULTIPLY
	OR
	TWIDDLE
	DIVIDE
	GREATER
	LESS
	LPAREN
	RPAREN
	LBRACE
	RBRACE
	LBRACKET
	RBRACKET
	SEMICOLON
	QUESTION
	COLON
	COMMA
	DOT
	EQUAL
	AT
	ELLIPSIS
	ARROW
	COLON_COLON
	BeginLambda
	BeginIntersectionCast
	BeginTypeArguments
	ElidedSemicolonAndRightBrace
	AT308
	AT308DOTDOTDOT
	BeginCaseExpr
	RestrictedIdentifierYield

-- {ObjectTeams
	ATOT
	BINDIN
	CALLOUT_OVERRIDE
	SYNTHBINDOUT
-- Markus Witte}
--    BodyMarker

$Alias

    '::'   ::= COLON_COLON
    '->'   ::= ARROW
	'++'   ::= PLUS_PLUS
	'--'   ::= MINUS_MINUS
	'=='   ::= EQUAL_EQUAL
	'<='   ::= LESS_EQUAL
	'>='   ::= GREATER_EQUAL
	'!='   ::= NOT_EQUAL
	'<<'   ::= LEFT_SHIFT
	'>>'   ::= RIGHT_SHIFT
	'>>>'  ::= UNSIGNED_RIGHT_SHIFT
	'+='   ::= PLUS_EQUAL
	'-='   ::= MINUS_EQUAL
	'*='   ::= MULTIPLY_EQUAL
	'/='   ::= DIVIDE_EQUAL
	'&='   ::= AND_EQUAL
	'|='   ::= OR_EQUAL
	'^='   ::= XOR_EQUAL
	'%='   ::= REMAINDER_EQUAL
	'<<='  ::= LEFT_SHIFT_EQUAL
	'>>='  ::= RIGHT_SHIFT_EQUAL
	'>>>=' ::= UNSIGNED_RIGHT_SHIFT_EQUAL
	'||'   ::= OR_OR
	'&&'   ::= AND_AND
	'+'    ::= PLUS
	'-'    ::= MINUS
	'!'    ::= NOT
	'%'    ::= REMAINDER
	'^'    ::= XOR
	'&'    ::= AND
	'*'    ::= MULTIPLY
	'|'    ::= OR
	'~'    ::= TWIDDLE
	'/'    ::= DIVIDE
	'>'    ::= GREATER
	'<'    ::= LESS
	'('    ::= LPAREN
	')'    ::= RPAREN
	'{'    ::= LBRACE
	'}'    ::= RBRACE
	'['    ::= LBRACKET
	']'    ::= RBRACKET
	';'    ::= SEMICOLON
	'?'    ::= QUESTION
	':'    ::= COLON
	','    ::= COMMA
	'.'    ::= DOT
	'='    ::= EQUAL
	'@'	   ::= AT
	'...'  ::= ELLIPSIS
	'@308' ::= AT308
	'@308...' ::= AT308DOTDOTDOT
	
-- {ObjectTeams
	'@OT' ::= ATOT
	'<-'   ::= BINDIN
	'=>'   ::= CALLOUT_OVERRIDE
-- Markus Witte}
	
$Start
	Goal

$Rules

/.// This method is part of an automatic generation : do NOT edit-modify
protected void consumeRule(int act) {
  switch ( act ) {
./

Goal ::= '++' CompilationUnit
Goal ::= '--' MethodBody
-- Initializer
Goal ::= '>>' StaticInitializer
Goal ::= '>>' Initializer
-- error recovery
-- Modifiersopt is used to properly consume a header and exit the rule reduction at the end of the parse() method
Goal ::= '>>>' Header1 Modifiersopt
Goal ::= '!' Header2 Modifiersopt
Goal ::= '*' BlockStatements
Goal ::= '*' CatchHeader
-- JDOM
Goal ::= '&&' FieldDeclaration
Goal ::= '||' ImportDeclaration
Goal ::= '?' PackageDeclaration
Goal ::= '+' TypeDeclaration
Goal ::= '/' GenericMethodDeclaration
Goal ::= '&' ClassBodyDeclarations
-- code snippet
Goal ::= '%' Expression
Goal ::= '%' ArrayInitializer
-- completion parser
Goal ::= '~' BlockStatementsopt
Goal ::= '{' BlockStatementopt
-- source type converter
Goal ::= '||' MemberValue
-- syntax diagnosis
Goal ::= '?' AnnotationTypeMemberDeclaration
-- JSR 335 Reconnaissance missions.
Goal ::= '->' ParenthesizedLambdaParameterList
Goal ::= '(' ParenthesizedCastNameAndBounds
Goal ::= '<' ReferenceExpressionTypeArgumentsAndTrunk
-- JSR 308 Reconnaissance mission.
Goal ::= '@' TypeAnnotations
-- JSR 354 Reconnaissance mission.
Goal ::= '->' YieldStatement
--{ObjectTeams new goals:
Goal ::= ':' CallinParameterMappings
Goal ::= ';' CalloutParameterMappings
Goal ::= '^' ParameterMapping
Goal ::= '<' MethodSpecShort
Goal ::= '>' MethodSpecLong
-- SH}
/:$readableName Goal:/

-- {ObjectTeams
-- insert new Goals for Delta-Compiling
-- for Callin/Callout-Binding
-- Markus Witte}

Literal -> IntegerLiteral
Literal -> LongLiteral
Literal -> FloatingPointLiteral
Literal -> DoubleLiteral
Literal -> CharacterLiteral
Literal -> StringLiteral
Literal -> TextBlock
Literal -> null
Literal -> BooleanLiteral
/:$readableName Literal:/
BooleanLiteral -> true
BooleanLiteral -> false
/:$readableName BooleanLiteral:/

Type ::= PrimitiveType
/.$putCase consumePrimitiveType(); $break ./
Type -> ReferenceType
/:$readableName Type:/

PrimitiveType -> TypeAnnotationsopt NumericType
/:$readableName PrimitiveType:/
NumericType -> IntegralType
NumericType -> FloatingPointType
/:$readableName NumericType:/

PrimitiveType -> TypeAnnotationsopt 'boolean'
PrimitiveType -> TypeAnnotationsopt 'void'
IntegralType -> 'byte'
IntegralType -> 'short'
IntegralType -> 'int'
IntegralType -> 'long'
IntegralType -> 'char'
/:$readableName IntegralType:/
FloatingPointType -> 'float'
FloatingPointType -> 'double'
/:$readableName FloatingPointType:/

ReferenceType ::= ClassOrInterfaceType
/.$putCase consumeReferenceType(); $break ./
ReferenceType -> ArrayType
/:$readableName ReferenceType:/

---------------------------------------------------------------
-- 1.5 feature
---------------------------------------------------------------
ClassOrInterfaceType -> ClassOrInterface
ClassOrInterfaceType -> GenericType
/:$readableName Type:/

ClassOrInterface ::= Name
/.$putCase consumeClassOrInterfaceName(); $break ./
ClassOrInterface ::= GenericType '.' Name
/.$putCase consumeClassOrInterface(); $break ./
/:$readableName Type:/

GenericType ::= ClassOrInterface TypeArguments
/.$putCase consumeGenericType(); $break ./
/:$readableName GenericType:/

GenericType ::= ClassOrInterface '<' '>'
/.$putCase consumeGenericTypeWithDiamond(); $break ./
/:$readableName GenericType:/
/:$compliance 1.7:/

-- {ObjectTeams: "Base as Role" types:
LiftingTypeopt ::= $empty

LiftingTypeopt ::= 'as' BeginLiftingType Type
/.$putCase consumeLiftingType(); $break ./
/:$readableName LiftingType:/

BeginLiftingType ::= $empty
/.$putCase consumeBeginLiftingType(); $break ./

CatchLiftingTypeopt ::= $empty

CatchLiftingTypeopt ::= 'as' Type
/.$putCase consumeLiftingType(); $break ./
/:$readableName LiftingType:/
-- SH}

-- {ObjectTeams: "base.R" types:

BaseAnchoredType ::= 'base' '.' SimpleName
/.$putCase consumeBaseAnchoredType(); $break ./
/:$readableName QualifiedName:/

-- Connecting BaseAnchoredType to other rules (2 variants):
-- 1.) connect via      Type->ReferenceType->ClassOrInterfaceType
--     as well as via   ClassType->ClassOrInterfaceType
ClassOrInterfaceType -> BaseAnchoredType
/:$readableName BaseAnchoredType:/

-- 2.) via ReferenceType we may add dimensions:
ReferenceType -> BaseAnchoredType Dims
/:$readableName ArrayOfBaseAnchoredType:/
-- SH}

--
-- These rules have been rewritten to avoid some conflicts introduced
-- by adding the 1.1 features
--
-- ArrayType ::= PrimitiveType '[' ']'
-- ArrayType ::= Name '[' ']'
-- ArrayType ::= ArrayType '[' ']'
--

ArrayTypeWithTypeArgumentsName ::= GenericType '.' Name
/.$putCase consumeArrayTypeWithTypeArgumentsName(); $break ./
/:$readableName ArrayTypeWithTypeArgumentsName:/

ArrayType ::= PrimitiveType Dims
/.$putCase consumePrimitiveArrayType(); $break ./
ArrayType ::= Name Dims
/.$putCase consumeNameArrayType(); $break ./
ArrayType ::= ArrayTypeWithTypeArgumentsName Dims
/.$putCase consumeGenericTypeNameArrayType(); $break ./
ArrayType ::= GenericType Dims
/.$putCase consumeGenericTypeArrayType(); $break ./
/:$readableName ArrayType:/

ClassType -> ClassOrInterfaceType
/:$readableName ClassType:/

--------------------------------------------------------------
--------------------------------------------------------------

Name ::= SimpleName
/.$putCase consumeZeroTypeAnnotations(); $break ./
Name -> TypeAnnotations SimpleName
/:$compliance 1.8:/
Name -> QualifiedName
/:$readableName Name:/
/:$recovery_template Identifier:/

SimpleName -> 'Identifier'
/:$readableName SimpleName:/

UnannotatableName -> SimpleName
UnannotatableName ::= UnannotatableName '.' SimpleName
/.$putCase consumeUnannotatableQualifiedName(); $break ./
/:$readableName UnannotatableQualifiedName:/

QualifiedName ::= Name '.' SimpleName 
/.$putCase consumeQualifiedName(false); $break ./
QualifiedName ::= Name '.' TypeAnnotations SimpleName 
/.$putCase consumeQualifiedName(true); $break ./
/:$compliance 1.8:/
/:$readableName QualifiedName:/

TypeAnnotationsopt ::= $empty
/.$putCase consumeZeroTypeAnnotations(); $break ./
TypeAnnotationsopt -> TypeAnnotations
--{ObjectTeams: after TentativeTypeAnchor confirm that it was a *type annotation*:
/.$putCase confirmTypeAnnotation(); $break ./
-- SH}
/:$compliance 1.8:/
/:$readableName TypeAnnotationsopt:/

-- Production name hardcoded in parser. Must be ::= and not -> 
TypeAnnotations ::= TypeAnnotations0
/:$readableName TypeAnnotations:/

TypeAnnotations0 -> TypeAnnotation
/:$compliance 1.8:/
TypeAnnotations0 ::= TypeAnnotations0 TypeAnnotation
/. $putCase consumeOneMoreTypeAnnotation(); $break ./
/:$compliance 1.8:/
/:$readableName TypeAnnotations:/

TypeAnnotation ::= NormalTypeAnnotation
/. $putCase consumeTypeAnnotation(); $break ./
/:$compliance 1.8:/
TypeAnnotation ::= MarkerTypeAnnotation
/. $putCase consumeTypeAnnotation(); $break ./
/:$compliance 1.8:/
TypeAnnotation ::= SingleMemberTypeAnnotation
/. $putCase consumeTypeAnnotation(); $break ./
/:$compliance 1.8:/
/:$readableName TypeAnnotation:/

TypeAnnotationName ::= @308 UnannotatableName
/.$putCase consumeAnnotationName() ; $break ./
/:$readableName AnnotationName:/
/:$compliance 1.8:/
/:$recovery_template @ Identifier:/
NormalTypeAnnotation ::= TypeAnnotationName '(' MemberValuePairsopt ')'
/.$putCase consumeNormalAnnotation(true) ; $break ./
/:$readableName NormalAnnotation:/
/:$compliance 1.8:/
MarkerTypeAnnotation ::= TypeAnnotationName
/.$putCase consumeMarkerAnnotation(true) ; $break ./
/:$readableName MarkerAnnotation:/
/:$compliance 1.8:/
SingleMemberTypeAnnotation ::= TypeAnnotationName '(' SingleMemberAnnotationMemberValue ')'
/.$putCase consumeSingleMemberAnnotation(true) ; $break ./
/:$readableName SingleMemberAnnotation:/
/:$compliance 1.8:/

RejectTypeAnnotations ::= $empty
/.$putCase consumeNonTypeUseName(); $break ./
/:$readableName RejectTypeAnnotations:/

PushZeroTypeAnnotations ::= $empty
/.$putCase consumeZeroTypeAnnotations(); $break ./
/:$readableName ZeroTypeAnnotations:/

VariableDeclaratorIdOrThis ::= 'this'
/.$putCase consumeExplicitThisParameter(false); $break ./
/:$compliance 1.8:/
VariableDeclaratorIdOrThis ::= UnannotatableName '.' 'this'
/.$putCase consumeExplicitThisParameter(true); $break ./
/:$compliance 1.8:/
VariableDeclaratorIdOrThis ::= VariableDeclaratorId
/.$putCase consumeVariableDeclaratorIdParameter(); $break ./
/:$readableName VariableDeclaratorId:/

CompilationUnit ::= EnterCompilationUnit InternalCompilationUnit
/.$putCase consumeCompilationUnit(); $break ./
/:$readableName CompilationUnit:/

InternalCompilationUnit ::= PackageDeclaration
/.$putCase consumeInternalCompilationUnit(); $break ./
InternalCompilationUnit ::= PackageDeclaration ImportDeclarations ReduceImports
/.$putCase consumeInternalCompilationUnit(); $break ./
InternalCompilationUnit ::= PackageDeclaration ImportDeclarations ReduceImports TypeDeclarations
/.$putCase consumeInternalCompilationUnitWithTypes(); $break ./
InternalCompilationUnit ::= PackageDeclaration TypeDeclarations
/.$putCase consumeInternalCompilationUnitWithTypes(); $break ./
InternalCompilationUnit ::= ImportDeclarations ReduceImports
/.$putCase consumeInternalCompilationUnit(); $break ./
InternalCompilationUnit ::= TypeDeclarations
/.$putCase consumeInternalCompilationUnitWithTypes(); $break ./
InternalCompilationUnit ::= ImportDeclarations ReduceImports TypeDeclarations
/.$putCase consumeInternalCompilationUnitWithTypes(); $break ./
InternalCompilationUnit ::= $empty
/.$putCase consumeEmptyInternalCompilationUnit(); $break ./
/:$readableName CompilationUnit:/

--Java9 features
InternalCompilationUnit ::= ImportDeclarations ReduceImports ModuleDeclaration
/:$compliance 9:/
/.$putCase consumeInternalCompilationUnitWithModuleDeclaration(); $break ./
InternalCompilationUnit ::= ModuleDeclaration
/:$compliance 9:/
/.$putCase consumeInternalCompilationUnitWithModuleDeclaration(); $break ./
ModuleDeclaration ::= ModuleHeader ModuleBody
/:$compliance 9:/
/.$putCase consumeModuleDeclaration(); $break ./

-- to work around shift/reduce conflicts, we allow Modifiersopt in order to support annotations
-- in a module declaration, and then report errors if any modifiers other than annotations are
-- encountered
ModuleHeader ::= Modifiersopt ModuleModifieropt 'module' UnannotatableName
/:$compliance 9:/
/.$putCase consumeModuleHeader(); $break ./
ModuleModifieropt ::= $empty
ModuleModifieropt ::= ModuleModifier
/:$compliance 9:/
/.$putCase consumeModuleModifiers(); $break ./
ModuleModifier -> 'open'

ModuleBody ::= '{' ModuleStatementsOpt '}'
/:$compliance 9:/
/:$no_statements_recovery:/
ModuleStatementsOpt ::= $empty
/:$compliance 9:/
/.$putCase consumeEmptyModuleStatementsOpt(); $break ./
ModuleStatementsOpt -> ModuleStatements
/:$compliance 9:/
ModuleStatements ::= ModuleStatement
ModuleStatements ::= ModuleStatements ModuleStatement
/:$compliance 9:/
/.$putCase consumeModuleStatements(); $break ./

ModuleStatement ::= RequiresStatement
/:$compliance 9:/
ModuleStatement ::= ExportsStatement
/:$compliance 9:/
ModuleStatement ::= OpensStatement
/:$compliance 9:/
ModuleStatement ::= UsesStatement
/:$compliance 9:/
ModuleStatement ::= ProvidesStatement
/:$compliance 9:/

RequiresStatement ::=  SingleRequiresModuleName ';'
/:$compliance 9:/
/.$putCase consumeRequiresStatement(); $break ./
SingleRequiresModuleName ::= 'requires' RequiresModifiersopt UnannotatableName
/:$compliance 9:/
/.$putCase consumeSingleRequiresModuleName(); $break ./
RequiresModifiersopt ::= RequiresModifiers
/:$compliance 9:/
/.$putCase consumeModifiers(); $break ./
RequiresModifiersopt ::= $empty
/:$compliance 9:/
/.$putCase consumeDefaultModifiers(); $break ./
RequiresModifiers -> RequiresModifier
RequiresModifiers ::= RequiresModifiers RequiresModifier
/:$compliance 9:/
/.$putCase consumeModifiers2(); $break ./
RequiresModifier -> 'transitive'
RequiresModifier -> 'static'
ExportsStatement ::=  ExportsHeader TargetModuleListopt ';'
/:$compliance 9:/
/.$putCase consumeExportsStatement(); $break ./
ExportsHeader ::= 'exports' SinglePkgName
/:$compliance 9:/
/.$putCase consumeExportsHeader(); $break ./
TargetModuleListopt ::= $empty
TargetModuleListopt ::= 'to' TargetModuleNameList
/:$compliance 9:/
/.$putCase consumeTargetModuleList(); $break ./
TargetModuleName ::= UnannotatableName
/:$compliance 9:/
/.$putCase consumeSingleTargetModuleName(); $break ./
TargetModuleNameList -> TargetModuleName
TargetModuleNameList ::= TargetModuleNameList ',' TargetModuleName
/:$compliance 9:/
/.$putCase consumeTargetModuleNameList(); $break ./
SinglePkgName ::= UnannotatableName
/:$compliance 9:/
/.$putCase consumeSinglePkgName(); $break ./
OpensStatement ::=  OpensHeader TargetModuleListopt ';'
/:$compliance 9:/
/.$putCase consumeOpensStatement(); $break ./
OpensHeader ::= 'opens' SinglePkgName
/:$compliance 9:/
/.$putCase consumeOpensHeader(); $break ./
UsesStatement ::=  UsesHeader ';'
/:$compliance 9:/
/.$putCase consumeUsesStatement(); $break ./
UsesHeader ::= 'uses' Name
/.$putCase consumeUsesHeader(); $break ./
ProvidesStatement ::= ProvidesInterface WithClause ';'
/:$compliance 9:/
/.$putCase consumeProvidesStatement(); $break ./
ProvidesInterface ::= 'provides' Name
/:$compliance 9:/
/.$putCase consumeProvidesInterface(); $break ./
ServiceImplName ::= Name
/:$compliance 9:/
/.$putCase consumeSingleServiceImplName(); $break ./
ServiceImplNameList -> ServiceImplName
ServiceImplNameList ::= ServiceImplNameList ',' ServiceImplName
/:$compliance 9:/
/.$putCase consumeServiceImplNameList(); $break ./

WithClause ::= 'with' ServiceImplNameList
/:$compliance 9:/
/.$putCase consumeWithClause(); $break ./

ReduceImports ::= $empty
/.$putCase consumeReduceImports(); $break ./
/:$readableName ReduceImports:/

EnterCompilationUnit ::= $empty
/.$putCase consumeEnterCompilationUnit(); $break ./
/:$readableName EnterCompilationUnit:/

Header -> ImportDeclaration
Header -> PackageDeclaration
Header -> ClassHeader
Header -> InterfaceHeader
Header -> EnumHeader
Header -> AnnotationTypeDeclarationHeader
Header -> StaticInitializer
Header -> RecoveryMethodHeader
Header -> FieldDeclaration
Header -> AllocationHeader
Header -> ArrayCreationHeader
--{ObjectTeams:
Header -> RecoveryBindingHeader
Header -> PrecedenceDeclaration
-- SH}
Header -> ModuleHeader
Header -> RequiresStatement
Header -> ExportsStatement
Header -> UsesStatement
Header -> ProvidesStatement
Header -> OpensStatement
/:$readableName Header:/

Header1 -> Header
Header1 -> ConstructorHeader
/:$readableName Header1:/

Header2 -> Header
Header2 -> EnumConstantHeader
/:$readableName Header2:/

CatchHeader ::= 'catch' '(' CatchFormalParameter ')' '{'
/.$putCase consumeCatchHeader(); $break ./
/:$readableName CatchHeader:/

ImportDeclarations -> ImportDeclaration
ImportDeclarations ::= ImportDeclarations ImportDeclaration 
/.$putCase consumeImportDeclarations(); $break ./
/:$readableName ImportDeclarations:/

TypeDeclarations -> TypeDeclaration
TypeDeclarations ::= TypeDeclarations TypeDeclaration
/.$putCase consumeTypeDeclarations(); $break ./
/:$readableName TypeDeclarations:/

PackageDeclaration ::= PackageDeclarationName ';'
/.$putCase consumePackageDeclaration(); $break ./
/:$readableName PackageDeclaration:/

PackageDeclarationName ::= Modifiers 'package' PushRealModifiers Name RejectTypeAnnotations
/.$putCase consumePackageDeclarationNameWithModifiers(); $break ./
/:$readableName PackageDeclarationName:/
/:$compliance 1.5:/

PackageDeclarationName ::= PackageComment 'package' Name RejectTypeAnnotations
/.$putCase consumePackageDeclarationName(); $break ./
/:$readableName PackageDeclarationName:/

PackageComment ::= $empty
/.$putCase consumePackageComment(); $break ./
/:$readableName PackageComment:/

ImportDeclaration -> SingleTypeImportDeclaration
ImportDeclaration -> TypeImportOnDemandDeclaration
-----------------------------------------------
-- 1.5 feature
-----------------------------------------------
ImportDeclaration -> SingleStaticImportDeclaration
--{ObjectTeams: base import:
ImportDeclaration -> SingleBaseImportDeclaration
-- SH}
ImportDeclaration -> StaticImportOnDemandDeclaration
/:$readableName ImportDeclaration:/

SingleTypeImportDeclaration ::= SingleTypeImportDeclarationName ';'
/.$putCase consumeImportDeclaration(); $break ./
/:$readableName SingleTypeImportDeclaration:/

-- {ObjectTeams: special case: allow 'team' in imported package/type name:
-- orig: SingleTypeImportDeclarationName ::= 'import' Name RejectTypeAnnotations
SingleTypeImportDeclarationName ::= 'import' ImportName RejectTypeAnnotations
/.$putCase consumeSingleTypeImportDeclarationName(); $break ./
/:$readableName SingleTypeImportDeclarationName:/

ImportName -> Name
-- FIXME: reject type annotations also for the first name:
ImportName ::= Name '.' 'team' '.' Name 
/.$putCase consumeNameContainingTeam(); $break ./
/:$readableName Name:/
-- SH}			  

TypeImportOnDemandDeclaration ::= TypeImportOnDemandDeclarationName ';'
/.$putCase consumeImportDeclaration(); $break ./
/:$readableName TypeImportOnDemandDeclaration:/

TypeImportOnDemandDeclarationName ::= 'import' Name '.' RejectTypeAnnotations '*'
/.$putCase consumeTypeImportOnDemandDeclarationName(); $break ./
/:$readableName TypeImportOnDemandDeclarationName:/

TypeDeclaration -> ClassDeclaration
TypeDeclaration -> InterfaceDeclaration
-- this declaration in part of a list od declaration and we will
-- use and optimized list length calculation process 
-- thus we decrement the number while it will be incremend.....
TypeDeclaration ::= ';' 
/. $putCase consumeEmptyTypeDeclaration(); $break ./
-----------------------------------------------
-- 1.5 feature
-----------------------------------------------
TypeDeclaration -> EnumDeclaration
TypeDeclaration -> AnnotationTypeDeclaration
/:$readableName TypeDeclaration:/

--18.7 Only in the LALR(1) Grammar

Modifiers -> Modifier
Modifiers ::= Modifiers Modifier
/.$putCase consumeModifiers2(); $break ./
/:$readableName Modifiers:/

Modifier -> 'public' 
Modifier -> 'protected'
Modifier -> 'private'
Modifier -> 'static'
Modifier -> 'abstract'
Modifier -> 'final'
Modifier -> 'native'
Modifier -> 'synchronized'
Modifier -> 'transient'
Modifier -> 'volatile'
Modifier -> 'strictfp'
Modifier ::= Annotation
/.$putCase consumeAnnotationAsModifier(); $break ./
-- {ObjectTeams
Modifier -> 'team'
Modifier -> 'callin'
-- Markus Witte}
/:$readableName Modifier:/

--18.8 Productions from 8: Class Declarations
--ClassModifier ::=
--      'abstract'
--    | 'final'
--    | 'public'
--18.8.1 Productions from 8.1: Class Declarations

ClassDeclaration ::= ClassHeader ClassBody
/.$putCase consumeClassDeclaration(); $break ./
/:$readableName ClassDeclaration:/

--{ObjectTeams: playedBy & guard predicate support:
-- orig: ClassHeader ::= ClassHeaderName ClassHeaderExtendsopt ClassHeaderImplementsopt
ClassHeader ::= ClassHeaderName ClassHeaderExtendsopt ClassHeaderImplementsopt ClassHeaderPlayedByopt Predicateopt
-- MW+SH}
/.$putCase consumeClassHeader(); $break ./
/:$readableName ClassHeader:/

-----------------------------------------------
-- 1.5 features : generics
-----------------------------------------------
ClassHeaderName ::= ClassHeaderName1 TypeParameters
/.$putCase consumeTypeHeaderNameWithTypeParameters(); $break ./

ClassHeaderName -> ClassHeaderName1
/:$readableName ClassHeaderName:/

ClassHeaderName1 ::= Modifiersopt 'class' 'Identifier'
/.$putCase consumeClassHeaderName1(); $break ./
/:$readableName ClassHeaderName:/

ClassHeaderExtends ::= 'extends' ClassType
/.$putCase consumeClassHeaderExtends(); $break ./
/:$readableName ClassHeaderExtends:/

ClassHeaderImplements ::= 'implements' InterfaceTypeList
/.$putCase consumeClassHeaderImplements(); $break ./
/:$readableName ClassHeaderImplements:/

-- {ObjectTeams: ==== playedBy ====
ClassHeaderPlayedByopt ::= $empty
ClassHeaderPlayedByopt -> ClassHeaderPlayedBy
/:$readableName ClassHeaderPlayedBy:/

ClassHeaderPlayedBy ::= 'playedBy' ClassType
/.$putCase consumeClassHeaderPlayedBy(); $break ./
/:$readableName ClassHeaderPlayedBy:/

-- Markus Witte}

--{ObjectTeams: ==== predicates ====
Predicateopt ::= $empty
Predicateopt -> Predicate

Predicate -> RolePredicate
Predicate -> BasePredicate

RolePredicate ::= PredicateHeader PredicateBody
/:$readableName Predicate:/

PredicateHeader ::= 'when'
/.$putCase consumePredicate(false); $break ./
/:$readableName PredicateHeader:/

BasePredicate ::= BasePredicateHeader ForceBaseIsIdentifier PredicateBody RestoreBaseKeyword
/:$readableName Predicate:/

BasePredicateHeader ::= 'base' 'when'
/.$putCase consumePredicate(true); $break ./
/:$readableName PredicateHeader:/

PredicateBody ::= '(' ForceNoDiet Expression RestoreDiet ')'
/.$putCase consumePredicateExpression(); $break ./
/:$readableName PredicateBody:/

PredicateBody ::= '(' ')'
/.$putCase consumePredicateMissingExpression(); $break ./


ForceBaseIsIdentifier ::= $empty
/.$putCase consumeForceBaseIsIdentifier(); $break ./
/:$readableName ForceBaseIsIdentifier:/

RestoreBaseKeyword ::= $empty
/.$putCase consumeRestoreBaseKeyword(); $break ./
/:$readableName RestoreBaseKeyword:/
-- SH}

InterfaceTypeList -> InterfaceType
InterfaceTypeList ::= InterfaceTypeList ',' InterfaceType
/.$putCase consumeInterfaceTypeList(); $break ./
/:$readableName InterfaceTypeList:/

InterfaceType ::= ClassOrInterfaceType
/.$putCase consumeInterfaceType(); $break ./
/:$readableName InterfaceType:/

ClassBody ::= '{' ClassBodyDeclarationsopt '}'
/:$readableName ClassBody:/
/:$no_statements_recovery:/

ClassBodyDeclarations ::= ClassBodyDeclaration
ClassBodyDeclarations ::= ClassBodyDeclarations ClassBodyDeclaration
/.$putCase consumeClassBodyDeclarations(); $break ./
/:$readableName ClassBodyDeclarations:/

ClassBodyDeclaration -> ClassMemberDeclaration
ClassBodyDeclaration -> StaticInitializer
ClassBodyDeclaration -> ConstructorDeclaration
--1.1 feature
ClassBodyDeclaration ::= Diet NestedMethod CreateInitializer Block
/.$putCase consumeClassBodyDeclaration(); $break ./
/:$readableName ClassBodyDeclaration:/

Diet ::= $empty
/.$putCase consumeDiet(); $break./
/:$readableName Diet:/

Initializer ::= Diet NestedMethod CreateInitializer Block
/.$putCase consumeClassBodyDeclaration(); $break ./
/:$readableName Initializer:/

CreateInitializer ::= $empty
/.$putCase consumeCreateInitializer(); $break./
/:$readableName CreateInitializer:/

ClassMemberDeclaration -> FieldDeclaration
ClassMemberDeclaration -> MethodDeclaration

-- {ObjectTeams 
ClassMemberDeclaration -> BindingDeclaration
ClassMemberDeclaration -> PrecedenceDeclaration
-- Markus Witte}

--1.1 feature
ClassMemberDeclaration -> ClassDeclaration
--1.1 feature
ClassMemberDeclaration -> InterfaceDeclaration
-- 1.5 feature
ClassMemberDeclaration -> EnumDeclaration
ClassMemberDeclaration -> AnnotationTypeDeclaration
/:$readableName ClassMemberDeclaration:/

-- Empty declarations are not valid Java ClassMemberDeclarations.
-- However, since the current (2/14/97) Java compiler accepts them 
-- (in fact, some of the official tests contain this erroneous
-- syntax)
ClassMemberDeclaration ::= ';'
/.$putCase consumeEmptyTypeDeclaration(); $break./

GenericMethodDeclaration -> MethodDeclaration
GenericMethodDeclaration -> ConstructorDeclaration
/:$readableName GenericMethodDeclaration:/

--18.8.2 Productions from 8.3: Field Declarations
--VariableModifier ::=
--      'public'
--    | 'protected'
--    | 'private'
--    | 'static'
--    | 'final'
--    | 'transient'
--    | 'volatile'

FieldDeclaration ::= Modifiersopt Type VariableDeclarators ';'
/.$putCase consumeFieldDeclaration(); $break ./
/:$readableName FieldDeclaration:/

VariableDeclarators -> VariableDeclarator 
VariableDeclarators ::= VariableDeclarators ',' VariableDeclarator
/.$putCase consumeVariableDeclarators(); $break ./
/:$readableName VariableDeclarators:/
/:$recovery_template Identifier:/

VariableDeclarator ::= VariableDeclaratorId EnterVariable ExitVariableWithoutInitialization
VariableDeclarator ::= VariableDeclaratorId EnterVariable '=' ForceNoDiet VariableInitializer RestoreDiet ExitVariableWithInitialization
/:$readableName VariableDeclarator:/
/:$recovery_template Identifier:/

EnterVariable ::= $empty
/.$putCase consumeEnterVariable(); $break ./
/:$readableName EnterVariable:/

ExitVariableWithInitialization ::= $empty
/.$putCase consumeExitVariableWithInitialization(); $break ./
/:$readableName ExitVariableWithInitialization:/

ExitVariableWithoutInitialization ::= $empty
/.$putCase consumeExitVariableWithoutInitialization(); $break ./
/:$readableName ExitVariableWithoutInitialization:/

ForceNoDiet ::= $empty
/.$putCase consumeForceNoDiet(); $break ./
/:$readableName ForceNoDiet:/
RestoreDiet ::= $empty
/.$putCase consumeRestoreDiet(); $break ./
/:$readableName RestoreDiet:/

VariableDeclaratorId ::= 'Identifier' Dimsopt
/:$readableName VariableDeclaratorId:/
/:$recovery_template Identifier:/

VariableInitializer -> Expression
VariableInitializer -> ArrayInitializer
/:$readableName VariableInitializer:/
/:$recovery_template Identifier:/

--18.8.3 Productions from 8.4: Method Declarations
--MethodModifier ::=
--      'public'
--    | 'protected'
--    | 'private'
--    | 'static'
--    | 'abstract'
--    | 'final'
--    | 'native'
--    | 'synchronized'
--

MethodDeclaration -> AbstractMethodDeclaration
MethodDeclaration ::= MethodHeader MethodBody 
/.$putCase // set to true to consume a method with a body
 consumeMethodDeclaration(true, false); $break ./
/:$readableName MethodDeclaration:/

MethodDeclaration ::= DefaultMethodHeader MethodBody 
/.$putCase // set to true to consume a method with a body
 consumeMethodDeclaration(true, true); $break ./
/:$readableName MethodDeclaration:/

AbstractMethodDeclaration ::= MethodHeader ';'
/.$putCase // set to false to consume a method without body
 consumeMethodDeclaration(false, false); $break ./
/:$readableName MethodDeclaration:/

MethodHeader ::= MethodHeaderName FormalParameterListopt MethodHeaderRightParen MethodHeaderExtendedDims MethodHeaderThrowsClauseopt
/.$putCase consumeMethodHeader(); $break ./
/:$readableName MethodDeclaration:/

DefaultMethodHeader ::= DefaultMethodHeaderName FormalParameterListopt MethodHeaderRightParen MethodHeaderExtendedDims MethodHeaderThrowsClauseopt
/.$putCase consumeMethodHeader(); $break ./
/:$readableName MethodDeclaration:/

MethodHeaderName ::= Modifiersopt TypeParameters Type 'Identifier' '('
/.$putCase consumeMethodHeaderNameWithTypeParameters(false); $break ./
MethodHeaderName ::= Modifiersopt Type 'Identifier' '('
/.$putCase consumeMethodHeaderName(false); $break ./
/:$readableName MethodHeaderName:/

DefaultMethodHeaderName ::= ModifiersWithDefault TypeParameters Type 'Identifier' '('
/.$putCase consumeMethodHeaderNameWithTypeParameters(false); $break ./
DefaultMethodHeaderName ::= ModifiersWithDefault Type 'Identifier' '('
/.$putCase consumeMethodHeaderName(false); $break ./
/:$readableName MethodHeaderName:/

ModifiersWithDefault ::= Modifiersopt 'default' Modifiersopt
/.$putCase consumePushCombineModifiers(); $break ./
/:$readableName Modifiers:/
/:$compliance 1.8:/

MethodHeaderRightParen ::= ')'
/.$putCase consumeMethodHeaderRightParen(); $break ./
/:$readableName ):/
/:$recovery_template ):/

MethodHeaderExtendedDims ::= Dimsopt
/.$putCase consumeMethodHeaderExtendedDims(); $break ./
/:$readableName MethodHeaderExtendedDims:/

MethodHeaderThrowsClause ::= 'throws' ClassTypeList
/.$putCase consumeMethodHeaderThrowsClause(); $break ./
/:$readableName MethodHeaderThrowsClause:/

ConstructorHeader ::= ConstructorHeaderName FormalParameterListopt MethodHeaderRightParen MethodHeaderThrowsClauseopt
/.$putCase consumeConstructorHeader(); $break ./
/:$readableName ConstructorDeclaration:/

ConstructorHeaderName ::= Modifiersopt TypeParameters 'Identifier' '('
/.$putCase consumeConstructorHeaderNameWithTypeParameters(); $break ./
ConstructorHeaderName ::= Modifiersopt 'Identifier' '('
/.$putCase consumeConstructorHeaderName(); $break ./
/:$readableName ConstructorHeaderName:/

FormalParameterList -> FormalParameter
FormalParameterList ::= FormalParameterList ',' FormalParameter
/.$putCase consumeFormalParameterList(); $break ./
/:$readableName FormalParameterList:/

--1.1 feature
--{ObjectTeams: inserted LiftingTypeopt:
FormalParameter ::= Modifiersopt Type LiftingTypeopt VariableDeclaratorIdOrThis
/.$putCase consumeFormalParameter(false); $break ./
FormalParameter ::= Modifiersopt Type LiftingTypeopt PushZeroTypeAnnotations '...' VariableDeclaratorIdOrThis
/.$putCase consumeFormalParameter(true); $break ./
/:$compliance 1.5:/
FormalParameter ::= Modifiersopt Type LiftingTypeopt @308... TypeAnnotations '...' VariableDeclaratorIdOrThis
/.$putCase consumeFormalParameter(true); $break ./
/:$readableName FormalParameter:/
/:$compliance 1.8:/
/:$recovery_template Identifier Identifier:/
-- SH}
CatchFormalParameter ::= Modifiersopt CatchType CatchLiftingTypeopt VariableDeclaratorId
/.$putCase consumeCatchFormalParameter(); $break ./
/:$readableName FormalParameter:/
/:$recovery_template Identifier Identifier:/

CatchType ::= UnionType
/.$putCase consumeCatchType(); $break ./
/:$readableName CatchType:/

UnionType ::= Type
/.$putCase consumeUnionTypeAsClassType(); $break ./
UnionType ::= UnionType '|' Type
/.$putCase consumeUnionType(); $break ./
/:$readableName UnionType:/
/:$compliance 1.7:/

ClassTypeList -> ClassTypeElt
ClassTypeList ::= ClassTypeList ',' ClassTypeElt
/.$putCase consumeClassTypeList(); $break ./
/:$readableName ClassTypeList:/

ClassTypeElt ::= ClassType
/.$putCase consumeClassTypeElt(); $break ./
/:$readableName ClassType:/

-- {ObjectTeams: added Predicateopt
MethodBody ::= Predicateopt NestedMethod '{' BlockStatementsopt '}' 
/.$putCase consumeMethodBody(); $break ./
/:$readableName MethodBody:/
/:$no_statements_recovery:/
-- SH}

NestedMethod ::= $empty
/.$putCase consumeNestedMethod(); $break ./
/:$readableName NestedMethod:/

-- {ObjectTeams ========== METHOD BINDINGS ==========
BindingDeclaration -> CalloutBinding
BindingDeclaration -> CallinBinding
BindingDeclaration -> InvalidCallinBinding
/:$readableName BindingDeclaration:/

-- ==== CALLOUT: ====

-- LONG:
CalloutBinding ::= CalloutHeaderLong CalloutParameterMappingsopt
/.$putCase consumeCalloutBindingLong(); $break ./
/:$readableName CalloutBinding:/

CalloutHeaderLong ::= CalloutBindingLeftLong MethodSpecLong
/.$putCase consumeCalloutHeader(); $break ./
/:$readableName CalloutHeader:/

CalloutHeaderLong ::= CalloutBindingLeftLong CalloutFieldSpecLong
/.$putCase consumeCalloutHeader(); $break ./
/:$readableName CalloutHeader:/

CalloutBindingLeftLong ::= MethodSpecLong CalloutKind
/.$putCase consumeCalloutBindingLeft(true); $break ./
/:$readableName CalloutBindingLeft:/


-- SHORT:
-- Note(SH): Modifiersopt is needed to make grammar LALR(1), in real life modifiers must be empty
-- Note(SH): This rule includes callout to field short (there cannot be a distinct FieldSpecShort)
CalloutBinding ::= Modifiersopt CalloutBindingLeftShort CalloutModifieropt MethodSpecShort ';'
/.$putCase consumeCalloutHeader(); $break ./
/:$readableName CalloutHeader:/

CalloutBindingLeftShort ::= MethodSpecShort CalloutKind
/.$putCase consumeCalloutBindingLeft(false); $break ./
/:$readableName CalloutBindingLeft:/

-- this one added to give better message for parameter mappings despite of missing signatures:
-- Note(SH): Modifiersopt is needed to make grammar LALR(1), in real life modifiers must be empty
CalloutBinding ::= Modifiersopt CalloutBindingLeftShort CalloutModifieropt MethodSpecShort CalloutParameterMappings
/.$putCase consumeCalloutParameterMappingsInvalid(); $break ./

-- SYMBOLS:
CalloutKind -> '->'
CalloutKind -> '=>'
/:$readableName CalloutKind:/

CalloutModifieropt ::= $empty
CalloutModifieropt -> CalloutModifier
/:$readableName CalloutModifier:/
-- get set
CalloutModifier ::= 'get'
/.$putCase consumeCalloutModifier(TokenNameget); $break ./
/:$readableName CalloutModifier:/
CalloutModifier ::= 'set'
/.$putCase consumeCalloutModifier(TokenNameset); $break ./
/:$readableName CalloutModifier:/


---- CALLOUT-BINDING-PARAMETERMAPPING
CalloutParameterMappingsopt -> CalloutParameterMappings
CalloutParameterMappingsopt ::= ';'
/.$putCase consumeParameterMappingsEmpty(); $break ./
/:$readableName EmptyParameterMappings:/


CalloutParameterMappings ::= 'with' NestedParamMappings '{' CalloutParameterMappingList ,opt '}'
/.$putCase consumeParameterMappings(); $break ./
/:$readableName CalloutParameterMappings:/

-- note that this rule is needed for diagnose parsing where bodies of parameter mappings are ignored
CalloutParameterMappingList ::= $empty
/:$readableName EmptyParameterMappings:/

CalloutParameterMappingList -> ParameterMapping
CalloutParameterMappingList ::= CalloutParameterMappingList ',' ParameterMapping
/.$putCase consumeParameterMappingList(); $break ./
/:$readableName CalloutParameterMappingList:/

-- Syntax error to be detected by consumeParameterMappingIn/Out():
CalloutParameterMappingList ::= CalloutParameterMappingList ';' ParameterMapping
/.$putCase consumeParameterMappingList(); $break ./
/:$readableName CalloutParameterMappingList:/


ParameterMapping ::= Expression SYNTHBINDOUT 'Identifier'
/.$putCase consumeParameterMappingOut(); $break ./

ParameterMapping ::= 'Identifier' '<-' ForceBaseIsIdentifier Expression RestoreBaseKeyword
/.$putCase consumeParameterMappingIn(); $break ./
/:$readableName ParameterMapping:/


NestedParamMappings ::= $empty
/.$putCase consumeNestedParamMappings(); $break ./
/:$readableName NestedParameterMappings:/

-- ==== CALLIN-BINDING: ====

-- LONG:
CallinBinding ::= CallinHeaderLong CallinParameterMappingsopt
/.$putCase consumeCallinBindingLong(); $break ./
/:$readableName CallinBinding:/

CallinHeaderLong ::= CallinBindingLeftLong CallinModifier MethodSpecsLong Predicateopt 
/.$putCase consumeCallinHeader(); $break ./
/:$readableName CallinHeader:/

-- Note(SH): Modifiersopt is needed to make grammar LALR(1), in real life modifiers must be empty
CallinHeaderLong ::= Modifiersopt CallinLabel CallinBindingLeftLong CallinModifier MethodSpecsLong Predicateopt 
/.$putCase consumeCallinHeader(); $break ./

CallinBindingLeftLong ::= MethodSpecLong '<-'
/.$putCase consumeCallinBindingLeft(true); $break ./
/:$readableName CallinBindingLeft:/

-- SHORT:
-- Note(SH): Modifiersopt is needed to make grammar LALR(1), in real life modifiers must be empty
CallinBinding ::= Modifiersopt CallinBindingLeftShort CallinModifier BaseMethodSpecsShort Predicateopt ';'
/.$putCase consumeCallinHeader(); $break ./

-- Note(SH): Modifiersopt is needed to make grammar LALR(1), in real life modifiers must be empty (2 OCCURENCES!)
CallinBinding ::= Modifiersopt CallinLabel Modifiersopt CallinBindingLeftShort CallinModifier BaseMethodSpecsShort Predicateopt ';'
/.$putCase consumeCallinHeader(); $break ./

CallinBindingLeftShort ::= MethodSpecShort '<-'
/.$putCase consumeCallinBindingLeft(false); $break ./
/:$readableName CallinBindingLeft:/

-- LABEL:
CallinLabel ::= SimpleName ':' 
/.$putCase consumeCallinLabel(); $break ./
/:$readableName CallinLabel:/

-- MODIFIER:
CallinModifier ::= 'replace'
/.$putCase consumeCallinModifier(TokenNamereplace); $break ./
/:$readableName CallinModifier:/

CallinModifier ::= 'before'
/.$putCase consumeCallinModifier(TokenNamebefore); $break ./
/:$readableName CallinModifier:/

CallinModifier ::= 'after'
/.$putCase consumeCallinModifier(TokenNameafter); $break ./
/:$readableName CallinModifier:/


-- These rules are added to parse invalid callin-bindings without replace/before/after modifier
InvalidCallinModifier ::= $empty
/.$putCase consumeCallinModifierMissing(); $break ./
/:$readableName InvalidCallinModifier:/

-- Note(SH): Modifiersopt is needed to make grammar LALR(1), in real life modifiers must be empty
InvalidCallinBinding ::= Modifiersopt CallinBindingLeftShort InvalidCallinModifier BaseMethodSpecsShort
/.$putCase consumeCallinBindingInvalid(false,false); $break ./
/:$readableName InvalidCallinBinding:/

InvalidCallinBinding ::= Modifiersopt CallinLabel Modifiersopt CallinBindingLeftShort InvalidCallinModifier BaseMethodSpecsShort
/.$putCase consumeCallinBindingInvalid(false,false); $break ./
/:$readableName InvalidCallinBinding:/

InvalidCallinBinding ::= Modifiersopt CallinBindingLeftShort CallinModifier BaseMethodSpecsShort CallinParameterMappings
/.$putCase consumeCallinBindingInvalid(false,true); $break ./
/:$readableName InvalidCallinBinding:/

InvalidCallinBinding ::= Modifiersopt CallinLabel Modifiersopt CallinBindingLeftShort CallinModifier BaseMethodSpecsShort CallinParameterMappings
/.$putCase consumeCallinBindingInvalid(false,true); $break ./
/:$readableName InvalidCallinBinding:/

InvalidCallinBinding ::= CallinBindingLeftLong InvalidCallinModifier MethodSpecsLong CallinParameterMappingsopt
/.$putCase consumeCallinBindingInvalid(true,false); $break ./
/:$readableName InvalidCallinBinding:/

InvalidCallinBinding ::= Modifiersopt CallinLabel CallinBindingLeftLong InvalidCallinModifier MethodSpecsLong CallinParameterMappingsopt
/.$putCase consumeCallinBindingInvalid(true,false); $break ./
/:$readableName InvalidCallinBinding:/


---- CALLIN-BINDING-PARAMETERMAPPING
CallinParameterMappingsopt -> CallinParameterMappings
CallinParameterMappingsopt ::= ';'
/.$putCase consumeParameterMappingsEmpty(); $break ./
/:$readableName EmptyParameterMappings:/

CallinParameterMappings ::= 'with' NestedParamMappings '{' CallinParameterMappingList ,opt '}'
/.$putCase consumeParameterMappings(); $break ./
/:$readableName CallinParameterMappings:/

-- note that this rule is needed for diagnose parsing where bodies of parameter mappings are ignored
CallinParameterMappingList ::= $empty
/:$readableName EmptyParameterMappings:/

CallinParameterMappingList -> ParameterMapping
CallinParameterMappingList ::= CallinParameterMappingList ',' ParameterMapping
/.$putCase consumeParameterMappingList(); $break ./
/:$readableName CallinParameterMappingList:/

-- Syntax error to be detected by consumeParameterMappingIn/Out():
CallinParameterMappingList ::= CallinParameterMappingList ';' ParameterMapping
/.$putCase consumeParameterMappingList(); $break ./
/:$readableName CallinParameterMappingList:/

-- ==== METHOD_SPEC: ====

MethodSpecShort ::= SimpleName
/.$putCase consumeMethodSpecShort(); $break ./
/:$readableName MethodSpecShort:/

MethodSpecLong ::= MethodHeaderName FormalParameterListopt MethodHeaderRightParen
/.$putCase consumeMethodSpecLong(false); $break ./
/:$readableName MethodSpecLong:/

-- explicitly handle this error case:
MethodSpecLong ::= ConstructorHeaderName FormalParameterListopt MethodHeaderRightParen
/.$putCase consumeMethodSpecLongCtor(); $break ./
/:$readableName IllegalMethodSpecLong:/

-- base of callin can use '+' after return type:
BaseMethodSpecLong ::= MethodHeaderName FormalParameterListopt MethodHeaderRightParen
/.$putCase consumeMethodSpecLong(false); $break ./
/:$readableName MethodSpecLong:/

BaseMethodSpecLong ::= MethodSpecNamePlus FormalParameterListopt MethodHeaderRightParen
/.$putCase consumeMethodSpecLong(true); $break ./
/:$readableName MethodSpecLong:/

BaseMethodSpecLong ::= ConstructorHeaderName FormalParameterListopt MethodHeaderRightParen
/.$putCase consumeMethodSpecLong(false); $break ./
/:$readableName ConstructorSpecLong:/

MethodSpecNamePlus ::= Modifiersopt Type '+' 'Identifier'  '('
/.$putCase consumeMethodHeaderName(false); $break ./
/:$readableName MethodSpecName:/

CalloutFieldSpecLong ::= CalloutModifier Type 'Identifier'
/.$putCase consumeFieldSpecLong(); $break ./
/:$readableName CallloutFieldSpec:/

BaseMethodSpecsShort -> BaseMethodSpecListShort
/:$readableName MethodSpecsShort:/

BaseMethodSpecListShort -> BaseMethodSpecShort
BaseMethodSpecListShort ::= BaseMethodSpecListShort ',' BaseMethodSpecShort
/.$putCase consumeMethodSpecList(); $break ./
/:$readableName MethodSpecListShort:/

BaseMethodSpecShort -> MethodSpecShort

MethodSpecsLong -> MethodSpecListLong
/:$readableName MethodSpecsLong:/

MethodSpecListLong -> BaseMethodSpecLong
MethodSpecListLong ::= MethodSpecListLong ',' BaseMethodSpecLong
/.$putCase consumeMethodSpecList(); $break ./
/:$readableName MethodSpecListLong:/

-- ==== PRECEDENCE DECLARATION ====
PrecedenceDeclaration ::= 'precedence' BindingNames ';'
/.$putCase consumePrecedenceDeclaration(false); $break ./
/:$readableName PrecedenceDeclaration:/

PrecedenceDeclaration ::= 'precedence' 'after' BindingNames ';'
/.$putCase consumePrecedenceDeclaration(true); $break ./
/:$readableName PrecedenceAfterDeclaration:/

BindingNames -> BindingName
BindingNames ::= BindingNames ',' BindingName
/.$putCase consumeBindingNames(); $break ./
/:$readableName CallinBindingNames:/

BindingName ::= Name
/.$putCase consumeBindingName(); $break ./
/:$readableName CallinBindingName:/
-- Markus Witte}

--18.8.4 Productions from 8.5: Static Initializers

StaticInitializer ::= StaticOnly Block
/.$putCase consumeStaticInitializer(); $break./
/:$readableName StaticInitializer:/

StaticOnly ::= 'static'
/.$putCase consumeStaticOnly(); $break ./
/:$readableName StaticOnly:/

--18.8.5 Productions from 8.6: Constructor Declarations
--ConstructorModifier ::=
--      'public'
--    | 'protected'
--    | 'private'
--
--
ConstructorDeclaration ::= ConstructorHeader MethodBody
/.$putCase consumeConstructorDeclaration() ; $break ./ 
-- These rules are added to be able to parse constructors with no body
ConstructorDeclaration ::= ConstructorHeader ';'
/.$putCase consumeInvalidConstructorDeclaration() ; $break ./ 
/:$readableName ConstructorDeclaration:/

-- the rules ExplicitConstructorInvocationopt has been expanded
-- in the rule below in order to make the grammar lalr(1).

ExplicitConstructorInvocation ::= 'this' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocation(0, THIS_CALL); $break ./

ExplicitConstructorInvocation ::= OnlyTypeArguments 'this' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocationWithTypeArguments(0,THIS_CALL); $break ./

ExplicitConstructorInvocation ::= 'super' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocation(0,SUPER_CALL); $break ./

ExplicitConstructorInvocation ::= OnlyTypeArguments 'super' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocationWithTypeArguments(0,SUPER_CALL); $break ./

-- {ObjectTeams
ExplicitConstructorInvocation ::= 'tsuper' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocation(0,TSUPER_CALL); $break ./

ExplicitConstructorInvocation ::= Name '.' 'tsuper' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocation(2,TSUPER_CALL); $break ./
-- Markus Witte}

--1.1 feature
ExplicitConstructorInvocation ::= Primary '.' 'super' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocation(1, SUPER_CALL); $break ./

ExplicitConstructorInvocation ::= Primary '.' OnlyTypeArguments 'super' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocationWithTypeArguments(1, SUPER_CALL); $break ./

--1.1 feature
ExplicitConstructorInvocation ::= Name '.' 'super' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocation(2, SUPER_CALL); $break ./

ExplicitConstructorInvocation ::= Name '.' OnlyTypeArguments 'super' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocationWithTypeArguments(2, SUPER_CALL); $break ./

--1.1 feature
ExplicitConstructorInvocation ::= Primary '.' 'this' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocation(1, THIS_CALL); $break ./

ExplicitConstructorInvocation ::= Primary '.' OnlyTypeArguments 'this' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocationWithTypeArguments(1, THIS_CALL); $break ./

--1.1 feature
ExplicitConstructorInvocation ::= Name '.' 'this' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocation(2, THIS_CALL); $break ./

ExplicitConstructorInvocation ::= Name '.' OnlyTypeArguments 'this' '(' ArgumentListopt ')' ';'
/.$putCase consumeExplicitConstructorInvocationWithTypeArguments(2, THIS_CALL); $break ./
/:$readableName ExplicitConstructorInvocation:/

-- {ObjectTeams
BaseConstructorExpression ::= 'base' '(' ArgumentListopt ')' 
/.$putCase consumeExplicitConstructorInvocationBase(0); $break ./
/:$readableName BaseConstructorInvocation:/

BaseConstructorInvocation ::= 'base' '(' ArgumentListopt ')' 
/.$putCase consumeExplicitConstructorInvocationBase(1); $break ./
/:$readableName BaseConstructorInvocation:/

BaseConstructorInvocation ::= Primary . 'base' '(' ArgumentListopt ')' 
/.$putCase consumeExplicitConstructorInvocationBase(2); $break ./
/:$readableName QualifiedBaseConstructorInvocation:/

BaseConstructorInvocation ::= Name . 'base' '(' ArgumentListopt ')' 
/.$putCase consumeExplicitConstructorInvocationBase(3); $break ./
/:$readableName QualifiedBaseConstructorInvocation:/
-- Markus Witte}

--18.9 Productions from 9: Interface Declarations

--18.9.1 Productions from 9.1: Interface Declarations
--InterfaceModifier ::=
--      'public'
--    | 'abstract'
--
InterfaceDeclaration ::= InterfaceHeader InterfaceBody
/.$putCase consumeInterfaceDeclaration(); $break ./
/:$readableName InterfaceDeclaration:/

-- {ObjectTeams
-- orig: InterfaceHeader ::= InterfaceHeaderName InterfaceHeaderExtendsopt 
InterfaceHeader ::= InterfaceHeaderName InterfaceHeaderExtendsopt ClassHeaderPlayedByopt
-- SH}
/.$putCase consumeInterfaceHeader(); $break ./
/:$readableName InterfaceHeader:/

-----------------------------------------------
-- 1.5 features : generics
-----------------------------------------------
InterfaceHeaderName ::= InterfaceHeaderName1 TypeParameters
/.$putCase consumeTypeHeaderNameWithTypeParameters(); $break ./

InterfaceHeaderName -> InterfaceHeaderName1
/:$readableName InterfaceHeaderName:/

InterfaceHeaderName1 ::= Modifiersopt interface Identifier
/.$putCase consumeInterfaceHeaderName1(); $break ./
/:$readableName InterfaceHeaderName:/

InterfaceHeaderExtends ::= 'extends' InterfaceTypeList
/.$putCase consumeInterfaceHeaderExtends(); $break ./
/:$readableName InterfaceHeaderExtends:/

InterfaceBody ::= '{' InterfaceMemberDeclarationsopt '}' 
/:$readableName InterfaceBody:/

InterfaceMemberDeclarations -> InterfaceMemberDeclaration
InterfaceMemberDeclarations ::= InterfaceMemberDeclarations InterfaceMemberDeclaration
/.$putCase consumeInterfaceMemberDeclarations(); $break ./
/:$readableName InterfaceMemberDeclarations:/

--same as for class members
InterfaceMemberDeclaration ::= ';'
/.$putCase consumeEmptyTypeDeclaration(); $break ./
/:$readableName InterfaceMemberDeclaration:/


InterfaceMemberDeclaration -> ConstantDeclaration
InterfaceMemberDeclaration ::= DefaultMethodHeader MethodBody
/:$compliance 1.8:/
/.$putCase consumeInterfaceMethodDeclaration(false); $break ./
InterfaceMemberDeclaration ::= MethodHeader MethodBody
/.$putCase consumeInterfaceMethodDeclaration(false); $break ./
/:$readableName InterfaceMemberDeclaration:/
-- the next rule is illegal but allows to give a more canonical error message from inside consumeInterfaceMethodDeclaration():
InterfaceMemberDeclaration ::= DefaultMethodHeader ';'
/:$compliance 1.8:/
/.$putCase consumeInterfaceMethodDeclaration(true); $break ./

-- These rules are added to be able to parse constructors inside interface and then report a relevent error message
InvalidConstructorDeclaration ::= ConstructorHeader MethodBody
/.$putCase consumeInvalidConstructorDeclaration(true); $break ./
InvalidConstructorDeclaration ::= ConstructorHeader ';'
/.$putCase consumeInvalidConstructorDeclaration(false); $break ./
/:$readableName InvalidConstructorDeclaration:/

-- These rules are added to be able to parse initializers inside an interface and then report a relevent error message (bug 212713)
InvalidInitializer -> StaticInitializer
InvalidInitializer -> Initializer
/:$readableName InvalidInitializer:/


InterfaceMemberDeclaration -> AbstractMethodDeclaration
InterfaceMemberDeclaration -> InvalidConstructorDeclaration
InterfaceMemberDeclaration -> InvalidInitializer
--1.1 feature
InterfaceMemberDeclaration -> ClassDeclaration
--1.1 feature
InterfaceMemberDeclaration -> InterfaceDeclaration
InterfaceMemberDeclaration -> EnumDeclaration
InterfaceMemberDeclaration -> AnnotationTypeDeclaration
--{ObjectTeams: bindings in role interfaces:
InterfaceMemberDeclaration -> BindingDeclaration
-- SH}
/:$readableName InterfaceMemberDeclaration:/

ConstantDeclaration -> FieldDeclaration
/:$readableName ConstantDeclaration:/

PushLeftBrace ::= $empty
/.$putCase consumePushLeftBrace(); $break ./
/:$readableName PushLeftBrace:/

ArrayInitializer ::= '{' PushLeftBrace ,opt '}'
/.$putCase consumeEmptyArrayInitializer(); $break ./
ArrayInitializer ::= '{' PushLeftBrace VariableInitializers '}'
/.$putCase consumeArrayInitializer(); $break ./
ArrayInitializer ::= '{' PushLeftBrace VariableInitializers , '}'
/.$putCase consumeArrayInitializer(); $break ./
/:$readableName ArrayInitializer:/
/:$recovery_template Identifier:/

VariableInitializers ::= VariableInitializer
VariableInitializers ::= VariableInitializers ',' VariableInitializer
/.$putCase consumeVariableInitializers(); $break ./
/:$readableName VariableInitializers:/

Block ::= OpenBlock '{' BlockStatementsopt '}'
/.$putCase consumeBlock(); $break ./
/:$readableName Block:/

OpenBlock ::= $empty
/.$putCase consumeOpenBlock() ; $break ./
/:$readableName OpenBlock:/

BlockStatements ::= BlockStatement
/.$putCase consumeBlockStatement() ; $break ./
/:$readableName BlockStatements:/
BlockStatements ::= BlockStatements BlockStatement
/.$putCase consumeBlockStatements() ; $break ./
/:$readableName BlockStatements:/

-- Production name hardcoded in parser. Must be ::= and not -> 
BlockStatementopt ::= BlockStatementopt0
/:$readableName BlockStatementopt:/
BlockStatementopt0 -> $empty
BlockStatementopt0 -> BlockStatement
/:$readableName BlockStatementopt0:/

BlockStatement -> LocalVariableDeclarationStatement
BlockStatement -> Statement
--1.1 feature
BlockStatement -> ClassDeclaration
BlockStatement ::= InterfaceDeclaration
/.$putCase consumeInvalidInterfaceDeclaration(); $break ./
/:$readableName BlockStatement:/
BlockStatement ::= AnnotationTypeDeclaration
/.$putCase consumeInvalidAnnotationTypeDeclaration(); $break ./
/:$readableName BlockStatement:/
BlockStatement ::= EnumDeclaration
/.$putCase consumeInvalidEnumDeclaration(); $break ./
/:$readableName BlockStatement:/

LocalVariableDeclarationStatement ::= LocalVariableDeclaration ';'
/.$putCase consumeLocalVariableDeclarationStatement(); $break ./
/:$readableName LocalVariableDeclarationStatement:/

LocalVariableDeclaration ::= Type PushModifiers VariableDeclarators
/.$putCase consumeLocalVariableDeclaration(); $break ./
-- 1.1 feature
-- The modifiers part of this rule makes the grammar more permissive. 
-- The only modifier here is final. We put Modifiers to allow multiple modifiers
-- This will require to check the validity of the modifier
LocalVariableDeclaration ::= Modifiers Type PushRealModifiers VariableDeclarators
/.$putCase consumeLocalVariableDeclaration(); $break ./
/:$readableName LocalVariableDeclaration:/

PushModifiers ::= $empty
/.$putCase consumePushModifiers(); $break ./
/:$readableName PushModifiers:/

PushModifiersForHeader ::= $empty
/.$putCase consumePushModifiersForHeader(); $break ./
/:$readableName PushModifiersForHeader:/

PushRealModifiers ::= $empty
/.$putCase consumePushRealModifiers(); $break ./
/:$readableName PushRealModifiers:/

Statement -> StatementWithoutTrailingSubstatement
Statement -> LabeledStatement
Statement -> IfThenStatement
Statement -> IfThenElseStatement
Statement -> WhileStatement
Statement -> ForStatement
-- {ObjectTeams
Statement -> WithinStatement
-- Markus Witte}
-----------------------------------------------
-- 1.5 feature
-----------------------------------------------
Statement -> EnhancedForStatement
/:$readableName Statement:/
/:$recovery_template ;:/

StatementNoShortIf -> StatementWithoutTrailingSubstatement
StatementNoShortIf -> LabeledStatementNoShortIf
StatementNoShortIf -> IfThenElseStatementNoShortIf
StatementNoShortIf -> WhileStatementNoShortIf
StatementNoShortIf -> ForStatementNoShortIf
-----------------------------------------------
-- 1.5 feature
-----------------------------------------------
StatementNoShortIf -> EnhancedForStatementNoShortIf
/:$readableName Statement:/

StatementWithoutTrailingSubstatement -> AssertStatement
StatementWithoutTrailingSubstatement -> Block
StatementWithoutTrailingSubstatement -> EmptyStatement
StatementWithoutTrailingSubstatement -> ExpressionStatement
StatementWithoutTrailingSubstatement -> SwitchStatement
StatementWithoutTrailingSubstatement -> DoStatement
StatementWithoutTrailingSubstatement -> BreakStatement
StatementWithoutTrailingSubstatement -> ContinueStatement
StatementWithoutTrailingSubstatement -> ReturnStatement
StatementWithoutTrailingSubstatement -> SynchronizedStatement
StatementWithoutTrailingSubstatement -> ThrowStatement
StatementWithoutTrailingSubstatement -> TryStatement
StatementWithoutTrailingSubstatement -> TryStatementWithResources
StatementWithoutTrailingSubstatement -> YieldStatement
/:$readableName Statement:/

EmptyStatement ::= ';'
/.$putCase consumeEmptyStatement(); $break ./
/:$readableName EmptyStatement:/

LabeledStatement ::= Label ':' Statement
/.$putCase consumeStatementLabel() ; $break ./
/:$readableName LabeledStatement:/

LabeledStatementNoShortIf ::= Label ':' StatementNoShortIf
/.$putCase consumeStatementLabel() ; $break ./
/:$readableName LabeledStatement:/

Label ::= 'Identifier'
/.$putCase consumeLabel() ; $break ./
/:$readableName Label:/

ExpressionStatement ::= StatementExpression ';'
/. $putCase consumeExpressionStatement(); $break ./
ExpressionStatement ::= ExplicitConstructorInvocation
/:$readableName Statement:/

StatementExpression ::= Assignment
StatementExpression ::= PreIncrementExpression
StatementExpression ::= PreDecrementExpression
StatementExpression ::= PostIncrementExpression
StatementExpression ::= PostDecrementExpression
StatementExpression ::= MethodInvocation
StatementExpression ::= ClassInstanceCreationExpression
-- {ObjectTeams
StatementExpression ::= BaseConstructorInvocation
-- SH}
/:$readableName Expression:/

IfThenStatement ::= 'if' '(' Expression ')' Statement
/.$putCase consumeStatementIfNoElse(); $break ./
/:$readableName IfStatement:/

IfThenElseStatement ::= 'if' '(' Expression ')' StatementNoShortIf 'else' Statement
/.$putCase consumeStatementIfWithElse(); $break ./
/:$readableName IfStatement:/

IfThenElseStatementNoShortIf ::= 'if' '(' Expression ')' StatementNoShortIf 'else' StatementNoShortIf
/.$putCase consumeStatementIfWithElse(); $break ./
/:$readableName IfStatement:/

SwitchStatement ::= 'switch' '(' Expression ')' OpenBlock SwitchBlock
/.$putCase consumeStatementSwitch() ; $break ./
/:$readableName SwitchStatement:/

SwitchBlock ::= '{' '}'
/.$putCase consumeEmptySwitchBlock() ; $break ./

SwitchBlock ::= '{' SwitchBlockStatements '}'
SwitchBlock ::= '{' SwitchLabels '}'
SwitchBlock ::= '{' SwitchBlockStatements SwitchLabels '}'
/.$putCase consumeSwitchBlock() ; $break ./
/:$readableName SwitchBlock:/

SwitchBlockStatements -> SwitchBlockStatement
SwitchBlockStatements ::= SwitchBlockStatements SwitchBlockStatement
/.$putCase consumeSwitchBlockStatements() ; $break ./
/:$readableName SwitchBlockStatements:/

SwitchBlockStatement -> SwitchLabeledRule
SwitchBlockStatement ::= SwitchLabels BlockStatements
/.$putCase consumeSwitchBlockStatement() ; $break ./
/:$readableName SwitchBlockStatement:/

SwitchLabels -> SwitchLabel
SwitchLabels ::= SwitchLabels SwitchLabel
/.$putCase consumeSwitchLabels() ; $break ./
/:$readableName SwitchLabels:/

SwitchLabel ::= SwitchLabelCaseLhs ':'
/. $putCase consumeCaseLabel(); $break ./

SwitchLabel ::= 'default' ':'
/. $putCase consumeDefaultLabel(); $break ./
/:$readableName SwitchLabel:/

-- BEGIN SwitchExpression (JEP 325) --

UnaryExpressionNotPlusMinus -> SwitchExpression
UnaryExpressionNotPlusMinus_NotName -> SwitchExpression

SwitchExpression ::= 'switch' '(' Expression ')' OpenBlock SwitchBlock
/.$putCase consumeSwitchExpression() ; $break ./
/:$readableName SwitchExpression:/

SwitchLabeledRule ::= SwitchLabeledExpression
SwitchLabeledRule ::= SwitchLabeledBlock
SwitchLabeledRule ::= SwitchLabeledThrowStatement
/. $putCase consumeSwitchLabeledRule(); $break ./
/:$readableName SwitchLabeledRule:/

SwitchLabeledExpression ::= SwitchLabelExpr Expression ';'
/. $putCase consumeSwitchLabeledExpression(); $break ./
/:$readableName SwitchLabeledExpression:/

SwitchLabeledBlock ::= SwitchLabelExpr Block
/. $putCase consumeSwitchLabeledBlock(); $break ./
/:$readableName SwitchLabeledBlock:/

SwitchLabeledThrowStatement ::= SwitchLabelExpr ThrowExpression ';'
/. $putCase consumeSwitchLabeledThrowStatement(); $break ./
/:$readableName SwitchLabeledThrowStatement:/

SwitchLabelExpr ::= 'default'  '->'
/. $putCase consumeDefaultLabelExpr(); $break ./
/:$readableName SwitchLabelDefaultExpr:/

SwitchLabelExpr ::= SwitchLabelCaseLhs BeginCaseExpr '->'
/. $putCase consumeCaseLabelExpr(); $break ./
/:$readableName SwitchLabelExpr:/

SwitchLabelCaseLhs ::= 'case' ConstantExpressions
/. $putCase consumeSwitchLabelCaseLhs(); $break ./
/:$readableName SwitchLabelCaseLhs:/

-- END SwitchExpression (JEP 325) --

YieldStatement ::= RestrictedIdentifierYield Expression ;
/.$putCase consumeStatementYield() ; $break ./
/:$readableName YieldStatement:/

WhileStatement ::= 'while' '(' Expression ')' Statement
/.$putCase consumeStatementWhile() ; $break ./
/:$readableName WhileStatement:/

WhileStatementNoShortIf ::= 'while' '(' Expression ')' StatementNoShortIf
/.$putCase consumeStatementWhile() ; $break ./
/:$readableName WhileStatement:/

DoStatement ::= 'do' Statement 'while' '(' Expression ')' ';'
/.$putCase consumeStatementDo() ; $break ./
/:$readableName DoStatement:/

ForStatement ::= 'for' '(' ForInitopt ';' Expressionopt ';' ForUpdateopt ')' Statement
/.$putCase consumeStatementFor() ; $break ./
/:$readableName ForStatement:/

ForStatementNoShortIf ::= 'for' '(' ForInitopt ';' Expressionopt ';' ForUpdateopt ')' StatementNoShortIf
/.$putCase consumeStatementFor() ; $break ./
/:$readableName ForStatement:/

--the minus one allows to avoid a stack-to-stack transfer
ForInit ::= StatementExpressionList
/.$putCase consumeForInit() ; $break ./
ForInit -> LocalVariableDeclaration
/:$readableName ForInit:/

ForUpdate -> StatementExpressionList
/:$readableName ForUpdate:/

StatementExpressionList -> StatementExpression
StatementExpressionList ::= StatementExpressionList ',' StatementExpression
/.$putCase consumeStatementExpressionList() ; $break ./
/:$readableName StatementExpressionList:/

-- {ObjectTeams
WithinStatement ::= 'within' '(' Expression ')' Statement
/.$putCase consumeWithinStatement(); $break ./
/:$readableName WithinStatement:/
-- Markus Witte}

-- 1.4 feature
AssertStatement ::= 'assert' Expression ';'
/.$putCase consumeSimpleAssertStatement() ; $break ./
/:$compliance 1.4:/

AssertStatement ::= 'assert' Expression ':' Expression ';'
/.$putCase consumeAssertStatement() ; $break ./
/:$readableName AssertStatement:/
/:$compliance 1.4:/

BreakStatement ::= 'break' ';'
/.$putCase consumeStatementBreak() ; $break ./

BreakStatement ::= 'break' Identifier ';'
/.$putCase consumeStatementBreakWithLabel() ; $break ./
/:$readableName BreakStatement:/

ContinueStatement ::= 'continue' ';'
/.$putCase consumeStatementContinue() ; $break ./

ContinueStatement ::= 'continue' Identifier ';'
/.$putCase consumeStatementContinueWithLabel() ; $break ./
/:$readableName ContinueStatement:/

ReturnStatement ::= 'return' Expressionopt ';'
/.$putCase consumeStatementReturn() ; $break ./
/:$readableName ReturnStatement:/

ThrowStatement ::= 'throw' Expression ';'
/.$putCase consumeStatementThrow(); $break ./
/:$readableName ThrowStatement:/

ThrowExpression ::= 'throw' Expression
/.$putCase consumeThrowExpression() ; $break ./
/:$readableName ThrowExpression:/

SynchronizedStatement ::= OnlySynchronized '(' Expression ')' Block
/.$putCase consumeStatementSynchronized(); $break ./
/:$readableName SynchronizedStatement:/

OnlySynchronized ::= 'synchronized'
/.$putCase consumeOnlySynchronized(); $break ./
/:$readableName OnlySynchronized:/

TryStatement ::= 'try' TryBlock Catches
/.$putCase consumeStatementTry(false, false); $break ./
TryStatement ::= 'try' TryBlock Catchesopt Finally
/.$putCase consumeStatementTry(true, false); $break ./
/:$readableName TryStatement:/

TryStatementWithResources ::= 'try' ResourceSpecification TryBlock Catchesopt
/.$putCase consumeStatementTry(false, true); $break ./
TryStatementWithResources ::= 'try' ResourceSpecification TryBlock Catchesopt Finally
/.$putCase consumeStatementTry(true, true); $break ./
/:$readableName TryStatementWithResources:/
/:$compliance 1.7:/

ResourceSpecification ::= '(' Resources ;opt ')'
/.$putCase consumeResourceSpecification(); $break ./
/:$readableName ResourceSpecification:/
/:$compliance 1.7:/

;opt ::= $empty
/.$putCase consumeResourceOptionalTrailingSemiColon(false); $break ./
;opt ::= ';'
/.$putCase consumeResourceOptionalTrailingSemiColon(true); $break ./
/:$readableName ;:/
/:$compliance 1.7:/

Resources ::= Resource
/.$putCase consumeSingleResource(); $break ./
Resources ::= Resources TrailingSemiColon Resource
/.$putCase consumeMultipleResources(); $break ./
/:$readableName Resources:/
/:$compliance 1.7:/

TrailingSemiColon ::= ';'
/.$putCase consumeResourceOptionalTrailingSemiColon(true); $break ./
/:$readableName ;:/
/:$compliance 1.7:/

Resource ::= Type PushModifiers VariableDeclaratorId EnterVariable '=' ForceNoDiet VariableInitializer RestoreDiet ExitVariableWithInitialization
/.$putCase consumeResourceAsLocalVariableDeclaration(); $break ./
/:$readableName Resource:/
/:$compliance 1.7:/

Resource ::= Modifiers Type PushRealModifiers VariableDeclaratorId EnterVariable '=' ForceNoDiet VariableInitializer RestoreDiet ExitVariableWithInitialization
/.$putCase consumeResourceAsLocalVariableDeclaration(); $break ./
/:$readableName Resource:/
/:$compliance 1.7:/

Resource ::= Name
/.$putCase consumeResourceAsLocalVariable(); $break ./
/:$readableName Resource:/
/:$compliance 1.9:/

Resource ::= FieldAccess
/.$putCase consumeResourceAsFieldAccess(); $break ./
/:$readableName Resource:/
/:$compliance 1.9:/

TryBlock ::= Block ExitTryBlock
/:$readableName Block:/

ExitTryBlock ::= $empty
/.$putCase consumeExitTryBlock(); $break ./
/:$readableName ExitTryBlock:/

Catches -> CatchClause
Catches ::= Catches CatchClause
/.$putCase consumeCatches(); $break ./
/:$readableName Catches:/

CatchClause ::= 'catch' '(' CatchFormalParameter ')' Block
/.$putCase consumeStatementCatch() ; $break ./
/:$readableName CatchClause:/

Finally ::= 'finally' Block
/:$readableName Finally:/
/:$recovery_template finally { }:/

--18.12 Productions from 14: Expressions

--for source positioning purpose
PushLPAREN ::= '('
/.$putCase consumeLeftParen(); $break ./
/:$readableName (:/
/:$recovery_template (:/
PushRPAREN ::= ')'
/.$putCase consumeRightParen(); $break ./
/:$readableName ):/
/:$recovery_template ):/

Primary -> PrimaryNoNewArray
Primary -> ArrayCreationWithArrayInitializer
Primary -> ArrayCreationWithoutArrayInitializer
/:$readableName Expression:/

PrimaryNoNewArray -> Literal
PrimaryNoNewArray ::= 'this'
/.$putCase consumePrimaryNoNewArrayThis(); $break ./

PrimaryNoNewArray ::= PushLPAREN Expression_NotName PushRPAREN 
/.$putCase consumePrimaryNoNewArray(); $break ./

PrimaryNoNewArray ::= PushLPAREN Name PushRPAREN 
/.$putCase consumePrimaryNoNewArrayWithName(); $break ./

PrimaryNoNewArray -> ClassInstanceCreationExpression
--{ObjectTeams:
PrimaryNoNewArray -> BaseConstructorExpression
-- SH}
PrimaryNoNewArray -> FieldAccess
--1.1 feature
PrimaryNoNewArray ::= Name '.' 'this'
/.$putCase consumePrimaryNoNewArrayNameThis(); $break ./

QualifiedSuperReceiver ::= Name '.' 'super'
/.$putCase consumeQualifiedSuperReceiver(); $break ./

--1.1 feature
--PrimaryNoNewArray ::= Type '.' 'class'
--inline Type in the previous rule in order to make the grammar LL1 instead 
-- of LL2. The result is the 3 next rules.

PrimaryNoNewArray ::= Name '.' 'class'
/.$putCase consumePrimaryNoNewArrayName(); $break ./

PrimaryNoNewArray ::= Name Dims '.' 'class'
/.$putCase consumePrimaryNoNewArrayArrayType(); $break ./

PrimaryNoNewArray ::= PrimitiveType Dims '.' 'class'
/.$putCase consumePrimaryNoNewArrayPrimitiveArrayType(); $break ./

PrimaryNoNewArray ::= PrimitiveType '.' 'class'
/.$putCase consumePrimaryNoNewArrayPrimitiveType(); $break ./

--{ObjectTeams: R<@t>.class 
-- (start with RelationalExpression to make the grammer LL1, further syntax checking in Parser)
RelationalExpression ::= RelationalExpression '<' AnyTypeAnchor '>' '.' 'class'
/.$putCase consumeRoleClassLiteral(); $break ./
-- SH}

PrimaryNoNewArray -> MethodInvocation
PrimaryNoNewArray -> ArrayAccess

-----------------------------------------------------------------------
--                   Start of rules for JSR 335
-----------------------------------------------------------------------

PrimaryNoNewArray -> LambdaExpression
PrimaryNoNewArray -> ReferenceExpression
/:$readableName Expression:/

-- Production name hardcoded in parser. Must be ::= and not -> 
ReferenceExpressionTypeArgumentsAndTrunk ::= ReferenceExpressionTypeArgumentsAndTrunk0
/:$readableName ReferenceExpressionTypeArgumentsAndTrunk:/

ReferenceExpressionTypeArgumentsAndTrunk0 ::= OnlyTypeArguments Dimsopt 
/.$putCase consumeReferenceExpressionTypeArgumentsAndTrunk(false); $break ./
/:$compliance 1.8:/
ReferenceExpressionTypeArgumentsAndTrunk0 ::= OnlyTypeArguments '.' ClassOrInterfaceType Dimsopt 
/.$putCase consumeReferenceExpressionTypeArgumentsAndTrunk(true); $break ./
/:$readableName ReferenceExpressionTypeArgumentsAndTrunk:/
/:$compliance 1.8:/

ReferenceExpression ::= PrimitiveType Dims '::' NonWildTypeArgumentsopt IdentifierOrNew
/.$putCase consumeReferenceExpressionTypeForm(true); $break ./
/:$compliance 1.8:/

ReferenceExpression ::= Name Dimsopt '::' NonWildTypeArgumentsopt IdentifierOrNew
/.$putCase consumeReferenceExpressionTypeForm(false); $break ./
/:$compliance 1.8:/

-- BeginTypeArguments is a synthetic token the scanner concocts to help disambiguate
-- between '<' as an operator and '<' in '<' TypeArguments '>'
ReferenceExpression ::= Name BeginTypeArguments ReferenceExpressionTypeArgumentsAndTrunk '::' NonWildTypeArgumentsopt IdentifierOrNew
/.$putCase consumeReferenceExpressionGenericTypeForm(); $break ./
/:$compliance 1.8:/

ReferenceExpression ::= Primary '::' NonWildTypeArgumentsopt Identifier
/.$putCase consumeReferenceExpressionPrimaryForm(); $break ./
/:$compliance 1.8:/
ReferenceExpression ::= QualifiedSuperReceiver '::' NonWildTypeArgumentsopt Identifier
/.$putCase consumeReferenceExpressionPrimaryForm(); $break ./
/:$compliance 1.8:/
ReferenceExpression ::= 'super' '::' NonWildTypeArgumentsopt Identifier
/.$putCase consumeReferenceExpressionSuperForm(); $break ./
/:$readableName ReferenceExpression:/
/:$compliance 1.8:/

NonWildTypeArgumentsopt ::= $empty
/.$putCase consumeEmptyTypeArguments(); $break ./
NonWildTypeArgumentsopt -> OnlyTypeArguments
/:$readableName NonWildTypeArgumentsopt:/
/:$compliance 1.8:/

IdentifierOrNew ::= 'Identifier'
/.$putCase consumeIdentifierOrNew(false); $break ./
IdentifierOrNew ::= 'new'
/.$putCase consumeIdentifierOrNew(true); $break ./
/:$readableName IdentifierOrNew:/
/:$compliance 1.8:/

--{ObjectTeams: replace invocation from consumeToken()
-- orig: LambdaExpression ::= LambdaParameters '->' LambdaBody
LambdaExpression ::= LambdaParameters '->' EnterLambda LambdaBody
-- orig:
/.$putCase consumeLambdaExpression(); $break ./
/:$readableName LambdaExpression:/
/:$compliance 1.8:/
-- OT:
EnterLambda ::= $empty
/.$putCase consumeLambdaHeader(); $break ./
-- SH}

NestedLambda ::= $empty
/.$putCase consumeNestedLambda(); $break ./
/:$readableName NestedLambda:/

LambdaParameters ::= Identifier NestedLambda
/.$putCase consumeTypeElidedLambdaParameter(false); $break ./
/:$readableName TypeElidedFormalParameter:/
/:$compliance 1.8:/

-- to make the grammar LALR(1), the scanner transforms the input string to
-- contain synthetic tokens to signal start of lambda parameter list.
LambdaParameters -> BeginLambda NestedLambda LambdaParameterList
/:$readableName LambdaParameters:/
/:$compliance 1.8:/

-- Production name hardcoded in parser. Must be ::= and not -> 
ParenthesizedLambdaParameterList ::= LambdaParameterList
/:$readableName ParenthesizedLambdaParameterList:/

LambdaParameterList -> PushLPAREN FormalParameterListopt PushRPAREN
LambdaParameterList -> PushLPAREN TypeElidedFormalParameterList PushRPAREN
/:$readableName LambdaParameterList:/
/:$compliance 1.8:/

TypeElidedFormalParameterList -> TypeElidedFormalParameter
TypeElidedFormalParameterList ::= TypeElidedFormalParameterList ',' TypeElidedFormalParameter
/.$putCase consumeFormalParameterList(); $break ./
/:$readableName TypeElidedFormalParameterList:/
/:$compliance 1.8:/

-- to work around a shift reduce conflict, we accept Modifiersopt prefixed
-- identifier - downstream phases should reject input strings with modifiers.
TypeElidedFormalParameter ::= Modifiersopt Identifier
/.$putCase consumeTypeElidedLambdaParameter(true); $break ./
/:$readableName TypeElidedFormalParameter:/
/:$compliance 1.8:/

-- A lambda body of the form x is really '{' return x; '}'
LambdaBody -> ElidedLeftBraceAndReturn Expression ElidedSemicolonAndRightBrace
LambdaBody -> Block
/:$readableName LambdaBody:/
/:$compliance 1.8:/

ElidedLeftBraceAndReturn ::= $empty
/.$putCase consumeElidedLeftBraceAndReturn(); $break ./
/:$readableName ElidedLeftBraceAndReturn:/
/:$compliance 1.8:/

-----------------------------------------------------------------------
--                   End of rules for JSR 335
-----------------------------------------------------------------------

--1.1 feature
--
-- In Java 1.0 a ClassBody could not appear at all in a
-- ClassInstanceCreationExpression.
--

AllocationHeader ::= 'new' ClassType '(' ArgumentListopt ')'
/.$putCase consumeAllocationHeader(); $break ./
/:$readableName AllocationHeader:/

ClassInstanceCreationExpression ::= 'new' OnlyTypeArguments ClassType EnterInstanceCreationArgumentList '(' ArgumentListopt ')' UnqualifiedClassBodyopt
/.$putCase consumeClassInstanceCreationExpressionWithTypeArguments(); $break ./

ClassInstanceCreationExpression ::= 'new' ClassType EnterInstanceCreationArgumentList '(' ArgumentListopt ')' UnqualifiedClassBodyopt
/.$putCase consumeClassInstanceCreationExpression(); $break ./
--1.1 feature

ClassInstanceCreationExpression ::= Primary '.' 'new' OnlyTypeArguments ClassType EnterInstanceCreationArgumentList '(' ArgumentListopt ')' QualifiedClassBodyopt
/.$putCase consumeClassInstanceCreationExpressionQualifiedWithTypeArguments() ; $break ./

ClassInstanceCreationExpression ::= Primary '.' 'new' ClassType EnterInstanceCreationArgumentList '(' ArgumentListopt ')' QualifiedClassBodyopt
/.$putCase consumeClassInstanceCreationExpressionQualified() ; $break ./

--1.1 feature
ClassInstanceCreationExpression ::= ClassInstanceCreationExpressionName ClassType EnterInstanceCreationArgumentList '(' ArgumentListopt ')' QualifiedClassBodyopt
/.$putCase consumeClassInstanceCreationExpressionQualified() ; $break ./
/:$readableName ClassInstanceCreationExpression:/

ClassInstanceCreationExpression ::= ClassInstanceCreationExpressionName OnlyTypeArguments ClassType EnterInstanceCreationArgumentList '(' ArgumentListopt ')' QualifiedClassBodyopt
/.$putCase consumeClassInstanceCreationExpressionQualifiedWithTypeArguments() ; $break ./
/:$readableName ClassInstanceCreationExpression:/

EnterInstanceCreationArgumentList ::= $empty
/.$putCase consumeEnterInstanceCreationArgumentList(); $break ./
/:$readableName EnterInstanceCreationArgumentList:/

ClassInstanceCreationExpressionName ::= Name '.' 'new'
/.$putCase consumeClassInstanceCreationExpressionName() ; $break ./
/:$readableName ClassInstanceCreationExpressionName:/

UnqualifiedClassBodyopt ::= $empty --test made using null as contents
/.$putCase consumeClassBodyopt(); $break ./
UnqualifiedClassBodyopt ::= UnqualifiedEnterAnonymousClassBody ClassBody
/:$readableName ClassBody:/
/:$no_statements_recovery:/

UnqualifiedEnterAnonymousClassBody ::= $empty
/.$putCase consumeEnterAnonymousClassBody(false); $break ./
/:$readableName EnterAnonymousClassBody:/

QualifiedClassBodyopt ::= $empty --test made using null as contents
/.$putCase consumeClassBodyopt(); $break ./
QualifiedClassBodyopt ::= QualifiedEnterAnonymousClassBody ClassBody
/:$readableName ClassBody:/
/:$no_statements_recovery:/

QualifiedEnterAnonymousClassBody ::= $empty
/.$putCase consumeEnterAnonymousClassBody(true); $break ./
/:$readableName EnterAnonymousClassBody:/

ArgumentList ::= Expression
ArgumentList ::= ArgumentList ',' Expression
/.$putCase consumeArgumentList(); $break ./
/:$readableName ArgumentList:/

ArrayCreationHeader ::= 'new' PrimitiveType DimWithOrWithOutExprs
/.$putCase consumeArrayCreationHeader(); $break ./

ArrayCreationHeader ::= 'new' ClassOrInterfaceType DimWithOrWithOutExprs
/.$putCase consumeArrayCreationHeader(); $break ./
/:$readableName ArrayCreationHeader:/

ArrayCreationWithoutArrayInitializer ::= 'new' PrimitiveType DimWithOrWithOutExprs
/.$putCase consumeArrayCreationExpressionWithoutInitializer(); $break ./
/:$readableName ArrayCreationWithoutArrayInitializer:/

ArrayCreationWithArrayInitializer ::= 'new' PrimitiveType DimWithOrWithOutExprs ArrayInitializer
/.$putCase consumeArrayCreationExpressionWithInitializer(); $break ./
/:$readableName ArrayCreationWithArrayInitializer:/

ArrayCreationWithoutArrayInitializer ::= 'new' ClassOrInterfaceType DimWithOrWithOutExprs
/.$putCase consumeArrayCreationExpressionWithoutInitializer(); $break ./

ArrayCreationWithArrayInitializer ::= 'new' ClassOrInterfaceType DimWithOrWithOutExprs ArrayInitializer
/.$putCase consumeArrayCreationExpressionWithInitializer(); $break ./

DimWithOrWithOutExprs ::= DimWithOrWithOutExpr
DimWithOrWithOutExprs ::= DimWithOrWithOutExprs DimWithOrWithOutExpr
/.$putCase consumeDimWithOrWithOutExprs(); $break ./
/:$readableName Dimensions:/

DimWithOrWithOutExpr ::= TypeAnnotationsopt '[' Expression ']'
DimWithOrWithOutExpr ::= TypeAnnotationsopt '[' ']'
/. $putCase consumeDimWithOrWithOutExpr(); $break ./
/:$readableName Dimension:/
-- -----------------------------------------------

Dims ::= DimsLoop
/. $putCase consumeDims(); $break ./
/:$readableName Dimensions:/
DimsLoop -> OneDimLoop
DimsLoop ::= DimsLoop OneDimLoop
/:$readableName Dimensions:/
OneDimLoop ::= '[' ']'
/. $putCase consumeOneDimLoop(false); $break ./
OneDimLoop ::= TypeAnnotations '[' ']'
/:$compliance 1.8:/
/. $putCase consumeOneDimLoop(true); $break ./
/:$readableName Dimension:/

FieldAccess ::= Primary '.' 'Identifier'
/.$putCase consumeFieldAccess(false); $break ./

FieldAccess ::= 'super' '.' 'Identifier'
/.$putCase consumeFieldAccess(true); $break ./
/:$readableName FieldAccess:/

FieldAccess ::= QualifiedSuperReceiver '.' 'Identifier'
/.$putCase consumeFieldAccess(false); $break ./
/:$readableName FieldAccess:/

MethodInvocation ::= Name '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationName(); $break ./

MethodInvocation ::= Name '.' OnlyTypeArguments 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationNameWithTypeArguments(); $break ./

MethodInvocation ::= Primary '.' OnlyTypeArguments 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationPrimaryWithTypeArguments(); $break ./

MethodInvocation ::= Primary '.' 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationPrimary(); $break ./

MethodInvocation ::= QualifiedSuperReceiver '.' 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationPrimary(); $break ./

MethodInvocation ::= QualifiedSuperReceiver '.' OnlyTypeArguments 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationPrimaryWithTypeArguments(); $break ./

MethodInvocation ::= 'super' '.' OnlyTypeArguments 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationSuperWithTypeArguments(); $break ./

MethodInvocation ::= 'super' '.' 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationSuper(); $break ./

-- {ObjectTeams
MethodInvocation ::= 'tsuper' '.' 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationTSuper(UNQUALIFIED); $break ./

MethodInvocation ::= 'tsuper' '.' OnlyTypeArguments 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationTSuperWithTypeArguments(0); $break ./

MethodInvocation ::= Name '.' 'tsuper' '.' 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationTSuper(QUALIFIED); $break ./

MethodInvocation ::= Name '.' 'tsuper' '.' OnlyTypeArguments 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationTSuperWithTypeArguments(2); $break ./

MethodInvocation ::= 'base' '.' 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationBase(false); $break ./

MethodInvocation ::= 'base' '.' OnlyTypeArguments 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationBaseWithTypeArguments(false); $break ./

MethodInvocation ::= 'base' '.' 'super' '.' 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationBase(true); $break ./

MethodInvocation ::= 'base' '.' 'super' '.' OnlyTypeArguments 'Identifier' '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationBaseWithTypeArguments(true); $break ./
-- Markus Witte}

/:$readableName MethodInvocation:/

ArrayAccess ::= Name '[' Expression ']'
/.$putCase consumeArrayAccess(true); $break ./
ArrayAccess ::= PrimaryNoNewArray '[' Expression ']'
/.$putCase consumeArrayAccess(false); $break ./
ArrayAccess ::= ArrayCreationWithArrayInitializer '[' Expression ']'
/.$putCase consumeArrayAccess(false); $break ./
/:$readableName ArrayAccess:/

PostfixExpression -> Primary
PostfixExpression ::= Name
/.$putCase consumePostfixExpression(); $break ./
PostfixExpression -> PostIncrementExpression
PostfixExpression -> PostDecrementExpression
/:$readableName Expression:/

PostIncrementExpression ::= PostfixExpression '++'
/.$putCase consumeUnaryExpression(OperatorIds.PLUS,true); $break ./
/:$readableName PostIncrementExpression:/

PostDecrementExpression ::= PostfixExpression '--'
/.$putCase consumeUnaryExpression(OperatorIds.MINUS,true); $break ./
/:$readableName PostDecrementExpression:/

--for source managment purpose
PushPosition ::= $empty
 /.$putCase consumePushPosition(); $break ./
/:$readableName PushPosition:/

UnaryExpression -> PreIncrementExpression
UnaryExpression -> PreDecrementExpression
UnaryExpression ::= '+' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.PLUS); $break ./
UnaryExpression ::= '-' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.MINUS); $break ./
UnaryExpression -> UnaryExpressionNotPlusMinus
/:$readableName Expression:/

PreIncrementExpression ::= '++' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.PLUS,false); $break ./
/:$readableName PreIncrementExpression:/

PreDecrementExpression ::= '--' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.MINUS,false); $break ./
/:$readableName PreDecrementExpression:/

UnaryExpressionNotPlusMinus -> PostfixExpression
UnaryExpressionNotPlusMinus ::= '~' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.TWIDDLE); $break ./
UnaryExpressionNotPlusMinus ::= '!' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.NOT); $break ./
UnaryExpressionNotPlusMinus -> CastExpression
/:$readableName Expression:/

CastExpression ::= PushLPAREN PrimitiveType Dimsopt AdditionalBoundsListOpt PushRPAREN InsideCastExpression UnaryExpression
/.$putCase consumeCastExpressionWithPrimitiveType(); $break ./
CastExpression ::= PushLPAREN Name OnlyTypeArgumentsForCastExpression Dimsopt AdditionalBoundsListOpt PushRPAREN InsideCastExpression UnaryExpressionNotPlusMinus
/.$putCase consumeCastExpressionWithGenericsArray(); $break ./
CastExpression ::= PushLPAREN Name OnlyTypeArgumentsForCastExpression '.' ClassOrInterfaceType Dimsopt AdditionalBoundsListOpt PushRPAREN InsideCastExpressionWithQualifiedGenerics UnaryExpressionNotPlusMinus
/.$putCase consumeCastExpressionWithQualifiedGenericsArray(); $break ./
CastExpression ::= PushLPAREN Name PushRPAREN InsideCastExpressionLL1 UnaryExpressionNotPlusMinus
/.$putCase consumeCastExpressionLL1(); $break ./
CastExpression ::=  BeginIntersectionCast PushLPAREN CastNameAndBounds PushRPAREN InsideCastExpressionLL1WithBounds UnaryExpressionNotPlusMinus
/.$putCase consumeCastExpressionLL1WithBounds(); $break ./
CastExpression ::= PushLPAREN Name Dims AdditionalBoundsListOpt PushRPAREN InsideCastExpression UnaryExpressionNotPlusMinus
/.$putCase consumeCastExpressionWithNameArray(); $break ./
/:$readableName CastExpression:/

AdditionalBoundsListOpt ::= $empty
/.$putCase consumeZeroAdditionalBounds(); $break ./
/:$readableName AdditionalBoundsListOpt:/
AdditionalBoundsListOpt -> AdditionalBoundList
/:$compliance 1.8:/
/:$readableName AdditionalBoundsListOpt:/

-- Production name hardcoded in parser. Must be ::= and not -> 
ParenthesizedCastNameAndBounds ::= '(' CastNameAndBounds ')'
/:$readableName ParenthesizedCastNameAndBounds:/

CastNameAndBounds -> Name AdditionalBoundList
/:$compliance 1.8:/
/:$readableName CastNameAndBounds:/

OnlyTypeArgumentsForCastExpression ::= OnlyTypeArguments
/.$putCase consumeOnlyTypeArgumentsForCastExpression(); $break ./
/:$readableName TypeArguments:/

InsideCastExpression ::= $empty
/.$putCase consumeInsideCastExpression(); $break ./
/:$readableName InsideCastExpression:/
InsideCastExpressionLL1 ::= $empty
/.$putCase consumeInsideCastExpressionLL1(); $break ./
/:$readableName InsideCastExpression:/
InsideCastExpressionLL1WithBounds ::= $empty
/.$putCase consumeInsideCastExpressionLL1WithBounds (); $break ./
/:$readableName InsideCastExpression:/
InsideCastExpressionWithQualifiedGenerics ::= $empty
/.$putCase consumeInsideCastExpressionWithQualifiedGenerics(); $break ./
/:$readableName InsideCastExpression:/

MultiplicativeExpression -> UnaryExpression
MultiplicativeExpression ::= MultiplicativeExpression '*' UnaryExpression
/.$putCase consumeBinaryExpression(OperatorIds.MULTIPLY); $break ./
MultiplicativeExpression ::= MultiplicativeExpression '/' UnaryExpression
/.$putCase consumeBinaryExpression(OperatorIds.DIVIDE); $break ./
MultiplicativeExpression ::= MultiplicativeExpression '%' UnaryExpression
/.$putCase consumeBinaryExpression(OperatorIds.REMAINDER); $break ./
/:$readableName Expression:/

AdditiveExpression -> MultiplicativeExpression
AdditiveExpression ::= AdditiveExpression '+' MultiplicativeExpression
/.$putCase consumeBinaryExpression(OperatorIds.PLUS); $break ./
AdditiveExpression ::= AdditiveExpression '-' MultiplicativeExpression
/.$putCase consumeBinaryExpression(OperatorIds.MINUS); $break ./
/:$readableName Expression:/

ShiftExpression -> AdditiveExpression
ShiftExpression ::= ShiftExpression '<<' AdditiveExpression
/.$putCase consumeBinaryExpression(OperatorIds.LEFT_SHIFT); $break ./
ShiftExpression ::= ShiftExpression '>>' AdditiveExpression
/.$putCase consumeBinaryExpression(OperatorIds.RIGHT_SHIFT); $break ./
ShiftExpression ::= ShiftExpression '>>>' AdditiveExpression
/.$putCase consumeBinaryExpression(OperatorIds.UNSIGNED_RIGHT_SHIFT); $break ./
/:$readableName Expression:/

RelationalExpression -> ShiftExpression
RelationalExpression ::= RelationalExpression '<' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.LESS); $break ./
RelationalExpression ::= RelationalExpression '>' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.GREATER); $break ./
RelationalExpression ::= RelationalExpression '<=' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.LESS_EQUAL); $break ./
RelationalExpression ::= RelationalExpression '>=' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.GREATER_EQUAL); $break ./
/:$readableName Expression:/

InstanceofExpression -> RelationalExpression
InstanceofExpression ::= InstanceofExpression 'instanceof' ReferenceType
/.$putCase consumeInstanceOfExpression(); $break ./
/:$readableName Expression:/

EqualityExpression -> InstanceofExpression
EqualityExpression ::= EqualityExpression '==' InstanceofExpression
/.$putCase consumeEqualityExpression(OperatorIds.EQUAL_EQUAL); $break ./
EqualityExpression ::= EqualityExpression '!=' InstanceofExpression
/.$putCase consumeEqualityExpression(OperatorIds.NOT_EQUAL); $break ./
/:$readableName Expression:/

AndExpression -> EqualityExpression
AndExpression ::= AndExpression '&' EqualityExpression
/.$putCase consumeBinaryExpression(OperatorIds.AND); $break ./
/:$readableName Expression:/

ExclusiveOrExpression -> AndExpression
ExclusiveOrExpression ::= ExclusiveOrExpression '^' AndExpression
/.$putCase consumeBinaryExpression(OperatorIds.XOR); $break ./
/:$readableName Expression:/

InclusiveOrExpression -> ExclusiveOrExpression
InclusiveOrExpression ::= InclusiveOrExpression '|' ExclusiveOrExpression
/.$putCase consumeBinaryExpression(OperatorIds.OR); $break ./
/:$readableName Expression:/

ConditionalAndExpression -> InclusiveOrExpression
ConditionalAndExpression ::= ConditionalAndExpression '&&' InclusiveOrExpression
/.$putCase consumeBinaryExpression(OperatorIds.AND_AND); $break ./
/:$readableName Expression:/

ConditionalOrExpression -> ConditionalAndExpression
ConditionalOrExpression ::= ConditionalOrExpression '||' ConditionalAndExpression
/.$putCase consumeBinaryExpression(OperatorIds.OR_OR); $break ./
/:$readableName Expression:/

ConditionalExpression -> ConditionalOrExpression
ConditionalExpression ::= ConditionalOrExpression '?' Expression ':' ConditionalExpression
/.$putCase consumeConditionalExpression(OperatorIds.QUESTIONCOLON) ; $break ./
/:$readableName Expression:/

AssignmentExpression -> ConditionalExpression
AssignmentExpression -> Assignment
/:$readableName Expression:/
/:$recovery_template Identifier:/

Assignment ::= PostfixExpression AssignmentOperator AssignmentExpression
/.$putCase consumeAssignment(); $break ./
/:$readableName Assignment:/

-- this rule is added to parse an array initializer in a assigment and then report a syntax error knowing the exact senario
InvalidArrayInitializerAssignement ::= PostfixExpression AssignmentOperator ArrayInitializer
/:$readableName ArrayInitializerAssignment:/
/:$recovery:/
Assignment ::= InvalidArrayInitializerAssignement
/.$putcase ignoreExpressionAssignment();$break ./
/:$recovery:/

AssignmentOperator ::= '='
/.$putCase consumeAssignmentOperator(EQUAL); $break ./
AssignmentOperator ::= '*='
/.$putCase consumeAssignmentOperator(MULTIPLY); $break ./
AssignmentOperator ::= '/='
/.$putCase consumeAssignmentOperator(DIVIDE); $break ./
AssignmentOperator ::= '%='
/.$putCase consumeAssignmentOperator(REMAINDER); $break ./
AssignmentOperator ::= '+='
/.$putCase consumeAssignmentOperator(PLUS); $break ./
AssignmentOperator ::= '-='
/.$putCase consumeAssignmentOperator(MINUS); $break ./
AssignmentOperator ::= '<<='
/.$putCase consumeAssignmentOperator(LEFT_SHIFT); $break ./
AssignmentOperator ::= '>>='
/.$putCase consumeAssignmentOperator(RIGHT_SHIFT); $break ./
AssignmentOperator ::= '>>>='
/.$putCase consumeAssignmentOperator(UNSIGNED_RIGHT_SHIFT); $break ./
AssignmentOperator ::= '&='
/.$putCase consumeAssignmentOperator(AND); $break ./
AssignmentOperator ::= '^='
/.$putCase consumeAssignmentOperator(XOR); $break ./
AssignmentOperator ::= '|='
/.$putCase consumeAssignmentOperator(OR); $break ./
/:$readableName AssignmentOperator:/
/:$recovery_template =:/

-- For handling lambda expressions, we need to know when a full Expression
-- has been reduced.
Expression ::= AssignmentExpression
/.$putCase consumeExpression(); $break ./
/:$readableName Expression:/
/:$recovery_template Identifier:/

-- The following rules are for optional nonterminals.
--
ClassHeaderExtendsopt ::= $empty
ClassHeaderExtendsopt -> ClassHeaderExtends
/:$readableName ClassHeaderExtends:/

Expressionopt ::= $empty
/.$putCase consumeEmptyExpression(); $break ./
Expressionopt -> Expression
/:$readableName Expression:/

ConstantExpressions -> Expression
ConstantExpressions ::= ConstantExpressions ',' Expression
/.$putCase consumeConstantExpressions(); $break ./
/:$readableName ConstantExpressions:/

ConstantExpression -> Expression
/:$readableName ConstantExpression:/

---------------------------------------------------------------------------------------
--
-- The rules below are for optional terminal symbols.  An optional comma,
-- is only used in the context of an array initializer - It is a
-- "syntactic sugar" that otherwise serves no other purpose. By contrast,
-- an optional identifier is used in the definition of a break and 
-- continue statement. When the identifier does not appear, a NULL
-- is produced. When the identifier is present, the user should use the
-- corresponding TOKEN(i) method. See break statement as an example.
--
---------------------------------------------------------------------------------------

,opt -> $empty
,opt -> ,
/:$readableName ,:/

ClassBodyDeclarationsopt ::= $empty
/.$putCase consumeEmptyClassBodyDeclarationsopt(); $break ./
ClassBodyDeclarationsopt ::= NestedType ClassBodyDeclarations
/.$putCase consumeClassBodyDeclarationsopt(); $break ./
/:$readableName ClassBodyDeclarations:/

Modifiersopt ::= $empty 
/. $putCase consumeDefaultModifiers(); $break ./
Modifiersopt ::= Modifiers
/.$putCase consumeModifiers(); $break ./ 
/:$readableName Modifiers:/

BlockStatementsopt ::= $empty
/.$putCase consumeEmptyBlockStatementsopt(); $break ./
BlockStatementsopt -> BlockStatements
/:$readableName BlockStatements:/

Dimsopt ::= $empty
/. $putCase consumeEmptyDimsopt(); $break ./
Dimsopt -> Dims
/:$readableName Dimensions:/

ArgumentListopt ::= $empty
/. $putCase consumeEmptyArgumentListopt(); $break ./
ArgumentListopt -> ArgumentList
/:$readableName ArgumentList:/

MethodHeaderThrowsClauseopt ::= $empty
MethodHeaderThrowsClauseopt -> MethodHeaderThrowsClause
/:$readableName MethodHeaderThrowsClause:/

FormalParameterListopt ::= $empty
/.$putcase consumeFormalParameterListopt(); $break ./
FormalParameterListopt -> FormalParameterList
/:$readableName FormalParameterList:/

ClassHeaderImplementsopt ::= $empty
ClassHeaderImplementsopt -> ClassHeaderImplements
/:$readableName ClassHeaderImplements:/

InterfaceMemberDeclarationsopt ::= $empty
/. $putCase consumeEmptyInterfaceMemberDeclarationsopt(); $break ./
InterfaceMemberDeclarationsopt ::= NestedType InterfaceMemberDeclarations
/. $putCase consumeInterfaceMemberDeclarationsopt(); $break ./
/:$readableName InterfaceMemberDeclarations:/

NestedType ::= $empty 
/.$putCase consumeNestedType(); $break./
/:$readableName NestedType:/

ForInitopt ::= $empty
/. $putCase consumeEmptyForInitopt(); $break ./
ForInitopt -> ForInit
/:$readableName ForInit:/

ForUpdateopt ::= $empty
/. $putCase consumeEmptyForUpdateopt(); $break ./
ForUpdateopt -> ForUpdate
/:$readableName ForUpdate:/

InterfaceHeaderExtendsopt ::= $empty
InterfaceHeaderExtendsopt -> InterfaceHeaderExtends
/:$readableName InterfaceHeaderExtends:/

Catchesopt ::= $empty
/. $putCase consumeEmptyCatchesopt(); $break ./
Catchesopt -> Catches
/:$readableName Catches:/

-----------------------------------------------
-- 1.5 features : enum type
-----------------------------------------------
EnumDeclaration ::= EnumHeader EnumBody
/. $putCase consumeEnumDeclaration(); $break ./
/:$readableName EnumDeclaration:/

EnumHeader ::= EnumHeaderName ClassHeaderImplementsopt
/. $putCase consumeEnumHeader(); $break ./
/:$readableName EnumHeader:/

EnumHeaderName ::= Modifiersopt 'enum' Identifier
/. $putCase consumeEnumHeaderName(); $break ./
/:$compliance 1.5:/
EnumHeaderName ::= Modifiersopt 'enum' Identifier TypeParameters
/. $putCase consumeEnumHeaderNameWithTypeParameters(); $break ./
/:$readableName EnumHeaderName:/
/:$compliance 1.5:/

EnumBody ::= '{' EnumBodyDeclarationsopt '}'
/. $putCase consumeEnumBodyNoConstants(); $break ./
EnumBody ::= '{' ',' EnumBodyDeclarationsopt '}'
/. $putCase consumeEnumBodyNoConstants(); $break ./
EnumBody ::= '{' EnumConstants ',' EnumBodyDeclarationsopt '}'
/. $putCase consumeEnumBodyWithConstants(); $break ./
EnumBody ::= '{' EnumConstants EnumBodyDeclarationsopt '}'
/. $putCase consumeEnumBodyWithConstants(); $break ./
/:$readableName EnumBody:/

EnumConstants -> EnumConstant
EnumConstants ::= EnumConstants ',' EnumConstant
/.$putCase consumeEnumConstants(); $break ./
/:$readableName EnumConstants:/

EnumConstantHeaderName ::= Modifiersopt Identifier
/.$putCase consumeEnumConstantHeaderName(); $break ./
/:$readableName EnumConstantHeaderName:/

EnumConstantHeader ::= EnumConstantHeaderName ForceNoDiet Argumentsopt RestoreDiet
/.$putCase consumeEnumConstantHeader(); $break ./
/:$readableName EnumConstantHeader:/

EnumConstant ::= EnumConstantHeader ForceNoDiet ClassBody RestoreDiet
/.$putCase consumeEnumConstantWithClassBody(); $break ./
EnumConstant ::= EnumConstantHeader
/.$putCase consumeEnumConstantNoClassBody(); $break ./
/:$readableName EnumConstant:/

Arguments ::= '(' ArgumentListopt ')'
/.$putCase consumeArguments(); $break ./
/:$readableName Arguments:/

Argumentsopt ::= $empty
/.$putCase consumeEmptyArguments(); $break ./
Argumentsopt -> Arguments
/:$readableName Argumentsopt:/

EnumDeclarations ::= ';' ClassBodyDeclarationsopt
/.$putCase consumeEnumDeclarations(); $break ./
/:$readableName EnumDeclarations:/

EnumBodyDeclarationsopt ::= $empty
/.$putCase consumeEmptyEnumDeclarations(); $break ./
EnumBodyDeclarationsopt -> EnumDeclarations
/:$readableName EnumBodyDeclarationsopt:/

-----------------------------------------------
-- 1.5 features : enhanced for statement
-----------------------------------------------
EnhancedForStatement ::= EnhancedForStatementHeader Statement
/.$putCase consumeEnhancedForStatement(); $break ./
/:$readableName EnhancedForStatement:/

EnhancedForStatementNoShortIf ::= EnhancedForStatementHeader StatementNoShortIf
/.$putCase consumeEnhancedForStatement(); $break ./
/:$readableName EnhancedForStatementNoShortIf:/

EnhancedForStatementHeaderInit ::= 'for' '(' Type PushModifiers Identifier Dimsopt
/.$putCase consumeEnhancedForStatementHeaderInit(false); $break ./
/:$readableName EnhancedForStatementHeaderInit:/

EnhancedForStatementHeaderInit ::= 'for' '(' Modifiers Type PushRealModifiers Identifier Dimsopt
/.$putCase consumeEnhancedForStatementHeaderInit(true); $break ./
/:$readableName EnhancedForStatementHeaderInit:/

EnhancedForStatementHeader ::= EnhancedForStatementHeaderInit ':' Expression ')'
/.$putCase consumeEnhancedForStatementHeader(); $break ./
/:$readableName EnhancedForStatementHeader:/
/:$compliance 1.5:/

--{ObjectTeams: base import:
SingleBaseImportDeclaration ::= SingleBaseImportDeclarationName ';'
/.$putCase consumeImportDeclaration(); $break ./
/:$readableName SingleBaseImportDeclaration:/

SingleBaseImportDeclarationName ::= 'import' 'base' Name
/.$putCase consumeSingleBaseImportDeclarationName(); $break ./
/:$readableName SingleBaseImportDeclarationName:/
-- SH}
-----------------------------------------------
-- 1.5 features : static imports
-----------------------------------------------
SingleStaticImportDeclaration ::= SingleStaticImportDeclarationName ';'
/.$putCase consumeImportDeclaration(); $break ./
/:$readableName SingleStaticImportDeclaration:/

SingleStaticImportDeclarationName ::= 'import' 'static' Name RejectTypeAnnotations
/.$putCase consumeSingleStaticImportDeclarationName(); $break ./
/:$readableName SingleStaticImportDeclarationName:/
/:$compliance 1.5:/

StaticImportOnDemandDeclaration ::= StaticImportOnDemandDeclarationName ';'
/.$putCase consumeImportDeclaration(); $break ./
/:$readableName StaticImportOnDemandDeclaration:/

StaticImportOnDemandDeclarationName ::= 'import' 'static' Name '.' RejectTypeAnnotations '*'
/.$putCase consumeStaticImportOnDemandDeclarationName(); $break ./
/:$readableName StaticImportOnDemandDeclarationName:/
/:$compliance 1.5:/

-----------------------------------------------
-- 1.5 features : generics
-----------------------------------------------
TypeArguments ::= '<' TypeArgumentList1
/.$putCase consumeTypeArguments(); $break ./
/:$readableName TypeArguments:/
/:$compliance 1.5:/

OnlyTypeArguments ::= '<' TypeArgumentList1
/.$putCase consumeOnlyTypeArguments(); $break ./
/:$readableName TypeArguments:/
/:$compliance 1.5:/

TypeArgumentList1 -> TypeArgument1
/:$compliance 1.5:/
TypeArgumentList1 ::= TypeArgumentList ',' TypeArgument1
/.$putCase consumeTypeArgumentList1(); $break ./
/:$readableName TypeArgumentList1:/
/:$compliance 1.5:/

TypeArgumentList -> TypeArgument
/:$compliance 1.5:/
TypeArgumentList ::= TypeArgumentList ',' TypeArgument
/.$putCase consumeTypeArgumentList(); $break ./
/:$readableName TypeArgumentList:/
/:$compliance 1.5:/

TypeArgument ::= ReferenceType
/.$putCase consumeTypeArgument(); $break ./
/:$compliance 1.5:/
TypeArgument -> Wildcard
/:$readableName TypeArgument:/
/:$compliance 1.5:/

--{ObjectTeams: anchored types: we explicitly don't decide between TypeAnchor and annotated type parameter yet
TypeArgument -> TypeAnchorOrAnnotatedTypeArgument
TypeArgument1 -> TypeAnchorOrAnnotatedTypeArgument1
TypeArgument2 -> TypeAnchorOrAnnotatedTypeArgument2
TypeArgument3 -> TypeAnchorOrAnnotatedTypeArgument3

-- ==== No Nested Generics ====
-- case 1: it was indeed a type anchor:
TypeAnchorOrAnnotatedTypeArgument -> AnyTypeAnchor
/.$putCase confirmTypeAnchor(); $break ./
-- case 2a: we were wrong in assuming a type anchor, converted marker type annotation exists, time to clean up
TypeAnchorOrAnnotatedTypeArgument ::= TentativeTypeAnchor NotAnAnchor ReferenceType
/.$putCase consumeTypeArgumentFromAnchor(); $break ./
/:$readableName TypeArgument:/
/:$compliance 1.5:/
-- case 2b: we were wrong in assuming a type anchor, converted marker type annotation exists, time to clean up
TypeAnchorOrAnnotatedTypeArgument -> TentativeTypeAnchor NotAnAnchor Wildcard
/.$putCase consumeAnnotationsOnTypeArgumentFromAnchor(); $break ./
/:$readableName TypeArgument:/
/:$compliance 1.5:/

-- ==== One Level Nested Generics ====
-- case 1: it was indeed a type anchor:
TypeAnchorOrAnnotatedTypeArgument1 -> AnyTypeAnchor '>'
/.$putCase confirmTypeAnchor(); $break ./
/:$readableName TypeAnchor:/
/:$compliance 1.5:/
-- case 2a: we were wrong in assuming a type anchor, converted marker type annotation exists, time to clean up
TypeAnchorOrAnnotatedTypeArgument1 -> TentativeTypeAnchor NotAnAnchor ReferenceType1
/.$putCase consumeAnnotationsOnTypeArgumentFromAnchor(); $break ./
/:$readableName TypeArgument:/
/:$compliance 1.5:/
-- case 2b: we were wrong in assuming a type anchor, converted marker type annotation exists, time to clean up
TypeAnchorOrAnnotatedTypeArgument1 -> TentativeTypeAnchor NotAnAnchor Wildcard1
/.$putCase consumeAnnotationsOnTypeArgumentFromAnchor(); $break ./
/:$readableName TypeArgument:/
/:$compliance 1.5:/

-- ==== Two Levels Nested Generics ====
-- case 1: it was indeed a type anchor:
TypeAnchorOrAnnotatedTypeArgument2 -> AnyTypeAnchor '>>'
/.$putCase confirmTypeAnchor(); $break ./
/:$readableName TypeAnchor:/
/:$compliance 1.5:/
-- case 2a: we were wrong in assuming a type anchor, converted marker type annotation exists, time to clean up
TypeAnchorOrAnnotatedTypeArgument2 -> TentativeTypeAnchor NotAnAnchor ReferenceType2
/.$putCase consumeAnnotationsOnTypeArgumentFromAnchor(); $break ./
/:$readableName TypeArgument:/
/:$compliance 1.5:/
-- case 2b: we were wrong in assuming a type anchor, converted marker type annotation exists, time to clean up
TypeAnchorOrAnnotatedTypeArgument2 -> TentativeTypeAnchor NotAnAnchor Wildcard2
/.$putCase consumeAnnotationsOnTypeArgumentFromAnchor(); $break ./
/:$readableName TypeArgument:/
/:$compliance 1.5:/

-- ==== Three Levels Nested Generics ====
-- case 1: it was indeed a type anchor:
TypeAnchorOrAnnotatedTypeArgument3 -> AnyTypeAnchor '>>>'
/.$putCase confirmTypeAnchor(); $break ./
/:$readableName TypeAnchor:/
/:$compliance 1.5:/
-- case 2a: we were wrong in assuming a type anchor, converted marker type annotation exists, time to clean up
TypeAnchorOrAnnotatedTypeArgument3 -> TentativeTypeAnchor NotAnAnchor ReferenceType3
/.$putCase consumeAnnotationsOnTypeArgumentFromAnchor(); $break ./
/:$readableName TypeArgument:/
/:$compliance 1.5:/
-- case 2b: we were wrong in assuming a type anchor, converted marker type annotation exists, time to clean up
TypeAnchorOrAnnotatedTypeArgument3 -> TentativeTypeAnchor NotAnAnchor Wildcard3
/.$putCase consumeAnnotationsOnTypeArgumentFromAnchor(); $break ./
/:$readableName TypeArgument:/
/:$compliance 1.5:/
-- =====================================

-- trigger converting a mistaken type anchor into a type annotation on a type argument
NotAnAnchor ::= $empty
/.$putCase convertTypeAnchor(0); $break ./
/:$readableName annotatedTypeArgument:/
NotAnAnchor -> '(' SingleMemberAnnotationMemberValue ')'
/.$putCase convertTypeAnchor(1); $break ./
/:$readableName annotatedTypeArgument:/
NotAnAnchor -> '(' MemberValuePairsopt ')'
/.$putCase convertTypeAnchor(2); $break ./
/:$readableName annotatedTypeArgument:/


AnyTypeAnchor -> TypeAnchor
/:$readableName typeAnchor:/
AnyTypeAnchor -> TentativeTypeAnchor
/:$readableName typeAnchor:/

-- this rule could indicate either a type anchor or a type annotation
TentativeTypeAnchor ::= '@OT' UnannotatableName
/.$putCase consumeTypeAnchor(false); $break ./
/:$readableName typeAnchor:/

-- the following rules indicate definite type anchors:
-- base is a keyword in this mode, so explicitly expect it:
TypeAnchor ::= '@OT' 'base'
/.$putCase consumeTypeAnchor(true); $break ./
/:$readableName typeAnchor:/

-- also 'this' requires special treatment (skip because redundant):
TypeAnchor ::= '@OT' 'this'
/.$putCase skipThisAnchor(); $break ./
/:$readableName typeAnchor:/

TypeAnchor ::= '@OT' UnannotatableName '.' 'base'
/:$readableName typeAnchor:/
/.$putCase consumeQualifiedBaseTypeAnchor(); $break ./

--SH}

TypeArgument1 -> ReferenceType1
/:$compliance 1.5:/
TypeArgument1 -> Wildcard1
/:$readableName TypeArgument1:/
/:$compliance 1.5:/

ReferenceType1 ::= ReferenceType '>'
/.$putCase consumeReferenceType1(); $break ./
/:$compliance 1.5:/
ReferenceType1 ::= ClassOrInterface '<' TypeArgumentList2
/.$putCase consumeTypeArgumentReferenceType1(); $break ./
/:$readableName ReferenceType1:/
/:$compliance 1.5:/

TypeArgumentList2 -> TypeArgument2
/:$compliance 1.5:/
TypeArgumentList2 ::= TypeArgumentList ',' TypeArgument2
/.$putCase consumeTypeArgumentList2(); $break ./
/:$readableName TypeArgumentList2:/
/:$compliance 1.5:/

TypeArgument2 -> ReferenceType2
/:$compliance 1.5:/
TypeArgument2 -> Wildcard2
/:$readableName TypeArgument2:/
/:$compliance 1.5:/

ReferenceType2 ::= ReferenceType '>>'
/.$putCase consumeReferenceType2(); $break ./
/:$compliance 1.5:/
ReferenceType2 ::= ClassOrInterface '<' TypeArgumentList3
/.$putCase consumeTypeArgumentReferenceType2(); $break ./
/:$readableName ReferenceType2:/
/:$compliance 1.5:/

TypeArgumentList3 -> TypeArgument3
TypeArgumentList3 ::= TypeArgumentList ',' TypeArgument3
/.$putCase consumeTypeArgumentList3(); $break ./
/:$readableName TypeArgumentList3:/
/:$compliance 1.5:/

TypeArgument3 -> ReferenceType3
TypeArgument3 -> Wildcard3
/:$readableName TypeArgument3:/
/:$compliance 1.5:/

ReferenceType3 ::= ReferenceType '>>>'
/.$putCase consumeReferenceType3(); $break ./
/:$readableName ReferenceType3:/
/:$compliance 1.5:/

Wildcard ::= TypeAnnotationsopt '?'
/.$putCase consumeWildcard(); $break ./
/:$compliance 1.5:/
Wildcard ::= TypeAnnotationsopt '?' WildcardBounds
/.$putCase consumeWildcardWithBounds(); $break ./
/:$readableName Wildcard:/
/:$compliance 1.5:/

WildcardBounds ::= 'extends' ReferenceType
/.$putCase consumeWildcardBoundsExtends(); $break ./
/:$compliance 1.5:/
WildcardBounds ::= 'super' ReferenceType
/.$putCase consumeWildcardBoundsSuper(); $break ./
/:$readableName WildcardBounds:/
/:$compliance 1.5:/

Wildcard1 ::= TypeAnnotationsopt '?' '>'
/.$putCase consumeWildcard1(); $break ./
/:$compliance 1.5:/
Wildcard1 ::= TypeAnnotationsopt '?' WildcardBounds1
/.$putCase consumeWildcard1WithBounds(); $break ./
/:$readableName Wildcard1:/
/:$compliance 1.5:/

WildcardBounds1 ::= 'extends' ReferenceType1
/.$putCase consumeWildcardBounds1Extends(); $break ./
/:$compliance 1.5:/
WildcardBounds1 ::= 'super' ReferenceType1
/.$putCase consumeWildcardBounds1Super(); $break ./
/:$readableName WildcardBounds1:/
/:$compliance 1.5:/

Wildcard2 ::= TypeAnnotationsopt '?' '>>'
/.$putCase consumeWildcard2(); $break ./
/:$compliance 1.5:/
Wildcard2 ::= TypeAnnotationsopt '?' WildcardBounds2
/.$putCase consumeWildcard2WithBounds(); $break ./
/:$readableName Wildcard2:/
/:$compliance 1.5:/

WildcardBounds2 ::= 'extends' ReferenceType2
/.$putCase consumeWildcardBounds2Extends(); $break ./
/:$compliance 1.5:/
WildcardBounds2 ::= 'super' ReferenceType2
/.$putCase consumeWildcardBounds2Super(); $break ./
/:$readableName WildcardBounds2:/
/:$compliance 1.5:/

Wildcard3 ::= TypeAnnotationsopt '?' '>>>'
/.$putCase consumeWildcard3(); $break ./
/:$compliance 1.5:/
Wildcard3 ::= TypeAnnotationsopt '?' WildcardBounds3
/.$putCase consumeWildcard3WithBounds(); $break ./
/:$readableName Wildcard3:/
/:$compliance 1.5:/

WildcardBounds3 ::= 'extends' ReferenceType3
/.$putCase consumeWildcardBounds3Extends(); $break ./
/:$compliance 1.5:/
WildcardBounds3 ::= 'super' ReferenceType3
/.$putCase consumeWildcardBounds3Super(); $break ./
/:$readableName WildcardBound3:/
/:$compliance 1.5:/

TypeParameterHeader ::= TypeAnnotationsopt Identifier
/.$putCase consumeTypeParameterHeader(); $break ./
/:$readableName TypeParameter:/
/:$compliance 1.5:/

TypeParameters ::= '<' TypeParameterList1
/.$putCase consumeTypeParameters(); $break ./
/:$readableName TypeParameters:/
/:$compliance 1.5:/

TypeParameterList -> TypeParameter
/:$compliance 1.5:/
TypeParameterList ::= TypeParameterList ',' TypeParameter
/.$putCase consumeTypeParameterList(); $break ./
/:$readableName TypeParameterList:/
/:$compliance 1.5:/

TypeParameter -> TypeParameterHeader
/:$compliance 1.5:/
TypeParameter ::= TypeParameterHeader 'extends' ReferenceType
/.$putCase consumeTypeParameterWithExtends(); $break ./
/:$compliance 1.5:/
TypeParameter ::= TypeParameterHeader 'extends' ReferenceType AdditionalBoundList
/.$putCase consumeTypeParameterWithExtendsAndBounds(); $break ./
/:$readableName TypeParameter:/
/:$compliance 1.5:/

--{ObjectTeams:
-- <B base R>
TypeParameter ::= TypeParameterHeader 'base' ReferenceType
/.$putCase consumeTypeParameterWithBase(); $break ./
/:$compliance 1.5:/

-- <... Team t ...>
TypeParameter -> TypeValueParameter
TypeParameter1 -> TypeValueParameter1

TypeValueParameter1 -> TypeValueParameter '>'

TypeValueParameter ::= TypeParameterHeader Identifier
/.$putCase consumeTypeValueParameter(); $break ./
/:$compliance 1.5:/
/:$readableName TypeValueParameter:/

TypeParameter -> AnchoredTypeParameterHeader0 '>' TypeBoundOpt
TypeParameter1 -> AnchoredTypeParameterHeader0 '>' TypeBoundOpt1
TypeParameter1 -> AnchoredTypeParameterHeader0 '>>'

TypeBoundOpt -> $empty
TypeBoundOpt ::= 'extends' ReferenceType
/.$putCase consumeBoundsOfAnchoredTypeParameter(); $break ./
/:$compliance 1.5:/
/:$readableName TypeParameterBound:/ 

TypeBoundOpt1 -> '>'
TypeBoundOpt1 ::= 'extends' ReferenceType1
/.$putCase consumeBoundsOfAnchoredTypeParameter(); $break ./
/:$compliance 1.5:/
/:$readableName TypeParameterBound:/ 

AnchoredTypeParameterHeader0 ::= TypeParameterHeader '<' AnyTypeAnchor
/.$putCase consumeAnchoredTypeParameter(); $break ./
/:$compliance 1.5:/
/:$readableName AnchoredTypeParameter:/
-- SH}

AdditionalBoundList -> AdditionalBound
/:$compliance 1.5:/
AdditionalBoundList ::= AdditionalBoundList AdditionalBound
/.$putCase consumeAdditionalBoundList(); $break ./
/:$readableName AdditionalBoundList:/

AdditionalBound ::= '&' ReferenceType
/.$putCase consumeAdditionalBound(); $break ./
/:$readableName AdditionalBound:/
/:$compliance 1.5:/

TypeParameterList1 -> TypeParameter1
/:$compliance 1.5:/
TypeParameterList1 ::= TypeParameterList ',' TypeParameter1
/.$putCase consumeTypeParameterList1(); $break ./
/:$readableName TypeParameterList1:/
/:$compliance 1.5:/

TypeParameter1 ::= TypeParameterHeader '>'
/.$putCase consumeTypeParameter1(); $break ./
/:$compliance 1.5:/
TypeParameter1 ::= TypeParameterHeader 'extends' ReferenceType1
/.$putCase consumeTypeParameter1WithExtends(); $break ./
/:$compliance 1.5:/

--{ObjectTeams: <B base R>
TypeParameter1 ::= TypeParameterHeader 'base' ReferenceType1
/.$putCase consumeTypeParameter1WithBase(); $break ./
/:$compliance 1.5:/
-- SH}

TypeParameter1 ::= TypeParameterHeader 'extends' ReferenceType AdditionalBoundList1
/.$putCase consumeTypeParameter1WithExtendsAndBounds(); $break ./
/:$readableName TypeParameter1:/
/:$compliance 1.5:/

AdditionalBoundList1 -> AdditionalBound1
/:$compliance 1.5:/
AdditionalBoundList1 ::= AdditionalBoundList AdditionalBound1
/.$putCase consumeAdditionalBoundList1(); $break ./
/:$readableName AdditionalBoundList1:/
/:$compliance 1.5:/

AdditionalBound1 ::= '&' ReferenceType1
/.$putCase consumeAdditionalBound1(); $break ./
/:$readableName AdditionalBound1:/
/:$compliance 1.5:/

-------------------------------------------------
-- Duplicate rules to remove ambiguity for (x) --
-------------------------------------------------
PostfixExpression_NotName -> Primary
PostfixExpression_NotName -> PostIncrementExpression
PostfixExpression_NotName -> PostDecrementExpression
/:$readableName Expression:/

UnaryExpression_NotName -> PreIncrementExpression
UnaryExpression_NotName -> PreDecrementExpression
UnaryExpression_NotName ::= '+' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.PLUS); $break ./
UnaryExpression_NotName ::= '-' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.MINUS); $break ./
UnaryExpression_NotName -> UnaryExpressionNotPlusMinus_NotName
/:$readableName Expression:/

UnaryExpressionNotPlusMinus_NotName -> PostfixExpression_NotName
UnaryExpressionNotPlusMinus_NotName ::= '~' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.TWIDDLE); $break ./
UnaryExpressionNotPlusMinus_NotName ::= '!' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.NOT); $break ./
UnaryExpressionNotPlusMinus_NotName -> CastExpression
/:$readableName Expression:/

MultiplicativeExpression_NotName -> UnaryExpression_NotName
MultiplicativeExpression_NotName ::= MultiplicativeExpression_NotName '*' UnaryExpression
/.$putCase consumeBinaryExpression(OperatorIds.MULTIPLY); $break ./
MultiplicativeExpression_NotName ::= Name '*' UnaryExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.MULTIPLY); $break ./
MultiplicativeExpression_NotName ::= MultiplicativeExpression_NotName '/' UnaryExpression
/.$putCase consumeBinaryExpression(OperatorIds.DIVIDE); $break ./
MultiplicativeExpression_NotName ::= Name '/' UnaryExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.DIVIDE); $break ./
MultiplicativeExpression_NotName ::= MultiplicativeExpression_NotName '%' UnaryExpression
/.$putCase consumeBinaryExpression(OperatorIds.REMAINDER); $break ./
MultiplicativeExpression_NotName ::= Name '%' UnaryExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.REMAINDER); $break ./
/:$readableName Expression:/

AdditiveExpression_NotName -> MultiplicativeExpression_NotName
AdditiveExpression_NotName ::= AdditiveExpression_NotName '+' MultiplicativeExpression
/.$putCase consumeBinaryExpression(OperatorIds.PLUS); $break ./
AdditiveExpression_NotName ::= Name '+' MultiplicativeExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.PLUS); $break ./
AdditiveExpression_NotName ::= AdditiveExpression_NotName '-' MultiplicativeExpression
/.$putCase consumeBinaryExpression(OperatorIds.MINUS); $break ./
AdditiveExpression_NotName ::= Name '-' MultiplicativeExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.MINUS); $break ./
/:$readableName Expression:/

ShiftExpression_NotName -> AdditiveExpression_NotName
ShiftExpression_NotName ::= ShiftExpression_NotName '<<' AdditiveExpression
/.$putCase consumeBinaryExpression(OperatorIds.LEFT_SHIFT); $break ./
ShiftExpression_NotName ::= Name '<<' AdditiveExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.LEFT_SHIFT); $break ./
ShiftExpression_NotName ::= ShiftExpression_NotName '>>' AdditiveExpression
/.$putCase consumeBinaryExpression(OperatorIds.RIGHT_SHIFT); $break ./
ShiftExpression_NotName ::= Name '>>' AdditiveExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.RIGHT_SHIFT); $break ./
ShiftExpression_NotName ::= ShiftExpression_NotName '>>>' AdditiveExpression
/.$putCase consumeBinaryExpression(OperatorIds.UNSIGNED_RIGHT_SHIFT); $break ./
ShiftExpression_NotName ::= Name '>>>' AdditiveExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.UNSIGNED_RIGHT_SHIFT); $break ./
/:$readableName Expression:/

RelationalExpression_NotName -> ShiftExpression_NotName
RelationalExpression_NotName ::= ShiftExpression_NotName '<' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.LESS); $break ./
RelationalExpression_NotName ::= Name '<' ShiftExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.LESS); $break ./
RelationalExpression_NotName ::= ShiftExpression_NotName '>' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.GREATER); $break ./
RelationalExpression_NotName ::= Name '>' ShiftExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.GREATER); $break ./
RelationalExpression_NotName ::= RelationalExpression_NotName '<=' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.LESS_EQUAL); $break ./
RelationalExpression_NotName ::= Name '<=' ShiftExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.LESS_EQUAL); $break ./
RelationalExpression_NotName ::= RelationalExpression_NotName '>=' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.GREATER_EQUAL); $break ./
RelationalExpression_NotName ::= Name '>=' ShiftExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.GREATER_EQUAL); $break ./
/:$readableName Expression:/

InstanceofExpression_NotName -> RelationalExpression_NotName
InstanceofExpression_NotName ::= Name 'instanceof' ReferenceType
/.$putCase consumeInstanceOfExpressionWithName(); $break ./
InstanceofExpression_NotName ::= InstanceofExpression_NotName 'instanceof' ReferenceType
/.$putCase consumeInstanceOfExpression(); $break ./
/:$readableName Expression:/

EqualityExpression_NotName -> InstanceofExpression_NotName
EqualityExpression_NotName ::= EqualityExpression_NotName '==' InstanceofExpression
/.$putCase consumeEqualityExpression(OperatorIds.EQUAL_EQUAL); $break ./
EqualityExpression_NotName ::= Name '==' InstanceofExpression
/.$putCase consumeEqualityExpressionWithName(OperatorIds.EQUAL_EQUAL); $break ./
EqualityExpression_NotName ::= EqualityExpression_NotName '!=' InstanceofExpression
/.$putCase consumeEqualityExpression(OperatorIds.NOT_EQUAL); $break ./
EqualityExpression_NotName ::= Name '!=' InstanceofExpression
/.$putCase consumeEqualityExpressionWithName(OperatorIds.NOT_EQUAL); $break ./
/:$readableName Expression:/

AndExpression_NotName -> EqualityExpression_NotName
AndExpression_NotName ::= AndExpression_NotName '&' EqualityExpression
/.$putCase consumeBinaryExpression(OperatorIds.AND); $break ./
AndExpression_NotName ::= Name '&' EqualityExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.AND); $break ./
/:$readableName Expression:/

ExclusiveOrExpression_NotName -> AndExpression_NotName
ExclusiveOrExpression_NotName ::= ExclusiveOrExpression_NotName '^' AndExpression
/.$putCase consumeBinaryExpression(OperatorIds.XOR); $break ./
ExclusiveOrExpression_NotName ::= Name '^' AndExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.XOR); $break ./
/:$readableName Expression:/

InclusiveOrExpression_NotName -> ExclusiveOrExpression_NotName
InclusiveOrExpression_NotName ::= InclusiveOrExpression_NotName '|' ExclusiveOrExpression
/.$putCase consumeBinaryExpression(OperatorIds.OR); $break ./
InclusiveOrExpression_NotName ::= Name '|' ExclusiveOrExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.OR); $break ./
/:$readableName Expression:/

ConditionalAndExpression_NotName -> InclusiveOrExpression_NotName
ConditionalAndExpression_NotName ::= ConditionalAndExpression_NotName '&&' InclusiveOrExpression
/.$putCase consumeBinaryExpression(OperatorIds.AND_AND); $break ./
ConditionalAndExpression_NotName ::= Name '&&' InclusiveOrExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.AND_AND); $break ./
/:$readableName Expression:/

ConditionalOrExpression_NotName -> ConditionalAndExpression_NotName
ConditionalOrExpression_NotName ::= ConditionalOrExpression_NotName '||' ConditionalAndExpression
/.$putCase consumeBinaryExpression(OperatorIds.OR_OR); $break ./
ConditionalOrExpression_NotName ::= Name '||' ConditionalAndExpression
/.$putCase consumeBinaryExpressionWithName(OperatorIds.OR_OR); $break ./
/:$readableName Expression:/

ConditionalExpression_NotName -> ConditionalOrExpression_NotName
ConditionalExpression_NotName ::= ConditionalOrExpression_NotName '?' Expression ':' ConditionalExpression
/.$putCase consumeConditionalExpression(OperatorIds.QUESTIONCOLON) ; $break ./
ConditionalExpression_NotName ::= Name '?' Expression ':' ConditionalExpression
/.$putCase consumeConditionalExpressionWithName(OperatorIds.QUESTIONCOLON) ; $break ./
/:$readableName Expression:/

AssignmentExpression_NotName -> ConditionalExpression_NotName
AssignmentExpression_NotName -> Assignment
/:$readableName Expression:/

Expression_NotName -> AssignmentExpression_NotName
/:$readableName Expression:/
-----------------------------------------------
-- 1.5 features : end of generics
-----------------------------------------------
-----------------------------------------------
-- 1.5 features : annotation - Metadata feature jsr175
-----------------------------------------------
AnnotationTypeDeclarationHeaderName ::= Modifiers '@' PushRealModifiers interface Identifier
/.$putCase consumeAnnotationTypeDeclarationHeaderName() ; $break ./
/:$compliance 1.5:/
AnnotationTypeDeclarationHeaderName ::= Modifiers '@' PushRealModifiers interface Identifier TypeParameters
/.$putCase consumeAnnotationTypeDeclarationHeaderNameWithTypeParameters() ; $break ./
/:$compliance 1.5:/
AnnotationTypeDeclarationHeaderName ::= '@' PushModifiersForHeader interface Identifier TypeParameters
/.$putCase consumeAnnotationTypeDeclarationHeaderNameWithTypeParameters() ; $break ./
/:$compliance 1.5:/
AnnotationTypeDeclarationHeaderName ::= '@' PushModifiersForHeader interface Identifier
/.$putCase consumeAnnotationTypeDeclarationHeaderName() ; $break ./
/:$readableName AnnotationTypeDeclarationHeaderName:/
/:$compliance 1.5:/

AnnotationTypeDeclarationHeader ::= AnnotationTypeDeclarationHeaderName ClassHeaderExtendsopt ClassHeaderImplementsopt
/.$putCase consumeAnnotationTypeDeclarationHeader() ; $break ./
/:$readableName AnnotationTypeDeclarationHeader:/
/:$compliance 1.5:/

AnnotationTypeDeclaration ::= AnnotationTypeDeclarationHeader AnnotationTypeBody
/.$putCase consumeAnnotationTypeDeclaration() ; $break ./
/:$readableName AnnotationTypeDeclaration:/
/:$compliance 1.5:/

AnnotationTypeBody ::= '{' AnnotationTypeMemberDeclarationsopt '}'
/:$readableName AnnotationTypeBody:/
/:$compliance 1.5:/

AnnotationTypeMemberDeclarationsopt ::= $empty
/.$putCase consumeEmptyAnnotationTypeMemberDeclarationsopt() ; $break ./
/:$compliance 1.5:/
AnnotationTypeMemberDeclarationsopt ::= NestedType AnnotationTypeMemberDeclarations
/.$putCase consumeAnnotationTypeMemberDeclarationsopt() ; $break ./
/:$readableName AnnotationTypeMemberDeclarations:/
/:$compliance 1.5:/

AnnotationTypeMemberDeclarations -> AnnotationTypeMemberDeclaration
/:$compliance 1.5:/
AnnotationTypeMemberDeclarations ::= AnnotationTypeMemberDeclarations AnnotationTypeMemberDeclaration
/.$putCase consumeAnnotationTypeMemberDeclarations() ; $break ./
/:$readableName AnnotationTypeMemberDeclarations:/
/:$compliance 1.5:/

AnnotationMethodHeaderName ::= Modifiersopt TypeParameters Type 'Identifier' '('
/.$putCase consumeMethodHeaderNameWithTypeParameters(true); $break ./
AnnotationMethodHeaderName ::= Modifiersopt Type 'Identifier' '('
/.$putCase consumeMethodHeaderName(true); $break ./
/:$readableName MethodHeaderName:/
/:$compliance 1.5:/

AnnotationMethodHeaderDefaultValueopt ::= $empty
/.$putCase consumeEmptyMethodHeaderDefaultValue() ; $break ./
/:$readableName MethodHeaderDefaultValue:/
/:$compliance 1.5:/
AnnotationMethodHeaderDefaultValueopt ::= DefaultValue
/.$putCase consumeMethodHeaderDefaultValue(); $break ./
/:$readableName MethodHeaderDefaultValue:/
/:$compliance 1.5:/

AnnotationMethodHeader ::= AnnotationMethodHeaderName FormalParameterListopt MethodHeaderRightParen MethodHeaderExtendedDims AnnotationMethodHeaderDefaultValueopt
/.$putCase consumeMethodHeader(); $break ./
/:$readableName AnnotationMethodHeader:/
/:$compliance 1.5:/

AnnotationTypeMemberDeclaration ::= AnnotationMethodHeader ';'
/.$putCase consumeAnnotationTypeMemberDeclaration() ; $break ./
/:$compliance 1.5:/
AnnotationTypeMemberDeclaration -> ConstantDeclaration
/:$compliance 1.5:/
AnnotationTypeMemberDeclaration -> ConstructorDeclaration
/:$compliance 1.5:/
AnnotationTypeMemberDeclaration -> TypeDeclaration
/:$readableName AnnotationTypeMemberDeclaration:/
/:$compliance 1.5:/

DefaultValue ::= 'default' MemberValue
/:$readableName DefaultValue:/
/:$compliance 1.5:/

Annotation -> NormalAnnotation
/:$compliance 1.5:/
Annotation -> MarkerAnnotation
/:$compliance 1.5:/
Annotation -> SingleMemberAnnotation
/:$readableName Annotation:/
/:$compliance 1.5:/

AnnotationName ::= '@' UnannotatableName
/.$putCase consumeAnnotationName() ; $break ./
/:$readableName AnnotationName:/
/:$compliance 1.5:/
/:$recovery_template @ Identifier:/

NormalAnnotation ::= AnnotationName '(' MemberValuePairsopt ')'
/.$putCase consumeNormalAnnotation(false) ; $break ./
/:$readableName NormalAnnotation:/
/:$compliance 1.5:/

MemberValuePairsopt ::= $empty
/.$putCase consumeEmptyMemberValuePairsopt() ; $break ./
/:$compliance 1.5:/
MemberValuePairsopt -> MemberValuePairs
/:$readableName MemberValuePairsopt:/
/:$compliance 1.5:/

MemberValuePairs -> MemberValuePair
/:$compliance 1.5:/
MemberValuePairs ::= MemberValuePairs ',' MemberValuePair
/.$putCase consumeMemberValuePairs() ; $break ./
/:$readableName MemberValuePairs:/
/:$compliance 1.5:/

MemberValuePair ::= SimpleName '=' EnterMemberValue MemberValue ExitMemberValue
/.$putCase consumeMemberValuePair() ; $break ./
/:$readableName MemberValuePair:/
/:$compliance 1.5:/

EnterMemberValue ::= $empty
/.$putCase consumeEnterMemberValue() ; $break ./
/:$readableName EnterMemberValue:/
/:$compliance 1.5:/

ExitMemberValue ::= $empty
/.$putCase consumeExitMemberValue() ; $break ./
/:$readableName ExitMemberValue:/
/:$compliance 1.5:/

MemberValue -> ConditionalExpression_NotName
/:$compliance 1.5:/
MemberValue ::= Name
/.$putCase consumeMemberValueAsName() ; $break ./
/:$compliance 1.5:/
MemberValue -> Annotation
/:$compliance 1.5:/
MemberValue -> MemberValueArrayInitializer
/:$readableName MemberValue:/
/:$recovery_template Identifier:/
/:$compliance 1.5:/

MemberValueArrayInitializer ::= EnterMemberValueArrayInitializer '{' PushLeftBrace MemberValues ',' '}'
/.$putCase consumeMemberValueArrayInitializer() ; $break ./
/:$compliance 1.5:/
MemberValueArrayInitializer ::= EnterMemberValueArrayInitializer '{' PushLeftBrace MemberValues '}'
/.$putCase consumeMemberValueArrayInitializer() ; $break ./
/:$compliance 1.5:/
MemberValueArrayInitializer ::= EnterMemberValueArrayInitializer '{' PushLeftBrace ',' '}'
/.$putCase consumeEmptyMemberValueArrayInitializer() ; $break ./
/:$compliance 1.5:/
MemberValueArrayInitializer ::= EnterMemberValueArrayInitializer '{' PushLeftBrace '}'
/.$putCase consumeEmptyMemberValueArrayInitializer() ; $break ./
/:$readableName MemberValueArrayInitializer:/
/:$compliance 1.5:/

EnterMemberValueArrayInitializer ::= $empty
/.$putCase consumeEnterMemberValueArrayInitializer() ; $break ./
/:$readableName EnterMemberValueArrayInitializer:/
/:$compliance 1.5:/

MemberValues -> MemberValue
/:$compliance 1.5:/
MemberValues ::= MemberValues ',' MemberValue
/.$putCase consumeMemberValues() ; $break ./
/:$readableName MemberValues:/
/:$compliance 1.5:/

MarkerAnnotation ::= AnnotationName
/.$putCase consumeMarkerAnnotation(false) ; $break ./
/:$readableName MarkerAnnotation:/
/:$compliance 1.5:/

SingleMemberAnnotationMemberValue ::= MemberValue
/.$putCase consumeSingleMemberAnnotationMemberValue() ; $break ./
/:$readableName MemberValue:/
/:$compliance 1.5:/

SingleMemberAnnotation ::= AnnotationName '(' SingleMemberAnnotationMemberValue ')'
/.$putCase consumeSingleMemberAnnotation(false) ; $break ./
/:$readableName SingleMemberAnnotation:/
/:$compliance 1.5:/
--------------------------------------
-- 1.5 features : end of annotation --
--------------------------------------

-----------------------------------
-- 1.5 features : recovery rules --
-----------------------------------
RecoveryMethodHeaderName ::= Modifiersopt TypeParameters Type 'Identifier' '('
/.$putCase consumeRecoveryMethodHeaderNameWithTypeParameters(); $break ./
/:$compliance 1.5:/
RecoveryMethodHeaderName ::= Modifiersopt Type 'Identifier' '('
/.$putCase consumeRecoveryMethodHeaderName(); $break ./
/:$readableName MethodHeaderName:/
RecoveryMethodHeaderName ::= ModifiersWithDefault TypeParameters Type 'Identifier' '('
/.$putCase consumeRecoveryMethodHeaderNameWithTypeParameters(); $break ./
/:$compliance 1.5:/
RecoveryMethodHeaderName ::= ModifiersWithDefault Type 'Identifier' '('
/.$putCase consumeRecoveryMethodHeaderName(); $break ./
/:$readableName MethodHeaderName:/

RecoveryMethodHeader ::= RecoveryMethodHeaderName FormalParameterListopt MethodHeaderRightParen MethodHeaderExtendedDims AnnotationMethodHeaderDefaultValueopt
/.$putCase consumeMethodHeader(); $break ./
RecoveryMethodHeader ::= RecoveryMethodHeaderName FormalParameterListopt MethodHeaderRightParen MethodHeaderExtendedDims MethodHeaderThrowsClause
/.$putCase consumeMethodHeader(); $break ./
/:$readableName MethodHeader:/

--{ObjectTeams:
RecoveryBindingHeader -> RecoveryCalloutHeader
RecoveryBindingHeader -> RecoveryCallinHeader
/:$readableName MethodBindingHeader:/

-- CALLIN-HEADER:

-- Note: do not include 'with' in header, it is used to recognize presence of param mappings,
-- while consuming the callXX header; this is easier if 'with' has not been consumed, yet.

RecoveryCallinHeader ::= RecoveryCallinBindingLeftLong CallinModifier MethodSpecsLong Predicateopt
/.$putCase consumeCallinHeader(); $break ./
/:$readableName CallinBindingLong:/

RecoveryCallinHeader ::= Modifiersopt CallinLabel RecoveryCallinBindingLeftLong CallinModifier MethodSpecsLong Predicateopt 
/.$putCase consumeCallinHeader(); $break ./

RecoveryCallinBindingLeftLong ::= RecoveryMethodSpecLong '<-'
/.$putCase consumeCallinBindingLeft(true); $break ./
/:$readableName CallinBindingLeft:/

-- Note(SH): Modifiersopt is needed to make grammar LALR(1), in real life modifiers must be empty
RecoveryCallinHeader ::= Modifiersopt CallinBindingLeftShort CallinModifier BaseMethodSpecsShort Predicateopt
/.$putCase consumeCallinHeader(); $break ./
RecoveryCallinHeader ::= Modifiersopt CallinLabel Modifiersopt CallinBindingLeftShort CallinModifier BaseMethodSpecsShort Predicateopt
/.$putCase consumeCallinHeader(); $break ./
/:$readableName CallinBindingShort:/

-- CALLOUT-HEADER:
RecoveryCalloutHeader ::= RecoveryCalloutBindingLeftLong MethodSpecLong
/.$putCase consumeCalloutHeader(); $break ./
/:$readableName CalloutBindingLong:/

RecoveryCalloutBindingLeftLong ::= RecoveryMethodSpecLong CalloutKind
/.$putCase consumeCalloutBindingLeft(true); $break ./
/:$readableName CalloutBindingLeftLong:/

RecoveryCalloutHeader ::= RecoveryCalloutBindingLeftLong CalloutFieldSpecLong
/.$putCase consumeCalloutHeader(); $break ./
/:$readableName CalloutToFieldLong:/

-- Note(SH): Modifiersopt is needed to make grammar LALR(1), in real life modifiers must be empty
RecoveryCalloutHeader ::= Modifiersopt CalloutBindingLeftShort CalloutModifieropt MethodSpecShort
/.$putCase consumeCalloutHeader(); $break ./
/:$readableName CalloutBindingShort:/


-- METHOD-SPEC (during recover):
RecoveryMethodSpecLong ::= RecoveryMethodHeaderName FormalParameterListopt MethodHeaderRightParen
/.$putCase consumeMethodSpecLong(false); $break ./
/:$readableName MethodSpecLong:/
-- SH}

-----------------------------------
-- 1.5 features : recovery rules --
-----------------------------------

/.	}
}./

$names

PLUS_PLUS ::=    '++'   
MINUS_MINUS ::=    '--'   
EQUAL_EQUAL ::=    '=='   
LESS_EQUAL ::=    '<='   
GREATER_EQUAL ::=    '>='   
NOT_EQUAL ::=    '!='   
LEFT_SHIFT ::=    '<<'   
RIGHT_SHIFT ::=    '>>'   
UNSIGNED_RIGHT_SHIFT ::=    '>>>'  
PLUS_EQUAL ::=    '+='   
MINUS_EQUAL ::=    '-='   
MULTIPLY_EQUAL ::=    '*='   
DIVIDE_EQUAL ::=    '/='   
AND_EQUAL ::=    '&='   
OR_EQUAL ::=    '|='   
XOR_EQUAL ::=    '^='   
REMAINDER_EQUAL ::=    '%='   
LEFT_SHIFT_EQUAL ::=    '<<='  
RIGHT_SHIFT_EQUAL ::=    '>>='  
UNSIGNED_RIGHT_SHIFT_EQUAL ::=    '>>>=' 
OR_OR ::=    '||'   
AND_AND ::=    '&&'
PLUS ::=    '+'    
MINUS ::=    '-'    
NOT ::=    '!'    
REMAINDER ::=    '%'    
XOR ::=    '^'    
AND ::=    '&'    
MULTIPLY ::=    '*'    
OR ::=    '|'    
TWIDDLE ::=    '~'    
DIVIDE ::=    '/'    
GREATER ::=    '>'    
LESS ::=    '<'    
LPAREN ::=    '('    
RPAREN ::=    ')'    
LBRACE ::=    '{'    
RBRACE ::=    '}'    
LBRACKET ::=    '['    
RBRACKET ::=    ']'    
SEMICOLON ::=    ';'    
QUESTION ::=    '?'    
COLON ::=    ':'    
COMMA ::=    ','    
DOT ::=    '.'    
EQUAL ::=    '='    
AT ::=    '@'
AT308 ::= '@'
AT308DOTDOTDOT ::= '@'
ELLIPSIS ::=    '...'    
ARROW ::= '->'
COLON_COLON ::= '::'

-- {ObjectTeams
ATOT ::= '@'
BINDIN ::= '<-'
CALLOUT_OVERRIDE ::= '=>'
SYNTHBINDOUT ::= '->'
-- Markus Witte}

$end
-- need a carriage return after the $end
