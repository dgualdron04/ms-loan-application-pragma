package co.com.bancolombia.api.docs;

import co.com.bancolombia.api.dto.request.CreateApplicationDTO;
import co.com.bancolombia.api.dto.response.ApplicationResponseDTO;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static co.com.bancolombia.api.docs.DocsHelper.errs;
import static co.com.bancolombia.api.docs.DocsHelper.jsonResp;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class ApplicationDocs {

    public Consumer<Builder> save() {
        return builder -> {
            builder.operationId("save").tag("Application").description("Save application");
            builder.requestBody(requestBodyBuilder().required(true)
                    .content(contentBuilder().mediaType(APPLICATION_JSON_VALUE)
                            .schema(schemaBuilder().implementation(CreateApplicationDTO.class))));
            jsonResp(builder, "201", ApplicationResponseDTO.class);
            errs(builder);
        };
    }

}
