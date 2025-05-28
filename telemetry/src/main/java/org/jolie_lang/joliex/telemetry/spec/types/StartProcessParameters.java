package org.jolie_lang.joliex.telemetry.spec.types;

/**
 * this class is a {@link jolie.runtime.embedding.java.TypedStructure} which can be described as follows:
 * <pre>
 * id[0,1]: {@link java.lang.String}
 * propagationData[0,1]: {@link java.lang.String}
 * </pre>
 * 
 * @see jolie.runtime.embedding.java.JolieValue
 * @see jolie.runtime.embedding.java.JolieNative
 * @see #builder()
 */
public final class StartProcessParameters extends jolie.runtime.embedding.java.TypedStructure {
    
    private static final java.util.Set<java.lang.String> FIELD_KEYS = fieldKeys( StartProcessParameters.class );
    
    @jolie.runtime.embedding.java.util.JolieName("id")
    private final java.lang.String id;
    @jolie.runtime.embedding.java.util.JolieName("propagationData")
    private final java.lang.String propagationData;
    
    public StartProcessParameters( java.lang.String id, java.lang.String propagationData ) {
        this.id = id;
        this.propagationData = propagationData;
    }
    
    public java.util.Optional<java.lang.String> id() { return java.util.Optional.ofNullable( id ); }
    public java.util.Optional<java.lang.String> propagationData() { return java.util.Optional.ofNullable( propagationData ); }
    
    public jolie.runtime.embedding.java.JolieNative.JolieVoid content() { return new jolie.runtime.embedding.java.JolieNative.JolieVoid(); }
    
    public static Builder builder() { return new Builder(); }
    public static Builder builder( jolie.runtime.embedding.java.JolieValue from ) { return from != null ? new Builder( from ) : builder(); }
    
    public static jolie.runtime.embedding.java.util.StructureListBuilder<StartProcessParameters, Builder> listBuilder() { return new jolie.runtime.embedding.java.util.StructureListBuilder<>( StartProcessParameters::builder ); }
    public static jolie.runtime.embedding.java.util.StructureListBuilder<StartProcessParameters, Builder> listBuilder( java.util.SequencedCollection<? extends jolie.runtime.embedding.java.JolieValue> from ) {
        return from != null ? new jolie.runtime.embedding.java.util.StructureListBuilder<>( from, StartProcessParameters::from, StartProcessParameters::builder ) : listBuilder();
    }
    
    public static StartProcessParameters from( jolie.runtime.embedding.java.JolieValue j ) throws jolie.runtime.embedding.java.TypeValidationException {
        return new StartProcessParameters(
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "id" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null ),
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "propagationData" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null )
        );
    }
    
    public static StartProcessParameters fromValue( jolie.runtime.Value v ) throws jolie.runtime.typing.TypeCheckingException {
        jolie.runtime.embedding.java.util.ValueManager.requireChildren( v, FIELD_KEYS );
        return new StartProcessParameters(
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "id", jolie.runtime.embedding.java.JolieNative.JolieString::fieldFromValue ),
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "propagationData", jolie.runtime.embedding.java.JolieNative.JolieString::fieldFromValue )
        );
    }
    
    public static jolie.runtime.Value toValue( StartProcessParameters t ) {
        final jolie.runtime.Value v = jolie.runtime.Value.create();
        
        t.id().ifPresent( c -> v.getFirstChild( "id" ).setValue( c ) );
        t.propagationData().ifPresent( c -> v.getFirstChild( "propagationData" ).setValue( c ) );
        
        return v;
    }
    
    public static class Builder {
        
        private java.lang.String id;
        private java.lang.String propagationData;
        
        private Builder() {}
        private Builder( jolie.runtime.embedding.java.JolieValue j ) {
            this.id = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "id" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null );
            this.propagationData = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "propagationData" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null );
        }
        
        public Builder id( java.lang.String id ) { this.id = id; return this; }
        public Builder propagationData( java.lang.String propagationData ) { this.propagationData = propagationData; return this; }
        
        public StartProcessParameters build() {
            return new StartProcessParameters( id, propagationData );
        }
    }
}