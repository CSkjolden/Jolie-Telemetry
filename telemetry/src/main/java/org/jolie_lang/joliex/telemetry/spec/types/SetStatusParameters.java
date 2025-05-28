package org.jolie_lang.joliex.telemetry.spec.types;

/**
 * this class is a {@link jolie.runtime.embedding.java.TypedStructure} which can be described as follows:
 * <pre>
 * id: {@link java.lang.String}
 * status: {@link java.lang.String}
 * </pre>
 * 
 * @see jolie.runtime.embedding.java.JolieValue
 * @see jolie.runtime.embedding.java.JolieNative
 * @see #builder()
 */
public final class SetStatusParameters extends jolie.runtime.embedding.java.TypedStructure {
    
    private static final java.util.Set<java.lang.String> FIELD_KEYS = fieldKeys( SetStatusParameters.class );
    
    @jolie.runtime.embedding.java.util.JolieName("id")
    private final java.lang.String id;
    @jolie.runtime.embedding.java.util.JolieName("status")
    private final java.lang.String status;
    
    public SetStatusParameters( java.lang.String id, java.lang.String status ) {
        this.id = jolie.runtime.embedding.java.util.ValueManager.validated( "id", id );
        this.status = jolie.runtime.embedding.java.util.ValueManager.validated( "status", jolie.runtime.embedding.java.util.RefinementValidator.enumeration( status, "OK", "ERROR", "ok", "error" ) );
    }
    
    public java.lang.String id() { return id; }
    public java.lang.String status() { return status; }
    
    public jolie.runtime.embedding.java.JolieNative.JolieVoid content() { return new jolie.runtime.embedding.java.JolieNative.JolieVoid(); }
    
    public static Builder builder() { return new Builder(); }
    public static Builder builder( jolie.runtime.embedding.java.JolieValue from ) { return from != null ? new Builder( from ) : builder(); }
    
    public static jolie.runtime.embedding.java.util.StructureListBuilder<SetStatusParameters, Builder> listBuilder() { return new jolie.runtime.embedding.java.util.StructureListBuilder<>( SetStatusParameters::builder ); }
    public static jolie.runtime.embedding.java.util.StructureListBuilder<SetStatusParameters, Builder> listBuilder( java.util.SequencedCollection<? extends jolie.runtime.embedding.java.JolieValue> from ) {
        return from != null ? new jolie.runtime.embedding.java.util.StructureListBuilder<>( from, SetStatusParameters::from, SetStatusParameters::builder ) : listBuilder();
    }
    
    public static SetStatusParameters from( jolie.runtime.embedding.java.JolieValue j ) throws jolie.runtime.embedding.java.TypeValidationException {
        return new SetStatusParameters(
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "id" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null ),
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "status" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null )
        );
    }
    
    public static SetStatusParameters fromValue( jolie.runtime.Value v ) throws jolie.runtime.typing.TypeCheckingException {
        jolie.runtime.embedding.java.util.ValueManager.requireChildren( v, FIELD_KEYS );
        return new SetStatusParameters(
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "id", jolie.runtime.embedding.java.JolieNative.JolieString::fieldFromValue ),
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "status", jolie.runtime.embedding.java.JolieNative.JolieString::fieldFromValue )
        );
    }
    
    public static jolie.runtime.Value toValue( SetStatusParameters t ) {
        final jolie.runtime.Value v = jolie.runtime.Value.create();
        
        v.getFirstChild( "id" ).setValue( t.id() );
        v.getFirstChild( "status" ).setValue( t.status() );
        
        return v;
    }
    
    public static class Builder {
        
        private java.lang.String id;
        private java.lang.String status;
        
        private Builder() {}
        private Builder( jolie.runtime.embedding.java.JolieValue j ) {
            this.id = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "id" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null );
            this.status = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "status" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null );
        }
        
        public Builder id( java.lang.String id ) { this.id = id; return this; }
        public Builder status( java.lang.String status ) { this.status = status; return this; }
        
        public SetStatusParameters build() {
            return new SetStatusParameters( id, status );
        }
    }
}