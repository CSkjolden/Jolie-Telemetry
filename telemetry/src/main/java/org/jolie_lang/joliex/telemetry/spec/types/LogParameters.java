package org.jolie_lang.joliex.telemetry.spec.types;

/**
 * this class is a {@link jolie.runtime.embedding.java.TypedStructure} which can be described as follows:
 * <pre>
 * severity[0,1]: {@link java.lang.String}
 * attributes[0,1]: {@link jolie.runtime.embedding.java.JolieValue}
 * id[0,1]: {@link java.lang.String}
 * message: {@link java.lang.String}
 * </pre>
 * 
 * @see jolie.runtime.embedding.java.JolieValue
 * @see jolie.runtime.embedding.java.JolieNative
 * @see #builder()
 */
public final class LogParameters extends jolie.runtime.embedding.java.TypedStructure {
    
    private static final java.util.Set<java.lang.String> FIELD_KEYS = fieldKeys( LogParameters.class );
    
    @jolie.runtime.embedding.java.util.JolieName("severity")
    private final java.lang.String severity;
    @jolie.runtime.embedding.java.util.JolieName("attributes")
    private final jolie.runtime.embedding.java.JolieValue attributes;
    @jolie.runtime.embedding.java.util.JolieName("id")
    private final java.lang.String id;
    @jolie.runtime.embedding.java.util.JolieName("message")
    private final java.lang.String message;
    
    public LogParameters( java.lang.String severity, jolie.runtime.embedding.java.JolieValue attributes, java.lang.String id, java.lang.String message ) {
        this.severity = jolie.runtime.embedding.java.util.RefinementValidator.enumeration( severity, "TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "trace", "debug", "info", "warn", "error", "fatal" );
        this.attributes = attributes;
        this.id = id;
        this.message = jolie.runtime.embedding.java.util.ValueManager.validated( "message", message );
    }
    
    public java.util.Optional<java.lang.String> severity() { return java.util.Optional.ofNullable( severity ); }
    public java.util.Optional<jolie.runtime.embedding.java.JolieValue> attributes() { return java.util.Optional.ofNullable( attributes ); }
    public java.util.Optional<java.lang.String> id() { return java.util.Optional.ofNullable( id ); }
    public java.lang.String message() { return message; }
    
    public jolie.runtime.embedding.java.JolieNative.JolieVoid content() { return new jolie.runtime.embedding.java.JolieNative.JolieVoid(); }
    
    public static Builder builder() { return new Builder(); }
    public static Builder builder( jolie.runtime.embedding.java.JolieValue from ) { return from != null ? new Builder( from ) : builder(); }
    
    public static jolie.runtime.embedding.java.util.StructureListBuilder<LogParameters, Builder> listBuilder() { return new jolie.runtime.embedding.java.util.StructureListBuilder<>( LogParameters::builder ); }
    public static jolie.runtime.embedding.java.util.StructureListBuilder<LogParameters, Builder> listBuilder( java.util.SequencedCollection<? extends jolie.runtime.embedding.java.JolieValue> from ) {
        return from != null ? new jolie.runtime.embedding.java.util.StructureListBuilder<>( from, LogParameters::from, LogParameters::builder ) : listBuilder();
    }
    
    public static LogParameters from( jolie.runtime.embedding.java.JolieValue j ) throws jolie.runtime.embedding.java.TypeValidationException {
        return new LogParameters(
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "severity" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null ),
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "attributes" ), jolie.runtime.embedding.java.JolieValue::from ),
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "id" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null ),
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "message" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null )
        );
    }
    
    public static LogParameters fromValue( jolie.runtime.Value v ) throws jolie.runtime.typing.TypeCheckingException {
        jolie.runtime.embedding.java.util.ValueManager.requireChildren( v, FIELD_KEYS );
        return new LogParameters(
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "severity", jolie.runtime.embedding.java.JolieNative.JolieString::fieldFromValue ),
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "attributes", jolie.runtime.embedding.java.JolieValue::fromValue ),
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "id", jolie.runtime.embedding.java.JolieNative.JolieString::fieldFromValue ),
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "message", jolie.runtime.embedding.java.JolieNative.JolieString::fieldFromValue )
        );
    }
    
    public static jolie.runtime.Value toValue( LogParameters t ) {
        final jolie.runtime.Value v = jolie.runtime.Value.create();
        
        t.severity().ifPresent( c -> v.getFirstChild( "severity" ).setValue( c ) );
        t.attributes().ifPresent( c -> v.getFirstChild( "attributes" ).deepCopy( jolie.runtime.embedding.java.JolieValue.toValue( c ) ) );
        t.id().ifPresent( c -> v.getFirstChild( "id" ).setValue( c ) );
        v.getFirstChild( "message" ).setValue( t.message() );
        
        return v;
    }
    
    public static class Builder {
        
        private java.lang.String severity;
        private jolie.runtime.embedding.java.JolieValue attributes;
        private java.lang.String id;
        private java.lang.String message;
        
        private Builder() {}
        private Builder( jolie.runtime.embedding.java.JolieValue j ) {
            this.severity = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "severity" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null );
            this.attributes = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "attributes" ), jolie.runtime.embedding.java.JolieValue::from );
            this.id = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "id" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null );
            this.message = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "message" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null );
        }
        
        public Builder severity( java.lang.String severity ) { this.severity = severity; return this; }
        public Builder attributes( jolie.runtime.embedding.java.JolieValue attributes ) { this.attributes = attributes; return this; }
        public Builder attributes( java.util.function.Function<jolie.runtime.embedding.java.JolieValue.InlineBuilder, jolie.runtime.embedding.java.JolieValue> f ) { return attributes( f.apply( jolie.runtime.embedding.java.JolieValue.builder() ) ); }
        public Builder id( java.lang.String id ) { this.id = id; return this; }
        public Builder message( java.lang.String message ) { this.message = message; return this; }
        
        public LogParameters build() {
            return new LogParameters( severity, attributes, id, message );
        }
    }
}