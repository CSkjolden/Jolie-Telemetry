package org.jolie_lang.joliex.telemetry.spec.types;

/**
 * this class is a {@link jolie.runtime.embedding.java.TypedStructure} which can be described as follows:
 * <pre>
 * id: {@link java.lang.String}
 * spanName: {@link java.lang.String}
 * </pre>
 * 
 * @see jolie.runtime.embedding.java.JolieValue
 * @see jolie.runtime.embedding.java.JolieNative
 * @see #builder()
 */
public final class CreateSpanParameters extends jolie.runtime.embedding.java.TypedStructure {
    
    private static final java.util.Set<java.lang.String> FIELD_KEYS = fieldKeys( CreateSpanParameters.class );
    
    @jolie.runtime.embedding.java.util.JolieName("id")
    private final java.lang.String id;
    @jolie.runtime.embedding.java.util.JolieName("spanName")
    private final java.lang.String spanName;
    
    public CreateSpanParameters( java.lang.String id, java.lang.String spanName ) {
        this.id = jolie.runtime.embedding.java.util.ValueManager.validated( "id", id );
        this.spanName = jolie.runtime.embedding.java.util.ValueManager.validated( "spanName", spanName );
    }
    
    public java.lang.String id() { return id; }
    public java.lang.String spanName() { return spanName; }
    
    public jolie.runtime.embedding.java.JolieNative.JolieVoid content() { return new jolie.runtime.embedding.java.JolieNative.JolieVoid(); }
    
    public static Builder builder() { return new Builder(); }
    public static Builder builder( jolie.runtime.embedding.java.JolieValue from ) { return from != null ? new Builder( from ) : builder(); }
    
    public static jolie.runtime.embedding.java.util.StructureListBuilder<CreateSpanParameters, Builder> listBuilder() { return new jolie.runtime.embedding.java.util.StructureListBuilder<>( CreateSpanParameters::builder ); }
    public static jolie.runtime.embedding.java.util.StructureListBuilder<CreateSpanParameters, Builder> listBuilder( java.util.SequencedCollection<? extends jolie.runtime.embedding.java.JolieValue> from ) {
        return from != null ? new jolie.runtime.embedding.java.util.StructureListBuilder<>( from, CreateSpanParameters::from, CreateSpanParameters::builder ) : listBuilder();
    }
    
    public static CreateSpanParameters from( jolie.runtime.embedding.java.JolieValue j ) throws jolie.runtime.embedding.java.TypeValidationException {
        return new CreateSpanParameters(
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "id" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null ),
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "spanName" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null )
        );
    }
    
    public static CreateSpanParameters fromValue( jolie.runtime.Value v ) throws jolie.runtime.typing.TypeCheckingException {
        jolie.runtime.embedding.java.util.ValueManager.requireChildren( v, FIELD_KEYS );
        return new CreateSpanParameters(
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "id", jolie.runtime.embedding.java.JolieNative.JolieString::fieldFromValue ),
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "spanName", jolie.runtime.embedding.java.JolieNative.JolieString::fieldFromValue )
        );
    }
    
    public static jolie.runtime.Value toValue( CreateSpanParameters t ) {
        final jolie.runtime.Value v = jolie.runtime.Value.create();
        
        v.getFirstChild( "id" ).setValue( t.id() );
        v.getFirstChild( "spanName" ).setValue( t.spanName() );
        
        return v;
    }
    
    public static class Builder {
        
        private java.lang.String id;
        private java.lang.String spanName;
        
        private Builder() {}
        private Builder( jolie.runtime.embedding.java.JolieValue j ) {
            this.id = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "id" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null );
            this.spanName = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "spanName" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null );
        }
        
        public Builder id( java.lang.String id ) { this.id = id; return this; }
        public Builder spanName( java.lang.String spanName ) { this.spanName = spanName; return this; }
        
        public CreateSpanParameters build() {
            return new CreateSpanParameters( id, spanName );
        }
    }
}