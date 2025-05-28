package org.jolie_lang.joliex.telemetry.spec.types;

/**
 * this class is a {@link jolie.runtime.embedding.java.TypedStructure} which can be described as follows:
 * <pre>
 * attributes: {@link jolie.runtime.embedding.java.JolieValue}
 * id: {@link java.lang.String}
 * </pre>
 * 
 * @see jolie.runtime.embedding.java.JolieValue
 * @see jolie.runtime.embedding.java.JolieNative
 * @see #builder()
 */
public final class AddAttributeParameters extends jolie.runtime.embedding.java.TypedStructure {
    
    private static final java.util.Set<java.lang.String> FIELD_KEYS = fieldKeys( AddAttributeParameters.class );
    
    @jolie.runtime.embedding.java.util.JolieName("attributes")
    private final jolie.runtime.embedding.java.JolieValue attributes;
    @jolie.runtime.embedding.java.util.JolieName("id")
    private final java.lang.String id;
    
    public AddAttributeParameters( jolie.runtime.embedding.java.JolieValue attributes, java.lang.String id ) {
        this.attributes = jolie.runtime.embedding.java.util.ValueManager.validated( "attributes", attributes );
        this.id = jolie.runtime.embedding.java.util.ValueManager.validated( "id", id );
    }
    
    public jolie.runtime.embedding.java.JolieValue attributes() { return attributes; }
    public java.lang.String id() { return id; }
    
    public jolie.runtime.embedding.java.JolieNative.JolieVoid content() { return new jolie.runtime.embedding.java.JolieNative.JolieVoid(); }
    
    public static Builder builder() { return new Builder(); }
    public static Builder builder( jolie.runtime.embedding.java.JolieValue from ) { return from != null ? new Builder( from ) : builder(); }
    
    public static jolie.runtime.embedding.java.util.StructureListBuilder<AddAttributeParameters, Builder> listBuilder() { return new jolie.runtime.embedding.java.util.StructureListBuilder<>( AddAttributeParameters::builder ); }
    public static jolie.runtime.embedding.java.util.StructureListBuilder<AddAttributeParameters, Builder> listBuilder( java.util.SequencedCollection<? extends jolie.runtime.embedding.java.JolieValue> from ) {
        return from != null ? new jolie.runtime.embedding.java.util.StructureListBuilder<>( from, AddAttributeParameters::from, AddAttributeParameters::builder ) : listBuilder();
    }
    
    public static AddAttributeParameters from( jolie.runtime.embedding.java.JolieValue j ) throws jolie.runtime.embedding.java.TypeValidationException {
        return new AddAttributeParameters(
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "attributes" ), jolie.runtime.embedding.java.JolieValue::from ),
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "id" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null )
        );
    }
    
    public static AddAttributeParameters fromValue( jolie.runtime.Value v ) throws jolie.runtime.typing.TypeCheckingException {
        jolie.runtime.embedding.java.util.ValueManager.requireChildren( v, FIELD_KEYS );
        return new AddAttributeParameters(
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "attributes", jolie.runtime.embedding.java.JolieValue::fromValue ),
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "id", jolie.runtime.embedding.java.JolieNative.JolieString::fieldFromValue )
        );
    }
    
    public static jolie.runtime.Value toValue( AddAttributeParameters t ) {
        final jolie.runtime.Value v = jolie.runtime.Value.create();
        
        v.getFirstChild( "attributes" ).deepCopy( jolie.runtime.embedding.java.JolieValue.toValue( t.attributes() ) );
        v.getFirstChild( "id" ).setValue( t.id() );
        
        return v;
    }
    
    public static class Builder {
        
        private jolie.runtime.embedding.java.JolieValue attributes;
        private java.lang.String id;
        
        private Builder() {}
        private Builder( jolie.runtime.embedding.java.JolieValue j ) {
            this.attributes = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "attributes" ), jolie.runtime.embedding.java.JolieValue::from );
            this.id = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "id" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null );
        }
        
        public Builder attributes( jolie.runtime.embedding.java.JolieValue attributes ) { this.attributes = attributes; return this; }
        public Builder attributes( java.util.function.Function<jolie.runtime.embedding.java.JolieValue.InlineBuilder, jolie.runtime.embedding.java.JolieValue> f ) { return attributes( f.apply( jolie.runtime.embedding.java.JolieValue.builder() ) ); }
        public Builder id( java.lang.String id ) { this.id = id; return this; }
        
        public AddAttributeParameters build() {
            return new AddAttributeParameters( attributes, id );
        }
    }
}