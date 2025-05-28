package org.jolie_lang.joliex.telemetry.spec.types;

/**
 * this class is a {@link jolie.runtime.embedding.java.TypedStructure} which can be described as follows:
 * <pre>
 * eventName: {@link java.lang.String}
 * attributes[0,1]: {@link jolie.runtime.embedding.java.JolieValue}
 * id: {@link java.lang.String}
 * </pre>
 * 
 * @see jolie.runtime.embedding.java.JolieValue
 * @see jolie.runtime.embedding.java.JolieNative
 * @see #builder()
 */
public final class AddEventParameters extends jolie.runtime.embedding.java.TypedStructure {
    
    private static final java.util.Set<java.lang.String> FIELD_KEYS = fieldKeys( AddEventParameters.class );
    
    @jolie.runtime.embedding.java.util.JolieName("eventName")
    private final java.lang.String eventName;
    @jolie.runtime.embedding.java.util.JolieName("attributes")
    private final jolie.runtime.embedding.java.JolieValue attributes;
    @jolie.runtime.embedding.java.util.JolieName("id")
    private final java.lang.String id;
    
    public AddEventParameters( java.lang.String eventName, jolie.runtime.embedding.java.JolieValue attributes, java.lang.String id ) {
        this.eventName = jolie.runtime.embedding.java.util.ValueManager.validated( "eventName", eventName );
        this.attributes = attributes;
        this.id = jolie.runtime.embedding.java.util.ValueManager.validated( "id", id );
    }
    
    public java.lang.String eventName() { return eventName; }
    public java.util.Optional<jolie.runtime.embedding.java.JolieValue> attributes() { return java.util.Optional.ofNullable( attributes ); }
    public java.lang.String id() { return id; }
    
    public jolie.runtime.embedding.java.JolieNative.JolieVoid content() { return new jolie.runtime.embedding.java.JolieNative.JolieVoid(); }
    
    public static Builder builder() { return new Builder(); }
    public static Builder builder( jolie.runtime.embedding.java.JolieValue from ) { return from != null ? new Builder( from ) : builder(); }
    
    public static jolie.runtime.embedding.java.util.StructureListBuilder<AddEventParameters, Builder> listBuilder() { return new jolie.runtime.embedding.java.util.StructureListBuilder<>( AddEventParameters::builder ); }
    public static jolie.runtime.embedding.java.util.StructureListBuilder<AddEventParameters, Builder> listBuilder( java.util.SequencedCollection<? extends jolie.runtime.embedding.java.JolieValue> from ) {
        return from != null ? new jolie.runtime.embedding.java.util.StructureListBuilder<>( from, AddEventParameters::from, AddEventParameters::builder ) : listBuilder();
    }
    
    public static AddEventParameters from( jolie.runtime.embedding.java.JolieValue j ) throws jolie.runtime.embedding.java.TypeValidationException {
        return new AddEventParameters(
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "eventName" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null ),
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "attributes" ), jolie.runtime.embedding.java.JolieValue::from ),
            jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "id" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null )
        );
    }
    
    public static AddEventParameters fromValue( jolie.runtime.Value v ) throws jolie.runtime.typing.TypeCheckingException {
        jolie.runtime.embedding.java.util.ValueManager.requireChildren( v, FIELD_KEYS );
        return new AddEventParameters(
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "eventName", jolie.runtime.embedding.java.JolieNative.JolieString::fieldFromValue ),
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "attributes", jolie.runtime.embedding.java.JolieValue::fromValue ),
            jolie.runtime.embedding.java.util.ValueManager.singleFieldFrom( v, "id", jolie.runtime.embedding.java.JolieNative.JolieString::fieldFromValue )
        );
    }
    
    public static jolie.runtime.Value toValue( AddEventParameters t ) {
        final jolie.runtime.Value v = jolie.runtime.Value.create();
        
        v.getFirstChild( "eventName" ).setValue( t.eventName() );
        t.attributes().ifPresent( c -> v.getFirstChild( "attributes" ).deepCopy( jolie.runtime.embedding.java.JolieValue.toValue( c ) ) );
        v.getFirstChild( "id" ).setValue( t.id() );
        
        return v;
    }
    
    public static class Builder {
        
        private java.lang.String eventName;
        private jolie.runtime.embedding.java.JolieValue attributes;
        private java.lang.String id;
        
        private Builder() {}
        private Builder( jolie.runtime.embedding.java.JolieValue j ) {
            this.eventName = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "eventName" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null );
            this.attributes = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "attributes" ), jolie.runtime.embedding.java.JolieValue::from );
            this.id = jolie.runtime.embedding.java.util.ValueManager.fieldFrom( j.getFirstChild( "id" ), c -> c.content() instanceof jolie.runtime.embedding.java.JolieNative.JolieString content ? content.value() : null );
        }
        
        public Builder eventName( java.lang.String eventName ) { this.eventName = eventName; return this; }
        public Builder attributes( jolie.runtime.embedding.java.JolieValue attributes ) { this.attributes = attributes; return this; }
        public Builder attributes( java.util.function.Function<jolie.runtime.embedding.java.JolieValue.InlineBuilder, jolie.runtime.embedding.java.JolieValue> f ) { return attributes( f.apply( jolie.runtime.embedding.java.JolieValue.builder() ) ); }
        public Builder id( java.lang.String id ) { this.id = id; return this; }
        
        public AddEventParameters build() {
            return new AddEventParameters( eventName, attributes, id );
        }
    }
}