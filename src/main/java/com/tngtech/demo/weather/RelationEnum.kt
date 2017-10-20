package com.tngtech.demo.weather

import com.mercateo.common.rest.schemagen.JerseyResource
import com.mercateo.common.rest.schemagen.link.LinkFactory
import com.mercateo.common.rest.schemagen.link.relation.Relation
import com.mercateo.common.rest.schemagen.link.relation.RelationContainer
import java.util.*
import javax.ws.rs.core.Link

fun <T : JerseyResource?> LinkFactory<T>.forCall(container : RelationContainer, invocation : (T) -> Unit) : Optional<Link> {
    return this.forCall(container.relation, invocation)
}

interface RelationEnum : RelationContainer {
    override fun getRelation(): Relation {
        return Relation.of(name.toLowerCase().replace('_', '-'))
    }

    val name: String
}