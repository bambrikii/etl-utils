package org.bambikii.etl.model.transformer.builders;

import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EtlAdapterBuilderTest {
    @Mock
    private EtlFieldReader reader;
    @Mock
    private EtlFieldWriter writer;

    @Mock
    private Object source;
    @Mock
    private Object target;

    @Mock
    private EtlFieldExtractable extractor1;
    @Mock
    private EtlFieldLoadable loader1;

    @Mock
    private EtlFieldExtractable extractor2;
    @Mock
    private EtlFieldLoadable loader2;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBuild() {
        when(reader.createOne(eq("field1"), eq("int"))).thenReturn(extractor1);
        when(writer.createOne(eq("field1_2"), eq("int"))).thenReturn(loader1);

        when(reader.createOne(eq("field2"), eq("string"))).thenReturn(extractor2);
        when(writer.createOne(eq("field2_2"), eq("string"))).thenReturn(loader2);

        EtlModelAdapter adapter = new EtlAdapterBuilder()
                .reader(reader)
                .writer(writer)
                .mapping("field1", "int", "field1_2")
                .mapping("field2", "string", "field2_2", "string")
                .buildAdapter();

        assertNotNull(adapter);

        adapter.adapt(source, target);

        verify(extractor1).readField(eq(source));
        verify(loader1).writeField(eq(target), any());

        verify(extractor2).readField(eq(source));
        verify(loader2).writeField(eq(target), any());
    }
}
