package com.tngtech.demo.weather.resources

import com.mercateo.common.rest.schemagen.JerseyResource
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.stereotype.Component
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/")
@Api(value = "/", description = "root resource")
@Component
open class RootResource(
        private val rootHyperSchemaCreator: RootHyperSchemaCreator
) : JerseyResource {


    open val root: ObjectWithSchema<Void>
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @ApiOperation(value = "Get all possible links")
        @ApiResponses(value = *arrayOf(ApiResponse(code = 200, message = "Success")))
        get() = rootHyperSchemaCreator.create()
}
