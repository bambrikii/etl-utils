## ETL Utility to Transform Data from One source to another
The utility can transform flat, hierarchical (table-like) data into flat, hierarchical data.
It has adapters to read and write the data.

|Type|Factory|Input Adapter|Output Adapter|Field Adapter for Reading|Field Adapter for Writing|Comments|
|---|---|---|---|---|---|---|
|POJO           |EtlPojoAdapterFactory           |createPojoInputAdapter |createPojoOutputAdapter    |createPojoFieldReader          |createPojoFieldWriter          | |
|DB                 |EtlDbAdapterFactory                |createDbInputAdapter   |createDbOutputAdapter      |createDbFieldReader        |createDbFieldWriter        | |
|SwiftMT    |EtlSwiftMtAdapterFactory    |createSwiftMtInputAdapter |createSwiftMtOutputAdapter (TODO)    |createSwiftMtFieldReader   |createSwiftMtFieldWriter (TODO)   | |

### Configuration
#### XML
The conversion can be configured in XML. For this there should be two XML files: one - to define model structure, another - to set field-to-field mappings.
For example:

![db adapter model-config.xml](./etl-adapters/etl-adapter-db/src/test/resources/model-config.xml)

![db adapter mapping-config.xml](./etl-adapters/etl-adapter-db/src/test/resources/mapping-config.xml)

It can be completed using EtlAdapterConfigBuilder class.
For example:

```java
        Map<String, EtlModelAdapter> adapters = new EtlAdapterConfigBuilder()
                .readerStrategy(EtlPojoAdapterFactory.createPojoFieldReader())
                .writerStrategy(EtlPojoAdapterFactory.createPojoFieldWriter())
                .modelConfig(EtlAdapterConfigBuilderTest.class.getResourceAsStream("/model-config.xml"))
                .conversionConfig(EtlAdapterConfigBuilderTest.class.getResourceAsStream("/mapping-config.xml"))
                .buildMap();

        EtlModelAdapter modelAdapter = adapters.get("conversion1");

        ...

        // source and target are objects of Map<String,Object> class

        modelAdapter.adapt(
                EtlPojoAdapterFactory.createPojoInputAdapter(source), 
                EtlPojoAdapterFactory.createPojoOutputAdapter(target)
        );

```

Please, see EtlAdapterConfigBuilderTest test case for details.

#### Java
Java conversion can be fulfilled using EtlAdapterBuilder.
For example:
```java
        EtlModelAdapter adapter = new EtlAdapterBuilder()
                .readerStrategy(readerStrategy)
                .writerStrategy(writerStrategy)
                .addFieldConversion("field1", "int", "field1_2")
                .addFieldConversion("field2", "string", "field2_2", "string")
                .buildAdapter();

        ...

        adapter.adapt(source, target);
```

Please, see EtlAdapterBuilder test for details.

### Conversion rules

#### Currently Supported Types
|Type|Supported in|Comments|
|---|---|---|
|String |all built-in adapters  |   |
|int    |all built-in adapters  |   |
|double |all built-in adapters  |   |


Types can be extend/customized via custom EtlFieldReaderStrategy/EtlFieldWriterStrategy
