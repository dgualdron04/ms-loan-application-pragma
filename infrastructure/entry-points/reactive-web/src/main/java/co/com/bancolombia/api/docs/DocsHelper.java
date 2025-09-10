package co.com.bancolombia.api.docs;

import co.com.bancolombia.api.exception.model.ErrorResponse;
import org.springframework.http.MediaType;
import org.springdoc.core.fn.builders.operation.Builder;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

public class DocsHelper {
    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public static void jsonResp(Builder builder, String code, Class<?> schema)
    {
        builder.response(
                responseBuilder()
                        .responseCode(code)
                        .content(
                                contentBuilder()
                                        .mediaType(JSON)
                                        .schema(
                                                schemaBuilder()
                                                        .implementation(schema)
                                        )
                        )
        );
    }

    public static void errs(Builder builder) {
        builder.response(
                responseBuilder()
                        .responseCode("400")
                        .content(
                                contentBuilder()
                                        .mediaType(JSON)
                                        .schema(
                                                schemaBuilder().implementation(ErrorResponse.class)
                                        )
                        )
        ).response(
                responseBuilder()
                        .responseCode("500")
                        .content(
                                contentBuilder()
                                        .mediaType(JSON)
                                        .schema(
                                                schemaBuilder().implementation(ErrorResponse.class)
                                        )
                        )
        );
    }
}
